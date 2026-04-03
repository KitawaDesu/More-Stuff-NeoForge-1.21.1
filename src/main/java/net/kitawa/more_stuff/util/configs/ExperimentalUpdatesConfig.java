package net.kitawa.more_stuff.util.configs;

import net.kitawa.more_stuff.blocks.custom.end.phantasmic.RevealableBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.Tags;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ExperimentalUpdatesConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // --- Config Values ---
    public static final ModConfigSpec.BooleanValue IS_COMBAT_UPDATE_ALLOWED =
            BUILDER.comment("If true, combat update experimental features will be enabled.")
                    .define("isCombatUpdateAllowed", false);

    public static final ModConfigSpec.BooleanValue IS_END_UPDATE_ALLOWED =
            BUILDER.comment("If true, end update experimental features will be enabled.")
                    .define("isEndUpdateAllowed", false);

    public static final ModConfigSpec.BooleanValue IS_HYBERNATUS_BIOMES_ALLOWED =
            BUILDER.comment("If true, Hybernatus biomes will generate.")
                    .define("isHybernatusBiomesAllowed", true);

    public static final ModConfigSpec.BooleanValue IS_PHANTASMIC_BIOMES_ALLOWED =
            BUILDER.comment("If true, Phantasmic biomes will generate.")
                    .define("isPhantasmicBiomesAllowed", false);

    public static final ModConfigSpec.EnumValue<RevealableBlock.RenderMode> PHANTASMIC_RENDER_TYPE =
            BUILDER.comment("Controls how phantasmic blocks cull faces between each other.",
                            "CURRENT: skip if blockstate is identical.",
                            "DISTANCE_STATES: skip if reveal distance is within tolerance.",
                            "LIGHT_STATES: skip if light level is within tolerance.",
                            "SHADER_FRIENDLY: standard vanilla culling, best for shader mods.")
                    .defineEnum("phantasmicRenderType", RevealableBlock.RenderMode.CURRENT);

    public static final ModConfigSpec.IntValue PHANTASMIC_RENDER_DISTANCE =
            BUILDER.comment("How many steps apart two phantasmic blocks can be before a face is drawn between them.",
                            "1 = exact match only, 2 = within 2 steps, etc.")
                    .defineInRange("phantasmicRenderDistance", 2, 1, 15);

    public static final ModConfigSpec SPEC = BUILDER.build();

    // --- Cached values ---
    public static boolean isCombatUpdateAllowed;
    public static boolean isEndUpdateAllowed;
    public static boolean isHybernatusBiomesAllowed;
    public static boolean isPhantasmicBiomesAllowed;
    public static RevealableBlock.RenderMode phantasmicRenderType;
    public static int phantasmicRenderDistance;

    /** Sync spec -> cached values */
    public static void bake() {
        isCombatUpdateAllowed       = IS_COMBAT_UPDATE_ALLOWED.get();
        isEndUpdateAllowed          = IS_END_UPDATE_ALLOWED.get();
        isHybernatusBiomesAllowed   = IS_HYBERNATUS_BIOMES_ALLOWED.get();
        isPhantasmicBiomesAllowed   = IS_PHANTASMIC_BIOMES_ALLOWED.get();
        phantasmicRenderType        = PHANTASMIC_RENDER_TYPE.get();
        phantasmicRenderDistance    = PHANTASMIC_RENDER_DISTANCE.get();
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