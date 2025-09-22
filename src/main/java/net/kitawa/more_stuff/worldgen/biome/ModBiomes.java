package net.kitawa.more_stuff.worldgen.biome;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes {
    public static final ResourceKey<Biome> FROZEN_AQUAPURANDA_FOREST = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "frozen_aquapuranda_forest"));
    public static final ResourceKey<Biome> COLD_AQUAPURANDA_FOREST = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "cold_aquapuranda_forest"));
    public static final ResourceKey<Biome> AQUAPURANDA_FOREST = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "aquapuranda_forest"));
    public static final ResourceKey<Biome> LUKEWARM_AQUAPURANDA_FOREST = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "lukewarm_aquapuranda_forest"));
    public static final ResourceKey<Biome> WARM_AQUAPURANDA_FOREST = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "warm_aquapuranda_forest"));
    public static final ResourceKey<Biome> METALLIC_FOREST = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "metallic_forest"));
    public static final ResourceKey<Biome> BLAZING_FOREST = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "blazing_forest"));
    public static final ResourceKey<Biome> PYROLIZED_FOREST = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "pyrolized_forest"));
    public static final ResourceKey<Biome> PYROLIZED_WASTES = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "pyrolized_wastes"));
    public static final ResourceKey<Biome> FROZEN_VALLEY = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "frozen_valley"));
    public static final ResourceKey<Biome> REDSTONIC_CAVES = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "redstonic_caves"));
    public static final ResourceKey<Biome> FROSTBITTEN_CAVERNS = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "frostbitten_caverns"));
    public static final ResourceKey<Biome> VOLTAIC_HOLLOWS = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "voltaic_hollows"));
    public static final ResourceKey<Biome> FUNGAL_CAVES = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "fungal_caves"));
}
