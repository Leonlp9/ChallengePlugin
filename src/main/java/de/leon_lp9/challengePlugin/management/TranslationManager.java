package de.leon_lp9.challengePlugin.management;

import com.google.gson.reflect.TypeToken;
import de.leon_lp9.challengePlugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TranslationManager implements Listener {

    private final Main plugin;
    private final Map<String, Map<String, String>> translations = new HashMap<>();

    public TranslationManager(Main plugin) {
        this.plugin = plugin;
        File directory = new File(Main.getInstance().getDataFolder(), "translations");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Main.getInstance().saveResource("translations/de_DE.json", true);
        Main.getInstance().saveResource("translations/en_US.json", true);
        Main.getInstance().saveResource("translations/en_PT.json", true);
        Main.getInstance().saveResource("translations/lol_US.json", true);
        loadTranslations();
    }

    private void loadTranslations() {

        //Liste alle datein auf die es gibt in /plugins/Challenge/
        File folder = new File("plugins\\Challenge\\translations");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
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

    public String[] getLanguages() {
        return translations.keySet().toArray(new String[0]);
    }

    public String getTranslation(String language, String key) {
        return translations.getOrDefault(language, new HashMap<>()).getOrDefault(key, "%" + key + "%").replace("&", "§").replace("%prefix%", translations.getOrDefault(language, new HashMap<>()).getOrDefault("prefix", "prefix"));
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
        if (getTranslation(language, "languageChanged").equals("%languageChanged%")){
            player.sendMessage("§7You use an unsupported language: §6" + language);
            player.sendMessage("§7Please use one of the following languages: §6" + String.join(", ", getLanguages()));
        }else {
            player.sendMessage(getTranslation(language, "languageChanged"));
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String language = getLanguageOfPlayer(player);
        player.closeInventory();
        if (getTranslation(language, "languageChanged").equals("%languageChanged%")){
            player.sendMessage("§7You use an unsupported language: §6" + language);
            player.sendMessage("§7Please use one of the following languages: §6" + String.join(", ", getLanguages()));
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            player.setTexturePack("https://cdn.discordapp.com/attachments/997527016557518940/1193639359660961832/pack.zip");
        }, 20);
    }

    @EventHandler
    public void onPlayerRecourcePackStatusEvent(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
            player.kickPlayer(Main.getInstance().getTranslationManager().getTranslation(player, "youNeedToAcceptTheResourcePack"));
        }
    }

}
