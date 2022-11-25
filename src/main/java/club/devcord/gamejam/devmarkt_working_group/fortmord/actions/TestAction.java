package club.devcord.gamejam.devmarkt_working_group.fortmord.actions;

import org.bukkit.entity.Player;

@LevelAction(1)
public class TestAction extends AbstractLevelAction {
    public TestAction(Player player) {
        super(player);
    }

    @Override
    public void start() {
        player().sendMessage("wuhu test action aktiviert");
    }

    @Override
    public void stop() {
        player().sendMessage("test action Deeeeaktiviert");
    }
}
