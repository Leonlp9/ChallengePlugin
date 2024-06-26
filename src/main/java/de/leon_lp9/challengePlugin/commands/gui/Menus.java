package de.leon_lp9.challengePlugin.commands.gui;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

@Getter
public class Menus {

    private ChallengeMenu challengeMenu;
    private ChallengeKategorieMenu challengeKategorieMenu;
    private HubMenu hubMenu;
    private GameRuleMenu gameRuleMenu;
    private TimerMenu timerMenu;
    private WorldGenerationMenu worldGenerationMenu;
    private WorldGeneratorsMenu worldGeneratorsMenu;
    private WorldPopulatorsMenu worldPopulatorsMenu;
    private WorldBiomProviderMenu worldBiomProviderMenu;
    private SetColorMenu setColorMenu;

    public Menus() {
        this.challengeMenu = new ChallengeMenu();
        this.challengeKategorieMenu = new ChallengeKategorieMenu();
        this.hubMenu = new HubMenu();
        this.gameRuleMenu = new GameRuleMenu();
        this.timerMenu = new TimerMenu();
        this.worldGenerationMenu = new WorldGenerationMenu();
        this.worldGeneratorsMenu = new WorldGeneratorsMenu();
        this.worldPopulatorsMenu = new WorldPopulatorsMenu();
        this.worldBiomProviderMenu = new WorldBiomProviderMenu();
        this.setColorMenu = new SetColorMenu();
        Main.getInstance().getServer().getPluginManager().registerEvents(challengeMenu, Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(challengeKategorieMenu, Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(hubMenu, Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(gameRuleMenu, Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(timerMenu, Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(worldGenerationMenu, Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(worldGeneratorsMenu, Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(worldPopulatorsMenu, Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(worldBiomProviderMenu, Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(setColorMenu, Main.getInstance());
    }
}

