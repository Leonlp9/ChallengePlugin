package de.leon_lp9.challengePlugin.commands.gui;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

public class WorldBiomProviderMenu implements Listener{

    public void openInventory(Player player, int startAt) {
        String lang = Main.getInstance().getTranslationManager().getLanguageOfPlayer(player);

        Inventory inventory = new MenuBuilder("§6" + Main.getInstance().getTranslationManager().getTranslation(lang, "worldBiomeProvider"), 7*4, lang).setBackButton(true).setClearCenter(true).build();

        inventory.setItem(4, new ItemBuilder(Material.BARRIER)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "selectAllBioms"))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "selectAllBioms")
                .build());

        int skip = startAt * 27;
        int i = 10;
        for (Biome value : Biome.values()) {
            if(value.equals(Biome.CUSTOM)) continue;

            if (skip > 0) {
                skip--;
                continue;
            }

            inventory.setItem(i, new ItemBuilder(Material.GRASS_BLOCK)
                    .setDisplayName("§6§l" + value.name())
                    .setLore("",
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "leftKlick") + ": " + Main.getInstance().getTranslationManager().getTranslation(lang, "activate"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "biome")
                    .addPersistentDataContainer("biome", PersistentDataType.STRING, value.name())
                    .build());
            i++;
            if (i == 17 || i == 26 || i == 35) {
                i++;
                i++;
            }
            if (i == 44) {

                inventory.setItem(53, new ItemBuilder(Material.ARROW)
                        .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "next"))
                        .addPersistentDataContainer("id", PersistentDataType.STRING, "next")
                        .addPersistentDataContainer("startAt", PersistentDataType.INTEGER, startAt + 1)
                        .setCustomModelData(1)
                        .build());

                break;
            }
        }

        if (startAt != 0) {
            inventory.setItem(45, new ItemBuilder(Material.ARROW)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "previous"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "previous")
                    .addPersistentDataContainer("startAt", PersistentDataType.INTEGER, startAt - 1)
                    .setCustomModelData(1)
                    .build());
        }

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("§6" + Main.getInstance().getTranslationManager().getTranslation(Main.getInstance().getTranslationManager().getLanguageOfPlayer((Player) event.getWhoClicked()), "worldBiomeProvider"))) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getItemMeta() != null) {
                    if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING)) {
                        String id = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING);
                        if (id.equals("back")) {
                            Main.getInstance().getMenus().getWorldGenerationMenu().openInventory(((Player) event.getWhoClicked()));
                        } else if (id.equals("next")) {
                            int startAt = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "startAt"), PersistentDataType.INTEGER);
                            Main.getInstance().getMenus().getWorldBiomProviderMenu().openInventory(((Player) event.getWhoClicked()), startAt);
                        } else if (id.equals("previous")) {
                            int startAt = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "startAt"), PersistentDataType.INTEGER);
                            Main.getInstance().getMenus().getWorldBiomProviderMenu().openInventory(((Player) event.getWhoClicked()), startAt);
                        } else if (id.equals("back")) {
                            Main.getInstance().getMenus().getWorldGenerationMenu().openInventory(((Player) event.getWhoClicked()));
                        } else if (id.equals("biome")) {
                            String biome = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "biome"), PersistentDataType.STRING);
                            Main.getInstance().getWorldGenerationManager().setSingleBiome(Biome.valueOf(biome));
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "biomeSelected").replace("%biome%", biome));
                                player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            });
                        } else if (id.equals("selectAllBioms")) {
                            Main.getInstance().getWorldGenerationManager().setSingleBiome(null);
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "biomeSelected").replace("%biome%", "ALL_BIOMES"));
                                player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            });
                        }
                    }
                }
            }
        }
    }

}
