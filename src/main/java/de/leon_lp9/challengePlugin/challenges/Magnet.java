package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@LoadChallenge
public class Magnet extends Challenge {

    @ConfigurationValue(title = "MagnetRange", icon = Material.COMPASS)
    int magnetRange = 25;

    public Magnet() {
        super(Material.COMPASS, ChallengeType.MOVEMENT);
    }

    @Override
    public void tick() {
        super.tick();

        if (!isRunning()) return;

        //new sync Thread because of the method is called async
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.getNearbyEntities(magnetRange, magnetRange, magnetRange).forEach(entity -> {
                    if (entity instanceof Player) return;
                    entity.setVelocity(entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(-0.3));
                });
            });
        });
    }

}
