package club.devcord.gamejam.devmarkt_working_group.fortmord.actions.mobs;

import club.devcord.gamejam.devmarkt_working_group.fortmord.FortMord;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.AbstractLevelAction;
import club.devcord.gamejam.devmarkt_working_group.fortmord.actions.LevelAction;
import io.micronaut.context.annotation.Parameter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@LevelAction(value = 1, levelBound = true)
public class BasicMobsSpawner extends AbstractLevelAction {

    public static final Map<EntityType, Integer> BASIC_MOBS = Map.of(
            EntityType.ZOMBIE, 20,
            EntityType.SPIDER, 15,
            EntityType.SKELETON, 25,
            EntityType.WITCH, 15,
            EntityType.CREEPER ,5
    );

    public BasicMobsSpawner(@Parameter Player player,  @Parameter int level, FortMord fortMord) {
        super(player, level, fortMord);
    }

    @PostConstruct
    public void startSpawnListener() {
        runTaskTimer(() -> chooseEntity().ifPresent(entityType -> Entities.spawnEntity(player(), entityType, __ -> {}, 20)),
                0, Math.round((260 - level()) * 3));
    }

    private Optional<EntityType> chooseEntity() {
        for (var entry : BASIC_MOBS.entrySet()) {
            var random = ThreadLocalRandom.current().nextInt(0, 101);
            if (random <= entry.getValue()) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }


}
