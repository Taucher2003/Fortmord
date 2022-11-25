package club.devcord.gamejam.devmarkt_working_group.fortmord.level;

import club.devcord.gamejam.devmarkt_working_group.fortmord.LevelCalculator;
import jakarta.inject.Singleton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Singleton
public class LevelCalculatorImpl implements LevelCalculator {

    @Override
    public int calculate(Player player) {
        return Arrays.stream(player.getInventory().getContents())
                .filter(Objects::nonNull)
                .map(this::calculateItem)
                .reduce(0D, Double::sum)
                .intValue();
    }

    private static final Map<Material, Double> BASE_LEVELS = new EnumMap<>(Material.class);

    static {
        var index = 0D;
        put(++index, Material.WOODEN_AXE, Material.WOODEN_PICKAXE, Material.WOODEN_SWORD);
        put(++index, Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET);
        put(++index, Material.STONE_AXE, Material.STONE_PICKAXE, Material.STONE_SWORD);
        put(++index, Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIAMOND);
        put(++index, Material.IRON_AXE, Material.IRON_PICKAXE, Material.IRON_SWORD);
        put(++index, Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET);
        put(++index, Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SWORD);
        put(++index, Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET);
    }

    private static void put(Double level, Material... materials) {
        for (var material : materials) {
            BASE_LEVELS.put(material, level);
        }
    }

    private double calculateItem(ItemStack item) {
        double result = BASE_LEVELS.getOrDefault(item.getType(), 0D);

        result += item.getAmount() * 0.1;

        result += item.getEnchantments().values().stream().mapToDouble(level -> level * 0.25).reduce(0, Double::sum);

        return result;
    }
}
