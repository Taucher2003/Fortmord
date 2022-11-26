package club.devcord.gamejam.devmarkt_working_group.fortmord.actions.effects;

import club.devcord.gamejam.devmarkt_working_group.fortmord.FortMord;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.AbstractLevelAction;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.LevelAction;
import io.micronaut.context.annotation.Parameter;
import org.bukkit.entity.Player;

@LevelAction(value = 1, levelBound = true)
public class DamageEffectAction extends AbstractLevelAction {
    public DamageEffectAction(@Parameter Player player, FortMord fortMord, @Parameter int level) {
        super(player, fortMord, level);
    }


}
