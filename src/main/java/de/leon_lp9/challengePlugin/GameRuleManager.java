package de.leon_lp9.challengePlugin;

import de.leon_lp9.challengePlugin.gamerules.GameRule;
import de.leon_lp9.challengePlugin.gamerules.config.LoadGamerule;
import lombok.Getter;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GameRuleManager {

    private final ArrayList<GameRule> gameRules = new ArrayList<>();

    public GameRuleManager(List<GameRule> gameRules){
        this.gameRules.addAll(gameRules);
        gameRules.forEach(gameRule -> Main.getInstance().getConfigurationReader().readConfigurableFields(gameRule));
    }

    public GameRuleManager(){
        Reflections reflections = new Reflections("de.leon_lp9.challengePlugin.gamerules");

        reflections.getTypesAnnotatedWith(LoadGamerule.class).forEach(aClass -> {
            System.out.println(aClass);
            try {
                GameRule gameRule = (GameRule) aClass.newInstance();
                addGameRuleIfNotExists(gameRule);
                Main.getInstance().getConfigurationReader().readConfigurableFields(gameRule);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void addGameRuleIfNotExists(GameRule gameRule){
        if (!classIsInGameRules(gameRule.getClass())) {
            gameRules.add(gameRule);
            Main.getInstance().getConfigurationReader().readConfigurableFields(gameRule);
        }
    }

    public boolean classIsInGameRules(Class<? extends GameRule> gameRuleClass) {
        for (GameRule gamerule : gameRules) {
            if (gamerule.getClass().equals(gameRuleClass)) {
                return true;
            }
        }
        return false;
    }

    public void registerAllGameRules(){
        for (GameRule gameRule : gameRules) {
            if (gameRule.isEnabled()) gameRule.register();
        }
    }

    public boolean isEnable(Class<? extends GameRule> gameRule){
        for (GameRule gameRule1 : gameRules) {
            if (gameRule1.getClass().equals(gameRule)){
                return gameRule1.isEnabled();
            }
        }
        return false;
    }

    public void setEnable(Class<? extends GameRule> gameRule, boolean enable){
        for (GameRule gameRule1 : gameRules) {
            if (gameRule1.getClass().equals(gameRule)){
                gameRule1.setEnabled(enable);
                if (enable){
                    gameRule1.register();
                }else{
                    gameRule1.unregister();
                }
            }
        }
    }

    public GameRule getGameRuleByClass(Class<? extends GameRule> gameRule){
        for (GameRule gameRule1 : gameRules) {
            if (gameRule1.getClass().equals(gameRule)){
                return gameRule1;
            }
        }
        return null;
    }

    public void resetGameRules(){
        for (GameRule gameRule : gameRules) {
            gameRule.unregister();
        }
        gameRules.clear();
        Main.getInstance().setGameruleManager(new GameRuleManager());
    }

}
