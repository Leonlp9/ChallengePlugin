package de.leon_lp9.challengePlugin.commands.gui;

import de.leon_lp9.challengePlugin.Main;
import lombok.Getter;

@Getter
public class Menus {

    private ChallengeMenu challengeMenu;
    private ChallengeKategorieMenu challengeKategorieMenu;
    private HubMenu hubMenu;
    private GameRuleMenu gameRuleMenu;
    private TimerMenu timerMenu;

    public Menus() {
        this.challengeMenu = new ChallengeMenu();
        this.challengeKategorieMenu = new ChallengeKategorieMenu();
        this.hubMenu = new HubMenu();
        this.gameRuleMenu = new GameRuleMenu();
        this.timerMenu = new TimerMenu();
        Main.getInstance().getServer().getPluginManager().registerEvents(challengeMenu, Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(challengeKategorieMenu, Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(hubMenu, Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(gameRuleMenu, Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(timerMenu, Main.getInstance());
    }

}
