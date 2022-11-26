package club.devcord.gamejam.devmarkt_working_group.fortmord.level;

import club.devcord.gamejam.devmarkt_working_group.fortmord.LevelCalculator;
import com.google.common.util.concurrent.AtomicDouble;
import jakarta.inject.Singleton;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Singleton
public class InventoryLevelCalculator implements LevelCalculator.Inner {

    private static final Map<Material, Double> BASE_LEVELS = new EnumMap<>(Material.class);

    private static void put(Double level, Material... materials) {
        for (var material : materials) {
            BASE_LEVELS.put(material, level);
        }
    }

    static {
        var index = 0D;
        put(++index, Material.ACACIA_WOOD, Material.BIRCH_WOOD, Material.DARK_OAK_WOOD, Material.JUNGLE_WOOD, Material.MANGROVE_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD);
        put(++index, Material.COBBLESTONE, Material.COBBLED_DEEPSLATE);
        put(++index, Material.LEATHER);
        put(++index, Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE);
        put(++index, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE);
    }

    private final Server server;
    private final Map<Material, Double> recipeCache = new EnumMap<>(Material.class);

    public InventoryLevelCalculator(Server server) {
        this.server = server;
    }

    @Override
    public double calculate(Player player) {
        return Arrays.stream(player.getInventory().getContents())
                .filter(Objects::nonNull)
                .map(this::calculateItem)
                .reduce(0D, Double::sum);
    }

    @Override
    public double weight() {
        return 0.5;
    }

    private double calculateItem(ItemStack item) {
        var result = IntStream.range(0, item.getAmount())
                .mapToDouble(ignored -> calculateBase(item.getType(), EnumSet.noneOf(Material.class)))
                .sum();

        result += item.getEnchantments().values().stream().mapToDouble(level -> level * 0.5).reduce(0, Double::sum);

        return result;
    }

    private double calculateBase(Material material, Set<Material> knownNodes) {
        if(recipeCache.containsKey(material)) {
            return recipeCache.get(material);
        }

        if(knownNodes.contains(material)) {
            return 0D;
        }
        knownNodes.add(material);

        var result = new AtomicDouble();

        server.getRecipesFor(new ItemStack(material))
                .stream()
                .limit(1)
                .flatMap(this::ingredientsForRecipe)
                .peek(m -> result.addAndGet(BASE_LEVELS.getOrDefault(m, 0.1)))
                .map(m -> calculateBase(m, knownNodes))
                .forEach(result::addAndGet);

        recipeCache.put(material, result.get());

        return result.get();
    }

    private Stream<Material> ingredientsForRecipe(Recipe recipe){
        if(recipe instanceof CookingRecipe<?> cookingRecipe) {
            return itemstackToMaterialList(cookingRecipe.getInputChoice().getItemStack());
        }
        if(recipe instanceof MerchantRecipe merchantRecipe) {
            return merchantRecipe.getIngredients()
                    .stream()
                    .flatMap(this::itemstackToMaterialList);
        }
        if(recipe instanceof SmithingRecipe smithingRecipe) {
            return Stream.of(smithingRecipe.getBase().getItemStack(), smithingRecipe.getAddition().getItemStack())
                    .flatMap(this::itemstackToMaterialList);
        }
        if(recipe instanceof StonecuttingRecipe stonecuttingRecipe) {
            return itemstackToMaterialList(stonecuttingRecipe.getInputChoice().getItemStack());
        }
        if(recipe instanceof ShapedRecipe shapedRecipe) {
            return shapedRecipe.getChoiceMap().values()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(RecipeChoice::getItemStack)
                    .flatMap(this::itemstackToMaterialList);
        }
        if(recipe instanceof ShapelessRecipe shapelessRecipe) {
            return shapelessRecipe.getChoiceList()
                    .stream()
                    .map(RecipeChoice::getItemStack)
                    .flatMap(this::itemstackToMaterialList);
        }

        return Stream.empty();
    }

    private Stream<Material> itemstackToMaterialList(ItemStack stack) {
        return IntStream.range(0, stack.getAmount()).mapToObj(ignored -> stack.getType());
    }
}
