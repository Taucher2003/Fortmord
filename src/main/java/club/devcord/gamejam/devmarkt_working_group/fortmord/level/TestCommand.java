package club.devcord.gamejam.devmarkt_working_group.fortmord.level;

import club.devcord.gamejam.devmarkt_working_group.fortmord.LevelCalculator;
import club.devcord.gamejam.devmarkt_working_group.fortmord.micropaper.command.McCommand;
import io.micronaut.context.ApplicationContext;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@McCommand("test")
public class TestCommand implements CommandExecutor {

    private final LevelCalculator calculator;
    private final InventoryLevelCalculator inventoryLevelCalculator;
    private final ApplicationContext context;

    public TestCommand(LevelCalculator calculator, InventoryLevelCalculator inventoryLevelCalculator, ApplicationContext context) {
        this.calculator = calculator;
        this.inventoryLevelCalculator = inventoryLevelCalculator;
        this.context = context;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player && player.isOp()) {
            var startCalculate = System.currentTimeMillis();
            var level = calculator.calculate(player);
            var endCalculate = System.currentTimeMillis();
            player.sendMessage(level + " | " + (endCalculate - startCalculate) + "ms");

            if(args.length == 1 && "cache".equals(args[0])) {
                player.sendMessage(inventoryLevelCalculator.getCache().toString());
                return false;
            }

            if(args.length == 1) {
                var event = new LevelChangeEvent(player, Integer.parseInt(args[0]));
                context.getEventPublisher(LevelChangeEvent.class).publishEvent(event);
            }
        }

        return false;
    }
}
