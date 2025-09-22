package net.kitawa.more_stuff.util.configs;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class MoreStuffGeneralConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // --- Config values (raw spec entries) ---
    public static final ModConfigSpec.DoubleValue APPLY_ARMOR_DYEING_MULTIPLIER = BUILDER
            .comment("Multiplier applied to armor dyeing")
            .defineInRange("applyArmorDyeingMultiplier", 0.5, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue APPLY_LOOT_DYEING_MULTIPLIER = BUILDER
            .comment("Multiplier applied to loot dyeing")
            .defineInRange("applyLootDyeingMultiplier", 0.75, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue LOGARITHMIC_ARMOR_SCALING_FACTOR = BUILDER
            .comment("Scaling factor for logarithmic armor")
            .defineInRange("logarithmicArmorScalingFactor", 0.6908, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue LOGARITHMIC_ENCHANTMENT_SCALING_FACTOR = BUILDER
            .comment("Scaling factor for logarithmic enchantments")
            .defineInRange("logarithmicEnchantmentScalingFactor", 7.824, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.IntValue NATURAL_ARMOR_MULTIPLIER = BUILDER
            .comment("Natural armor multiplier")
            .defineInRange("naturalArmorMultiplier", 1, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue VILLAGER_ARMOR_MULTIPLIER = BUILDER
            .comment("Multiplier for villager armor")
            .defineInRange("villagerArmorMultiplier", 0.5, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue NATURAL_ARMOR_ENCHANT_CHANCE = BUILDER
            .comment("Chance for natural armor enchantments")
            .defineInRange("naturalArmorEnchantChance", 0.5, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue NATURAL_WEAPON_ENCHANT_CHANCE = BUILDER
            .comment("Chance for natural weapon enchantments")
            .defineInRange("naturalWeaponEnchantChance", 0.25, 0.0, 1.0);

    public static final ModConfigSpec.BooleanValue ALLOW_LOGARITHMIC_ARMOR = BUILDER
            .comment("Allow logarithmic armor scaling")
            .define("allowLogarithmicArmor", false);

    public static final ModConfigSpec.BooleanValue ALLOW_LOGARITHMIC_ENCHANTMENTS = BUILDER
            .comment("Allow logarithmic enchantments")
            .define("allowLogarithmicEnchantments", false);

    public static final ModConfigSpec.IntValue R = BUILDER
            .comment("Red value for general color (0-255)")
            .defineInRange("R", 255, 0, 255);

    public static final ModConfigSpec.IntValue G = BUILDER
            .comment("Green value for general color (0-255)")
            .defineInRange("G", 255, 0, 255);

    public static final ModConfigSpec.IntValue B = BUILDER
            .comment("Blue value for general color (0-255)")
            .defineInRange("B", 255, 0, 255);

    // --- Final spec ---
    public static final ModConfigSpec SPEC = BUILDER.build();

    // --- Cached values ---
    public static double applyArmorDyeingMultiplier;
    public static double applyLootDyeingMultiplier;
    public static double logarithmicArmorScalingFactor;
    public static double logarithmicEnchantmentScalingFactor;
    public static int naturalArmorMultiplier;
    public static double villagerArmorMultiplier;
    public static double naturalArmorEnchantChance;
    public static double naturalWeaponEnchantChance;
    public static boolean allowLogarithmicArmor;
    public static boolean allowLogarithmicEnchantments;
    public static int r, g, b;

    /** Sync spec -> cached values */
    public static void bake() {
        applyArmorDyeingMultiplier = APPLY_ARMOR_DYEING_MULTIPLIER.get();
        applyLootDyeingMultiplier = APPLY_LOOT_DYEING_MULTIPLIER.get();
        logarithmicArmorScalingFactor = LOGARITHMIC_ARMOR_SCALING_FACTOR.get();
        logarithmicEnchantmentScalingFactor = LOGARITHMIC_ENCHANTMENT_SCALING_FACTOR.get();
        naturalArmorMultiplier = NATURAL_ARMOR_MULTIPLIER.get();
        villagerArmorMultiplier = VILLAGER_ARMOR_MULTIPLIER.get();
        naturalArmorEnchantChance = NATURAL_ARMOR_ENCHANT_CHANCE.get();
        naturalWeaponEnchantChance = NATURAL_WEAPON_ENCHANT_CHANCE.get();
        allowLogarithmicArmor = ALLOW_LOGARITHMIC_ARMOR.get();
        allowLogarithmicEnchantments = ALLOW_LOGARITHMIC_ENCHANTMENTS.get();
        r = R.get();
        g = G.get();
        b = B.get();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) bake();
    }

    @SubscribeEvent
    static void onReload(final ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == SPEC) bake();
    }
}
