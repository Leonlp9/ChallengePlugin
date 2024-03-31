package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import javax.annotation.Nonnull;

@LoadChallenge
public class AdvancementDamageChallenge extends Challenge{

    @ConfigurationValue(title = "Damage", icon = Material.DIAMOND_SWORD, min = 1, max = 20)
    private int value = 1;

    public AdvancementDamageChallenge() {
        super(Material.KNOWLEDGE_BOOK, ChallengeType.DAMAGE);
    }

    @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
    )
    public void onPlayerAdvancementDone(@Nonnull PlayerAdvancementDoneEvent event) {
        if (isPlayerInChallenge(event.getPlayer())) {
            if (!event.getAdvancement().getKey().toString().contains(":recipes/")) {
                event.getPlayer().setNoDamageTicks(0);
                event.getPlayer().damage(value);
            }
        }
    }
}
