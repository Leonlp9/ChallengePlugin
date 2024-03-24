package de.leon_lp9.challengePlugin.challenges.config;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import de.leon_lp9.challengePlugin.commands.gui.ChallengeMenu;
import de.leon_lp9.challengePlugin.commands.gui.GameRuleMenu;
import de.leon_lp9.challengePlugin.gamerules.GameRule;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ein Listener, der Konfigurationsänderungen für Herausforderungen und Spielregeln in einem Bukkit-Plugin verarbeitet.
 */
public class ConfigurationReader implements Listener {

    /**
     * Konstruktor für den ConfigurationReader. Registriert den Listener beim Bukkit-Plugin.
     */
    public ConfigurationReader() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    /**
     * Liest konfigurierbare Felder aus einer Herausforderungsklasse und speichert sie in der Herausforderung.
     *
     * @param challenge Die Herausforderung, aus der die konfigurierbaren Felder gelesen werden sollen.
     */
    public void readConfigurableFields(Challenge challenge) {
        Class<? extends Challenge> challengeClass = challenge.getClass();

        List<ConfigurableField> list = Arrays.stream(challengeClass.getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .filter(field -> field.isAnnotationPresent(ConfigurationValue.class))
                .map(ConfigurableField::new)
                .collect(Collectors.toList());

        challenge.setConfigurableFields(list);
    }

    /**
     * Liest konfigurierbare Felder aus einer Spielregelklasse und speichert sie in der Spielregel.
     *
     * @param challenge Die Spielregel, aus der die konfigurierbaren Felder gelesen werden sollen.
     */
    public void readConfigurableFields(GameRule challenge) {
        Class<? extends GameRule> challengeClass = challenge.getClass();

        List<ConfigurableField> list = Arrays.stream(challengeClass.getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .filter(field -> field.isAnnotationPresent(ConfigurationValue.class))
                .map(ConfigurableField::new)
                .collect(Collectors.toList());

        challenge.setConfigurableFields(list);
    }

    /**
     * Öffnet einen Konfigurator für eine Herausforderung.
     *
     * @param challenge Die Herausforderung, für die der Konfigurator geöffnet werden soll.
     * @param lang      Die Sprache, in der der Konfigurator angezeigt werden soll.
     * @return Das Inventar des Konfigurators.
     */
    public Inventory openConfigurator(Challenge challenge, String lang) {
        Collection<ConfigurableField> configurableFields = challenge.getConfigurableFields();

        int size = (int) Math.max(1, Math.ceil((configurableFields.size() + 1) / 9f)) * 9;

        Inventory inventory = Bukkit.createInventory(null, Math.min(6*9, size), "§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, challenge.getName()) + " §7- §6" + Main.getInstance().getTranslationManager().getTranslation(lang, "configuration"));

        for (ConfigurableField configurableField : configurableFields) {
            ItemBuilder itemBuilder = new ItemBuilder(configurableField.getMetadata().icon())
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, configurableField.getMetadata().title()))
                    .setLore(
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, configurableField.getMetadata().title() + "Description"),
                            "",
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "value") + ": §6" + getDisplayValue(configurableField, challenge, lang),
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "type") + ": §6" + getDisplayType(configurableField, lang),
                            "",
                            getDisplayLeftAction(configurableField, challenge, lang),
                            getDisplayRightAction(configurableField, challenge, lang)
                    )
                    .addPersistentDataContainer("cField", PersistentDataType.STRING, configurableField.getId().toString())
                    .addPersistentDataContainer("cId", PersistentDataType.STRING, challenge.getClass().getName());
            inventory.addItem(itemBuilder.build());
        }

        inventory.setItem(size - 1, new ItemBuilder(Material.BARRIER)
                .setDisplayName("§c§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "back"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "backDescription"))
                .addPersistentDataContainer("cId", PersistentDataType.STRING, challenge.getClass().getName())
                .build());

        return inventory;
    }

    /**
     * Öffnet einen Konfigurator für eine Spielregel.
     *
     * @param gameRule Die Spielregel, für die der Konfigurator geöffnet werden soll.
     * @param lang     Die Sprache, in der der Konfigurator angezeigt werden soll.
     * @return Das Inventar des Konfigurators.
     */
    public Inventory openConfigurator(GameRule gameRule, String lang){
        Collection<ConfigurableField> configurableFields = gameRule.getConfigurableFields();

        int size = (int) Math.max(1, Math.ceil((configurableFields.size() + 1) / 9f)) * 9;

        Inventory inventory = Bukkit.createInventory(null, Math.min(6*9, size), "§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, gameRule.getName()) + " §7- §6" + Main.getInstance().getTranslationManager().getTranslation(lang, "configurationGameRule"));

        for (ConfigurableField configurableField : configurableFields) {
            ItemBuilder itemBuilder = new ItemBuilder(configurableField.getMetadata().icon())
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, configurableField.getMetadata().title()))
                    .setLore(
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, configurableField.getMetadata().title() + "Description"),
                            "",
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "value") + ": §6" + getDisplayValue(configurableField, gameRule, lang),
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "type") + ": §6" + getDisplayType(configurableField, lang),
                            "",
                            getDisplayLeftAction(configurableField, gameRule, lang),
                            getDisplayRightAction(configurableField, gameRule, lang)
                    )
                    .addPersistentDataContainer("cField", PersistentDataType.STRING, configurableField.getId().toString())
                    .addPersistentDataContainer("cId", PersistentDataType.STRING, gameRule.getClass().getName());
            inventory.addItem(itemBuilder.build());
        }

        inventory.setItem(size - 1, new ItemBuilder(Material.BARRIER)
                .setDisplayName("§c§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "back"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "backDescription"))
                .addPersistentDataContainer("cId", PersistentDataType.STRING, gameRule.getClass().getName())
                .build());

        return inventory;
    }

    @SneakyThrows
    private String getDisplayValue(ConfigurableField configurableField, Object instance, String lang) {
        Object value = configurableField.getField().get(instance);
        if (value instanceof Boolean) {
            return (boolean) value ? "§a" + Main.getInstance().getTranslationManager().getTranslation(lang, "enabled"): "§c" + Main.getInstance().getTranslationManager().getTranslation(lang, "disabled");
        } else if (value instanceof Number) {
            return value.toString();
        } else if (value instanceof String) {
            return value.toString();
        } else if (value instanceof Enum) {
            return value.toString();
        } else {
            return "§c" + Main.getInstance().getTranslationManager().getTranslation(lang, "unknown");
        }
    }

    private String getDisplayType(ConfigurableField field, String lang) {
        String lowerCase = field.getType().getSimpleName().toLowerCase();
        return switch (lowerCase) {
            case "boolean" -> Main.getInstance().getTranslationManager().getTranslation(lang, "onOff");
            case "integer", "int", "double", "float" -> Main.getInstance().getTranslationManager().getTranslation(lang, "number");
            case "string" -> Main.getInstance().getTranslationManager().getTranslation(lang, "text");
            default -> {
                if (field.getType().isEnum()) {
                    yield field.getType().getSimpleName();
                }
                yield Main.getInstance().getTranslationManager().getTranslation(lang, "unknown");
            }
        };
    }

    @SneakyThrows
    private String getDisplayLeftAction(ConfigurableField field, Object instance, String lang) {
        Class<?> type = field.getType();
        Object object = field.getField().get(instance);

        String action;
        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            action = !((boolean) object) ? "§a" + Main.getInstance().getTranslationManager().getTranslation(lang, "activate"): "§c" + Main.getInstance().getTranslationManager().getTranslation(lang, "deactivate");
        } else if (type.isEnum()) {
            action = "§6" + Main.getInstance().getTranslationManager().getTranslation(lang, "previousValue");
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            action = "§a" + Main.getInstance().getTranslationManager().getTranslation(lang, "increase");
        } else {
            action = "§6" + Main.getInstance().getTranslationManager().getTranslation(lang, "unknown");
        }
        return "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "leftKlick") + action + "§7.";
    }

    @SneakyThrows
    private String getDisplayRightAction(ConfigurableField field, Object instance, String lang) {
        Class<?> type = field.getType();
        Object object = field.getField().get(instance);

        String action;
        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            action = ((boolean) object) ? "§a" + Main.getInstance().getTranslationManager().getTranslation(lang, "activate"): "§c" + Main.getInstance().getTranslationManager().getTranslation(lang, "deactivate");
        } else if (type.isEnum()) {
            action = "§6" + Main.getInstance().getTranslationManager().getTranslation(lang, "nextValue");
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            action = "§c" + Main.getInstance().getTranslationManager().getTranslation(lang, "decrease");
        } else {
            action = "§6" + Main.getInstance().getTranslationManager().getTranslation(lang, "unknown");
        }
        return "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "rightKlick") + action + "§7.";
    }

    /**
     * Reagiert auf Klick-Ereignisse im Inventar des Konfigurators.
     *
     * @param event Das Klick-Ereignis.
     */
    @SneakyThrows
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String lang = Main.getInstance().getTranslationManager().getLanguageOfPlayer((Player) event.getWhoClicked());
        if (event.getView().getTitle().matches("§6§l.* §7- §6" + Main.getInstance().getTranslationManager().getTranslation(lang, "configuration"))) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (!event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "cField"), PersistentDataType.STRING)) {
                if (!event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "cID"), PersistentDataType.STRING))
                    return;

                if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
                    new ChallengeMenu().openInventory((Player) event.getWhoClicked());
                }

                return;
            }

            String fieldId = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "cField"), PersistentDataType.STRING);
            String id = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "cId"), PersistentDataType.STRING);
            Class<? extends Challenge> challengeClass = (Class<? extends Challenge>) Class.forName(id);

            Challenge challenge = Main.getInstance().getChallengeManager().getActiveChallengeByClass(challengeClass);

            ConfigurableField configurableField = challenge.getConfigurableFields().stream().filter(field -> field.getId().toString().equals(fieldId)).findFirst().orElse(null);
            if (configurableField == null) return;

            if (configurableField.getType().equals(Boolean.class) || configurableField.getType().equals(boolean.class)) {
                boolean object = (boolean) configurableField.getField().get(challenge);
                configurableField.getField().set(challenge, !object);

                challenge.update();
                Inventory itemStacks = openConfigurator(challenge, lang);
                event.getWhoClicked().openInventory(itemStacks);
            }

            if (configurableField.getType().isEnum()) {
                Enum<?>[] enumConstants = (Enum<?>[]) configurableField.getType().getEnumConstants();
                int ordinal = ((Enum<?>) configurableField.getField().get(challenge)).ordinal();

                int nextOrdinal = ordinal;
                if (event.isLeftClick()) {
                    nextOrdinal = ordinal - 1;
                } else if (event.isRightClick()) {
                    nextOrdinal = ordinal + 1;
                }

                if (nextOrdinal < 0) {
                    nextOrdinal = enumConstants.length - 1;
                } else if (nextOrdinal >= enumConstants.length) {
                    nextOrdinal = 0;
                }

                configurableField.getField().set(challenge, enumConstants[nextOrdinal]);


                challenge.update();
                Inventory itemStacks = openConfigurator(challenge, lang);
                event.getWhoClicked().openInventory(itemStacks);
            }

            if (configurableField.getType().equals(Integer.class) || configurableField.getType().equals(int.class)) {
                int object = (int) configurableField.getField().get(challenge);

                int min = configurableField.getMetadata().min();
                int max = configurableField.getMetadata().max();

                if (event.isLeftClick() && object < max) {
                    configurableField.getField().set(challenge, object + 1);
                } else if (event.isRightClick() && object > min) {
                    configurableField.getField().set(challenge, object - 1);
                }

                challenge.update();
                Inventory itemStacks = openConfigurator(challenge, lang);
                event.getWhoClicked().openInventory(itemStacks);
            }
        }else if (event.getView().getTitle().matches("§6§l.* §7- §6" + Main.getInstance().getTranslationManager().getTranslation(lang, "configurationGameRule"))) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (!event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "cField"), PersistentDataType.STRING)) {
                if (!event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "cID"), PersistentDataType.STRING))
                    return;

                if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
                    new GameRuleMenu().openInventory((Player) event.getWhoClicked());
                }

                return;
            }

            String fieldId = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "cField"), PersistentDataType.STRING);
            String id = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "cId"), PersistentDataType.STRING);
            Class<? extends GameRule> challengeClass = (Class<? extends GameRule>) Class.forName(id);

            GameRule challenge = Main.getInstance().getGameruleManager().getGameRuleByClass(challengeClass);

            ConfigurableField configurableField = challenge.getConfigurableFields().stream().filter(field -> field.getId().toString().equals(fieldId)).findFirst().orElse(null);
            if (configurableField == null) return;

            if (configurableField.getType().equals(Boolean.class) || configurableField.getType().equals(boolean.class)) {
                boolean object = (boolean) configurableField.getField().get(challenge);
                configurableField.getField().set(challenge, !object);

                challenge.update();
                Inventory itemStacks = openConfigurator(challenge, lang);
                event.getWhoClicked().openInventory(itemStacks);
            }

            if (configurableField.getType().isEnum()) {
                Enum<?>[] enumConstants = (Enum<?>[]) configurableField.getType().getEnumConstants();
                int ordinal = ((Enum<?>) configurableField.getField().get(challenge)).ordinal();

                int nextOrdinal = ordinal;
                if (event.isLeftClick()) {
                    nextOrdinal = ordinal - 1;
                } else if (event.isRightClick()) {
                    nextOrdinal = ordinal + 1;
                }

                if (nextOrdinal < 0) {
                    nextOrdinal = enumConstants.length - 1;
                } else if (nextOrdinal >= enumConstants.length) {
                    nextOrdinal = 0;
                }

                configurableField.getField().set(challenge, enumConstants[nextOrdinal]);


                challenge.update();
                Inventory itemStacks = openConfigurator(challenge, lang);
                event.getWhoClicked().openInventory(itemStacks);
            }

            if (configurableField.getType().equals(Integer.class) || configurableField.getType().equals(int.class)) {
                int object = (int) configurableField.getField().get(challenge);

                int min = configurableField.getMetadata().min();
                int max = configurableField.getMetadata().max();
                int step = configurableField.getMetadata().step();

                if (event.isLeftClick() && object < max) {
                    configurableField.getField().set(challenge, object + step);
                } else if (event.isRightClick() && object > min) {
                    configurableField.getField().set(challenge, object - step);
                }

                challenge.update();
                Inventory itemStacks = openConfigurator(challenge, lang);
                event.getWhoClicked().openInventory(itemStacks);
            }
        }
    }

}
