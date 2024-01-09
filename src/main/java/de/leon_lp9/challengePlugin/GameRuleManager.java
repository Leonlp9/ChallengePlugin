package de.leon_lp9.challengePlugin;

import de.leon_lp9.challengePlugin.gamerules.GameRule;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class GameRuleManager {

    private final ArrayList<GameRule> gameRules = new ArrayList<>();

    public void addGameRuleIfNotExists(Class<? extends GameRule> gameRuleClass){
        if (!classIsInGameRules(gameRuleClass)) {
            GameRule gamerule = null;
            try {
                gamerule = (GameRule) gameRuleClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            gameRules.add(gamerule);
            Main.getInstance().getConfigurationReader().readConfigurableFields(gamerule);
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

}
