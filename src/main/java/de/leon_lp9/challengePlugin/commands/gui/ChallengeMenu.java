package de.leon_lp9.challengePlugin.commands.gui;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ColorBuilder;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

        int size = (int) Math.max(1, (Math.ceil((Main.getInstance().getChallengeManager().getAllChallenges().values().stream()
                .filter(challenge -> challenge.getType().equals(type))
                .count() / 9f)) + 1) * 9);

        Inventory inventory = Bukkit.createInventory(null, size, "§6" + Main.getInstance().getTranslationManager().getTranslation(lang, "challenges"));

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

        //fill the rest of the inventory with paper
        for (int i = 1; i < 10; i++) {
            inventory.setItem(inventory.getSize() - i, new ItemBuilder(Material.PAPER)
                    .setCustomModelData(1)
                    .setDisplayName(" ")
                    .build());

        }

        inventory.setItem(size - 5, new ItemBuilder(Material.BARRIER)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "back"))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "back")
                .addPersistentDataContainer("type", PersistentDataType.STRING, type.name())
                .setCustomModelData(1)
                .build());

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

                    ItemStack backItem = event.getInventory().getItem(event.getInventory().getSize() - 5);
                    Challenge.ChallengeType type = Challenge.ChallengeType.valueOf(backItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "type"), PersistentDataType.STRING));

                    Class<? extends Challenge> challengeClass = (Class<? extends Challenge>) Class.forName(id);

                    if (event.getClick().isLeftClick()) {
                        if (!Main.getInstance().getChallengeManager().isChallengeActive(challengeClass)) {
                            Main.getInstance().getChallengeManager().activateChallenge(challengeClass);
                            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "hasBeenActivated").replace("%challenge%", Main.getInstance().getChallengeManager().getLoadedChallengeByClass(challengeClass).getTranslationName(player))));
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
