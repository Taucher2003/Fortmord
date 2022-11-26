package club.devcord.gamejam.devmarkt_working_group.fortmord.level;

import club.devcord.gamejam.devmarkt_working_group.fortmord.LevelCalculator;
import org.bukkit.entity.Player;

//@Singleton
public class LocationLevelCalculator implements LevelCalculator.Inner {
    @Override
    public double calculate(Player player) {
        var baseLocation = player.getBedSpawnLocation();
        if(baseLocation == null) {
            baseLocation = player.getWorld().getSpawnLocation();
        }

        return player.getLocation().toVector().distanceSquared(baseLocation.toVector());
    }

    @Override
    public double weight() {
        return 0.0001;
    }
}
