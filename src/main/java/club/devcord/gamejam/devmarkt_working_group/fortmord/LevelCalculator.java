package club.devcord.gamejam.devmarkt_working_group.fortmord;

import org.bukkit.entity.Player;

public interface LevelCalculator {

    int calculate(Player player);

    interface Inner {
        double calculate(Player player);

        default double weight() {
            return 1D;
        }
    }
}
