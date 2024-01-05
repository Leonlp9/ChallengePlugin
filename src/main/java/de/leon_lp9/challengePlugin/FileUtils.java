package de.leon_lp9.challengePlugin;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

public class FileUtils {

    @Getter
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
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
        try (Reader reader = new FileReader("plugins\\Challenge\\" + fileName + ".json")) {
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

}
