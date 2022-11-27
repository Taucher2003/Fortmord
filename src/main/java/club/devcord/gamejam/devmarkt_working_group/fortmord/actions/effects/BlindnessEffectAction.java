package club.devcord.gamejam.devmarkt_working_group.fortmord.actions.effects;

import club.devcord.gamejam.devmarkt_working_group.fortmord.FortMord;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.AbstractLevelAction;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.LevelAction;
import io.micronaut.context.annotation.Parameter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.PostConstruct;

@LevelAction(30)
public class BlindnessEffectAction extends AbstractLevelAction {


    public BlindnessEffectAction(@Parameter Player player, @Parameter int level, FortMord fortMord) {
        super(player, level, fortMord);
    }


    @PostConstruct
    public void start() {
        runTaskTimer(() -> {
            if (player().hasPotionEffect(PotionEffectType.BLINDNESS)) {
                return;
            }
            var duration = ((int) (random(3, 15) * 20));
            player().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 2));
        }, 5, (descLevel() + 5) * random(0, 15) * 20);
    }
}
