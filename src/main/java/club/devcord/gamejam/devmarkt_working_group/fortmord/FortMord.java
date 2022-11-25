package club.devcord.gamejam.devmarkt_working_group.fortmord;

import club.devcord.gamejam.devmarkt_working_group.fortmord.event.OnDisableEvent;
import club.devcord.gamejam.devmarkt_working_group.fortmord.event.OnEnableEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

@Singleton
public class FortMord extends MicronautJavaPlugin {

    @EventListener
    public void onEnable(OnEnableEvent event) {
        getLogger().info("Weeeeee");
    }

    @EventListener
    public void onDisable(OnDisableEvent event) {
        getLogger().info("bye... *sad fortmord noises*");
    }

}
