package de.leon_lp9.challengePlugin.gamerules;

import de.leon_lp9.challengePlugin.gamerules.config.LoadGamerule;
import io.papermc.paper.event.player.PlayerPickItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@LoadGamerule
public class SyncInventory extends GameRule {
    public SyncInventory() {
        super(Material.CHEST);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        syncInventory((Player) event.getWhoClicked());
    }

    @EventHandler
    public void onItemPickUp(PlayerPickItemEvent event) {
        syncInventory(event.getPlayer());
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        syncInventory(event.getPlayer());
    }

    @EventHandler
    public void onItemDrop(PlayerInteractEvent event) {
        syncInventory(event.getPlayer());
    }

    public void syncInventory(Player ofPlayer){
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(onlinePlayer.getUniqueId().equals(ofPlayer.getUniqueId())) continue;
            onlinePlayer.getInventory().setContents(ofPlayer.getInventory().getContents());
            onlinePlayer.getInventory().setArmorContents(ofPlayer.getInventory().getArmorContents());
            onlinePlayer.updateInventory();
        }
    }

}
