package net.kitawa.more_stuff.util.configs;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class LifeTokensConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // --- Config values (raw spec entries) ---
    private static final ModConfigSpec.BooleanValue ADD_TO_SPAWNER_LOOT = BUILDER
            .comment("Add Life Token Bits to spawner loot")
            .define("addLifeTokenBitsToSpawnerLoot", true);

    private static final ModConfigSpec.BooleanValue ADD_TO_DUNGEON_LOOT = BUILDER
            .comment("Add Life Token Bits to dungeon loot")
            .define("addLifeTokenBitsToDungeonLoot", true);

    private static final ModConfigSpec.IntValue MAX_LIFE = BUILDER
            .comment("Maximum life that can be obtained")
            .defineInRange("maxLife", 60, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue LIFE_INCREMENT = BUILDER
            .comment("How much life is gained per token")
            .defineInRange("lifeIncrement", 1, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.BooleanValue ADD_LIFE_TOKENS = BUILDER
            .comment("Whether to drop life tokens at all")
            .define("addLifeTokens", true);

    // --- Final spec (registered in your main mod class) ---
    public static final ModConfigSpec SPEC = BUILDER.build();

    // --- Cached values (safe to use anywhere else in your mod) ---
    public static boolean addLifeTokenBitsToSpawnerLoot;
    public static boolean addLifeTokenBitsToDungeonLoot;
    public static int maxLife;
    public static int lifeIncrement;
    public static boolean addLifeTokens;

    /**
     * Called when the config is loaded or reloaded.
     */
    @net.neoforged.bus.api.SubscribeEvent
    public static void onReloading(final ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == SPEC) {
            bake();
        }
    }

    /**
     * Syncs spec values into static cached fields.
     */
    public static void bake() {
        addLifeTokenBitsToSpawnerLoot = ADD_TO_SPAWNER_LOOT.get();
        addLifeTokenBitsToDungeonLoot = ADD_TO_DUNGEON_LOOT.get();
        maxLife = MAX_LIFE.get();
        lifeIncrement = LIFE_INCREMENT.get();
        addLifeTokens = ADD_LIFE_TOKENS.get();
    }
}