package de.leon_lp9.challengePlugin.management;

import de.leon_lp9.challengePlugin.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class GlobalEvents implements Listener {


    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Main.getInstance().getPlayerHeadManager().downloadPlayerHeadAsync(player.getUniqueId().toString());
        String language = Main.getInstance().getTranslationManager().getLanguageOfPlayer(player);
        player.closeInventory();
        if (Main.getInstance().getTranslationManager().getTranslation(language, "languageChanged").equals("%languageChanged%")){
            player.sendMessage("§7You use an unsupported language: §6" + language);
            player.sendMessage("§7Please use one of the following languages: §6" + String.join(", ", Main.getInstance().getTranslationManager().getLanguages()));
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            player.setTexturePack("https://i9klpg.ph.files.1drv.com/y4m52ad-vEoZ_xeIhfYevVzM9FmOZRwOxIKjOS4ckJ27RWNFg0rPQJrPGcDxig_oa0AypWgZ5uLMkdoXosIZ_xtq9GqhZ74s5gcji0QFRN7_zasVZHzlBLVUDFZA-iXmIyxs-ecPW7zJ0jiscrfGMbm6LijWjIycCQlXX9HIBphbrTfAqFuu7razEPA78VIXrrjOXJbxwZ_umoitpYVlX_Ciw");
        }, 20);
    }

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {

        String config = "";
        if (Main.getInstance().getConfig().contains("motd")) {
            config = Main.getInstance().getConfig().getString("motd");
        } else {
            config = "§7Version §e" + Main.getInstance().getDescription().getVersion();
            Main.getInstance().getConfig().set("motd", config);
            Main.getInstance().saveConfig();
        }

        if (!Main.getInstance().getConfig().contains("motdEnabled")) {
            Main.getInstance().getConfig().set("motdEnabled", true);
            Main.getInstance().saveConfig();
        }
        if (Main.getInstance().getConfig().getBoolean("motdEnabled")) {
            event.setMotd(colorGradient("-- --- ---- Herausforderung ---- --- --", Color.fromRGB(43, 198, 64), Color.fromRGB(73, 172, 186), true) + "\n" + config);
        }

        event.setMaxPlayers(1000);

    }

    //chat event
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setFormat("§7[§e" + colorGradient(event.getPlayer().getName(), Color.fromRGB(186,186,73), Color.fromRGB(251, 251, 84), false) + "§7] §f" + event.getMessage());
    }

    public static String colorGradient(String text, Color color1, Color color2, boolean bold) {
        int length = text.length();
        StringBuilder gradientText = new StringBuilder();

        for (int i = 0; i < length; i++) {
            float ratio = (float) i / (length - 1); // Calculate the ratio based on the current index

            // Interpolate the RGB values between color1 and color2
            int red = (int) (color1.getRed() * (1 - ratio) + color2.getRed() * ratio);
            int green = (int) (color1.getGreen() * (1 - ratio) + color2.getGreen() * ratio);
            int blue = (int) (color1.getBlue() * (1 - ratio) + color2.getBlue() * ratio);

            // Create a new Color object based on the interpolated RGB values
            java.awt.Color interpolatedColor = new java.awt.Color(red, green, blue);

            // Convert the interpolatedColor to a Minecraft ChatColor
            ChatColor chatColor = ChatColor.of(new java.awt.Color(interpolatedColor.getRGB()));

            // Append the text with the ChatColor to create the gradient effect
            gradientText.append(chatColor).append((bold ? "§l" + text.charAt(i) : text.charAt(i)));
        }

        return gradientText.toString();
    }


    @EventHandler
    public void onPlayerRecourcePackStatusEvent(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
            player.kickPlayer(Main.getInstance().getTranslationManager().getTranslation(player, "youNeedToAcceptTheResourcePack"));
        }
    }

    @EventHandler
    public void onPackSuccess(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            player.spawnParticle(org.bukkit.Particle.FIREWORKS_SPARK, player.getLocation(), 100, 0.5, 0.5, 0.5, 0.1);
            player.sendTitle(Main.getInstance().getPlayerHeadManager().getHeadComponent(player), "§aHallo " + player.getName(), 10, 100, 20);
        }
    }

}
