package club.devcord.gamejam.devmarkt_working_group.fortmord.actions.effects;

import club.devcord.gamejam.devmarkt_working_group.fortmord.FortMord;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.AbstractLevelAction;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.LevelAction;
import io.micronaut.context.annotation.Parameter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.PostConstruct;

@LevelAction(value = 15, levelBound = false)
public class WeaknessEffectAction extends AbstractLevelAction {
    public WeaknessEffectAction(@Parameter Player player, @Parameter int level, FortMord fortMord) {
        super(player, level, fortMord);
    }

    @PostConstruct
    void start() {
        runTaskTimer(this::apply, 5, () -> random(20, 30) * 20);
    }

    private void apply() {
        if(random(0, 257) > descLevel()) {
            player().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
        }
    }
}
