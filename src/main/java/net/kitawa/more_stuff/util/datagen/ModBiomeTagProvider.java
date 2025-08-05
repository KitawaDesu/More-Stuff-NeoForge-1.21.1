package net.kitawa.more_stuff.util.datagen;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.worldgen.biome.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends BiomeTagsProvider{
    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MoreStuff.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(BiomeTags.IS_NETHER)
                .add(ModBiomes.BLAZING_FOREST)
                .add(ModBiomes.PYROLIZED_FOREST)
                .add(ModBiomes.PYROLIZED_WASTES)
                .add(ModBiomes.METALLIC_FOREST)
                .add(ModBiomes.FROZEN_VALLEY);

        tag(BiomeTags.IS_OCEAN)
                .add(ModBiomes.FROZEN_AQUAPURANDA_FOREST)
                .add(ModBiomes.COLD_AQUAPURANDA_FOREST)
                .add(ModBiomes.AQUAPURANDA_FOREST)
                .add(ModBiomes.LUKEWARM_AQUAPURANDA_FOREST)
                .add(ModBiomes.WARM_AQUAPURANDA_FOREST);

        tag(BiomeTags.HAS_OCEAN_MONUMENT)
                .add(ModBiomes.FROZEN_AQUAPURANDA_FOREST)
                .add(ModBiomes.COLD_AQUAPURANDA_FOREST)
                .add(ModBiomes.AQUAPURANDA_FOREST)
                .add(ModBiomes.LUKEWARM_AQUAPURANDA_FOREST)
                .add(ModBiomes.WARM_AQUAPURANDA_FOREST);

        tag(BiomeTags.HAS_BASTION_REMNANT)
                .add(ModBiomes.BLAZING_FOREST)
                .add(ModBiomes.PYROLIZED_FOREST)
                .add(ModBiomes.PYROLIZED_WASTES)
                .add(ModBiomes.METALLIC_FOREST)
                .add(ModBiomes.FROZEN_VALLEY);

        tag(BiomeTags.HAS_NETHER_FOSSIL)
                .add(ModBiomes.FROZEN_VALLEY);
    }
}
