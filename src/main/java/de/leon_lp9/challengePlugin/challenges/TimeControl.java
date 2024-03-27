package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.ServerTickManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

@LoadChallenge
public class TimeControl extends Challenge{
    private transient List<Player> cooldown = new ArrayList<>();
    private transient ServerTickManager serverTickManager;

    public TimeControl() {
        super(Material.CLOCK, ChallengeType.MISC);
        serverTickManager = Bukkit.getServer().getServerTickManager();
    }

    @Override
    public void unregister() {
        super.unregister();
        unfreeze();

        //entferne alle Items mit dem Tag "timeControl"
        Bukkit.getOnlinePlayers().forEach(player -> {
            for (ItemStack itemStack : player.getInventory()) {
                if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "timeControl"), PersistentDataType.STRING)){
                    if (itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "timeControl"), PersistentDataType.STRING).equals("timeControlPause") || itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "timeControl"), PersistentDataType.STRING).equals("timeControlResume")){
                        player.getInventory().remove(itemStack);
                    }
                }
            }
        });
    }

    @Override
    public void register() {
        super.register();
        unfreeze();

        Bukkit.getOnlinePlayers().forEach(player -> {

            ItemStack timePause = new ItemBuilder(Material.CLOCK).setDisplayName(plugin.getTranslationManager().getTranslation(player, "TimeControlPause")).setLore(plugin.getTranslationManager().getTranslation(player, "TimeControlDescriptionItem")).addPersistentDataContainer("timeControl", PersistentDataType.STRING, "timeControlPause").build();
            boolean hasTimePause = false;

            //entferne alle Items mit dem Tag "timeControl"
            for (ItemStack itemStack : player.getInventory()) {
                if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "timeControl"), PersistentDataType.STRING)){
                    if (itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "timeControl"), PersistentDataType.STRING).equals("timeControlPause") || itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "timeControl"), PersistentDataType.STRING).equals("timeControlResume")){
                        itemStack.setItemMeta(timePause.getItemMeta());
                        hasTimePause = true;
                    }
                }
            }

            if (!hasTimePause) player.getInventory().addItem(timePause);
        });
    }

    public void freeze(){
        serverTickManager.setFrozen(true);

        //Items Tauschen von Pause zu Resume
        Bukkit.getOnlinePlayers().forEach(player -> {
            for (ItemStack itemStack : player.getInventory()) {
                if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "timeControl"), PersistentDataType.STRING)){
                    if (itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "timeControl"), PersistentDataType.STRING).equals("timeControlPause")){
                        ItemStack timeResume = new ItemBuilder(Material.CLOCK).setDisplayName(plugin.getTranslationManager().getTranslation(player, "TimeControlResume")).setLore(plugin.getTranslationManager().getTranslation(player, "TimeControlDescriptionItem")).addPersistentDataContainer("timeControl", PersistentDataType.STRING, "timeControlResume").build();
                        itemStack.setItemMeta(timeResume.getItemMeta());
                    }
                }
            }
        });

    }

    public void unfreeze(){
        serverTickManager.setFrozen(false);

        //Items Tauschen von Resume zu Pause
        Bukkit.getOnlinePlayers().forEach(player -> {
            for (ItemStack itemStack : player.getInventory()) {
                if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "timeControl"), PersistentDataType.STRING)){
                    if (itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "timeControl"), PersistentDataType.STRING).equals("timeControlResume")){
                        ItemStack timePause = new ItemBuilder(Material.CLOCK).setDisplayName(plugin.getTranslationManager().getTranslation(player, "TimeControlPause")).setLore(plugin.getTranslationManager().getTranslation(player, "TimeControlDescriptionItem")).addPersistentDataContainer("timeControl", PersistentDataType.STRING, "timeControlPause").build();
                        itemStack.setItemMeta(timePause.getItemMeta());
                    }
                }
            }
        });

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if (cooldown.contains(e.getPlayer())){
            return;
        }
        if (e.getItem() != null && e.getItem().getType().equals(Material.CLOCK)){
            if (serverTickManager.isFrozen()){
                unfreeze();
                cooldown.add(e.getPlayer());
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    cooldown.remove(e.getPlayer());
                }, 5);
            }else{
                freeze();
                cooldown.add(e.getPlayer());
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    cooldown.remove(e.getPlayer());
                }, 5);
            }
        }
    }

}
