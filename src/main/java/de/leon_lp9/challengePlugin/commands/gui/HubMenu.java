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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class HubMenu implements Listener {

    public void openInventory(Player player) {
        String lang = Main.getInstance().getTranslationManager().getLanguageOfPlayer(player);

        Inventory inventory = Bukkit.createInventory(null, 9+9+9, "§6" + Main.getInstance().getTranslationManager().getTranslation(lang, "hubmenu"));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.PAPER)
                    .setCustomModelData(1)
                    .setDisplayName(" ")
                    .build());
        }

        int anzahlDerChallenges = Main.getInstance().getChallengeManager().getAllChallenges().values().stream().mapToInt(challenge -> 1).sum();
        int anzahlDerAktivenChallenges = Main.getInstance().getChallengeManager().getAllChallenges().values().stream().filter(challenge -> Main.getInstance().getChallengeManager().isChallengeActive(challenge.getClass())).mapToInt(challenge -> 1).sum();

        inventory.setItem(10, new ItemBuilder(Material.DIAMOND_SWORD)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "challenges"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "challengesDescription"),
                        "",
                        "§a" + anzahlDerAktivenChallenges + " §7/ §c" + anzahlDerChallenges,
                        "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "challenges") + " §a" + Main.getInstance().getTranslationManager().getTranslation(lang, "enabled"),
                        ""
                        )
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addPersistentDataContainer("id", PersistentDataType.STRING, "challenges")
                .build());

        inventory.setItem(12, new ItemBuilder(Material.COMMAND_BLOCK)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "gamerules"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "gamerulesDescription"))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "gamerules")
                .build());

        inventory.setItem(14, new ItemBuilder(Material.GRASS_BLOCK)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "worldGeneration"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "worldGenerationDescription"))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "worldGeneration")
                .build());

        inventory.setItem(16, new ItemBuilder(Material.CLOCK)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "timer"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "timerDescription"))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "timer")
                .build());

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player ePlayer)) {
            return;
        }
        if(event.getView().getTitle().equals("§6" + Main.getInstance().getTranslationManager().getTranslation(ePlayer, "hubmenu"))) {
            event.setCancelled(true);
            ItemStack currentItem = event.getCurrentItem();
            if(currentItem != null) {
                ItemMeta itemMeta = currentItem.getItemMeta();
                if(itemMeta != null) {
                    if (!itemMeta.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING)) return;
                    String id = itemMeta.getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING);
                    switch (id) {
                        case "challenges":
                            Main.getInstance().getMenus().getChallengeKategorieMenu().openInventory(ePlayer);
                            break;
                        case "gamerules":
                            Main.getInstance().getMenus().getGameRuleMenu().openInventory(ePlayer);
                            break;
                        case "timer":
                            Main.getInstance().getMenus().getTimerMenu().openInventory(ePlayer);
                            break;
                        case "worldGeneration":
                            Main.getInstance().getMenus().getWorldGenerationMenu().openInventory(ePlayer);
                            break;
                    }
                }
            }
        }
    }

}
