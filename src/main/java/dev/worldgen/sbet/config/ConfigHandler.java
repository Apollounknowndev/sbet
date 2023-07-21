package dev.worldgen.sbet.config;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import dev.worldgen.sbet.SBET;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConfigHandler {
    private static Path FILE_PATH;
    private static final ConfigCodec DEFAULT_CONFIG = new ConfigCodec(
        new ConfigCodec.TooltipColors(5635925, 16733695, 16733525), true, ConfigCodec.DEFAULT_DESCRIPTIONS);
    private static ConfigCodec CONFIG;

    public static void load() {
        FILE_PATH = FabricLoader.getInstance().getConfigDir().resolve("subjectively-better-enchantment-tooltips.json");
        if (!Files.isRegularFile(FILE_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            SBET.LOGGER.info("Config file for Subjectively Better Enchantment Tooltips not found, creating file with default values...");
            try(BufferedWriter writer = Files.newBufferedWriter(FILE_PATH)) {
                writer.write(gson.toJson(ConfigCodec.CODEC.encodeStart(JsonOps.INSTANCE, DEFAULT_CONFIG).result().get()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        try (BufferedReader reader = Files.newBufferedReader(FILE_PATH)) {
            JsonElement json = JsonParser.parseReader(reader);
            Optional<ConfigCodec> result = ConfigCodec.CODEC.parse(JsonOps.INSTANCE, json).result();
            if (result.isEmpty()) {
                result = Optional.of(DEFAULT_CONFIG);
            }
            CONFIG = result.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try(BufferedWriter writer = Files.newBufferedWriter(FILE_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(ConfigCodec.CODEC.encodeStart(JsonOps.INSTANCE, CONFIG).result().get()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ConfigCodec getConfig() {
        return CONFIG;
    }
}