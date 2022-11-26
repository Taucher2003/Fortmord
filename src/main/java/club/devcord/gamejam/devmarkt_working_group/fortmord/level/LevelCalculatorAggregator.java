package club.devcord.gamejam.devmarkt_working_group.fortmord.level;

import club.devcord.gamejam.devmarkt_working_group.fortmord.LevelCalculator;
import jakarta.inject.Singleton;
import org.bukkit.entity.Player;

import java.util.Arrays;

@Singleton
public class LevelCalculatorAggregator implements LevelCalculator {

    private final LevelCalculator.Inner[] calculators;

    public LevelCalculatorAggregator(LevelCalculator.Inner[] calculators) {
        this.calculators = calculators;
    }

    @Override
    public int calculate(Player player) {
        return Arrays.stream(calculators)
                .map(calculator -> calculator.calculate(player) * calculator.weight())
                .reduce(0D, Double::sum)
                .intValue();
    }
}
