package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


@LoadChallenge
public class ImmerLaufen extends Challenge {
    public ImmerLaufen() {
        super(Material.IRON_BOOTS);
    }

    @Override
    public void tick() {
        if (!isRunning()){
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.isSprinting()) {
                Location location = player.getLocation();
                Vector velocity = new Vector();
                double rotX = (double)location.getYaw();
                double xz = Math.cos(Math.toRadians(0.0));
                velocity.setX(-xz * Math.sin(Math.toRadians(rotX)));
                velocity.setZ(xz * Math.cos(Math.toRadians(rotX)));
                velocity.multiply(0.5);
                if (player.isSneaking()) {
                    velocity.multiply(0.5);
                } else if (player.isInWater()) {
                    velocity.multiply(0.5);
                } else if (!player.isOnGround()) {
                    velocity.multiply(0.85);
                }

                Vector oldVelocity = player.getVelocity();
                if (oldVelocity.getY() > 0.0) {
                    oldVelocity.multiply(0.9);
                }

                velocity.setY(oldVelocity.getY());
                player.setVelocity(velocity);
            }
        }
    }
}
