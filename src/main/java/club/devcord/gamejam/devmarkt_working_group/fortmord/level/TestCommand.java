package club.devcord.gamejam.devmarkt_working_group.fortmord.level;

import club.devcord.gamejam.devmarkt_working_group.fortmord.LevelCalculator;
import io.micronaut.context.ApplicationContext;
import jakarta.inject.Singleton;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
public class TestCommand implements CommandExecutor {

    private final LevelCalculator calculator;
    private final ApplicationContext context;

    public TestCommand(LevelCalculator calculator, ApplicationContext context) {
        this.calculator = calculator;
        this.context = context;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            player.sendMessage("" + calculator.calculate(player));

            if(args.length == 1) {
                var event = new LevelChangeEvent(player, Integer.parseInt(args[0]));
                context.getEventPublisher(LevelChangeEvent.class).publishEvent(event);
            }
        }

        return false;
    }
}
