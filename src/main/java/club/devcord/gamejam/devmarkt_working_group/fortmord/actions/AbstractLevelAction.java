package club.devcord.gamejam.devmarkt_working_group.fortmord.actions;

import club.devcord.gamejam.devmarkt_working_group.fortmord.FortMord;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.core.annotation.Introspected;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collection;

@Introspected
public abstract class AbstractLevelAction {

    private final Collection<BukkitTask> tasks = new ArrayList<>();

    private final Player player;
    private final FortMord fortMord;

    public AbstractLevelAction(@Parameter Player player, FortMord fortMord) {
        this.player = player;
        this.fortMord = fortMord;
    }

    public Player player() {
        return player;
    }

    public FortMord fortMord() {
        return fortMord;
    }

    public BukkitTask runTaskTimer(Runnable runnable, long delay, long period) throws IllegalArgumentException {
        var bukkitTask = fortMord.getServer().getScheduler().runTaskTimer(fortMord, runnable, delay, period);
        tasks.add(bukkitTask);
        return bukkitTask;
    }

    @PreDestroy
    public void cancelAllTasks() {
        tasks.forEach(BukkitTask::cancel);
    }
}
