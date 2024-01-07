package de.leon_lp9.challengePlugin.gamerules;

import de.leon_lp9.challengePlugin.gamerules.config.LoadGamerule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

@LoadGamerule
public class DamageMessages extends Gamerule{

    private boolean showToAllPlayers;

    public DamageMessages() {
        super(Material.REDSTONE, true);
        showToAllPlayers = true;
    }

    @EventHandler
    public void onDammage(EntityDamageEvent event){
        if (event.getEntity() instanceof Player player){

        }
    }

}
