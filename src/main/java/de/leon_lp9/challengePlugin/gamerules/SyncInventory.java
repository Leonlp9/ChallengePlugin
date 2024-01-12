package de.leon_lp9.challengePlugin.gamerules;

import de.leon_lp9.challengePlugin.gamerules.config.LoadGamerule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

@LoadGamerule
public class SyncInventory extends GameRule {
    public SyncInventory() {
        super(Material.CHEST);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

    }

}
