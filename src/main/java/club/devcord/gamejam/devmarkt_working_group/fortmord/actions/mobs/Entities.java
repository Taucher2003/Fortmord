package club.devcord.gamejam.devmarkt_working_group.fortmord.actions.mobs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public final class Entities {

    private Entities() {
    }

    public static void spawnEntity(Player player, EntityType type, Consumer<Entity> entityMutator, int radius) {
        var playerLocation = player.getLocation();
        var spawnLocation = findSpawnLocation(playerLocation, radius);
        spawnLocation.getWorld().spawnEntity(spawnLocation, type, CreatureSpawnEvent.SpawnReason.CUSTOM, entityMutator::accept);
    }

    private static Location findSpawnLocation(Location playerLocation, int radius) {
        var location = randomizeLocation(playerLocation, radius);
        while (location.subtract(0, 1, 0).getBlock().getType() == Material.AIR);
        location.add(0, 1, 0);

        while (location.getBlock().getType() != Material.AIR) {
            location.add(0, 1, 0);
            if (location.getY() - playerLocation.getY() > 5) {
                return findSpawnLocation(playerLocation, radius);
            }
        }
        return location;
    }

    private static Location randomizeLocation(Location location, int radius) {
        var clone = location.clone();
        clone.add(randomInt(-radius, radius), 0, randomInt(-radius, radius));
        return clone;
    }

    private static int randomInt(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to);
    }
}
