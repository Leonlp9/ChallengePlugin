package de.leon_lp9.challengePlugin.gamerules;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.gamerules.config.LoadGamerule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

@LoadGamerule
public class DamageMessages extends GameRule {

    @ConfigurationValue(title = "damageGameRuleShowToAllPlayers", description = "damageGameRuleShowToAllPlayersDescription", icon = Material.PLAYER_HEAD)
    private boolean showToAllPlayers;

    public DamageMessages() {
        super(Material.REDSTONE, true);
        showToAllPlayers = true;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.getFinalDamage() == 0){
            return;
        }
        if (event.getEntity() instanceof Player player){
            double damage = (Math.round(event.getFinalDamage() * 100.0) / 100.0) / 2.0;
            if (showToAllPlayers){
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.equals(player)){
                        onlinePlayer.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "damageGameRuleDamageYou").replace("%amount%", "" + damage));
                    }else{
                        onlinePlayer.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "damageGameRuleDamageOther").replace("%player%", player.getName()).replace("%amount%", "" + damage));
                    }
                }
            }else{
                player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "damageGameRuleDamageYou"));
            }
        }
    }

}
