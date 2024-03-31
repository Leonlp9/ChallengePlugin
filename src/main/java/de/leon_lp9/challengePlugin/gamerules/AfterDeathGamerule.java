package de.leon_lp9.challengePlugin.gamerules;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.gamerules.config.LoadGamerule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

@LoadGamerule
public class AfterDeathGamerule extends GameRule{

    @ConfigurationValue(title = "RespawnPlayer", icon = Material.PLAYER_HEAD)
    public boolean respawnPlayer = false;

    @ConfigurationValue(title = "StopTimer", icon = Material.CLOCK)
    public boolean stopTimer = true;

    public AfterDeathGamerule() {
        super(Material.SKELETON_SKULL, true);
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event){
        if (!respawnPlayer){

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.sendMessage(" \n§c" + event.getPlayer().getName() + " §7ist gestorben!\n ");
            });

            event.setCancelled(true);
            event.getPlayer().setGameMode(org.bukkit.GameMode.SPECTATOR);

            //droppe alle items aus dem inventar
            for (ItemStack drop : event.getDrops()) {
                Item item = event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), drop);

                Vector velocity = item.getVelocity().multiply(2);

                item.setVelocity(velocity);
            }
            event.getDrops().clear();
            event.getPlayer().getInventory().clear();
            event.getPlayer().getActivePotionEffects().forEach(potionEffect -> event.getPlayer().removePotionEffect(potionEffect.getType()));
        }

        if (stopTimer){
            Main.getInstance().getChallengeManager().getTimer().stopTimer(null);
        }

    }

}
