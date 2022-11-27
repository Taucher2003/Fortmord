package club.devcord.gamejam.devmarkt_working_group.fortmord.actions.listeners;

import jakarta.inject.Singleton;
import org.bukkit.World;
import org.bukkit.entity.Blaze;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

@Singleton
public class EntityDeathListener implements Listener {

    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getWorld().getEnvironment() == World.Environment.NORMAL) {
            if (event.getEntity() instanceof Blaze blaze)
                blaze.clearLootTable();
        }
    }
}
