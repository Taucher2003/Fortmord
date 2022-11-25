package club.devcord.gamejam.devmarkt_working_group.fortmord;

import club.devcord.gamejam.devmarkt_working_group.fortmord.event.BukkitEvent;
import club.devcord.gamejam.devmarkt_working_group.fortmord.event.OnEnableEvent;
import club.devcord.gamejam.devmarkt_working_group.fortmord.event.OnLoadEvent;
import club.devcord.gamejam.devmarkt_working_group.fortmord.event.OnDisableEvent;

import io.micronaut.context.ApplicationContext;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MicronautJavaPlugin extends JavaPlugin {

    private final ApplicationContext context;

    public MicronautJavaPlugin() {
        this.context = ApplicationContext.run(this.getClassLoader()).start();
        context.registerSingleton(this);
        context.registerSingleton(this.getServer());
    }

    @Override
    public void onLoad() {
        publishEvent(new OnLoadEvent(), OnLoadEvent.class);
    }

    @Override
    public void onEnable() {
        context.getBeansOfType(Listener.class)
                .forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));


        publishEvent(new OnEnableEvent(), OnEnableEvent.class);
    }

    @Override
    public void onDisable() {
        publishEvent(new OnDisableEvent(), OnDisableEvent.class);
        context.close();
    }

    private <T extends BukkitEvent> void publishEvent(T event, Class<T> tClass) {
        context.getEventPublisher(tClass).publishEvent(event);
    }

    public <T> T bean(Class<T> klass) {
        return context.getBean(klass);
    }

    public ApplicationContext context() {
        return context;
    }
}
