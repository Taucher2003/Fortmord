package club.devcord.gamejam.devmarkt_working_group.fortmord.actions.effects;

import club.devcord.gamejam.devmarkt_working_group.fortmord.FortMord;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.AbstractLevelAction;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.LevelAction;
import io.micronaut.context.annotation.Parameter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.PostConstruct;

@LevelAction(value = 30, levelBound = false)
public class SlowMiningEffectAction extends AbstractLevelAction {
    public SlowMiningEffectAction(@Parameter Player player, @Parameter int level, FortMord fortMord) {
        super(player, level, fortMord);
    }

    @PostConstruct
    void start() {
        runTaskTimer(this::apply, 5, () -> random(20, 30) * 20);
    }

    private void apply() {
        if(random(0, 257) > descLevel()) {
            player().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 2));
        }
    }
}
