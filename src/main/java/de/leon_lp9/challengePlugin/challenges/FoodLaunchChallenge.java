package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

@LoadChallenge
public class FoodLaunchChallenge extends Challenge{

    @ConfigurationValue(title = "JumpHeight", icon = Material.FEATHER, min = 2, max = 10)
    private int value = 4;

    public FoodLaunchChallenge() {
        super(Material.COOKED_BEEF, ChallengeType.MISC);
    }

    @EventHandler
    public void onPlayerItemConsume(@Nonnull PlayerItemConsumeEvent event) {
        if (isPlayerInChallenge(event.getPlayer())) {
            Vector velocityToAdd = new Vector(0, value / 2, 0);

            Vector currentVelocity = new Vector(event.getPlayer().getVelocity().getX(), 0.98 * (event.getPlayer().getVelocity().getY() - 0.08), event.getPlayer().getVelocity().getZ());
            Vector newVelocity = currentVelocity.add(velocityToAdd);
            event.getPlayer().setVelocity(newVelocity);
        }
    }
}
