package club.devcord.gamejam.devmarkt_working_group.fortmord.actions.misc;

import club.devcord.gamejam.devmarkt_working_group.fortmord.FortMord;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.AbstractLevelAction;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.LevelAction;
import io.micronaut.context.annotation.Parameter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.PostConstruct;

@LevelAction(value = 100, levelBound = true)
public class FallingAnvilAction extends AbstractLevelAction {

    public FallingAnvilAction(@Parameter Player player, FortMord fortMord, @Parameter int level) {
        super(player, level, fortMord);
    }

    @PostConstruct
    public void start() {
        runTaskTimer(() -> {
            var location = player().getLocation().add(0, 25, 0);
            location.getWorld().getBlockAt(location).setType(Material.ANVIL);
        }, 5, descLevel() * random(1, 5) * 20);
    }
}
