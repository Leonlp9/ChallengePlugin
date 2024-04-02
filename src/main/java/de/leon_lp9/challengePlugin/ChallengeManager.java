package de.leon_lp9.challengePlugin;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class ChallengeManager {

    private final ArrayList<Challenge> activeChallenges = new ArrayList<>();
    private final Map<Class<? extends Challenge>, Challenge> allChallenges = new HashMap<>();
    private transient final Map<Class<? extends Challenge>, BaseAdvancement> advancements = new HashMap<>();
    private final Timer timer;

    private transient AdvancementTab advancementTab;
    private transient UltimateAdvancementAPI api;

    public ChallengeManager(){
        timer = new Timer(0);

    }

    public ChallengeManager(Timer timer, List<Challenge> activeChallenges){
        this.timer = timer;
        this.activeChallenges.addAll(activeChallenges);

    }

    public void createRootAdvancements() {
        api = Main.getInstance().getUltimateAdvancementAPI();

        api.unregisterAdvancementTab("challenges");
        advancementTab = api.createAdvancementTab("challenges");

        RootAdvancement root = new RootAdvancement(advancementTab, "root", new AdvancementDisplay(Material.DRAGON_HEAD, "Finish all challenges", AdvancementFrameType.TASK, true, false, 0, 0), "textures/block/pink_wool.png");

        ArrayList<BaseAdvancement> baseAdvancements = new ArrayList<>();
        ArrayList<BaseAdvancement> typeBaseAdvancements = new ArrayList<>();
        //get all challenges ordered by type
        getAllChallenges().values().stream().
                sorted(Comparator.comparing(Challenge::getType))
            .forEach(challenge -> {
                boolean containsChallengeType = false;
                BaseAdvancement typeBaseAdvancement = null;
                for (BaseAdvancement baseAdvancement : baseAdvancements) {
                    if (baseAdvancement.getKey().equals(challenge.getType().name().toLowerCase())){
                        containsChallengeType = true;
                        typeBaseAdvancement = baseAdvancement;
                        break;
                    }
                }
                if (!containsChallengeType){
                    baseAdvancements.add(new BaseAdvancement(challenge.getType().name().toLowerCase(), new AdvancementDisplay(challenge.getIcon(), challenge.getType().name(), AdvancementFrameType.TASK, false, false, 2, baseAdvancements.size()), root));
                    typeBaseAdvancement = baseAdvancements.get(baseAdvancements.size()-1);
                    typeBaseAdvancements.add(typeBaseAdvancement);
                }

                baseAdvancements.add(new BaseAdvancement(challenge.getName().toLowerCase(), new AdvancementDisplay(challenge.getIcon(), challenge.getName(), AdvancementFrameType.CHALLENGE, true, true, 4, baseAdvancements.size()), typeBaseAdvancement));
                advancements.put(challenge.getClass(), baseAdvancements.get(baseAdvancements.size()-1));
            });

        advancementTab.registerAdvancements(root, baseAdvancements.toArray(new BaseAdvancement[0]));

        advancementTab.automaticallyShowToPlayers();
        advancementTab.automaticallyGrantRootAdvancement();

        advancementTab.registerEvent(PlayerLoadingCompletedEvent.class, e -> {
            //grant all challenge types
            for (BaseAdvancement typeBaseAdvancement : typeBaseAdvancements) {
                typeBaseAdvancement.grant(e.getPlayer());
            }
        });
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
        getActiveChallenges().forEach(Challenge::done);
    }

    public void deactivateAllChallenges(){
        if (activeChallenges.isEmpty()) return;
        try {
            for (Challenge activeChallenge : activeChallenges) {
                deactivateChallenge(activeChallenge.getClass());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        allChallenges.clear();
        Main.getInstance().addChallenges();

    }
}
