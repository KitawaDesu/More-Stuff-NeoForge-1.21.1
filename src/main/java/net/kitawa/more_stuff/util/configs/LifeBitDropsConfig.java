package net.kitawa.more_stuff.util.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LifeBitDropsConfig {

    public static LifeBitDropsConfig CONFIG = new LifeBitDropsConfig(
            1.0f, 1.0f, 0.10f, 0.15f, 0.75f, 0.5f, 0.5f, 0.5f, 0.375f,
            20.0f, 7.0f, 0.0f, 8.0f // default thresholds
    );
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();
    private static final Path CONFIG_PATH = Paths.get("config", "more_stuff", "life_bit_drops_config.json");

    // Descriptions
    private final String ArmorBonusPercentageDescription = "Sets the percentage of Life Bits dropped, based on the entity's armor value.";
    private final String ToughnessBonusPercentageDescription = "Sets the percentage of Life Bits dropped, based on the entity's armor toughness value.";
    private final String MinimumDropPercentageDescription = "The minimum percentage chance required for Life Bits to drop. Drops will fail if the roll is below this value.";
    private final String BiasedTwordsPercentageDescription = "Determines the bias of the drop percentage. It’s recommended to keep this value above the minimum drop percentage.";
    private final String MobAttackDamageBonusDescription = "Increases the number of Life Bits dropped based on the defeated entity's attack damage. Stronger enemies give more drops.";
    private final String PlayerHealthPenaltyDescription = "Reduces the number of Life Bits dropped based on the player's maximum health. Higher health leads to fewer drops.";
    private final String PlayerArmorPenaltyDescription = "Reduces the number of Life Bits dropped based on the player's armor value. More armor results in fewer drops.";
    private final String PlayerToughnessPenaltyDescription = "Reduces the number of Life Bits dropped based on the player's armor toughness. Higher toughness decreases drop amount.";
    private final String PlayerAttackDamagePenaltyDescription = "Reduces the number of Life Bits dropped based on the player's attack damage. Stronger attackers yield fewer drops.";
    private final String PlayerHealthPenaltyThresholdDescription = "Minimum health before the health penalty is applied.";
    private final String PlayerArmorPenaltyThresholdDescription = "Minimum armor before the armor penalty is applied.";
    private final String PlayerToughnessPenaltyThresholdDescription = "Minimum armor toughness before the toughness penalty is applied.";
    private final String PlayerAttackDamagePenaltyThresholdDescription = "Minimum attack damage before the attack damage penalty is applied.";

    // Values
    private float ArmorBonusPercentageValue;
    private float ToughnessBonusPercentageValue;
    private float MinimumDropPercentageValue;
    private float BiasedTwordsPercentageValue;
    private float MobAttackDamageBonus;
    private float PlayerHealthPenalty;
    private float PlayerArmorPenalty;
    private float PlayerToughnessPenalty;
    private float PlayerAttackDamagePenalty;
    private float PlayerHealthPenaltyThreshold;
    private float PlayerArmorPenaltyThreshold;
    private float PlayerToughnessPenaltyThreshold;
    private float PlayerAttackDamagePenaltyThreshold;

    public LifeBitDropsConfig(
            float ArmorBonusPercentageValue,
            float ToughnessBonusPercentageValue,
            float MinimumDropPercentageValue,
            float BiasedTwordsPercentageValue,
            float MobAttackDamageBonus,
            float PlayerHealthPenalty,
            float PlayerArmorPenalty,
            float PlayerToughnessPenalty,
            float PlayerAttackDamagePenalty,
            float PlayerHealthPenaltyThreshold,
            float PlayerArmorPenaltyThreshold,
            float PlayerToughnessPenaltyThreshold,
            float PlayerAttackDamagePenaltyThreshold
    ) {
        this.ArmorBonusPercentageValue = ArmorBonusPercentageValue;
        this.ToughnessBonusPercentageValue = ToughnessBonusPercentageValue;
        this.MinimumDropPercentageValue = MinimumDropPercentageValue;
        this.BiasedTwordsPercentageValue = BiasedTwordsPercentageValue;
        this.MobAttackDamageBonus = MobAttackDamageBonus;
        this.PlayerHealthPenalty = PlayerHealthPenalty;
        this.PlayerArmorPenalty = PlayerArmorPenalty;
        this.PlayerToughnessPenalty = PlayerToughnessPenalty;
        this.PlayerAttackDamagePenalty = PlayerAttackDamagePenalty;
        this.PlayerHealthPenaltyThreshold = PlayerHealthPenaltyThreshold;
        this.PlayerArmorPenaltyThreshold = PlayerArmorPenaltyThreshold;
        this.PlayerToughnessPenaltyThreshold = PlayerToughnessPenaltyThreshold;
        this.PlayerAttackDamagePenaltyThreshold = PlayerAttackDamagePenaltyThreshold;
    }

    public static void setup() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                read();
            } else {
                write();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read() throws IOException {
        try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
            CONFIG = GSON.fromJson(reader, LifeBitDropsConfig.class);
        }
    }

    public static void write() throws IOException {
        Path configDir = CONFIG_PATH.getParent();
        if (!Files.exists(configDir)) {
            Files.createDirectories(configDir);
        }

        // Custom writer to add comments manually
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_PATH.toFile()))) {
            writer.write("{\n");

            writer.write("  // " + CONFIG.ArmorBonusPercentageDescription + "\n");
            writer.write("  \"ArmorBonusPercentageValue\": " + CONFIG.ArmorBonusPercentageValue + ",\n");

            writer.write("  // " + CONFIG.ToughnessBonusPercentageDescription + "\n");
            writer.write("  \"ToughnessBonusPercentageValue\": " + CONFIG.ToughnessBonusPercentageValue + ",\n");

            writer.write("  // " + CONFIG.MinimumDropPercentageDescription + "\n");
            writer.write("  \"MinimumDropPercentageValue\": " + CONFIG.MinimumDropPercentageValue + ",\n");

            writer.write("  // " + CONFIG.BiasedTwordsPercentageDescription + "\n");
            writer.write("  \"BiasedTwordsPercentageValue\": " + CONFIG.BiasedTwordsPercentageValue + ",\n");

            writer.write("  // " + CONFIG.MobAttackDamageBonusDescription + "\n");
            writer.write("  \"MobAttackDamageBonus\": " + CONFIG.MobAttackDamageBonus + ",\n");

            writer.write("  // " + CONFIG.PlayerHealthPenaltyDescription + "\n");
            writer.write("  \"PlayerHealthPenalty\": " + CONFIG.PlayerHealthPenalty + ",\n");

            writer.write("  // " + CONFIG.PlayerArmorPenaltyDescription + "\n");
            writer.write("  \"PlayerArmorPenalty\": " + CONFIG.PlayerArmorPenalty + ",\n");

            writer.write("  // " + CONFIG.PlayerToughnessPenaltyDescription + "\n");
            writer.write("  \"PlayerToughnessPenalty\": " + CONFIG.PlayerToughnessPenalty + ",\n");

            writer.write("  // " + CONFIG.PlayerAttackDamagePenaltyDescription + "\n");
            writer.write("  \"PlayerAttackDamagePenalty\": " + CONFIG.PlayerAttackDamagePenalty + ",\n");

            writer.write("  // " + CONFIG.PlayerHealthPenaltyThresholdDescription + "\n");
            writer.write("  \"PlayerHealthPenaltyThreshold\": " + CONFIG.PlayerHealthPenaltyThreshold + ",\n");

            writer.write("  // " + CONFIG.PlayerArmorPenaltyThresholdDescription + "\n");
            writer.write("  \"PlayerArmorPenaltyThreshold\": " + CONFIG.PlayerArmorPenaltyThreshold + ",\n");

            writer.write("  // " + CONFIG.PlayerToughnessPenaltyThresholdDescription + "\n");
            writer.write("  \"PlayerToughnessPenaltyThreshold\": " + CONFIG.PlayerToughnessPenaltyThreshold + ",\n");

            writer.write("  // " + CONFIG.PlayerAttackDamagePenaltyThresholdDescription + "\n");
            writer.write("  \"PlayerAttackDamagePenaltyThreshold\": " + CONFIG.PlayerAttackDamagePenaltyThreshold + "\n");

            writer.write("}\n");
        }
    }


    // Getters
    public float ArmorBonusPercentageValue() {
        return ArmorBonusPercentageValue;
    }

    public float ToughnessBonusPercentageValue() {
        return ToughnessBonusPercentageValue;
    }

    public float MinimumDropPercentageValue() {
        return MinimumDropPercentageValue;
    }

    public float BiasedTwordsPercentageValue() {
        return BiasedTwordsPercentageValue;
    }

    public float MobAttackDamageBonus() {
        return MobAttackDamageBonus;
    }

    public float PlayerHealthPenalty() {
        return PlayerHealthPenalty;
    }

    public float PlayerArmorPenalty() {
        return PlayerArmorPenalty;
    }

    public float PlayerToughnessPenalty() {
        return PlayerToughnessPenalty;
    }

    public float PlayerAttackDamagePenalty() {
        return PlayerAttackDamagePenalty;
    }

    public float PlayerHealthPenaltyThreshold() {
        return PlayerHealthPenaltyThreshold;
    }

    public float PlayerArmorPenaltyThreshold() {
        return PlayerArmorPenaltyThreshold;
    }

    public float PlayerToughnessPenaltyThreshold() {
        return PlayerToughnessPenaltyThreshold;
    }

    public float PlayerAttackDamagePenaltyThreshold() {
        return PlayerAttackDamagePenaltyThreshold;
    }
}
