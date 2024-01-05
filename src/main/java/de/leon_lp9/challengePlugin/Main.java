package de.leon_lp9.challengePlugin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.leon_lp9.challengePlugin.challenges.*;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationReader;
import de.leon_lp9.challengePlugin.command.CommandManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public final class Main extends JavaPlugin {
    @Getter
    private static Main instance;

    private ConfigurationReader configurationReader;
    private FileUtils fileUtils;
    private ChallengeManager challengeManager;

    @Override
    public void onEnable() {
        instance = this;
        fileUtils = new FileUtils();
        configurationReader = new ConfigurationReader();

        if (fileUtils.fileExists("challengeManager")){
            Map<String, Object> data = fileUtils.readFromJsonFile("ChallengeManager", new TypeToken<Map<String, Object>>() {});
            Gson gson = fileUtils.getGson();
            Timer timer = gson.fromJson(gson.toJson(data.get("timer")), Timer.class);
            List<Challenge> activeChallenges = gson.fromJson(gson.toJson(data.get("activeChallenges")), new TypeToken<Map<String, Object>>() {
                    }).entrySet().stream()
                    .map(e -> {
                        try {
                            return (Challenge) gson.fromJson(gson.toJson(e.getValue()), Class.forName(e.getKey()));
                        } catch (ClassNotFoundException classNotFoundException) {
                            classNotFoundException.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList());
            challengeManager = new ChallengeManager(timer, activeChallenges);
        }else{
            challengeManager = new ChallengeManager();
        }
        challengeManager.getTimer().startTask();
        challengeManager.getTimer().setResumed(false);

        addChallenges();
        challengeManager.activateChallenge(TheFloorIsLava.class);

        challengeManager.registerAllAktiveChallenges();


        CommandManager commandManager = new CommandManager();

        commandManager.init();
    }

    @Override
    public void onDisable() {
        Map<String, Object> data = new HashMap<>();
        data.put("timer", challengeManager.getTimer());

        Map<String, Object> activeChallenges = new HashMap<>();

        for (Challenge aktiveChallenge : challengeManager.getActiveChallenges()) {
            activeChallenges.put(aktiveChallenge.getClass().getName(), aktiveChallenge);
        }

        data.put("activeChallenges", activeChallenges);

        fileUtils.writeToJsonFile("ChallengeManager", data);
    }

    public void addChallenges(){
        challengeManager.loadChallenge(new RandomBlockDrops(true));
        challengeManager.loadChallenge(new TheFloorIsLava());
    }

}
