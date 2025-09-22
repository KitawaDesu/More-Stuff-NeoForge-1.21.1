package net.kitawa.more_stuff.util.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ShockPriorityConfig {

    public static ShockPriorityConfig CONFIG = new ShockPriorityConfig(
            Map.of(
                    "minecraft:iron_golem", 2.0f,
                    "minecraft:player", 1.0f
            )
    );

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();
    private static final Path CONFIG_PATH = Paths.get("config", "more_stuff", "shock_priority.json");

    private Map<String, Float> entityTypePriority;

    public ShockPriorityConfig(Map<String, Float> entityTypePriority) {
        this.entityTypePriority = entityTypePriority != null ? entityTypePriority : new HashMap<>();
    }

    public Map<String, Float> entityTypePriority() {
        return entityTypePriority;
    }

    // --- File Handling ---
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
            CONFIG = GSON.fromJson(reader, ShockPriorityConfig.class);
        }
        if (CONFIG.entityTypePriority == null) {
            CONFIG.entityTypePriority = new HashMap<>();
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
}
