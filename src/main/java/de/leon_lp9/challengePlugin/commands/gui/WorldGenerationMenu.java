package de.leon_lp9.challengePlugin.commands.gui;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.persistence.PersistentDataType;

public class WorldGenerationMenu implements Listener {

    public void openInventory(Player player) {
        String lang = Main.getInstance().getTranslationManager().getLanguageOfPlayer(player);

        Inventory inventory = Bukkit.createInventory(null, 9+9+9, "§6" + Main.getInstance().getTranslationManager().getTranslation(lang, "worldGeneration"));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.PAPER)
                    .setCustomModelData(1)
                    .setDisplayName(" ")
                    .build());
        }

        inventory.setItem(11, new ItemBuilder(Material.BRICKS)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "worldGenerators"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "worldGeneratorsDescription"), "", "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "worldGeneratorsDescription2").replace("%activeWorldGenerator%", Main.getInstance().getWorldGenerationManager().getActiveWorldGenerator().name()))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "worldGenerators")
                .build());

        inventory.setItem(13, new ItemBuilder(Material.DIAMOND_PICKAXE)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "worldPopulators"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "worldPopulatorsDescription"), "", "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "worldPopulatorsDescription2").replace("%activeWorldPopulator%", Main.getInstance().getWorldGenerationManager().getActiveWorldPopulator().name()))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "worldPopulators")
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .build());

        inventory.setItem(15, new ItemBuilder(Material.MYCELIUM)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "worldBiomeProvider"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "worldBiomeProviderDescription"), "", "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "worldBiomeProviderDescription2").replace("%activeWorldBiome%", (Main.getInstance().getWorldGenerationManager().getSingleBiome() == null ? "ALL_BIOMES" : Main.getInstance().getWorldGenerationManager().getSingleBiome().name())))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "worldBiomeProvider")
                .build());

        inventory.setItem(18, new ItemBuilder(Material.RED_DYE)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "regenerateWorlds"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "regenerateWorldsDescription"))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "regenerateWorlds")
                .build());

        inventory.setItem(22, new ItemBuilder(Material.BARRIER)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "back"))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "back")
                .setCustomModelData(1)
                .build());

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("§6" + Main.getInstance().getTranslationManager().getTranslation(Main.getInstance().getTranslationManager().getLanguageOfPlayer((Player) event.getWhoClicked()), "worldGeneration"))) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getItemMeta() != null) {
                    if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING)) {
                        String id = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING);
                        if (id.equals("worldGenerators")) {
                            Main.getInstance().getMenus().getWorldGeneratorsMenu().openInventory(((Player) event.getWhoClicked()));
                        } else if (id.equals("worldPopulators")) {
                            Main.getInstance().getMenus().getWorldPopulatorsMenu().openInventory(((Player) event.getWhoClicked()));
                        } else if (id.equals("back")) {
                            Main.getInstance().getMenus().getHubMenu().openInventory(((Player) event.getWhoClicked()));
                        } else if (id.equals("regenerateWorlds")) {
                            Main.getInstance().getWorldGenerationManager().regenerateWorlds(null);
                        } else if (id.equals("worldBiomeProvider")) {
                            Main.getInstance().getMenus().getWorldBiomProviderMenu().openInventory(((Player) event.getWhoClicked()), 0);
                        }
                    }
                }
            }
        }
    }

}
