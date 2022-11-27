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
    private double setupCounter = 1;
    private final Map<Material, Double> recipeCache = new EnumMap<>(Material.class);

    public InventoryLevelCalculator(Server server) {
        this.server = server;
        setupBaseWeights();
    }

    private void setupBaseWeights() {
        put(Material.ACACIA_WOOD, Material.BIRCH_WOOD, Material.DARK_OAK_WOOD, Material.JUNGLE_WOOD, Material.MANGROVE_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD,
                Material.COBBLESTONE, Material.COBBLED_DEEPSLATE,
                Material.LEATHER);

        put(Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE, Material.RAW_IRON, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.RAW_GOLD);

        put(Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE);

        put(Material.ANCIENT_DEBRIS);
    }

    private void put(Material... materials) {
        for (var material : materials) {
            recipeCache.put(material, setupCounter);
        }
        setupCounter++;
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
        return calculateBase(item.getType()) * item.getAmount() +
                item.getEnchantments().values().stream().mapToDouble(level -> level * 0.5).sum();
    }

    private double calculateBase(Material material) {
        var result = calculateBase(material, EnumSet.noneOf(Material.class));
        recipeCache.put(material, result);
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

        return server.getRecipesFor(new ItemStack(material))
                .stream()
                .filter(recipe -> isNoBlockOf(recipe, material))
                .sorted((recipe, recipe1) -> recipe.equals(recipe1) ? 0 : recipe instanceof CookingRecipe<?> ? -1 :  1)
                .limit(1)
                .flatMap(this::ingredientsForRecipe)
                .mapToDouble(m -> calculateBase(m.getType(), knownNodes) * m.getAmount())
                .sum();
    }

    private boolean isNoBlockOf(Recipe recipe, Material material) {
        var name = material.name().replace("_INGOT", "_BLOCK");
        var blockMaterial = Material.getMaterial(name);
        if (recipe instanceof ShapelessRecipe shapelessRecipe && blockMaterial != null) {
            return shapelessRecipe.getChoiceList()
                    .stream()
                    .noneMatch(recipeChoice -> recipeChoice.test(new ItemStack(blockMaterial)));
        }
        return true;
    }

    private Stream<ItemStack> ingredientsForRecipe(Recipe recipe){
        if(recipe instanceof CookingRecipe<?> cookingRecipe) {
            server.broadcastMessage(cookingRecipe.getInputChoice().getItemStack().toString());
            return Stream.of(cookingRecipe.getInputChoice().getItemStack());
        }
        if(recipe instanceof MerchantRecipe merchantRecipe) {
            return merchantRecipe.getIngredients().stream();
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
                    .map(RecipeChoice::getItemStack)
                    .reduce(new EnumMap<Material, ItemStack>(Material.class), (map, itemStack) -> {
                        var type = itemStack.getType();
                        var inserted = map.putIfAbsent(type, itemStack) != null;
                        var stack = map.get(type);
                        if (inserted) {
                            stack.setAmount(stack.getAmount() + itemStack.getAmount());
                        }
                        return map;
                    }, (map1, map2) -> {
                        map1.putAll(map2);
                        return map1;
                    })
                    .values()
                    .stream();
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
