package de.leon_lp9.challengePlugin;

import de.leon_lp9.challengePlugin.challenges.Challenge;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ChallengeManager {

    private final ArrayList<Challenge> activeChallenges = new ArrayList<>();
    private final Map<Class<? extends Challenge>, Challenge> allChallenges = new HashMap<>();
    private final Timer timer;

    public ChallengeManager(){
        timer = new Timer(0);
    }

    public ChallengeManager(Timer timer, List<Challenge> activeChallenges){
        this.timer = timer;
        this.activeChallenges.addAll(activeChallenges);
    }

    public void registerAllAktiveChallenges(){
        for (Challenge aktiveChallenge : activeChallenges) {
            aktiveChallenge.register();
        }
    }

    public boolean isChallengeActive(Class<? extends Challenge> challenge){
        for (Challenge aktiveChallenge : activeChallenges) {
            if (aktiveChallenge.getClass().equals(challenge)){
                return true;
            }
        }
        return false;
    }

    /**
     * Speichert die Config der Challenge
     * @param challenge challenge to save
     */
    public void loadChallenge(Challenge challenge){
        if (!isChallengesLoaded(challenge.getClass())){
            allChallenges.put(challenge.getClass(), challenge);
        }
    }

    /**
     * Wird geschaut, ob die Config der Challenge schon geladen ist
     * @param challenge challenge to load
     * @return true if challenge is loaded
     */
    private boolean isChallengesLoaded(Class<? extends Challenge> challenge){
        return allChallenges.containsKey(challenge);
    }

    public void activateChallenge(Class<? extends Challenge> challenge){
        if (!isChallengeActive(challenge)){
            Challenge e = allChallenges.get(challenge);
            activeChallenges.add(e);
            e.register();
        }
    }

    public void deactivateChallenge(Class<? extends Challenge> challenge){
        if (isChallengeActive(challenge)){
            activeChallenges.removeIf(challenge1 -> {
                boolean equals = challenge1.getClass().equals(challenge);
                if (equals){
                    challenge1.unregister();
                }
                return equals;
            });
        }
    }

    public Challenge getActiveChallengeByClass(Class<? extends Challenge> challenge){
        for (Challenge activeChallenge : activeChallenges) {
            if (activeChallenge.getClass().equals(challenge)){
                return activeChallenge;
            }
        }
        return null;
    }

    public Challenge getLoadedChallengeByClass(Class<? extends Challenge> challenge){
        for (Challenge activeChallenge : activeChallenges) {
            if (activeChallenge.getClass().equals(challenge)){
                return activeChallenge;
            }
        }
        return null;
    }

    public void sendChallengeDoneMessage() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(Main.getInstance().getTranslationManager().getTranslation(onlinePlayer, "winMessage").replace("%time%", Main.getInstance().getChallengeManager().getTimer().getFormattedTime()));
        }
    }
}
