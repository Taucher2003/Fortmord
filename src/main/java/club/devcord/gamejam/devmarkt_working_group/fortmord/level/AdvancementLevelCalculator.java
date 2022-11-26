package club.devcord.gamejam.devmarkt_working_group.fortmord.level;

import club.devcord.gamejam.devmarkt_working_group.fortmord.LevelCalculator;
import io.papermc.paper.advancement.AdvancementDisplay;
import jakarta.inject.Singleton;
import org.bukkit.Server;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import static club.devcord.gamejam.devmarkt_working_group.fortmord.FortMord.iteratorToStream;

@Singleton
public class AdvancementLevelCalculator implements LevelCalculator.Inner {

    private final Server server;

    public AdvancementLevelCalculator(Server server) {
        this.server = server;
    }

    @Override
    public double calculate(Player player) {
        return iteratorToStream(server.advancementIterator())
                .filter(advancement -> player.getAdvancementProgress(advancement).isDone())
                .mapToDouble(this::calculateAdvancement)
                .sum();
    }

    @Override
    public double weight() {
        return 0.02;
    }

    private double calculateAdvancement(Advancement advancement) {
        var result = 0;
        if(advancement.getDisplay() != null && advancement.getDisplay().frame() == AdvancementDisplay.Frame.CHALLENGE) {
            result = 5;
        }

        result += advancement.getCriteria().size();
        result += calculateParents(advancement);

        return result;
    }

    private double calculateParents(Advancement advancement) {
        var parentCount = 0;
        var currentAdvancement = advancement;
        while((currentAdvancement = currentAdvancement.getParent()) != null) {
            parentCount++;
        }
        return parentCount;
    }
}
