package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

@LoadChallenge
public class PermanenteItemsChallenge extends Challenge{
    public PermanenteItemsChallenge() {
        super(Material.VINE);
    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onInventoryClick(@Nonnull InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (event.getCursor() != null) {
            if (clickedInventory != null) {
                InventoryType type = event.getWhoClicked().getOpenInventory().getTopInventory().getType();
                if (type != InventoryType.WORKBENCH && type != InventoryType.CRAFTING) {
                    if (clickedInventory.getType() != InventoryType.CRAFTING) {
                        if (clickedInventory.getType() == InventoryType.PLAYER && event.getInventory().getType() != InventoryType.PLAYER) {
                            event.setCancelled(true);
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
        event.setCancelled(true);
    }
}
