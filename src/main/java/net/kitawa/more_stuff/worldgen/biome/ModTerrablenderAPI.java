package net.kitawa.more_stuff.worldgen.biome;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.resources.ResourceLocation;
import terrablender.api.Regions;

public class ModTerrablenderAPI {
    public static void registerBiomes() {
        Regions.register(new ModOverworldRegion(ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "overworld"), 10));
        Regions.register(new ModNetherRegion(ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "the_nether"), 100));
    }
}
