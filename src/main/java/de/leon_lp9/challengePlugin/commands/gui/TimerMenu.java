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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
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

        ItemStack firstColor = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) firstColor.getItemMeta();
        meta.setColor(org.bukkit.Color.fromRGB(Main.getInstance().getChallengeManager().getTimer().getFirstColor().getRed(), Main.getInstance().getChallengeManager().getTimer().getFirstColor().getGreen(), Main.getInstance().getChallengeManager().getTimer().getFirstColor().getBlue()));
        firstColor.setItemMeta(meta);

        inventory.setItem(20, new ItemBuilder(firstColor)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "firstColor"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "firstColorDescription"))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "firstColor")
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .build());

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

        if (Main.getInstance().getChallengeManager().getTimer().getDisplayType().equals(Timer.DisplayType.ActionBar)) {
            inventory.setItem(22, new ItemBuilder(Material.BLUE_CANDLE)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "switchToBossBar"))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "switchToBossBarDescription"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "setdisplaybossbar")
                    .build());
        }else if (Main.getInstance().getChallengeManager().getTimer().getDisplayType().equals(Timer.DisplayType.BossBar)) {
            inventory.setItem(22, new ItemBuilder(Material.RED_CANDLE)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "switchToNone"))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "switchToNoneDescription"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "setdisplaynone")
                    .build());
        }else {
            inventory.setItem(22, new ItemBuilder(Material.PURPLE_CANDLE)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "switchToActionBar"))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "switchToActionBarDescription"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "setdisplayactionbar")
                    .build());
        }

        inventory.setItem(23, new ItemBuilder(Material.BARRIER)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "resetTimer"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "resetTimerDescription"))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "reset")
                .build());

        ItemStack secondColor = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta2 = (LeatherArmorMeta) secondColor.getItemMeta();
        meta2.setColor(org.bukkit.Color.fromRGB(Main.getInstance().getChallengeManager().getTimer().getSecondColor().getRed(), Main.getInstance().getChallengeManager().getTimer().getSecondColor().getGreen(), Main.getInstance().getChallengeManager().getTimer().getSecondColor().getBlue()));
        secondColor.setItemMeta(meta2);

        inventory.setItem(24, new ItemBuilder(secondColor)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "secondColor"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "secondColorDescription"))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "secondColor")
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
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
                            Main.getInstance().getChallengeManager().getTimer().startTimer(ePlayer);
                            openInventory(ePlayer);
                            break;
                        case "pause":
                            Main.getInstance().getChallengeManager().getTimer().stopTimer(ePlayer);
                            openInventory(ePlayer);
                            break;
                        case "background":
                            Main.getInstance().getChallengeManager().getTimer().setBackground(!Main.getInstance().getChallengeManager().getTimer().isBackground(), ePlayer);
                            openInventory(ePlayer);
                            break;
                        case "countdown":
                            Main.getInstance().getChallengeManager().getTimer().setState(Timer.TimerState.Countdown, ePlayer);
                            openInventory(ePlayer);
                            break;
                        case "countup":
                            Main.getInstance().getChallengeManager().getTimer().setState(Timer.TimerState.Countup, ePlayer);
                            openInventory(ePlayer);
                            break;
                        case "back":
                            Main.getInstance().getMenus().getHubMenu().openInventory(ePlayer);
                            break;
                        case "bold":
                            Main.getInstance().getChallengeManager().getTimer().setBold(!Main.getInstance().getChallengeManager().getTimer().isBold(), ePlayer);
                            openInventory(ePlayer);
                            break;
                        case "reset":
                            Main.getInstance().getChallengeManager().getTimer().resetTimer(ePlayer);
                            openInventory(ePlayer);
                            break;
                        case "setdisplaybossbar":
                            Main.getInstance().getChallengeManager().getTimer().setDisplayType(Timer.DisplayType.BossBar, ePlayer);
                            openInventory(ePlayer);
                            break;
                        case "setdisplaynone":
                            Main.getInstance().getChallengeManager().getTimer().setDisplayType(Timer.DisplayType.None, ePlayer);
                            openInventory(ePlayer);
                            break;
                        case "setdisplayactionbar":
                            Main.getInstance().getChallengeManager().getTimer().setDisplayType(Timer.DisplayType.ActionBar, ePlayer);
                            openInventory(ePlayer);
                            break;
                        case "firstColor":
                            Main.getInstance().getMenus().getSetColorMenu().openInventory(ePlayer, "firstColor");
                            break;
                        case "secondColor":
                            Main.getInstance().getMenus().getSetColorMenu().openInventory(ePlayer, "secondColor");
                            break;
                    }
                }
            }
        }
    }

}
