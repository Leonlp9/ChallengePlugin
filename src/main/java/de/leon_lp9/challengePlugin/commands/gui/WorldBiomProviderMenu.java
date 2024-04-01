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
import org.jetbrains.annotations.NotNull;

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

            Material material = getMaterialByBiome(value);

            inventory.setItem(i, new ItemBuilder(material)
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

    @NotNull
    private static Material getMaterialByBiome(Biome value) {
        Material material = Material.GRASS_BLOCK;

        if (value.equals(Biome.BADLANDS) || value.equals(Biome.ERODED_BADLANDS)) {
            material = Material.RED_SAND;
        } else if (value.equals(Biome.BAMBOO_JUNGLE)) {
            material = Material.BAMBOO;
        } else if (value.equals(Biome.BASALT_DELTAS)) {
            material = Material.BASALT;
        } else if (value.equals(Biome.BEACH)) {
            material = Material.SAND;
        } else if (value.equals(Biome.BIRCH_FOREST)) {
            material = Material.BIRCH_LOG;
        } else if (value.equals(Biome.COLD_OCEAN)) {
            material = Material.BLUE_ICE;
        } else if (value.equals(Biome.CRIMSON_FOREST)) {
            material = Material.CRIMSON_NYLIUM;
        } else if (value.equals(Biome.DARK_FOREST) ) {
            material = Material.DARK_OAK_LOG;
        } else if (value.equals(Biome.DEEP_COLD_OCEAN) || value.equals(Biome.DEEP_FROZEN_OCEAN) || value.equals(Biome.DEEP_LUKEWARM_OCEAN) || value.equals(Biome.DEEP_OCEAN)) {
            material = Material.BLUE_ICE;
        } else if (value.equals(Biome.DESERT)) {
            material = Material.SAND;
        } else if (value.equals(Biome.END_BARRENS) || value.equals(Biome.END_HIGHLANDS) || value.equals(Biome.END_MIDLANDS) || value.equals(Biome.SMALL_END_ISLANDS)) {
            material = Material.END_STONE;
        } else if (value.equals(Biome.FLOWER_FOREST)) {
            material = Material.POPPY;
        } else if (value.equals(Biome.FOREST)) {
            material = Material.OAK_LOG;
        } else if (value.equals(Biome.FROZEN_OCEAN) || value.equals(Biome.FROZEN_RIVER)) {
            material = Material.ICE;
        } else if (value.equals(Biome.ICE_SPIKES)) {
            material = Material.PACKED_ICE;
        } else if (value.equals(Biome.JUNGLE)) {
            material = Material.JUNGLE_LOG;
        } else if (value.equals(Biome.LUKEWARM_OCEAN)) {
            material = Material.LAVA_BUCKET;
        } else if (value.equals(Biome.MUSHROOM_FIELDS)) {
            material = Material.MUSHROOM_STEM;
        } else if (value.equals(Biome.NETHER_WASTES)) {
            material = Material.NETHERRACK;
        } else if (value.equals(Biome.OCEAN)) {
            material = Material.WATER_BUCKET;
        } else if (value.equals(Biome.PLAINS)) {
            material = Material.GRASS_BLOCK;
        } else if (value.equals(Biome.RIVER)) {
            material = Material.WATER_BUCKET;
        } else if (value.equals(Biome.SAVANNA) || value.equals(Biome.SAVANNA_PLATEAU)) {
            material = Material.ACACIA_LOG;
        } else if (value.equals(Biome.WARPED_FOREST)){
            material = Material.WARPED_NYLIUM;
        }else if (value.equals(Biome.DRIPSTONE_CAVES)){
            material = Material.DRIPSTONE_BLOCK;
        }else if (value.equals(Biome.SOUL_SAND_VALLEY)){
            material = Material.SOUL_SAND;
        }else if (value.equals(Biome.SUNFLOWER_PLAINS)){
            material = Material.SUNFLOWER;
        }else if (value.equals(Biome.SNOWY_BEACH)) {
            material = Material.SNOW_BLOCK;
        }else if (value.equals(Biome.SNOWY_TAIGA)) {
            material = Material.SNOW_BLOCK;
        }else if (value.equals(Biome.OLD_GROWTH_BIRCH_FOREST)) {
            material = Material.BIRCH_LOG;
        }else if (value.equals(Biome.OLD_GROWTH_SPRUCE_TAIGA)) {
            material = Material.SPRUCE_LOG;
        }else if (value.equals(Biome.OLD_GROWTH_PINE_TAIGA)) {
            material = Material.SPRUCE_LOG;
        }else if (value.equals(Biome.THE_VOID)) {
            material = Material.BEDROCK;
        }else if (value.equals(Biome.MANGROVE_SWAMP)) {
            material = Material.MANGROVE_LOG;
        }else if (value.equals(Biome.WINDSWEPT_HILLS)) {
            material = Material.STONE;
        }else if (value.equals(Biome.TAIGA)) {
            material = Material.SPRUCE_LOG;
        }else if (value.equals(Biome.SWAMP)) {
            material = Material.MUD;
        }else if (value.equals(Biome.THE_END)) {
            material = Material.END_STONE;
        }else if (value.equals(Biome.SNOWY_PLAINS)) {
            material = Material.SNOW_BLOCK;
        }else if (value.equals(Biome.STONY_SHORE)) {
            material = Material.STONE;
        }else if (value.equals(Biome.SPARSE_JUNGLE)) {
            material = Material.JUNGLE_LOG;
        }else if (value.equals(Biome.WINDSWEPT_FOREST)) {
            material = Material.OAK_LOG;
        }else if (value.equals(Biome.WOODED_BADLANDS)) {
            material = Material.RED_SAND;
        }else if (value.equals(Biome.WARM_OCEAN)) {
            material = Material.WATER_BUCKET;
        }else if (value.equals(Biome.WINDSWEPT_GRAVELLY_HILLS)) {
            material = Material.GRAVEL;
        }else if (value.equals(Biome.WINDSWEPT_SAVANNA)) {
            material = Material.ACACIA_LOG;
        }else if (value.equals(Biome.LUSH_CAVES)) {
            material = Material.GLOW_LICHEN;
        }else if (value.equals(Biome.DEEP_DARK)) {
            material = Material.SCULK;
        }else if (value.equals(Biome.MEADOW)) {
            material = Material.GRASS_BLOCK;
        }else if (value.equals(Biome.GROVE)) {
            material = Material.OAK_LOG;
        }else if (value.equals(Biome.SNOWY_SLOPES)) {
            material = Material.SNOW_BLOCK;
        }else if (value.equals(Biome.FROZEN_PEAKS)) {
            material = Material.PACKED_ICE;
        }else if (value.equals(Biome.JAGGED_PEAKS)) {
            material = Material.STONE;
        }else if (value.equals(Biome.STONY_PEAKS)) {
            material = Material.STONE;
        }else if (value.equals(Biome.CHERRY_GROVE)) {
            material = Material.CHERRY_LOG;
        }
        return material;
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
