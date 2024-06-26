package de.leon_lp9.challengePlugin.management;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import de.leon_lp9.challengePlugin.Main;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Getter
public class FileUtils {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()

            /* Color */
            .registerTypeAdapter(Color.class, (JsonDeserializer<Color>) (jsonElement, type, jsonDeserializationContext) -> Color.decode(jsonElement.getAsString()))
            .registerTypeAdapter(Color.class, (JsonSerializer<Color>) (color, type, jsonSerializationContext) -> jsonSerializationContext.serialize("#" + Integer.toHexString(color.getRGB()).substring(2)))

            .create();

    public void writeToJsonFile(String fileName, Object data) {
        //wenn der Ordner nicht existiert, erstelle ihn
        File folder = new File("plugins\\Challenge");
        if (!folder.exists()) {
            if (folder.mkdir()) {
                Main.getInstance().getLogger().info(folder + " wurde erstellt");
            } else {
                Main.getInstance().getLogger().info(folder + " konnte nicht erstellt werden");
            }
        }


        try (FileWriter writer = new FileWriter("plugins\\Challenge\\" + fileName + ".json")) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public <T> T readFromJsonFile(String fileName, Class<T> type) {
        try (Reader reader = new FileReader("plugins\\Challenge\\" + fileName + ".json")) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T readFromJsonFile(String fileName, TypeToken<T> type) {
        try (Reader reader = new FileReader("plugins\\Challenge\\" + fileName + ".json", StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean fileExists(String fileName) {
        File file = new File("plugins\\Challenge\\" + fileName + ".json");
        return file.exists();
    }

    public void deleteFile(String fileName) {
        File file = new File("plugins\\Challenge\\" + fileName + ".json");
        file.delete();
    }

    public void deleteDirectory(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    deleteDirectory(f);
                }
            }
        }
        file.delete();
    }

}
