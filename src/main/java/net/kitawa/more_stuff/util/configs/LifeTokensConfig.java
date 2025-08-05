package net.kitawa.more_stuff.util.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LifeTokensConfig {

    public static LifeTokensConfig CONFIG = new LifeTokensConfig(true, true, 60, 1, true);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();
    private static final Path CONFIG_PATH = Paths.get("config", "more_stuff", "life_tokens_config.json");

    private boolean addLifeTokenBitsToSpawnerLoot;
    private boolean addLifeTokenBitsToDungeonLoot;
    private int maxLife;
    private int lifeIncrement;
    private boolean addLifeTokens;

    public LifeTokensConfig(boolean spawnerLoot, boolean dungeonLoot, int maxLife, int lifeIncrement, boolean addLifeTokens) {
        this.addLifeTokenBitsToSpawnerLoot = spawnerLoot;
        this.addLifeTokenBitsToDungeonLoot = dungeonLoot;
        this.maxLife = maxLife;
        this.lifeIncrement = lifeIncrement;
        this.addLifeTokens = addLifeTokens;
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
            CONFIG = GSON.fromJson(reader, LifeTokensConfig.class);
        }
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

    public boolean addLifeTokenBitsToSpawnerLoot() {
        return addLifeTokenBitsToSpawnerLoot;
    }

    public boolean addLifeTokenBitsToDungeonLoot() {
        return addLifeTokenBitsToDungeonLoot;
    }

    public int maxLife() {
        return maxLife;
    }

    public int lifeIncrement() {
        return lifeIncrement;
    }

    public boolean addLifeTokens() {
        return addLifeTokens;
    }
}
