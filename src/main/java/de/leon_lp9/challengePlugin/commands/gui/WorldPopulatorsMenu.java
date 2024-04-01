package de.leon_lp9.challengePlugin.commands.gui;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.worldgeneration.WorldGenerationManager;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
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
import org.bukkit.persistence.PersistentDataType;

public class WorldPopulatorsMenu implements Listener {

    public void openInventory(Player player) {
        String lang = Main.getInstance().getTranslationManager().getLanguageOfPlayer(player);

        Inventory inventory = new MenuBuilder("§6" + Main.getInstance().getTranslationManager().getTranslation(lang, "worldPopulators"), WorldGenerationManager.WorldPopulators.values().length, lang).setBackButton(true).setClearCenter(true).build();

        for (WorldGenerationManager.WorldPopulators value : WorldGenerationManager.WorldPopulators.values()) {
            inventory.addItem(new ItemBuilder(value.getIcon())
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, value.name() + "Name"))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, value.name() + "Description"),
                            "",
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "leftKlick") + ": " + Main.getInstance().getTranslationManager().getTranslation(lang, "activate"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, value.name())
                    .build());

        }

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("§6" + Main.getInstance().getTranslationManager().getTranslation(Main.getInstance().getTranslationManager().getLanguageOfPlayer((Player) event.getWhoClicked()), "worldPopulators"))) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getItemMeta() != null) {
                    if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING)) {
                        String id = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING);
                        if (id.equals("back")) {
                            Main.getInstance().getMenus().getWorldGenerationMenu().openInventory(((Player) event.getWhoClicked()));
                        } else {
                            WorldGenerationManager.WorldPopulators worldPopulator = WorldGenerationManager.WorldPopulators.valueOf(id);
                            Main.getInstance().getWorldGenerationManager().setActiveWorldPopulator(worldPopulator);
                            Main.getInstance().getMenus().getWorldGenerationMenu().openInventory(((Player) event.getWhoClicked()));

                            Bukkit.getOnlinePlayers().forEach(player -> {
                                player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "worldPopulatorChanged").replace("%worldPopulator%", Main.getInstance().getTranslationManager().getTranslation(player, worldPopulator.name() + "Name")));
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            });
                        }
                    }
                }
            }
        }
    }

}
