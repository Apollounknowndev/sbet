package dev.worldgen.sbet;

import dev.worldgen.sbet.config.ConfigHandler;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SBET implements ModInitializer {
    public static final String MOD_ID = "sbet";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ConfigHandler.load();
    }
}
