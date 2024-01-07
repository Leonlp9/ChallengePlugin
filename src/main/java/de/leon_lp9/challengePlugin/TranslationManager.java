package de.leon_lp9.challengePlugin;

import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TranslationManager implements Listener {

    private final Main plugin;
    private final Map<String, Map<String, String>> translations = new HashMap<>();

    public TranslationManager(Main plugin) {
        this.plugin = plugin;
        loadTranslations();

        System.out.println(translations);
    }

    private void loadTranslations() {

        //Liste alle datein auf die es gibt in /plugins/Challenge/
        File folder = new File("plugins\\Challenge\\translations");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                System.out.println(file);

                if (file.isFile()) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".json")) {
                        String language = fileName.substring(0, fileName.length() - 5);
                        if (!language.equals("ChallengeManager")) {
                            translations.put(language, plugin.getFileUtils().readFromJsonFile("translations\\" + language, new TypeToken<Map<String, String>>() {
                            }));
                        }
                    }
                }
            }
        }

    }

    public String getTranslation(String language, String key) {
        return translations.getOrDefault(language, new HashMap<>()).getOrDefault(key, key).replace("&", "§").replace("%prefix%", translations.getOrDefault(language, new HashMap<>()).getOrDefault("prefix", "prefix"));
    }

    public String getTranslation(Player player, String key) {
        return getTranslation(getLanguageOfPlayer(player), key);
    }

    public String getLanguageOfPlayer(Player player){
        String locale = player.getLocale();
        //mach de_de zu de_DE
        if (locale.contains("_")){
            String[] split = locale.split("_");
            locale = split[0] + "_" + split[1].toUpperCase();
        }
        return locale;
    }

    //playerChangeLocaleEvent
    @EventHandler
    public void onPlayerChangeLocaleEvent(PlayerLocaleChangeEvent event) {
        Player player = event.getPlayer();
        String language = event.getLocale();
        if (language.contains("_")){
            String[] split = language.split("_");
            language = split[0] + "_" + split[1].toUpperCase();
        }
        player.sendMessage(getTranslation(language, "languageChanged"));
        System.out.println(language);
    }

}
