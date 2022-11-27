package club.devcord.gamejam.devmarkt_working_group.fortmord.actions.effects;

import club.devcord.gamejam.devmarkt_working_group.fortmord.FortMord;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.AbstractLevelAction;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.LevelAction;
import io.micronaut.context.annotation.Parameter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.PostConstruct;

@LevelAction(70)
public class PoisonEffectAction extends AbstractLevelAction {
    public PoisonEffectAction(@Parameter Player player, @Parameter int level, FortMord fortMord) {
        super(player, level, fortMord);
    }

    @PostConstruct
    public void start() {
        runTaskTimer(() -> {
            if (random(0, 256) >= descLevel()) {
                var duration = random(0, 10) * 20;
                player().addPotionEffect(new PotionEffect(PotionEffectType.POISON, ((int) duration), 0));
            }
        }, 0, (long) descLevel() * 20 * 30);
    }

}
