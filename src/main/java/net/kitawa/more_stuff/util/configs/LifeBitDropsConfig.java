package net.kitawa.more_stuff.util.configs;


import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.toml.TomlWriter;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class LifeBitDropsConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // --- Config Values ---
    public static final ModConfigSpec.DoubleValue ARMOR_BONUS_PERCENTAGE =
            BUILDER.comment("Sets the percentage of Life Bits dropped based on the entity's armor value.")
                    .defineInRange("armorBonusPercentage", 1.0, 0.0, 10.0);

    public static final ModConfigSpec.DoubleValue TOUGHNESS_BONUS_PERCENTAGE =
            BUILDER.comment("Sets the percentage of Life Bits dropped based on the entity's armor toughness value.")
                    .defineInRange("toughnessBonusPercentage", 1.0, 0.0, 10.0);

    public static final ModConfigSpec.DoubleValue MINIMUM_DROP_PERCENTAGE =
            BUILDER.comment("Minimum chance required for Life Bits to drop.")
                    .defineInRange("minimumDropPercentage", 0.10, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue BIASED_TOWARDS_PERCENTAGE =
            BUILDER.comment("Bias of the drop percentage. Recommended above minimum drop.")
                    .defineInRange("biasedTowardsPercentage", 0.15, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue MOB_ATTACK_DAMAGE_BONUS =
            BUILDER.comment("Increases drops based on mob's attack damage.")
                    .defineInRange("mobAttackDamageBonus", 0.75, 0.0, 10.0);

    public static final ModConfigSpec.DoubleValue PLAYER_HEALTH_PENALTY =
            BUILDER.comment("Reduces drops based on player's max health.")
                    .defineInRange("playerHealthPenalty", 0.5, 0.0, 10.0);

    public static final ModConfigSpec.DoubleValue PLAYER_ARMOR_PENALTY =
            BUILDER.comment("Reduces drops based on player's armor value.")
                    .defineInRange("playerArmorPenalty", 0.5, 0.0, 10.0);

    public static final ModConfigSpec.DoubleValue PLAYER_TOUGHNESS_PENALTY =
            BUILDER.comment("Reduces drops based on player's armor toughness.")
                    .defineInRange("playerToughnessPenalty", 0.5, 0.0, 10.0);

    public static final ModConfigSpec.DoubleValue PLAYER_ATTACK_DAMAGE_PENALTY =
            BUILDER.comment("Reduces drops based on player's attack damage.")
                    .defineInRange("playerAttackDamagePenalty", 0.375, 0.0, 10.0);

    public static final ModConfigSpec.DoubleValue PLAYER_HEALTH_THRESHOLD =
            BUILDER.comment("Minimum player health before health penalty applies.")
                    .defineInRange("playerHealthThreshold", 20.0, 0.0, 1024.0);

    public static final ModConfigSpec.DoubleValue PLAYER_ARMOR_THRESHOLD =
            BUILDER.comment("Minimum player armor before armor penalty applies.")
                    .defineInRange("playerArmorThreshold", 7.0, 0.0, 1024.0);

    public static final ModConfigSpec.DoubleValue PLAYER_TOUGHNESS_THRESHOLD =
            BUILDER.comment("Minimum player armor toughness before toughness penalty applies.")
                    .defineInRange("playerToughnessThreshold", 0.0, 0.0, 1024.0);

    public static final ModConfigSpec.DoubleValue PLAYER_ATTACK_DAMAGE_THRESHOLD =
            BUILDER.comment("Minimum player attack damage before penalty applies.")
                    .defineInRange("playerAttackDamageThreshold", 8.0, 0.0, 1024.0);

    public static final ModConfigSpec SPEC = BUILDER.build();

    // --- Cached static values ---
    public static double armorBonus;
    public static double toughnessBonus;
    public static double minimumDrop;
    public static double biasedTowards;
    public static double mobAttackBonus;
    public static double playerHealthPenalty;
    public static double playerArmorPenalty;
    public static double playerToughnessPenalty;
    public static double playerAttackDamagePenalty;
    public static double playerHealthThreshold;
    public static double playerArmorThreshold;
    public static double playerToughnessThreshold;
    public static double playerAttackDamageThreshold;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == SPEC) {
            bake();
        }
    }

    @SubscribeEvent
    static void onReloading(final ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == SPEC) {
            bake();
        }
    }

    /**
     * Syncs spec values into static cached fields.
     */
    public static void bake() {
        armorBonus = ARMOR_BONUS_PERCENTAGE.get();
        toughnessBonus = TOUGHNESS_BONUS_PERCENTAGE.get();
        minimumDrop = MINIMUM_DROP_PERCENTAGE.get();
        biasedTowards = BIASED_TOWARDS_PERCENTAGE.get();
        mobAttackBonus = MOB_ATTACK_DAMAGE_BONUS.get();
        playerHealthPenalty = PLAYER_HEALTH_PENALTY.get();
        playerArmorPenalty = PLAYER_ARMOR_PENALTY.get();
        playerToughnessPenalty = PLAYER_TOUGHNESS_PENALTY.get();
        playerAttackDamagePenalty = PLAYER_ATTACK_DAMAGE_PENALTY.get();
        playerHealthThreshold = PLAYER_HEALTH_THRESHOLD.get();
        playerArmorThreshold = PLAYER_ARMOR_THRESHOLD.get();
        playerToughnessThreshold = PLAYER_TOUGHNESS_THRESHOLD.get();
        playerAttackDamageThreshold = PLAYER_ATTACK_DAMAGE_THRESHOLD.get();
    }
}