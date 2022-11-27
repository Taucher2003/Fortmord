package club.devcord.gamejam.devmarkt_working_group.fortmord.actions.listeners;

import jakarta.inject.Singleton;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

@Singleton
public class BlockIgniteListener implements Listener {


    @EventHandler
    public void handleIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.FIREBALL
                && event.getIgnitingEntity().getType() == EntityType.BLAZE) {
            event.setCancelled(true);
        }
    }
}
