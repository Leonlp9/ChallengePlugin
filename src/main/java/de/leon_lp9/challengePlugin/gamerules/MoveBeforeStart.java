package de.leon_lp9.challengePlugin.gamerules;

import de.leon_lp9.challengePlugin.gamerules.config.LoadGamerule;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

@LoadGamerule
public class MoveBeforeStart extends GameRule{
    public MoveBeforeStart() {
        super(Material.IRON_BOOTS, true);
    }

    @EventHandler
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        if (isEnabled() && !getPlugin().getChallengeManager().getTimer().isResumed() && getPlugin().getChallengeManager().getTimer().getSeconds() == 0 && !event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            event.setCancelled(true);
        }
    }
}
