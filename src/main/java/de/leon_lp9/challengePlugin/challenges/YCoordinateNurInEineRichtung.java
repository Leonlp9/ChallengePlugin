package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

@LoadChallenge
public class YCoordinateNurInEineRichtung extends Challenge{

    public enum Direction {
        UP,
        DOWN
    }

    @ConfigurationValue(title = "Richtung", description = "In welche Richtung du dich bewegen darfst", icon = Material.COMPASS)
    private Direction direction = Direction.DOWN;

    public YCoordinateNurInEineRichtung() {
        super("Y Coordinate Nur In Eine Richtung", "Du darfst dich nur entweder nach oben oder nach unten bewegen", Material.BEDROCK);
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
