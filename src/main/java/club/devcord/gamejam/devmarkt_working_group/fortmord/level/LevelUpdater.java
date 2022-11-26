package club.devcord.gamejam.devmarkt_working_group.fortmord.level;

import club.devcord.gamejam.devmarkt_working_group.fortmord.FortMord;
import club.devcord.gamejam.devmarkt_working_group.fortmord.LevelCalculator;
import club.devcord.gamejam.devmarkt_working_group.fortmord.micropaper.event.OnEnableEvent;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class LevelUpdater {

    private final LevelCalculator calculator;
    private final ApplicationContext context;
    private final Server server;
    private final FortMord fortMord;
    private final Map<Player, Integer> currentPlayerLevels = new HashMap<>();

    public LevelUpdater(LevelCalculator calculator, ApplicationContext context, Server server, FortMord fortMord) {
        this.calculator = calculator;
        this.context = context;
        this.server = server;
        this.fortMord = fortMord;
    }

    @EventListener
    void onEnable(OnEnableEvent event) {
        server.getScheduler().runTaskTimer(fortMord, this::schedule, 10, 10);
    }

    void schedule() {
        server.getOnlinePlayers().forEach(this::updateForPlayer);
    }

    private void updateForPlayer(Player player) {
        var oldLevel = currentPlayerLevels.getOrDefault(player, 0);
        var newLevel = calculator.calculate(player) & 0xFF;
        if(oldLevel != newLevel) {
            var event = new LevelChangeEvent(player, newLevel);
            context.getEventPublisher(LevelChangeEvent.class).publishEvent(event);
        }
    }
}
