package de.leon_lp9.challengePlugin.commands.gui;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ChallengeKategorieMenu implements Listener {

    public void openInventory(Player player) {
        String lang = Main.getInstance().getTranslationManager().getLanguageOfPlayer(player);

        Inventory inventory = Bukkit.createInventory(null, 9+9+9+9, "§6" + Main.getInstance().getTranslationManager().getTranslation(lang, "challengeKategorieMenu"));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.PAPER)
                    .setCustomModelData(1)
                    .setDisplayName(" ")
                    .build());
        }

        int i = 10;
        for (Challenge.ChallengeType value : Challenge.ChallengeType.values()) {
            int anzahlDerChallengesMitDiesemTyp = Main.getInstance().getChallengeManager().getAllChallenges().values().stream().filter(challenge -> challenge.getType().equals(value)).mapToInt(challenge -> 1).sum();
            int anzahlDerAktivenChallengesMitDiesemTyp = Main.getInstance().getChallengeManager().getAllChallenges().values().stream().filter(challenge -> challenge.getType().equals(value)).filter(challenge -> Main.getInstance().getChallengeManager().isChallengeActive(challenge.getClass())).mapToInt(challenge -> 1).sum();

            inventory.setItem(i, new ItemBuilder(value.getIcon())
                    .setDisplayName("§6§l" + value.getTranslationName(player))
                    .setLore("§7" + value.getTranslationDescription(player),
                            "",
                            "§a" + anzahlDerAktivenChallengesMitDiesemTyp + " §7/ §c" + anzahlDerChallengesMitDiesemTyp,
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "challenges") + " §a" + Main.getInstance().getTranslationManager().getTranslation(lang, "enabled"),
                            "")

                    .addPersistentDataContainer("id", PersistentDataType.STRING, value.name())
                    .build());
            i++;
            if (i == 9+8) {
                i += 2;
            }
        }

        inventory.setItem(9+9+9+4, new ItemBuilder(Material.BARRIER)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "back"))
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
        if(event.getView().getTitle().endsWith(Main.getInstance().getTranslationManager().getTranslation(ePlayer, "challengeKategorieMenu"))) {
            event.setCancelled(true);
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem != null) {
                ItemMeta itemMeta = currentItem.getItemMeta();
                if (itemMeta != null) {
                    if (!itemMeta.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING))
                        return;
                    String id = itemMeta.getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING);

                    if (id.equals("back")) {
                        Main.getInstance().getMenus().getHubMenu().openInventory(ePlayer);
                        return;
                    }

                    Challenge.ChallengeType type = Challenge.ChallengeType.valueOf(id);
                    Main.getInstance().getMenus().getChallengeMenu().openInventory(ePlayer, type);
                }
            }
        }
    }

}
