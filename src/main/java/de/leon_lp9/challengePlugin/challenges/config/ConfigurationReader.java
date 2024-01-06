package de.leon_lp9.challengePlugin.challenges.config;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import de.leon_lp9.challengePlugin.commands.gui.ChallengeMenu;
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

public class ConfigurationReader implements Listener {

    public ConfigurationReader() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public void readConfigurableFields(Challenge challenge) {
        Class<? extends Challenge> challengeClass = challenge.getClass();

        List<ConfigurableField> list = Arrays.stream(challengeClass.getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .filter(field -> field.isAnnotationPresent(ConfigurationValue.class))
                .map(ConfigurableField::new)
                .collect(Collectors.toList());

        challenge.setConfigurableFields(list);
    }

    public Inventory openConfigurator(Challenge challenge) {
        Collection<ConfigurableField> configurableFields = challenge.getConfigurableFields();

        int size = (int) Math.max(1, Math.ceil((configurableFields.size() + 1) / 9f)) * 9;

        Inventory inventory = Bukkit.createInventory(null, Math.min(6*9, size), "§6§l" + challenge.getName() + " §7- §6Konfiguration");

        for (ConfigurableField configurableField : configurableFields) {
            ItemBuilder itemBuilder = new ItemBuilder(configurableField.getMetadata().icon())
                    .setDisplayName("§6§l" + configurableField.getMetadata().title())
                    .setLore(
                            "§7" + configurableField.getMetadata().description(),
                            "",
                            "§7Wert: §6" + getDisplayValue(configurableField, challenge),
                            "§7Typ: §6" + getDisplayType(configurableField),
                            "",
                            getDisplayLeftAction(configurableField, challenge),
                            getDisplayRightAction(configurableField, challenge)
                    )
                    .addPersistentDataContainer("cField", PersistentDataType.STRING, configurableField.getId().toString())
                    .addPersistentDataContainer("cId", PersistentDataType.STRING, challenge.getClass().getName());
            inventory.addItem(itemBuilder.build());
        }

        inventory.setItem(size - 1, new ItemBuilder(Material.BARRIER)
                .setDisplayName("§c§lZurück")
                .setLore("§7Klicke hier um zurück zu gehen.")
                .addPersistentDataContainer("cId", PersistentDataType.STRING, challenge.getClass().getName())
                .build());

        return inventory;
    }

    @SneakyThrows
    private String getDisplayValue(ConfigurableField configurableField, Object instance) {
        Object value = configurableField.getField().get(instance);
        if (value instanceof Boolean) {
            return (boolean) value ? "§aAktiviert" : "§cDeaktiviert";
        } else if (value instanceof Number) {
            return value.toString();
        } else if (value instanceof String) {
            return value.toString();
        } else if (value instanceof Enum) {
            return value.toString();
        } else {
            return "§cUnbekannt";
        }
    }

    private String getDisplayType(ConfigurableField field) {
        String lowerCase = field.getType().getSimpleName().toLowerCase();
        return switch (lowerCase) {
            case "boolean" -> "Ein/Aus";
            case "integer", "int", "double", "float" -> "Zahl";
            case "string" -> "Text";
            default -> {
                if (field.getType().isEnum()) {
                    yield field.getType().getSimpleName();
                }
                yield "Unbekannt";
            }
        };
    }

    @SneakyThrows
    private String getDisplayLeftAction(ConfigurableField field, Object instance) {
        Class<?> type = field.getType();
        Object object = field.getField().get(instance);

        String action;
        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            action = !((boolean) object) ? "§aAktivieren" : "§cDeaktivieren";
        } else if (type.isEnum()) {
            action = "§6Vorheriger Wert";
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            action = "§aErhöhen";
        } else {
            action = "§6Unbekannt";
        }
        return "§7Linksklick zum " + action + "§7.";
    }

    @SneakyThrows
    private String getDisplayRightAction(ConfigurableField field, Object instance) {
        Class<?> type = field.getType();
        Object object = field.getField().get(instance);

        String action;
        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            action = ((boolean) object) ? "§aAktivieren" : "§cDeaktivieren";
        } else if (type.isEnum()) {
            action = "§6Nächster Wert";
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            action = "§cVerringern";
        } else {
            action = "§6Unbekannt";
        }
        return "§7Rechtsklick zum " + action + "§7.";
    }

    @SneakyThrows
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().matches("§6§l.* §7- §6Konfiguration")) {
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
                Inventory itemStacks = openConfigurator(challenge);
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
                Inventory itemStacks = openConfigurator(challenge);
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
                Inventory itemStacks = openConfigurator(challenge);
                event.getWhoClicked().openInventory(itemStacks);
            }
        }
    }

}
