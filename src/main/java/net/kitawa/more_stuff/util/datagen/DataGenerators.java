package net.kitawa.more_stuff.util.datagen;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;


import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = MoreStuff.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
        BlockTagsProvider blockTagsProvider = new ModBlockTagProvider(packOutput, lookupProvider, existingFileHelper);
        BiomeTagsProvider biomeTagsProvider = new ModBiomeTagProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), biomeTagsProvider);
        EntityTypeTagsProvider entityTypeTagsProvider = new ModEntityTypeTagProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), entityTypeTagsProvider);
        generator.addProvider(event.includeServer(), new ModItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new ModDatapackProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ModGlobalLootModifierProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ModDataMapProviders(packOutput, lookupProvider));
    }
}