package net.kitawa.more_stuff.worldgen.biomelayers;

import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.*;

public class BiomeLayerManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();

    private static final Map<ResourceKey<Biome>, BiomeLayer> NETHER_LAYERS = new HashMap<>();
    private static final Map<ResourceKey<Biome>, OverworldBiomeLayer> OVERWORLD_LAYERS = new HashMap<>(); // store as BiomeLayer

    public BiomeLayerManager() {
        super(GSON, "biome_layers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager resourceManager, ProfilerFiller profiler) {
        NETHER_LAYERS.clear();
        OVERWORLD_LAYERS.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : elements.entrySet()) {
            try {
                JsonObject obj = entry.getValue().getAsJsonObject();

                // Parse biomes
                List<ResourceLocation> biomeIds = new ArrayList<>();
                JsonElement biomeEl = obj.get("biome");
                if (biomeEl.isJsonArray()) {
                    for (JsonElement e : biomeEl.getAsJsonArray()) {
                        biomeIds.add(ResourceLocation.tryParse(GsonHelper.convertToString(e, "biome")));
                    }
                } else {
                    biomeIds.add(ResourceLocation.tryParse(GsonHelper.getAsString(obj, "biome")));
                }

                // Determine type
                String dimensionType = GsonHelper.getAsString(obj, "dimension", "nether");

                if (dimensionType.equalsIgnoreCase("overworld")) {
                    // Parse top, middle, bottom
                    LayerDefinition top = parseLayerDefinition(obj.getAsJsonObject("top"));
                    MiddleLayer middle = parseMiddleLayer(obj.getAsJsonObject("middle"));
                    LayerDefinition bottom = parseLayerDefinition(obj.getAsJsonObject("bottom"));

                    // Correct order: top, middle, bottom
                    OverworldBiomeLayer layer = new OverworldBiomeLayer(top, middle, bottom);

                    for (ResourceLocation biomeId : biomeIds) {
                        OVERWORLD_LAYERS.put(ResourceKey.create(Registries.BIOME, biomeId), layer);
                    }
                } else {
                    BiomeLayer layer = new BiomeLayer(
                            parseLayerDefinition(obj.getAsJsonObject("top")),
                            parseLayerDefinition(obj.getAsJsonObject("under"))
                    );

                    for (ResourceLocation biomeId : biomeIds) {
                        NETHER_LAYERS.put(ResourceKey.create(Registries.BIOME, biomeId), layer);
                    }
                }

            } catch (Exception e) {
                System.err.println("Failed to load biome layer: " + entry.getKey() + " -> " + e.getMessage());
            }
        }
    }

    private LayerDefinition parseLayerDefinition(JsonObject obj) {
        List<NoiseBlockRule> rules = new ArrayList<>();
        JsonArray arr = GsonHelper.getAsJsonArray(obj, "rules", new JsonArray());
        for (JsonElement el : arr) {
            JsonObject ruleObj = el.getAsJsonObject();
            BlockState block = BuiltInRegistries.BLOCK
                    .get(ResourceLocation.tryParse(GsonHelper.getAsString(ruleObj, "block")))
                    .defaultBlockState();

            ResourceKey<NormalNoise.NoiseParameters> noise = ResourceKey.create(
                    Registries.NOISE,
                    Objects.requireNonNull(ResourceLocation.tryParse(GsonHelper.getAsString(ruleObj, "noise")))
            );

            double threshold = GsonHelper.getAsDouble(ruleObj, "threshold", 0.0);
            double scale = GsonHelper.getAsDouble(ruleObj, "scale", 1.0);
            boolean invert = GsonHelper.getAsBoolean(ruleObj, "invert", false);
            rules.add(new NoiseBlockRule(block, noise, threshold, scale, invert));
        }

        BlockState fallback = BuiltInRegistries.BLOCK
                .get(ResourceLocation.tryParse(GsonHelper.getAsString(obj, "fallback")))
                .defaultBlockState();

        return new LayerDefinition(rules, fallback);
    }

    private MiddleLayer parseMiddleLayer(JsonObject obj) {
        List<MiddleSection> sections = new ArrayList<>();
        JsonArray arr = GsonHelper.getAsJsonArray(obj, "sections", new JsonArray());
        for (JsonElement el : arr) {
            JsonObject sec = el.getAsJsonObject();

            BlockState block = BuiltInRegistries.BLOCK
                    .get(ResourceLocation.tryParse(GsonHelper.getAsString(sec, "block")))
                    .defaultBlockState();

            int height = GsonHelper.getAsInt(sec, "height", 1);
            double threshold = GsonHelper.getAsDouble(sec, "threshold", 0.0);
            double scale = GsonHelper.getAsDouble(sec, "scale", 1.0);

            // Noise can be null for static blocks
            ResourceKey<NormalNoise.NoiseParameters> noise = null;
            if (sec.has("noise")) {
                noise = ResourceKey.create(
                        Registries.NOISE,
                        Objects.requireNonNull(ResourceLocation.tryParse(GsonHelper.getAsString(sec, "noise"))))
                ;
            }

            sections.add(new MiddleSection(block, noise, threshold, scale, height));
        }

        BlockState fallback = BuiltInRegistries.BLOCK
                .get(ResourceLocation.tryParse(GsonHelper.getAsString(obj, "fallback")))
                .defaultBlockState();

        return new MiddleLayer(sections, fallback);
    }

    public static BiomeLayer getNether(ResourceKey<Biome> biomeKey) {
        return NETHER_LAYERS.get(biomeKey);
    }

    public static OverworldBiomeLayer getOverworld(ResourceKey<Biome> biomeKey) {
        return OVERWORLD_LAYERS.get(biomeKey);
    }
}
