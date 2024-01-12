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

        int size = (int) Math.max(1, Math.ceil((Main.getInstance().getChallengeManager().getAllChallenges().size() + 1) / 9f)) * 9;

        Inventory inventory = Bukkit.createInventory(null, size, "§6" + Main.getInstance().getTranslationManager().getTranslation(lang, "challenges"));

        Main.getInstance().getChallengeManager().getAllChallenges().values().stream().sorted(Comparator.comparing(Challenge::getName)).forEach(challenge -> {
            inventory.addItem(new ItemBuilder(challenge.getIcon())
                    .setDisplayName("§6§l" + Main.getInstance().getTranslationManager().getTranslation(lang, challenge.getName()))
                    .setLore("§7" + Main.getInstance().getTranslationManager().getTranslation(lang, challenge.getDescription()),
                            "",
                            Main.getInstance().getChallengeManager().isChallengeActive(challenge.getClass())
                                    ? "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "status") + ": §a" + Main.getInstance().getTranslationManager().getTranslation(lang, "enabled")
                                    : "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "status") + ": §c" + Main.getInstance().getTranslationManager().getTranslation(lang, "disabled"),
                            "",
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "leftKlick") + ": " + new ColorBuilder(Main.getInstance().getTranslationManager().getTranslation(lang, "activate")).addColorToString(new Color(151, 199, 156, 255)).getText() + " §7/ " + new ColorBuilder(Main.getInstance().getTranslationManager().getTranslation(lang, "deactivate")).addColorToString(new Color(182, 144, 144, 255)).getText(),
                            "§7" + Main.getInstance().getTranslationManager().getTranslation(lang, "rightKlick") + ": " + new ColorBuilder(Main.getInstance().getTranslationManager().getTranslation(lang, "openConfiguration")).addColorToString(new Color(151, 199, 193, 255)).getText())
                    .addPersistentDataContainer("id", PersistentDataType.STRING, challenge.getClass().getName())
                    .build());
        });

        player.openInventory(inventory);
    }

}
