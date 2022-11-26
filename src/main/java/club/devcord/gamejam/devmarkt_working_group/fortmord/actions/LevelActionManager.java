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
import java.util.Optional;
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
        var level = event.newLevel();
        var player = event.player();
        for (var action : applicationContext.getBeanDefinitions(AbstractLevelAction.class)) {
            log.info("bean defintion {}", action.getBeanType());
            var actionClass = action.getBeanType();
            var requiredLevel = action.getAnnotation(LevelAction.class).intValue().orElseThrow();
            var levelBound = action.getAnnotation(LevelAction.class).booleanValue("levelBound").orElseThrow();
            var presentAction = findAction(player, action.getBeanType());

            if (level >= requiredLevel) {
                presentAction.ifPresentOrElse(playerAction -> {
                    if (levelBound) {
                        destroyAction(player, actionClass);
                        playerAction = createAction(player, level, actionClass);
                    }
                    playerAction.level(level);
                }, () -> createAction(player, level, actionClass).level(level));
            } else {
                presentAction.ifPresent(__ -> destroyAction(player, actionClass));
            }
        }
    }

    public AbstractLevelAction createAction(Player player, int level, Class<? extends AbstractLevelAction> actionClass) {
        var actionInstance = applicationContext.createBean(actionClass, player, level);
        playerActions.computeIfAbsent(player, value -> ConcurrentHashMap.newKeySet())
                .add(actionInstance);
        return actionInstance;
    }

    public void destroyAction(Player player, Class<? extends AbstractLevelAction> actionClass) {
        playerActions.get(player)
                .stream()
                .filter(activeAction -> activeAction.getClass().equals(actionClass))
                .peek(applicationContext::destroyBean)
                .forEach(playerActions.get(player)::remove);
    }

    public Optional<AbstractLevelAction> findAction(Player player, Class<? extends AbstractLevelAction> action) {
        if (playerActions.containsKey(player)) {
            return playerActions.get(player)
                    .stream()
                    .filter(activeAction -> activeAction.getClass().equals(action))
                    .findFirst();
        }
        return Optional.empty();
    }
}
