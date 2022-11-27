package club.devcord.gamejam.devmarkt_working_group.fortmord.actions.effects;

import club.devcord.gamejam.devmarkt_working_group.fortmord.FortMord;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.AbstractLevelAction;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.LevelAction;
import io.micronaut.context.annotation.Parameter;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import javax.annotation.PostConstruct;

@LevelAction(value = 25, levelBound = false)
public class HealthLimiterAction extends AbstractLevelAction {
    private final Server server;

    public HealthLimiterAction(@Parameter Player player, FortMord fortMord, Server server, @Parameter int level) {
        super(player, level, fortMord);
        this.server = server;
    }

    @PostConstruct
    void start() {
        runTaskTimer(this::apply, 5, () -> random(40, 200));
    }

    private void apply() {
        player().setMaxHealth(random(14, 18));
        server.getScheduler().runTaskLater(fortMord(), player()::resetMaxHealth, random(40, 60));
    }
}
