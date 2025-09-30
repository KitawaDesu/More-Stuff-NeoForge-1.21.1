package net.kitawa.more_stuff.util.datagen;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.experimentals.entities.ExperimentalCombatEntities;
import net.kitawa.more_stuff.util.tags.ModEntityTags;
import net.kitawa.more_stuff.worldgen.biome.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagProvider extends EntityTypeTagsProvider {
    public ModEntityTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MoreStuff.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(ModEntityTags.PRIORITY_TARGET)
                .add(EntityType.IRON_GOLEM);
        tag(EntityTypeTags.IMPACT_PROJECTILES)
                .add(ExperimentalCombatEntities.JAVELIN.get());
    }
}
