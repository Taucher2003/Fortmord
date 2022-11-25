package club.devcord.gamejam.devmarkt_working_group.fortmord.level;

import org.bukkit.entity.Player;

public record LevelChangeEvent(Player player, int newLevel) {
}
