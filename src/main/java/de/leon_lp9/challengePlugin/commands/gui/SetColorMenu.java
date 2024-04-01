package de.leon_lp9.challengePlugin.commands.gui;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

public class SetColorMenu implements Listener {

    public void openInventory(Player player, String wichValue) {
        String lang = Main.getInstance().getTranslationManager().getLanguageOfPlayer(player);

        Inventory inventory = new MenuBuilder(Main.getInstance().getTranslationManager().getTranslation(lang, "colorMenu"), 7*3, lang).setBackButton(true).build();

        org.bukkit.Color color;
        if (wichValue.equals("secondColor")) {
            color = org.bukkit.Color.fromRGB(Main.getInstance().getChallengeManager().getTimer().getSecondColor().getRed(), Main.getInstance().getChallengeManager().getTimer().getSecondColor().getGreen(), Main.getInstance().getChallengeManager().getTimer().getSecondColor().getBlue());
        } else if (wichValue.equals("firstColor")) {
            color = org.bukkit.Color.fromRGB(Main.getInstance().getChallengeManager().getTimer().getFirstColor().getRed(), Main.getInstance().getChallengeManager().getTimer().getFirstColor().getGreen(), Main.getInstance().getChallengeManager().getTimer().getFirstColor().getBlue());
        } else {
            color = org.bukkit.Color.fromRGB(0, 0, 0);
        }

        inventory.setItem(10, new ItemBuilder(Material.WATER_BUCKET)
                .setDisplayName("§c§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "addToColor"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "addDescriptionToColor"),
                        "§7Linksklick: §c+1",
                        "§7Shift + Linksklick: §c+25",
                        "§7Rechtsklick: §c+10",
                        "§7Shift + Rechtsklick: §c+100")
                .addPersistentDataContainer("id", PersistentDataType.STRING, "add")
                .addPersistentDataContainer("color", PersistentDataType.STRING, "red")
                .build());

        inventory.setItem(19, new ItemBuilder(Material.RED_DYE)
                .setDisplayName("§c§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "red"))
                        .setLore("§7" + color.getRed())
                .addPersistentDataContainer("id", PersistentDataType.STRING, "red")
                .build());

        inventory.setItem(28, new ItemBuilder(Material.BUCKET)
                .setDisplayName("§c§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "removeFromColor"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "removeDescriptionFromColor"),
                        "§7Linksklick: §c-1",
                        "§7Shift + Linksklick: §c-25",
                        "§7Rechtsklick: §c-10",
                        "§7Shift + Rechtsklick: §c-100")
                .addPersistentDataContainer("id", PersistentDataType.STRING, "remove")
                .addPersistentDataContainer("color", PersistentDataType.STRING, "red")
                .build());

        inventory.setItem(11, new ItemBuilder(Material.WATER_BUCKET)
                .setDisplayName("§a§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "addToColor"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "addDescriptionToColor"),
                        "§7Linksklick: §a+1",
                        "§7Shift + Linksklick: §a+25",
                        "§7Rechtsklick: §a+10",
                        "§7Shift + Rechtsklick: §a+100")
                .addPersistentDataContainer("id", PersistentDataType.STRING, "add")
                .addPersistentDataContainer("color", PersistentDataType.STRING, "green")
                .build());

        inventory.setItem(20, new ItemBuilder(Material.GREEN_DYE)
                .setDisplayName("§a§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "green"))
                        .setLore("§7" + color.getGreen())
                .addPersistentDataContainer("id", PersistentDataType.STRING, "green")
                .build());

        inventory.setItem(29, new ItemBuilder(Material.BUCKET)
                .setDisplayName("§a§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "removeFromColor"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "removeDescriptionFromColor"),
                        "§7Linksklick: §a-1",
                        "§7Shift + Linksklick: §a-25",
                        "§7Rechtsklick: §a-10",
                        "§7Shift + Rechtsklick: §a-100")
                .addPersistentDataContainer("id", PersistentDataType.STRING, "remove")
                .addPersistentDataContainer("color", PersistentDataType.STRING, "green")
                .build());

        inventory.setItem(12, new ItemBuilder(Material.WATER_BUCKET)
                .setDisplayName("§b§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "addToColor"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "addDescriptionToColor"),
                        "§7Linksklick: §b+1",
                        "§7Shift + Linksklick: §b+25",
                        "§7Rechtsklick: §b+10",
                        "§7Shift + Rechtsklick: §b+100")
                .addPersistentDataContainer("id", PersistentDataType.STRING, "add")
                .addPersistentDataContainer("color", PersistentDataType.STRING, "blue")
                .build());

        inventory.setItem(21, new ItemBuilder(Material.BLUE_DYE)
                .setDisplayName("§b§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "blue"))
                        .setLore("§7" + color.getBlue())
                .addPersistentDataContainer("id", PersistentDataType.STRING, "blue")
                .build());

        inventory.setItem(30, new ItemBuilder(Material.BUCKET)
                .setDisplayName("§b§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "removeFromColor"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "removeDescriptionFromColor"),
                        "§7Linksklick: §b-1",
                        "§7Shift + Linksklick: §b-25",
                        "§7Rechtsklick: §b-10",
                        "§7Shift + Rechtsklick: §b-100")
                .addPersistentDataContainer("id", PersistentDataType.STRING, "remove")
                .addPersistentDataContainer("color", PersistentDataType.STRING, "blue")
                .build());

        ItemStack resultColor = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) resultColor.getItemMeta();
        meta.setColor(color);
        resultColor.setItemMeta(meta);

        inventory.setItem(24, new ItemBuilder(resultColor)
                .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "resulColor"))
                .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "resulColorLore"))
                .addPersistentDataContainer("id", PersistentDataType.STRING, "resultColor")
                .addPersistentDataContainer("value", PersistentDataType.STRING, wichValue)
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .build());

        player.openInventory(inventory);

    }

    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        if (event.getView().getTitle().equals(Main.getInstance().getTranslationManager().getTranslation(Main.getInstance().getTranslationManager().getLanguageOfPlayer((Player) event.getWhoClicked()), "colorMenu"))) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new org.bukkit.NamespacedKey(Main.getInstance(), "id"), org.bukkit.persistence.PersistentDataType.STRING)) {
                String id = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new org.bukkit.NamespacedKey(Main.getInstance(), "id"), org.bukkit.persistence.PersistentDataType.STRING);
                String value = event.getInventory().getItem(24).getItemMeta().getPersistentDataContainer().get(new org.bukkit.NamespacedKey(Main.getInstance(), "value"), org.bukkit.persistence.PersistentDataType.STRING);

                Color color = null;
                if (value.equals("secondColor")) {
                    color = org.bukkit.Color.fromRGB(Main.getInstance().getChallengeManager().getTimer().getSecondColor().getRed(), Main.getInstance().getChallengeManager().getTimer().getSecondColor().getGreen(), Main.getInstance().getChallengeManager().getTimer().getSecondColor().getBlue());
                } else if (value.equals("firstColor")) {
                    color = org.bukkit.Color.fromRGB(Main.getInstance().getChallengeManager().getTimer().getFirstColor().getRed(), Main.getInstance().getChallengeManager().getTimer().getFirstColor().getGreen(), Main.getInstance().getChallengeManager().getTimer().getFirstColor().getBlue());
                }

                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                int step = 1;
                if (!event.isShiftClick() && event.isRightClick()) {
                    step = 10;
                } else if (event.isShiftClick() && event.isRightClick()) {
                    step = 100;
                } else if (!event.isShiftClick() && event.isLeftClick()) {
                    step = 1;
                } else if (event.isShiftClick() && event.isLeftClick()) {
                    step = 25;
                }

                if (id.equals("add")) {
                    String colorString = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new org.bukkit.NamespacedKey(Main.getInstance(), "color"), org.bukkit.persistence.PersistentDataType.STRING);
                    if (colorString.equals("red")) {
                        red += step;
                    } else if (colorString.equals("green")) {
                        green += step;
                    } else if (colorString.equals("blue")) {
                        blue += step;
                    }

                    if (red > 255) {
                        red = 255;
                    } else if (red < 0) {
                        red = 0;
                    }

                    if (green > 255) {
                        green = 255;
                    } else if (green < 0) {
                        green = 0;
                    }

                    if (blue > 255) {
                        blue = 255;
                    } else if (blue < 0) {
                        blue = 0;
                    }

                    if (value.equals("secondColor")) {
                        Main.getInstance().getChallengeManager().getTimer().setSecondColor(new java.awt.Color(red, green, blue));
                    } else if (value.equals("firstColor")) {
                        Main.getInstance().getChallengeManager().getTimer().setFirstColor(new java.awt.Color(red, green, blue));
                    }
                    openInventory((Player) event.getWhoClicked(), value);
                } else if (id.equals("remove")) {
                    String colorString = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new org.bukkit.NamespacedKey(Main.getInstance(), "color"), org.bukkit.persistence.PersistentDataType.STRING);
                    if (colorString.equals("red")) {
                        red -= step;
                    } else if (colorString.equals("green")) {
                        green -= step;
                    } else if (colorString.equals("blue")) {
                        blue -= step;
                    }

                    if (red > 255) {
                        red = 255;
                    } else if (red < 0) {
                        red = 0;
                    }

                    if (green > 255) {
                        green = 255;
                    } else if (green < 0) {
                        green = 0;
                    }

                    if (blue > 255) {
                        blue = 255;
                    } else if (blue < 0) {
                        blue = 0;
                    }

                    if (value.equals("secondColor")) {
                        Main.getInstance().getChallengeManager().getTimer().setSecondColor(new java.awt.Color(red, green, blue));
                    } else if (value.equals("firstColor")) {
                        Main.getInstance().getChallengeManager().getTimer().setFirstColor(new java.awt.Color(red, green, blue));
                    }
                    openInventory((Player) event.getWhoClicked(), value);
                } else if (id.equals("resultColor")) {
                    if (value.equals("secondColor")) {
                        Main.getInstance().getChallengeManager().getTimer().setSecondColor(new java.awt.Color(red, green, blue));
                    } else if (value.equals("firstColor")) {
                        Main.getInstance().getChallengeManager().getTimer().setFirstColor(new java.awt.Color(red, green, blue));
                    }
                } else if (id.equals("back")) {
                    Main.getInstance().getMenus().getTimerMenu().openInventory((Player) event.getWhoClicked());
                }
            }
        }
    }

}
