package de.leon_lp9.challengePlugin.gamerules;

import de.leon_lp9.challengePlugin.gamerules.config.LoadGamerule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

@LoadGamerule
public class BackPack extends GameRule {

    private final transient Inventory inventory = Bukkit.createInventory(null, 9*3, "BackPack");

    public BackPack(){
        super(Material.CHEST);
    }

    public void openInventory(org.bukkit.entity.Player player){
        player.openInventory(inventory);
    }

}
