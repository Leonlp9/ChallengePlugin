package de.leon_lp9.challengePlugin.commands.gui;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

public class MenuBuilder {

    private boolean backButton;
    private final String inventoryName;
    private final int arrayLength;
    private String lang;
    private boolean clearCenter;
    private Integer nextPage;
    private Integer previousPage;

    public MenuBuilder(String inventoryName, int arrayLength, String lang) {
        this.inventoryName = inventoryName;
        this.arrayLength = arrayLength;
        this.lang = lang;
        backButton = false;
        clearCenter = false;
        nextPage = null;
        previousPage = null;
    }

    public MenuBuilder setBackButton(boolean backButton) {
        this.backButton = backButton;
        return this;
    }

    public MenuBuilder setClearCenter(boolean clearCenter) {
        this.clearCenter = clearCenter;
        return this;
    }

    public MenuBuilder setNextPage(int nextPage) {
        this.nextPage = nextPage;
        return this;
    }

    public MenuBuilder setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
        return this;
    }

    public Inventory build() {
        int size = (int) Math.max(1, (Math.ceil(((long) arrayLength / 7f)) + 2) * 9);

        Inventory inventory = Bukkit.createInventory(null, size, inventoryName);

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.PAPER)
                    .setCustomModelData(1)
                    .setDisplayName(" ")
                    .build());
            if (clearCenter && i%9 == 0 && i != 0 && i < inventory.getSize()-9) {
                i = i+7;
            }
        }

        if (backButton) {
            inventory.setItem(inventory.getSize() - 5, new ItemBuilder(Material.BARRIER)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "back"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "back")
                    .setCustomModelData(1)
                    .build());
        }

        if (nextPage != null) {
            inventory.setItem(inventory.getSize() - 1, new ItemBuilder(Material.ARROW)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "nextPage"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "nextPage")
                    .addPersistentDataContainer("startAt", PersistentDataType.INTEGER, nextPage)
                    .setCustomModelData(1)
                    .build());
        }

        if (previousPage != null) {
            inventory.setItem(inventory.getSize() - 9, new ItemBuilder(Material.ARROW)
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, "previousPage"))
                    .addPersistentDataContainer("id", PersistentDataType.STRING, "previousPage")
                    .addPersistentDataContainer("startAt", PersistentDataType.INTEGER, previousPage)
                    .setCustomModelData(1)
                    .build());
        }

        return inventory;
    }

}
