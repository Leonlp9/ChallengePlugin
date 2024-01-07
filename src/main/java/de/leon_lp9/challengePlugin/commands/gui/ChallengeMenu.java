package de.leon_lp9.challengePlugin.commands.gui;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ColorBuilder;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

import java.awt.*;
import java.util.Comparator;

public class ChallengeMenu {

    public void openInventory(Player player) {
        String lang = Main.getInstance().getTranslationManager().getLanguageOfPlayer(player);

        Inventory inventory = Bukkit.createInventory(null, 9, "§6" + Main.getInstance().getTranslationManager().getTranslation(lang, "challenges"));

        Main.getInstance().getChallengeManager().getAllChallenges().values().stream().sorted(Comparator.comparing(Challenge::getName)).forEach(challenge -> {
            inventory.addItem(new ItemBuilder(challenge.getIcon())
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, challenge.getName()))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, challenge.getDescription()),
                            "",
                            Main.getInstance().getChallengeManager().isChallengeActive(challenge.getClass())
                                    ? "§7" + Main.getInstance().getTranslationManager().getTranslation(player, "status") + ": §a" + Main.getInstance().getTranslationManager().getTranslation(player, "enabled")
                                    : "§7" + Main.getInstance().getTranslationManager().getTranslation(player, "status") + ": §c" + Main.getInstance().getTranslationManager().getTranslation(player, "disabled"),
                            "",
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(player, "leftKlick") + ": " + new ColorBuilder(Main.getInstance().getTranslationManager().getTranslation(player, "activate")).addColorToString(new Color(151, 199, 156, 255)).getText() + " §7/ " + new ColorBuilder(Main.getInstance().getTranslationManager().getTranslation(player, "deactivate")).addColorToString(new Color(182, 144, 144, 255)).getText(),
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(player, "rightKlick") + ": " + new ColorBuilder(Main.getInstance().getTranslationManager().getTranslation(player, "openConfiguration")).addColorToString(new Color(151, 199, 193, 255)).getText())
                    .addPersistentDataContainer("id", PersistentDataType.STRING, challenge.getClass().getName())
                    .build());
        });

        player.openInventory(inventory);
    }

}
