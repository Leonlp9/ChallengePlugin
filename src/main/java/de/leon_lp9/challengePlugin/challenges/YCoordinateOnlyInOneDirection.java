package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

@LoadChallenge
public class YCoordinateOnlyInOneDirection extends Challenge{

    public enum Direction {
        UP,
        DOWN
    }

    @ConfigurationValue(title = "YCoordinateOnlyInOneDirectionDirectionName", icon = Material.COMPASS)
    private Direction direction = Direction.DOWN;

    public YCoordinateOnlyInOneDirection() {
        super(Material.ARROW, ChallengeType.MOVEMENT);
        System.out.println("YCoordinateNurInEineRichtung");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!isRunning()) {
            return;
        }
        if (event.getTo().getY() > event.getFrom().getY() && direction == Direction.DOWN) {
            event.getPlayer().setHealth(0);
            getTimer().setResumed(false);
        } else if (event.getTo().getY() < event.getFrom().getY() && direction == Direction.UP) {
            event.getPlayer().setHealth(0);
            getTimer().setResumed(false);
        }
    }
}
