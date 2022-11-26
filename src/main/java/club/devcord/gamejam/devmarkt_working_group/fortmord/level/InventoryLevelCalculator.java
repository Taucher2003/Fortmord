package club.devcord.gamejam.devmarkt_working_group.fortmord.level;

import club.devcord.gamejam.devmarkt_working_group.fortmord.LevelCalculator;
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
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Singleton
public class InventoryLevelCalculator implements LevelCalculator.Inner {
    private final Server server;
    private final Map<Material, Double> recipeCache = new EnumMap<>(Material.class);

    public InventoryLevelCalculator(Server server) {
        this.server = server;

        var index = 0D;
        put(++index, Material.ACACIA_WOOD, Material.BIRCH_WOOD, Material.DARK_OAK_WOOD, Material.JUNGLE_WOOD, Material.MANGROVE_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD);
        put(++index, Material.COBBLESTONE, Material.COBBLED_DEEPSLATE);
        put(++index, Material.LEATHER);
        put(++index, Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE, Material.RAW_IRON, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.RAW_GOLD);
        put(++index, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE);
        put(++index, Material.ACACIA_WOOD, Material.BIRCH_WOOD, Material.DARK_OAK_WOOD, Material.JUNGLE_WOOD, Material.MANGROVE_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD);
        put(++index, Material.COBBLESTONE, Material.COBBLED_DEEPSLATE);
        put(++index, Material.LEATHER);
        put(++index, Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE);
        put(++index, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE);
        put(++index, Material.ANCIENT_DEBRIS);
    }
    private void put(Double level, Material... materials) {
        for (var material : materials) {
            recipeCache.put(material, level);
        }
    }

    @Override
    public double calculate(Player player) {
        return Arrays.stream(player.getInventory().getContents())
                .filter(Objects::nonNull)
                .mapToDouble(this::calculateItem)
                .sum();
    }

    @Override
    public double weight() {
        return 0.5;
    }

    private double calculateItem(ItemStack item) {
        var result = calculateBase(item.getType()) * item.getAmount();

        result += item.getEnchantments().values().stream().mapToDouble(level -> level * 0.5).reduce(0, Double::sum);

        return result;
    }

    private double calculateBase(Material material) {
        if(recipeCache.containsKey(material)) {
            return recipeCache.get(material);
        }

        var result = calculateBase(material, EnumSet.noneOf(Material.class));
        recipeCache.put(material, result);
        return result;
    }

    private double calculateBase(Material material, Set<Material> knownNodes) {
        if(knownNodes.contains(material)) {
            return 0D;
        }
        knownNodes.add(material);

        return server.getRecipesFor(new ItemStack(material))
                .stream()
                .filter(recipe -> {
                    var name = material.name().replace("_INGOT", "_BLOCK");
                    var blockMaterial = Material.getMaterial(name);
                    if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                        return shapelessRecipe.getChoiceList()
                                .stream()
                                .noneMatch(recipeChoice -> recipeChoice.test(new ItemStack(blockMaterial)));
                    }
                    return true;
                })
                .sorted((recipe, recipe1) -> recipe.equals(recipe1) ? 0 : recipe instanceof CookingRecipe<?> ? -1 :  1)
                .flatMap(this::ingredientsForRecipe)
                .mapToDouble(m -> calculateBase(m.getType(), knownNodes) * m.getAmount())
                .filter(value -> value > 0)
                .limit(1)
                .sum();
    }

    private Stream<ItemStack> ingredientsForRecipe(Recipe recipe){
        if(recipe instanceof CookingRecipe<?> cookingRecipe) {
            server.broadcastMessage(cookingRecipe.getInputChoice().getItemStack().toString());
            return Stream.of(cookingRecipe.getInputChoice().getItemStack());
        }
        if(recipe instanceof MerchantRecipe merchantRecipe) {
            return merchantRecipe.getIngredients()
                    .stream();
        }
        if(recipe instanceof SmithingRecipe smithingRecipe) {
            return Stream.of(smithingRecipe.getBase().getItemStack(), smithingRecipe.getAddition().getItemStack());
        }
        if(recipe instanceof StonecuttingRecipe stonecuttingRecipe) {
            return Stream.of(stonecuttingRecipe.getInputChoice().getItemStack());
        }
        if(recipe instanceof ShapedRecipe shapedRecipe) {
            return shapedRecipe.getChoiceMap().values()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(RecipeChoice::getItemStack);
        }
        if(recipe instanceof ShapelessRecipe shapelessRecipe) {
            return shapelessRecipe.getChoiceList()
                    .stream()
                    .map(RecipeChoice::getItemStack);
        }

        return Stream.empty();
    }

    public Map<Material, Double> getCache() {
        return Collections.unmodifiableMap(recipeCache);
    }
}
