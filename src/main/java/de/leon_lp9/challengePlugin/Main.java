package de.leon_lp9.challengePlugin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.leon_lp9.challengePlugin.challenges.*;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationReader;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import de.leon_lp9.challengePlugin.command.CommandManager;
import de.leon_lp9.challengePlugin.gamerules.GameRule;
import de.leon_lp9.challengePlugin.gamerules.config.LoadGamerule;
import de.leon_lp9.challengePlugin.management.FileUtils;
import de.leon_lp9.challengePlugin.management.Metrics;
import de.leon_lp9.challengePlugin.management.SpigotUpdateChecker;
import de.leon_lp9.challengePlugin.management.TranslationManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public final class Main extends JavaPlugin {
    @Getter
    private static Main instance;

    private ConfigurationReader configurationReader;
    @Getter
    private FileUtils fileUtils;
    private ChallengeManager challengeManager;
    private GameRuleManager gameruleManager;
    @Getter
    private TranslationManager translationManager;
    private Metrics metrics;

    @Override
    public void onEnable() {
        instance = this;
        fileUtils = new FileUtils();
        configurationReader = new ConfigurationReader();
        translationManager = new TranslationManager(this);
        getServer().getPluginManager().registerEvents(translationManager, this);

        loadChallengeManagerFromConfig();
        loadGameRuleManagerFromConfig();
        addChallenges();
        challengeManager.getTimer().startTask();
        challengeManager.getTimer().setResumed(false);
        challengeManager.registerAllAktiveChallenges();
        addGameRules();
        gameruleManager.registerAllGameRules();

        CommandManager commandManager = new CommandManager();
        commandManager.init();

        metrics = new Metrics(this, 20679);
        new SpigotUpdateChecker(this, 107018).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version)) {
                getLogger().warning("There is a new update available.");
                getLogger().warning("Your are using version " + this.getDescription().getVersion());
                getLogger().warning("The latest version is " + version);
                getLogger().warning("Download it here: https://www.spigotmc.org/resources/compactcrates.107018/");
            }
        });
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



        Map<String, Object> data2 = new HashMap<>();

        Map<String, Object> activeGameRules = new HashMap<>();

        for (GameRule activeGamerule : gameruleManager.getGameRules()) {
            activeGameRules.put(activeGamerule.getClass().getName(), activeGamerule);
        }

        data2.put("activeGameRules", activeGameRules);

        fileUtils.writeToJsonFile("GameRuleManager", data2);
    }



    private void loadChallengeManagerFromConfig() {
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
    }

    private void loadGameRuleManagerFromConfig() {
        if (fileUtils.fileExists("GameRuleManager")){
            Map<String, Object> data = fileUtils.readFromJsonFile("GameRuleManager", new TypeToken<Map<String, Object>>() {});
            Gson gson = fileUtils.getGson();
            Timer timer = gson.fromJson(gson.toJson(data.get("timer")), Timer.class);
            List<GameRule> activeGamerules = gson.fromJson(gson.toJson(data.get("activeGameRules")), new TypeToken<Map<String, Object>>() {
                    }).entrySet().stream()
                    .map(e -> {
                        try {
                            return (GameRule) gson.fromJson(gson.toJson(e.getValue()), Class.forName(e.getKey()));
                        } catch (ClassNotFoundException classNotFoundException) {
                            classNotFoundException.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList());
            gameruleManager = new GameRuleManager();
        }else{
            gameruleManager = new GameRuleManager();
        }
    }

    public void addChallenges(){

        //Alle Challenges werden hier hinzugefügt und geladen die ein @LoadChallenge haben

        Reflections reflections = new Reflections("de.leon_lp9.challengePlugin.challenges");

        reflections.getTypesAnnotatedWith(LoadChallenge.class).forEach(aClass -> {
            try {
                Challenge challenge = (Challenge) aClass.newInstance();
                challengeManager.loadChallenge(challenge);
                configurationReader.readConfigurableFields(challenge);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

    }

    public void addGameRules(){
        Reflections reflections = new Reflections("de.leon_lp9.challengePlugin.gamerules");

        reflections.getTypesAnnotatedWith(LoadGamerule.class).forEach(aClass -> {
            System.out.println(aClass);
            gameruleManager.addGameRuleIfNotExists((Class<? extends GameRule>) aClass);
        });
    }

}
