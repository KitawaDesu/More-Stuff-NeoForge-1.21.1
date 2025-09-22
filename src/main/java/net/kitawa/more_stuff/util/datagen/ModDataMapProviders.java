package net.kitawa.more_stuff.util.datagen;

import net.kitawa.more_stuff.blocks.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable;
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProviders extends DataMapProvider {
    public ModDataMapProviders(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {
        // === Oxidizables ===
        addOxidizable(ModBlocks.CUT_COPPER_BRICKS.get(), ModBlocks.EXPOSED_CUT_COPPER_BRICKS.get());
        addOxidizable(ModBlocks.EXPOSED_CUT_COPPER_BRICKS.get(), ModBlocks.WEATHERED_CUT_COPPER_BRICKS.get());
        addOxidizable(ModBlocks.WEATHERED_CUT_COPPER_BRICKS.get(), ModBlocks.OXIDIZED_CUT_COPPER_BRICKS.get());

        addOxidizable(ModBlocks.COPPER_PILLAR.get(), ModBlocks.EXPOSED_COPPER_PILLAR.get());
        addOxidizable(ModBlocks.EXPOSED_COPPER_PILLAR.get(), ModBlocks.WEATHERED_COPPER_PILLAR.get());
        addOxidizable(ModBlocks.WEATHERED_COPPER_PILLAR.get(), ModBlocks.OXIDIZED_COPPER_PILLAR.get());

        // === Waxables ===
        addWaxable(ModBlocks.CUT_COPPER_BRICKS.get(), ModBlocks.WAXED_CUT_COPPER_BRICKS.get());
        addWaxable(ModBlocks.EXPOSED_CUT_COPPER_BRICKS.get(), ModBlocks.WAXED_EXPOSED_CUT_COPPER_BRICKS.get());
        addWaxable(ModBlocks.WEATHERED_CUT_COPPER_BRICKS.get(), ModBlocks.WAXED_WEATHERED_CUT_COPPER_BRICKS.get());
        addWaxable(ModBlocks.OXIDIZED_CUT_COPPER_BRICKS.get(), ModBlocks.WAXED_OXIDIZED_CUT_COPPER_BRICKS.get());

        addWaxable(ModBlocks.COPPER_PILLAR.get(), ModBlocks.WAXED_COPPER_PILLAR.get());
        addWaxable(ModBlocks.EXPOSED_COPPER_PILLAR.get(), ModBlocks.WAXED_EXPOSED_COPPER_PILLAR.get());
        addWaxable(ModBlocks.WEATHERED_COPPER_PILLAR.get(), ModBlocks.WAXED_WEATHERED_COPPER_PILLAR.get());
        addWaxable(ModBlocks.OXIDIZED_COPPER_PILLAR.get(), ModBlocks.WAXED_OXIDIZED_COPPER_PILLAR.get());
    }

    // === Helper Methods ===
    private void addOxidizable(Block from, Block to) {
        builder(NeoForgeDataMaps.OXIDIZABLES)
                .add(from.builtInRegistryHolder(), new Oxidizable(to), false);
    }

    private void addWaxable(Block from, Block to) {
        builder(NeoForgeDataMaps.WAXABLES)
                .add(from.builtInRegistryHolder(), new Waxable(to), false);
    }
}
