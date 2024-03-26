package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

@LoadChallenge
public class Hydra extends Challenge {
    public Hydra() {
        super(Material.DRAGON_HEAD, ChallengeType.ENTITYS);
    }

    @EventHandler
    public void onEntityDespawn(EntityDeathEvent event) {
        if (isRunning()){
            for (int i = 0; i < 2; i++) {
                event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), event.getEntityType());
            }
        }
    }

    @EventHandler
    public void onTntExplode(EntityExplodeEvent event) {
        if (isRunning()){
            for (int i = 0; i < 2; i++) {
                event.getEntity().getWorld().spawnEntity(event.getLocation(), event.getEntityType());
            }
        }
    }
}
