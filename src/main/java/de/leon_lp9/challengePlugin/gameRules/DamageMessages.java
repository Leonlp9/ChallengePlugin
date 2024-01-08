package de.leon_lp9.challengePlugin.gameRules;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.gameRules.config.LoadGamerule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

@LoadGamerule
public class DamageMessages extends GameRule {

    private boolean showToAllPlayers;

    public DamageMessages() {
        super(Material.REDSTONE, true);
        showToAllPlayers = true;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.getEntity() instanceof Player player){
            if (showToAllPlayers){
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.equals(player)){
                        onlinePlayer.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "damageGameRuleDamageYou"));
                    }else{
                        onlinePlayer.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "damageGameRuleDamageOther").replace("%player%", player.getName()));
                    }
                }
            }else{
                player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "damageGameRuleDamageYou"));
            }
        }
    }

}
