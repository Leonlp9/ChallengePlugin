package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

@LoadChallenge
public class LevelEqualsTeleport extends Challenge{
    public LevelEqualsTeleport() {
        super(Material.ENDER_PEARL, ChallengeType.MOVEMENT);
    }

    @EventHandler
    public void onPlayerMove(PlayerLevelChangeEvent event){
        if (isPlayerInChallenge(event.getPlayer())) {
            if (!isRunning()) {
                return;
            }
            if (event.getNewLevel() > event.getOldLevel()) {
                event.getPlayer().getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
            }
        }
    }
}
