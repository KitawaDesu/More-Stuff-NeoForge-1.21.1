package net.kitawa.more_stuff.util.tags;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ModBiomeTags {
    public static final TagKey<Biome> AQUANDA_BIOMES = create("aquanda_biomes");


    private ModBiomeTags() {
    }

    private static TagKey<Biome> create(String name) {
        return TagKey.create(
                Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name)
        );
    }
}