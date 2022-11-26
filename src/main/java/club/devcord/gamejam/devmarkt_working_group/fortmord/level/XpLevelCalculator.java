package club.devcord.gamejam.devmarkt_working_group.fortmord.level;

import club.devcord.gamejam.devmarkt_working_group.fortmord.LevelCalculator;
import jakarta.inject.Singleton;
import org.bukkit.entity.Player;

@Singleton
public class XpLevelCalculator implements LevelCalculator.Inner {
    @Override
    public double calculate(Player player) {
        return player.getLevel();
    }

    @Override
    public double weight() {
        return 0.25;
    }
}
