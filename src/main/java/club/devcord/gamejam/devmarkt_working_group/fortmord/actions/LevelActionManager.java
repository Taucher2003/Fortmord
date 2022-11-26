package club.devcord.gamejam.devmarkt_working_group.fortmord.actions;

import club.devcord.gamejam.devmarkt_working_group.fortmord.level.LevelChangeEvent;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class LevelActionManager {

    private static final Logger log = LoggerFactory.getLogger(LevelActionManager.class);

    private final Map<Player, Collection<AbstractLevelAction>> playerActions = new HashMap<>();

    private final ApplicationContext applicationContext;

    public LevelActionManager(ApplicationContext beanContext) {
        this.applicationContext = beanContext;
    }

    @EventListener
    public void onLevelChange(LevelChangeEvent event) {
        log.info("level change");
        var player = event.player();
        for (var action : applicationContext.getBeanDefinitions(AbstractLevelAction.class)) {
            log.info("bean defintion {}", action.getBeanType());
            var requiredLevel = action.getAnnotation(LevelAction.class).intValue().orElseThrow();
            var hasAction = hasAction(player, action.getBeanType());

            if (event.newLevel() >= requiredLevel) {
                if (!hasAction) {
                    var actionInstance = applicationContext.createBean(action.getBeanType(), player);
                    playerActions.computeIfAbsent(player, value -> ConcurrentHashMap.newKeySet())
                            .add(actionInstance);
                }
            } else if (hasAction) {
                playerActions.get(player)
                        .stream()
                        .filter(activeAction -> activeAction.getClass().equals(action.getBeanType()))
                        .peek(applicationContext::destroyBean)
                        .forEach(playerActions.get(player)::remove);
            }
        }
    }

    public boolean hasAction(Player player, Class<? extends AbstractLevelAction> action) {
        return playerActions.containsKey(player) && playerActions.get(player)
                .stream()
                .anyMatch(activeAction -> activeAction.getClass().equals(action));
    }
}
