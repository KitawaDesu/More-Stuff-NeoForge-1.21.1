package net.kitawa.more_stuff.util.configs;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class LifeTokensConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // --- Raw spec entries ---
    public static final ModConfigSpec.BooleanValue ADD_TO_SPAWNER_LOOT = BUILDER
            .comment("Add Life Token Bits to spawner loot")
            .define("addLifeTokenBitsToSpawnerLoot", true);

    public static final ModConfigSpec.BooleanValue ADD_TO_DUNGEON_LOOT = BUILDER
            .comment("Add Life Token Bits to dungeon loot")
            .define("addLifeTokenBitsToDungeonLoot", true);

    public static final ModConfigSpec.IntValue MAX_LIFE = BUILDER
            .comment("Maximum life that can be obtained")
            .defineInRange("maxLife", 60, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue LIFE_INCREMENT = BUILDER
            .comment("How much life is gained per token")
            .defineInRange("lifeIncrement", 1, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.BooleanValue ADD_LIFE_TOKENS = BUILDER
            .comment("Whether to drop life tokens at all")
            .define("addLifeTokens", true);

    // --- Final spec ---
    public static final ModConfigSpec SPEC = BUILDER.build();

    // --- Cached values ---
    public static boolean addLifeTokenBitsToSpawnerLoot;
    public static boolean addLifeTokenBitsToDungeonLoot;
    public static int maxLife;
    public static int lifeIncrement;
    public static boolean addLifeTokens;

    // --- Sync spec -> cached ---
    public static void bake() {
        addLifeTokenBitsToSpawnerLoot = ADD_TO_SPAWNER_LOOT.get();
        addLifeTokenBitsToDungeonLoot = ADD_TO_DUNGEON_LOOT.get();
        maxLife = MAX_LIFE.get();
        lifeIncrement = LIFE_INCREMENT.get();
        addLifeTokens = ADD_LIFE_TOKENS.get();
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
