package de.leon_lp9.challengePlugin;

import de.leon_lp9.challengePlugin.gamerules.Gamerule;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class GameruleManager {

    private ArrayList<Gamerule> gamerules = new ArrayList<>();

    public void addGameRuleIfNotExists(Class<? extends Gamerule> gameruleClass){
        if (!classIsInGamerules(gameruleClass)) {
            Gamerule gamerule = null;
            try {
                gamerule = (Gamerule) gameruleClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            gamerules.add(gamerule);
        }
    }

    public boolean classIsInGamerules(Class gameruleClass) {
        for (Gamerule gamerule : gamerules) {
            if (gamerule.getClass().equals(gameruleClass)) {
                return true;
            }
        }
        return false;
    }

    public void registerAllGamerules(){
        for (Gamerule gamerule : gamerules) {
            if (gamerule.isEnabled()) gamerule.register();
        }
    }

    public boolean isEnable(Class<? extends Gamerule> gamerule){
        for (Gamerule gamerule1 : gamerules) {
            if (gamerule1.getClass().equals(gamerule)){
                return gamerule1.isEnabled();
            }
        }
        return false;
    }

    public void setEnable(Class<? extends Gamerule> gamerule, boolean enable){
        for (Gamerule gamerule1 : gamerules) {
            if (gamerule1.getClass().equals(gamerule)){
                gamerule1.setEnabled(enable);
                if (enable){
                    gamerule1.register();
                }else{
                    gamerule1.unregister();
                }
            }
        }
    }

}
