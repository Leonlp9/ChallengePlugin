package de.leon_lp9.challengePlugin.commands.gui;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ColorBuilder;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import lombok.SneakyThrows;
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
import org.bukkit.persistence.PersistentDataType;

import java.awt.*;
import java.util.Comparator;

public class ChallengeMenu implements Listener {

    public void openInventory(Player player, Challenge.ChallengeType type) {
        String lang = Main.getInstance().getTranslationManager().getLanguageOfPlayer(player);

        Inventory inventory = new MenuBuilder("§6" + Main.getInstance().getTranslationManager().getTranslation(lang, "challenges"), Math.toIntExact(Main.getInstance().getChallengeManager().getAllChallenges().values().stream()
                .filter(challenge -> challenge.getType().equals(type))
                .count()), lang).setBackButton(true).setClearCenter(true).build();

        inventory.setItem(4, new ItemBuilder(type.getIcon())
                .setDisplayName("§6§l" + type.getTranslationName(player))
                .setLore("§7" + type.getTranslationDescription(player))
                        .addPersistentDataContainer("type", PersistentDataType.STRING, type.name())
                .build());

        Main.getInstance().getChallengeManager().getAllChallenges().values().stream()
                .filter(challenge -> challenge.getType().equals(type))
                .sorted(Comparator.comparing(Challenge::getName))
                .forEach(challenge -> {
            inventory.addItem(new ItemBuilder(challenge.getIcon())
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, challenge.getName()))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, challenge.getDescription()),
                            "",
                            Main.getInstance().getChallengeManager().isChallengeActive(challenge.getClass())
                                    ? "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "status") + ": §a" + Main.getInstance().getTranslationManager().getTranslation(lang, "enabled")
                                    : "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "status") + ": §c" + Main.getInstance().getTranslationManager().getTranslation(lang, "disabled"),
                            "",
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "leftKlick") + ": " + new ColorBuilder(Main.getInstance().getTranslationManager().getTranslation(lang, "activate")).addColorToString(new Color(151, 199, 156, 255)).getText() + " §7/ " + new ColorBuilder(Main.getInstance().getTranslationManager().getTranslation(lang, "deactivate")).addColorToString(new Color(182, 144, 144, 255)).getText(),
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "rightKlick") + ": " + new ColorBuilder(Main.getInstance().getTranslationManager().getTranslation(lang, "openConfiguration")).addColorToString(new Color(151, 199, 193, 255)).getText(),
                            ""
                            )
                            .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .addPersistentDataContainer("id", PersistentDataType.STRING, challenge.getClass().getName())
                    .build());
        });

        player.openInventory(inventory);
    }

    @SneakyThrows
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player ePlayer)) {
            return;
        }
        if(event.getView().getTitle().equals("§6" + Main.getInstance().getTranslationManager().getTranslation(ePlayer, "challenges"))) {
            event.setCancelled(true);
            ItemStack currentItem = event.getCurrentItem();
            if(currentItem != null) {
                ItemMeta itemMeta = currentItem.getItemMeta();
                if(itemMeta != null) {
                    if (!itemMeta.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING)) return;

                    String id = itemMeta.getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING);

                    if (id.equals("back")) {
                        Main.getInstance().getMenus().getChallengeKategorieMenu().openInventory(ePlayer);
                        return;
                    }

                    ItemStack typeItem = event.getInventory().getItem(4);
                    Challenge.ChallengeType type = Challenge.ChallengeType.valueOf(typeItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "type"), PersistentDataType.STRING));

                    Class<? extends Challenge> challengeClass = (Class<? extends Challenge>) Class.forName(id);

                    if (event.getClick().isLeftClick()) {
                        if (!Main.getInstance().getChallengeManager().isChallengeActive(challengeClass)) {
                            Main.getInstance().getChallengeManager().activateChallenge(challengeClass);
                            if (Main.getInstance().getChallengeManager().isChallengeActive(challengeClass)) {
                                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "hasBeenActivated").replace("%challenge%", Main.getInstance().getChallengeManager().getLoadedChallengeByClass(challengeClass).getTranslationName(player))));
                            }else {
                                Bukkit.getOnlinePlayers().forEach(player -> {
                                    player.sendMessage("§cAn error occurred while activating the challenge! See console for more information.");
                                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                });
                            }
                        } else {
                            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "hasBeenDeactivated").replace("%challenge%", Main.getInstance().getChallengeManager().getLoadedChallengeByClass(challengeClass).getTranslationName(player))));
                            Main.getInstance().getChallengeManager().deactivateChallenge(challengeClass);
                        }
                        new ChallengeMenu().openInventory(((Player) event.getWhoClicked()), type);
                    } else if (event.getClick().isRightClick() && Main.getInstance().getChallengeManager().isChallengeActive(challengeClass)) {
                        Inventory itemStacks = Main.getInstance().getConfigurationReader().openConfigurator(Main.getInstance().getChallengeManager().getActiveChallengeByClass(challengeClass), Main.getInstance().getTranslationManager().getLanguageOfPlayer(((Player) event.getWhoClicked())));
                        event.getWhoClicked().openInventory(itemStacks);
                    }else if (event.getClick().isRightClick() && !Main.getInstance().getChallengeManager().isChallengeActive(challengeClass)){
                        event.getWhoClicked().sendMessage(Main.getInstance().getTranslationManager().getTranslation(((Player) event.getWhoClicked()), "challengeNotActive"));
                    }
                }
            }
        }
    }

}
