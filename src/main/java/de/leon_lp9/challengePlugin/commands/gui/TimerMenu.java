package de.leon_lp9.challengePlugin.commands.gui;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.Timer;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import de.leon_lp9.challengePlugin.gamerules.GameRule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class TimerMenu implements Listener {

    public void openInventory(Player player){
        String lang = Main.getInstance().getTranslationManager().getLanguageOfPlayer(player);

        Inventory inventory = player.getServer().createInventory(null, 9*5, Main.getInstance().getTranslationManager().getTranslation(lang, "timermenu"));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.PAPER)
                    .setCustomModelData(1)
                    .setDisplayName(" ")
                    .build());
        }

        if (!Main.getInstance().getChallengeManager().getTimer().isResumed()) {
            inventory.setItem(13, new ItemBuilder(Material.GREEN_DYE)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "startTimer"))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "startTimerDescription"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "start")
                    .build());
        }else {
            inventory.setItem(13, new ItemBuilder(Material.RED_DYE)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "pauseTimer"))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "pauseTimerDescription"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "pause")
                    .build());
        }

        if (Main.getInstance().getChallengeManager().getTimer().isBackground()) {
            inventory.setItem(19, new ItemBuilder(Material.INK_SAC)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "toggleBackgroundOff"))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "toggleBackgroundDescriptionOff"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "background")
                    .build());
        }else {
            inventory.setItem(19, new ItemBuilder(Material.GLOW_INK_SAC)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "toggleBackgroundOn"))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "toggleBackgroundDescriptionOn"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "background")
                    .build());
        }

        if (Main.getInstance().getChallengeManager().getTimer().isBold()) {
            inventory.setItem(21, new ItemBuilder(Material.IRON_NUGGET)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "toggleBoldOff"))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "toggleBoldDescriptionOff"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "bold")
                    .build());
        }else {
            inventory.setItem(21, new ItemBuilder(Material.IRON_INGOT)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "toggleBoldOn"))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "toggleBoldDescriptionOn"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "bold")
                    .build());
        }

        inventory.setItem(23, new ItemBuilder(Material.BARRIER)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "resetTimer"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "resetTimerDescription"))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "reset")
                .build());

        if (Main.getInstance().getChallengeManager().getTimer().getState().equals(Timer.TimerState.Countdown)) {
            inventory.setItem(25, new ItemBuilder(Material.OAK_STAIRS)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "switchtocountup"))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "switchtocountupDescription"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "countup")
                    .build());
        }else {
            inventory.setItem(25, new ItemBuilder(Material.OAK_SLAB)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "switchtocountdown"))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "switchtocountdownDescription"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "countdown")
                    .build());
        }

        inventory.setItem(31+9, new ItemBuilder(Material.BARRIER)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "back"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "backDescription"))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "back")
                .setCustomModelData(1)
                .build());

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player ePlayer)) {
            return;
        }
        if(event.getView().getTitle().equals(Main.getInstance().getTranslationManager().getTranslation(ePlayer, "timermenu"))) {
            event.setCancelled(true);
            ItemStack currentItem = event.getCurrentItem();
            if(currentItem != null) {
                ItemMeta itemMeta = currentItem.getItemMeta();
                if(itemMeta != null) {
                    if (!itemMeta.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING)) return;
                    String id = itemMeta.getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING);
                    switch (id) {
                        case "start":
                            if (Main.getInstance().getChallengeManager().getTimer().getSeconds() == 0) {
                                Main.getInstance().getChallengeManager().getActiveChallenges().forEach(Challenge::timerFirstTimeResume);
                                Main.getInstance().getGameruleManager().getGameRules().forEach(GameRule::timerFirstTimeResume);
                            }

                            Main.getInstance().getChallengeManager().getTimer().setResumed(true);
                            openInventory(ePlayer);
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "timerResumed").replace("%player%", Main.getInstance().getPlayerHeadManager().getHeadComponent(ePlayer) + "§6" + ePlayer.getName()));
                            });
                            break;
                        case "pause":
                            Main.getInstance().getChallengeManager().getTimer().setResumed(false);
                            openInventory(ePlayer);
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "timerPaused").replace("%player%", Main.getInstance().getPlayerHeadManager().getHeadComponent(ePlayer) + "§6" + ePlayer.getName()));
                            });
                            break;
                        case "background":
                            Main.getInstance().getChallengeManager().getTimer().setBackground(!Main.getInstance().getChallengeManager().getTimer().isBackground());
                            openInventory(ePlayer);
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "backgroundChanged").replace("%player%", Main.getInstance().getPlayerHeadManager().getHeadComponent(ePlayer) + "§6" + ePlayer.getName()).replace("%background%", Main.getInstance().getChallengeManager().getTimer().isBackground() ? "§a" + Main.getInstance().getTranslationManager().getTranslation(player, "enabled") : "§c" + Main.getInstance().getTranslationManager().getTranslation(player, "disabled")));
                            });
                            break;
                        case "countdown":
                            Main.getInstance().getChallengeManager().getTimer().setState(Timer.TimerState.Countdown);
                            openInventory(ePlayer);
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "modeChanged").replace("%player%", Main.getInstance().getPlayerHeadManager().getHeadComponent(ePlayer) + "§6" + ePlayer.getName()).replace("%mode%", Main.getInstance().getChallengeManager().getTimer().getState().name()));
                            });
                            break;
                        case "countup":
                            Main.getInstance().getChallengeManager().getTimer().setState(Timer.TimerState.Countup);
                            openInventory(ePlayer);
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "modeChanged").replace("%player%", Main.getInstance().getPlayerHeadManager().getHeadComponent(ePlayer) + "§6" + ePlayer.getName()).replace("%mode%", Main.getInstance().getChallengeManager().getTimer().getState().name()));
                            });
                            break;
                        case "back":
                            Main.getInstance().getMenus().getHubMenu().openInventory(ePlayer);
                            break;
                        case "bold":
                            Main.getInstance().getChallengeManager().getTimer().setBold(!Main.getInstance().getChallengeManager().getTimer().isBold());
                            openInventory(ePlayer);
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "boldChanged").replace("%player%", Main.getInstance().getPlayerHeadManager().getHeadComponent(ePlayer) + "§6" + ePlayer.getName()).replace("%bold%", Main.getInstance().getChallengeManager().getTimer().isBold() ? "§a" + Main.getInstance().getTranslationManager().getTranslation(player, "enabled") : "§c" + Main.getInstance().getTranslationManager().getTranslation(player, "disabled")));
                            });
                            break;
                        case "reset":
                            Main.getInstance().getChallengeManager().getTimer().setSeconds(0);
                            Main.getInstance().getChallengeManager().getTimer().setResumed(false);
                            openInventory(ePlayer);
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "timerReset").replace("%player%", Main.getInstance().getPlayerHeadManager().getHeadComponent(ePlayer) +  ePlayer.getName()));
                            });
                            break;
                    }
                }
            }
        }
    }

}
