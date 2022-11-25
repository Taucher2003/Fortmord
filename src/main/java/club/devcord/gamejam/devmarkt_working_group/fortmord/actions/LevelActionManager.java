package club.devcord.gamejam.devmarkt_working_group.fortmord.actions;

import club.devcord.gamejam.devmarkt_working_group.fortmord.level.LevelChangeEvent;
import io.micronaut.context.ApplicationContext;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class LevelActionManager {

    private final Map<Player, Collection<AbstractLevelAction>> playerActions = new ConcurrentHashMap<>();

    private final ApplicationContext applicationContext;

    public LevelActionManager(ApplicationContext beanContext) {
        this.applicationContext = beanContext;
    }

    @EventListener
    public void onLevelChange(LevelChangeEvent event) {
        var player = event.player();
        for (var action : applicationContext.getBeanDefinitions(AbstractLevelAction.class, Qualifiers.byStereotype(LevelAction.class))) {
            var requiredLevel = action.getAnnotation(LevelAction.class).intValue().orElseThrow();
            var hasAction = hasAction(player, action.getBeanType());

            if (event.newLevel() >= requiredLevel && !hasAction) {
                var actionInstance = applicationContext.createBean(action.getBeanType(), player);
                playerActions.computeIfAbsent(player, value -> new HashSet<>())
                        .add(actionInstance);
                actionInstance.start();
            } else {
                if (hasAction) {
                    playerActions.get(player)
                            .stream()
                            .filter(activeAction -> activeAction.getClass().equals(action.getBeanType()))
                            .peek(AbstractLevelAction::stop)
                            .forEach(playerActions.get(player)::remove);
                }
            }
        }
    }

    public boolean hasAction(Player player, Class<? extends AbstractLevelAction> action) {
        return playerActions.containsKey(player) && playerActions.get(player)
                .stream()
                .anyMatch(activeAction -> activeAction.getClass().equals(action));
    }
}
