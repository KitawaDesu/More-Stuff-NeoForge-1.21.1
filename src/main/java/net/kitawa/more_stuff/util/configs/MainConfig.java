package net.kitawa.more_stuff.util.configs;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;

public class MainConfig {

    public static void registerConfigs() {
        ModContainer modContainer = ModLoadingContext.get().getActiveContainer();

        // Each client-side config can have its own file
        modContainer.registerConfig(ModConfig.Type.CLIENT,
                LifeBitDropsConfig.SPEC,
                "more_stuff/life_bit_drops.toml");

        modContainer.registerConfig(ModConfig.Type.CLIENT,
                LifeTokensConfig.SPEC,
                "more_stuff/life_tokens.toml");

        modContainer.registerConfig(ModConfig.Type.CLIENT,
                MoreStuffGeneralConfig.SPEC,
                "more_stuff/general_config.toml");

        modContainer.registerConfig(ModConfig.Type.COMMON,
                ExperimentalUpdatesConfig.SPEC,
                "more_stuff/experimental_updates.toml");
    }
}