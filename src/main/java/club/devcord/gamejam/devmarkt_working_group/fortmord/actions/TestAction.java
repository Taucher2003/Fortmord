package club.devcord.gamejam.devmarkt_working_group.fortmord.actions;

import club.devcord.gamejam.devmarkt_working_group.fortmord.FortMord;
import io.micronaut.context.annotation.Parameter;
import org.bukkit.entity.Player;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@LevelAction(1)
public class TestAction extends AbstractLevelAction {
    public TestAction(@Parameter Player player, FortMord fortMord) {
        super(player, fortMord);
    }

    @PostConstruct
    public void start() {
        player().sendMessage("wuhu test action aktiviert");
        player().sendMessage(fortMord().getDataFolder().toString());

        runTaskTimer(() -> player().sendMessage("haha hahah haha"), 0, 10);
    }

    @PreDestroy
    public void stop() {
        player().sendMessage("test action Deeeeaktiviert");
    }
}
