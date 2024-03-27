package de.leon_lp9.challengePlugin.management;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerHeadManager {

    private final HashMap<UUID, String> cachedHeads = new HashMap<>();

    public CompletableFuture<BufferedImage> downloadPlayerHeadAsync(String playerUUID) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return downloadPlayerHead(playerUUID);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public BufferedImage downloadPlayerHead(String playerUUID) throws IOException {
        String skinURL = "https://mc-heads.net/avatar/" + playerUUID + "/8";
        URL url = new URL(skinURL);
        return ImageIO.read(url);
    }

    public String getHeadComponent(Player player) {
        return getHeadComponent(player.getUniqueId().toString());
    }

    public String getHeadComponent(UUID playerUUID) {
        return getHeadComponent(playerUUID.toString());
    }

    private String getHeadComponent(String playerUUID) {
        if (cachedHeads.containsKey(UUID.fromString(playerUUID))) {
            return cachedHeads.get(UUID.fromString(playerUUID));
        }

        try {

        CompletableFuture<BufferedImage> futureImage = downloadPlayerHeadAsync(playerUUID);
        BufferedImage image = futureImage.get(); // This will block until the image is downloaded

        StringBuilder message = new StringBuilder();
        message.append(Spacing.ZEROPIXEl.getSpacing());
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                message.append(ChatColor.of(new java.awt.Color(image.getRGB(j, i))));
                if (i == 0) {
                    message.append("\uDAC0\uDC41");
                }else if (i == 1) {
                    message.append("\uDAC0\uDC42");
                }else if (i == 2) {
                    message.append("\uDAC0\uDC43");
                }else if (i == 3) {
                    message.append("\uDAC0\uDC44");
                }else if (i == 4) {
                    message.append("\uDAC0\uDC45");
                }else if (i == 5) {
                    message.append("\uDAC0\uDC46");
                }else if (i == 6) {
                    message.append("\uDAC0\uDC47");
                }else {
                    message.append("\uDAC0\uDC48");
                }
                message.append(Spacing.NEGATIVE1PIXEl.getSpacing());
            }
            if (i != 7){ message.append(Spacing.NEGATIVE8PIXEl.getSpacing()); }else { message.append(Spacing.POSITIVE2PIXEl.getSpacing()); message.append(Spacing.POSITIVE1PIXEl.getSpacing());  }
        }
        message.append(Spacing.ZEROPIXEl.getSpacing());
        cachedHeads.put(UUID.fromString(playerUUID), message.toString());
        return message.toString();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
