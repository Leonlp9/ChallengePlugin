package de.leon_lp9.challengePlugin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.leon_lp9.challengePlugin.challenges.*;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationReader;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import de.leon_lp9.challengePlugin.commands.HelpCommand;
import de.leon_lp9.challengePlugin.command.CommandManager;
import de.leon_lp9.challengePlugin.commands.gui.Menus;
import de.leon_lp9.challengePlugin.gamerules.GameRule;
import de.leon_lp9.challengePlugin.management.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public final class Main extends JavaPlugin {
    @Getter
    private static Main instance;
    @Getter

    private ConfigurationReader configurationReader;
    @Getter
    private FileUtils fileUtils;
    @Getter
    @Setter
    private ChallengeManager challengeManager;
    @Getter
    @Setter
    private GameRuleManager gameruleManager;
    @Getter
    private TranslationManager translationManager;
    @Getter
    private PlayerListManager playerListManager;
    @Getter
    private BossBarInformation bossBarInformation;
    @Getter
    PlayerHeadManager playerHeadManager;
    @Getter
    private Menus menus;
    private Metrics metrics;
    private HelpCommand helpEvents;

    @Override
    public void onEnable() {

        requireSpigot();
        requirePaper();

        instance = this;
        fileUtils = new FileUtils();
        configurationReader = new ConfigurationReader();
        translationManager = new TranslationManager(this);
        bossBarInformation = new BossBarInformation();
        playerHeadManager = new PlayerHeadManager();
        getServer().getPluginManager().registerEvents(translationManager, this);
        getServer().getPluginManager().registerEvents(new GlobalEvents(), this);

        loadChallengeManagerFromConfig();
        loadGameRuleManagerFromConfig();
        addChallenges();
        challengeManager.getTimer().startTask();
        challengeManager.getTimer().setResumed(false);
        challengeManager.registerAllAktiveChallenges();
        gameruleManager.registerAllGameRules();
        playerListManager = new PlayerListManager();
        helpEvents = new HelpCommand();
        menus = new Menus();

        CommandManager commandManager = new CommandManager();
        commandManager.init();

        metrics = new Metrics(this, 20679);
        new SpigotUpdateChecker(this, 115834).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version)) {
                getLogger().warning("There is a new update available.");
                getLogger().warning("Your are using version " + this.getDescription().getVersion());
                getLogger().warning("The latest version is " + version);
                getLogger().warning("Download it here: https://www.spigotmc.org/resources/challenges-plugin.115834/");
            }
        });

        if (Bukkit.getWorld("ChallengeWorld") == null) {
            Bukkit.createWorld(new org.bukkit.WorldCreator("ChallengeWorld"));
        }
        if (Bukkit.getWorld("ChallengeWorld_nether") == null) {
            Bukkit.createWorld(new WorldCreator("ChallengeWorld_nether").environment(World.Environment.NETHER));
        }
        if (Bukkit.getWorld("ChallengeWorld_the_end") == null) {
            Bukkit.createWorld(new WorldCreator("ChallengeWorld_the_end").environment(World.Environment.THE_END));
        }
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

        challengeManager.getActiveChallenges().forEach(Challenge::unload);
    }

    private void requireSpigot() {
        try {
            Bukkit.spigot();
        } catch (Throwable var2) {
            getLogger().warning("");
            getLogger().warning("============================== ERROR ==============================");
            getLogger().warning("");
            getLogger().warning("This plugin requires Spigot or Paper to run (Your server: " + Bukkit.getVersion() + ")");
            getLogger().warning("Please ensure you are using Spigot or Paper to utilize this plugin!");
            getLogger().warning("");
            getLogger().warning("Download PaperMC: https://papermc.io/downloads");
            getLogger().warning("Download Spigot: https://getbukkit.org/download/spigot");
            getLogger().warning("");
            getLogger().warning("============================== ERROR ==============================");
            getLogger().warning("");
            throw new IllegalStateException();
        }
    }

    private void requirePaper() {
        try {
            Class.forName("com.destroystokyo.paper.VersionHistoryManager");
        } catch (Throwable var2) {
            getLogger().warning("");
            getLogger().warning("============================== ERROR ==============================");
            getLogger().warning("");
            getLogger().warning("This plugin requires Paper to run (Your server: " + Bukkit.getVersion() + ")");
            getLogger().warning("Please make sure you are using Paper to utilize this plugin!");
            getLogger().warning("");
            getLogger().warning("Download PaperMC: https://papermc.io/downloads");
            getLogger().warning("");
            getLogger().warning("============================== ERROR ==============================");
            getLogger().warning("");
            throw new IllegalStateException();
        }
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
            gameruleManager = new GameRuleManager(activeGamerules);
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

}
