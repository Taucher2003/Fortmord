package club.devcord.gamejam.devmarkt_working_group.fortmord.micropaper;

import club.devcord.gamejam.devmarkt_working_group.fortmord.micropaper.command.McCommand;
import club.devcord.gamejam.devmarkt_working_group.fortmord.micropaper.event.BukkitEvent;
import club.devcord.gamejam.devmarkt_working_group.fortmord.micropaper.event.OnDisableEvent;
import club.devcord.gamejam.devmarkt_working_group.fortmord.micropaper.event.OnEnableEvent;
import club.devcord.gamejam.devmarkt_working_group.fortmord.micropaper.event.OnLoadEvent;
import io.micronaut.context.ApplicationContext;
import io.micronaut.inject.qualifiers.Qualifiers;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabExecutor;
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
    public final void onEnable() {
        context.getBeansOfType(Listener.class)
                .forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
        context.getBeansOfType(CommandExecutor.class, Qualifiers.byStereotype(McCommand.class))
                        .forEach(command -> {
                            var beanDefinition = context.getBeanDefinition(command.getClass());
                            var commandName = beanDefinition.stringValue(McCommand.class).orElseThrow();
                            var pluginCommand = getServer().getPluginCommand(commandName);
                            if (pluginCommand == null) {
                                throw new IllegalArgumentException("Command with name %s not found. You probably forgot to register it in the plugin.yml"
                                        .formatted(commandName));
                            }
                            pluginCommand.setExecutor(command);
                            if (command instanceof TabExecutor tabExecutor) {
                                pluginCommand.setTabCompleter(tabExecutor);
                            }
                        });


        publishEvent(new OnEnableEvent(), OnEnableEvent.class);
    }

    @Override
    public final void onDisable() {
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
