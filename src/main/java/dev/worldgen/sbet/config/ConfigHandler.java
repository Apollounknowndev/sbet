package dev.worldgen.sbet.config;

import com.google.gson.*;
import dev.worldgen.sbet.SubjectivelyBetterEnchantmentTooltips;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigHandler {
    private static Path FILE_PATH;
    private static final Map<String, Object> DEFAULT_CONFIG_VALUES = new LinkedHashMap<>(){
        {
            put("regular", 5635925);
            put("treasure", 16733695);
            put("cursed", 16733525);
        }
    };
    private static Map<String, Object> CONFIG_VALUES = new HashMap<>();

    public static void loadOrCreateDefaultConfig() {
        FILE_PATH = getConfigFilePath();
        if (!Files.isRegularFile(FILE_PATH)) {
            SubjectivelyBetterEnchantmentTooltips.LOGGER.info("Config file for Subjectively Better Enchantment Tooltips not found, creating file with default values...");
            try(BufferedWriter writer = Files.newBufferedWriter(FILE_PATH)) {
                writer.write("{}");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        try (BufferedReader reader = Files.newBufferedReader(FILE_PATH)) {
            JsonElement json = JsonParser.parseReader(reader);
            JsonObject jsonObject = json.getAsJsonObject().getAsJsonObject("tooltip_colors");
            if (jsonObject != null) {
                for (Map.Entry<String, JsonElement> configValues : jsonObject.entrySet()) {
                    if (DEFAULT_CONFIG_VALUES.containsKey(configValues.getKey())) {
                        String key = configValues.getKey();
                        Object value = configValues.getValue().getAsInt();
                        CONFIG_VALUES.put(key, value);
                    }
                }
            }
            for (Map.Entry<String, Object> defaultConfigValues : DEFAULT_CONFIG_VALUES.entrySet()) {
                CONFIG_VALUES.putIfAbsent(defaultConfigValues.getKey(), defaultConfigValues.getValue());
            }
            writeToFile(CONFIG_VALUES);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Path getConfigFilePath() {
        return FabricLoader.getInstance().getConfigDir().resolve("subjectively-better-enchantment-tooltips.json");
    }
    public static Object getConfigValue(String key) {
        Object value;
        if (CONFIG_VALUES.containsKey(key)) {
            value = CONFIG_VALUES.get(key);
        } else if (DEFAULT_CONFIG_VALUES.containsKey(key)) {
            value = DEFAULT_CONFIG_VALUES.get(key);
        } else {
            throw new NullPointerException("Could not find config key: "+key);
        }
        return value;
    }

    private static void writeToFile(Map<String, Object> input) {
        try(BufferedWriter writer = Files.newBufferedWriter(FILE_PATH)) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("tooltip_colors", input);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(map));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}