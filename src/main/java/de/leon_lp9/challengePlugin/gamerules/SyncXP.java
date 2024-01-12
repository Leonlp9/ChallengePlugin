package de.leon_lp9.challengePlugin.gamerules;

import de.leon_lp9.challengePlugin.gamerules.config.LoadGamerule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

@LoadGamerule
public class SyncXP extends GameRule {
    public SyncXP() {
        super(Material.EXPERIENCE_BOTTLE);
    }

    @EventHandler
    public void onInventoryClick(PlayerExpChangeEvent event) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setExp(event.getAmount());
        });
    }

    @EventHandler
    public void onInventoryClick(PlayerLevelChangeEvent event) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setLevel(event.getNewLevel());
        });
    }



}
