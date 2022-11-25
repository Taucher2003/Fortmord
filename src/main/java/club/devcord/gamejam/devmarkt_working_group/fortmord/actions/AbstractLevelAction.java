package club.devcord.gamejam.devmarkt_working_group.fortmord.actions;

import io.micronaut.core.annotation.Introspected;
import org.bukkit.entity.Player;

@Introspected
public abstract class AbstractLevelAction {
    private final Player player;

    public AbstractLevelAction(Player player) {
        this.player = player;
    }

    public Player player() {
        return player;
    }

    public abstract void start();

    public abstract void stop();
}
