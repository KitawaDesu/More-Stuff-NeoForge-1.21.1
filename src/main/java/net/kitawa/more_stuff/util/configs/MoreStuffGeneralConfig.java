package net.kitawa.more_stuff.util.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MoreStuffGeneralConfig {

    public static MoreStuffGeneralConfig CONFIG = new MoreStuffGeneralConfig(0.5, 0.75, 0.6908f, 7.824, 1, 0.5F, 0.5F, 0.25F, false, false, 255, 255, 255);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();
    private static final Path CONFIG_PATH = Paths.get("config", "more_stuff", "general_config.json");

    private double applyArmorDyeingMultiplier;
    private double applyLootDyeingMultiplier;
    private double LogarithmicArmorScalingFactor;
    private double LogarithmicEnchantmentScalingFactor;
    private int naturalArmorMultiplier;
    private float villagerArmorMultiplier;
    private float naturalArmorEnchantChance;
    private float naturalWeaponEnchantChance;
    private boolean AllowLogarithmicArmor;
    private boolean AllowLogarithmicEnchantments;
    private int R;
    private int G;
    private int B;

    public MoreStuffGeneralConfig(double applyArmorDyeingMultiplier, double applyLootDyeingMultiplier, double LogarithmicArmorScalingFactor, double LogarithmicEnchantmentScalingFactor,  int naturalArmorMultiplier, float villagerArmorMultiplier, float naturalArmorEnchantChance, float naturalWeaponEnchantChance, boolean AllowLogarithmicArmor, boolean AllowLogarithmicEnchantments, int R, int G, int B) {
        this.applyArmorDyeingMultiplier = applyArmorDyeingMultiplier;
        this.applyLootDyeingMultiplier = applyLootDyeingMultiplier;
        this.LogarithmicArmorScalingFactor = LogarithmicArmorScalingFactor;
        this.LogarithmicEnchantmentScalingFactor = LogarithmicEnchantmentScalingFactor;
        this.naturalArmorMultiplier = naturalArmorMultiplier;
        this.villagerArmorMultiplier = villagerArmorMultiplier;
        this.naturalArmorEnchantChance = naturalArmorEnchantChance;
        this.naturalWeaponEnchantChance = naturalWeaponEnchantChance;
        this.AllowLogarithmicArmor = AllowLogarithmicArmor;
        this.AllowLogarithmicEnchantments = AllowLogarithmicEnchantments;

        this.R = clampColor(R);
        this.G = clampColor(G);
        this.B = clampColor(B);
    }

    private int clampColor(int value) {
        return (value >= 0 && value <= 255) ? value : 255;
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
            CONFIG = GSON.fromJson(reader, MoreStuffGeneralConfig.class);
        }

        // Clamp values post-load as well, just to be safe
        CONFIG.R = CONFIG.clampColor(CONFIG.R);
        CONFIG.G = CONFIG.clampColor(CONFIG.G);
        CONFIG.B = CONFIG.clampColor(CONFIG.B);
    }

    public static void write() throws IOException {
        Path configDir = CONFIG_PATH.getParent();
        if (!Files.exists(configDir)) {
            Files.createDirectories(configDir);
        }

        try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
            writer.write(GSON.toJson(CONFIG));
        }
    }

    public double applyArmorDyeingMultiplier() {
        return applyArmorDyeingMultiplier;
    }

    public double applyLootDyeingMultiplier() {
        return applyLootDyeingMultiplier;
    }

    public double LogarithmicArmorScalingFactor() {
        return LogarithmicArmorScalingFactor;
    }

    public double LogarithmicEnchantmentScalingFactor() {
        return LogarithmicEnchantmentScalingFactor;
    }

    public int naturalArmorMultiplier() {
        return naturalArmorMultiplier;
    }

    public float naturalArmorEnchantChance() {
        return naturalArmorEnchantChance;
    }

    public float villagerArmorMultiplier() {
        return villagerArmorMultiplier;
    }

    public float naturalWeaponEnchantChance() {
        return naturalWeaponEnchantChance;
    }

    public boolean AllowLogarithmicArmor() {
        return AllowLogarithmicArmor;
    }

    public boolean AllowLogarithmicEnchantments() {
        return AllowLogarithmicEnchantments;
    }

    public int R() {
        return R;
    }

    public int G() {
        return G;
    }

    public int B() {
        return B;
    }
}
