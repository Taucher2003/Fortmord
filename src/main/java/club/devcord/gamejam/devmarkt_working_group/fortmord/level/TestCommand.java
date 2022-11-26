package club.devcord.gamejam.devmarkt_working_group.fortmord.level;

import club.devcord.gamejam.devmarkt_working_group.fortmord.LevelCalculator;
import club.devcord.gamejam.devmarkt_working_group.fortmord.micropaper.command.McCommand;
import io.micronaut.context.ApplicationContext;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


@McCommand("test")
public class TestCommand implements TabExecutor {

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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
