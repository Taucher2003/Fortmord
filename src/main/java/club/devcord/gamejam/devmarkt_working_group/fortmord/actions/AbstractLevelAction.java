package club.devcord.gamejam.devmarkt_working_group.fortmord.actions;

import club.devcord.gamejam.devmarkt_working_group.fortmord.FortMord;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.core.annotation.Introspected;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.LongSupplier;

@Introspected
public abstract class AbstractLevelAction {

    private final Collection<BukkitTask> tasks = new ArrayList<>();

    private final Player player;
    private final FortMord fortMord;

    private int level;

    public AbstractLevelAction(@Parameter Player player, FortMord fortMord, int level) {
        this.player = player;
        this.fortMord = fortMord;
        this.level = level;
    }

    public Player player() {
        return player;
    }

    public FortMord fortMord() {
        return fortMord;
    }

    public int level() {
        return level;
    }

    public void level(int level) {
        this.level = level;
    }

    public BukkitTask runTaskTimer(Runnable runnable, long delay, long period) throws IllegalArgumentException {
        var bukkitTask = fortMord.getServer().getScheduler().runTaskTimer(fortMord, runnable, delay, period);
        tasks.add(bukkitTask);
        return bukkitTask;
    }

    public void runTaskTimer(Runnable runnable, long delay, LongSupplier period) throws IllegalArgumentException {
        Runnable restarter = () -> {
            runnable.run();
            runTaskTimer(runnable, period.getAsLong(), period);
        };

        tasks.add(fortMord.getServer().getScheduler().runTaskLater(fortMord, restarter, delay));
    }

    @PreDestroy
    public void cancelAllTasks() {
        tasks.forEach(BukkitTask::cancel);
    }
}
