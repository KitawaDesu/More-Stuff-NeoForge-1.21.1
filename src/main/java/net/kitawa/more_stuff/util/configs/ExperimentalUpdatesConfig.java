package net.kitawa.more_stuff.util.configs;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ExperimentalUpdatesConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // --- Config Values ---
    public static final ModConfigSpec.BooleanValue IS_COMBAT_UPDATE_ALLOWED =
            BUILDER.comment("If true, combat update experimental features will be enabled.")
                    .define("isCombatUpdateAllowed", false);

    public static final ModConfigSpec SPEC = BUILDER.build();

    // --- Cached value ---
    public static boolean isCombatUpdateAllowed;

    /** Sync spec -> cached values */
    private static void bake() {
        isCombatUpdateAllowed = IS_COMBAT_UPDATE_ALLOWED.get();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            bake();
        }
    }

    @SubscribeEvent
    static void onReload(final ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == SPEC) {
            bake();
        }
    }
}