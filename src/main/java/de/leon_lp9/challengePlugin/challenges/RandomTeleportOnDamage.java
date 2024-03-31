package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

@LoadChallenge
public class RandomTeleportOnDamage extends Challenge{

    @ConfigurationValue(title = "maxRadius", icon = Material.COMPASS)
    private int maxRadius = 200;

    public RandomTeleportOnDamage() {
        super(Material.ENDER_PEARL, ChallengeType.RANDOMIZER);
    }

    public Location getRandomLocation(Location center, int maxRadius) {

        double x, y, z;
        int attempts = 0;
        do {
            x = center.getX() + (Math.random() * maxRadius * 2) - maxRadius;
            z = center.getZ() + (Math.random() * maxRadius * 2) - maxRadius;
            y = center.getWorld().getHighestBlockYAt((int) x, (int) z);

            if (center.getWorld().getBlockAt((int) x, (int) y, (int) z).getType().equals(Material.BEDROCK)) {
                y = 0;
                do {
                    y++;
                } while (center.getWorld().getBlockAt((int) x, (int) y, (int) z).getType().isSolid());
                y--;
            }

            attempts++;
        }while ((!center.getWorld().getBlockAt((int) x, (int) y, (int) z).getType().isSolid() && attempts < 20) || (center.getWorld().getBlockAt((int) x, (int) y, (int) z).getType().equals(Material.BEDROCK) && attempts < 20));
        y++;

        Location add = new Location(center.getWorld(), x, y, z).getBlock().getLocation().clone().add(0.5, 0, 0.5);
        add.setYaw(center.getYaw());
        add.setPitch(center.getPitch());
        return add;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (isPlayerInChallenge(player)) {
                if (!isRunning()) {
                    return;
                }
                Location randomLocation = getRandomLocation(player.getLocation(), maxRadius);

                getAllSurvivalPlayers().forEach(allPlayers -> {
                    allPlayers.teleport(randomLocation);
                });

            }
        }
    }

}
