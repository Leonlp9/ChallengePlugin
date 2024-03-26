package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

@LoadChallenge
public class BlockLaufenSchaden extends Challenge {
    public BlockLaufenSchaden() {
        super(Material.REDSTONE_BLOCK, ChallengeType.DAMAGE);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!isRunning()) {
            return;
        }
        if (event.getFrom().getBlock().getLocation().equals(event.getTo().getBlock().getLocation())) {
            return;
        }

        event.getPlayer().damage(1.0);

    }

}
