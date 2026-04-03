package net.kitawa.more_stuff.util.datagen;

import net.kitawa.more_stuff.blocks.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.LinkedHashSet;
import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {


        // =======================================================
        //  YOUR NORMAL SELF-DROPPING MOD BLOCKS
        // =======================================================
        ModBlocks.TERRACOTTA_COBBLED.values().forEach(b -> dropSelf(b.get()));
        ModBlocks.TERRACOTTA_BRICKS.values().forEach(b -> dropSelf(b.get()));
        dropSelf(ModBlocks.COBBLED_SANDSTONE.get());
        dropSelf(ModBlocks.COBBLED_RED_SANDSTONE.get());
        dropSelf(ModBlocks.SANDSTONE_BRICKS.get());
        dropSelf(ModBlocks.RED_SANDSTONE_BRICKS.get());
        ModBlocks.SLIME_BLOCKS.values().forEach(b -> dropSelf(b.get()));
        ModBlocks.STAINED_TINTED_GLASS.values().forEach(b -> dropSelf(b.get()));
        dropSelf(ModBlocks.AQUANDA_SLIME_BLOCK.get());
        dropSelf(ModBlocks.BASALT_BRICKS.get());
        dropSelf(ModBlocks.ANDESITE_BRICKS.get());
        dropSelf(ModBlocks.GRANITE_BRICKS.get());
        dropSelf(ModBlocks.DIORITE_BRICKS.get());
        dropSelf(ModBlocks.CUT_ANDESITE.get());
        dropSelf(ModBlocks.CUT_DIORITE.get());
        dropSelf(ModBlocks.CUT_GRANITE.get());
        dropSelf(ModBlocks.CHISELED_BASALT.get());
        dropSelf(ModBlocks.BASALT_TILES.get());
        dropSelf(ModBlocks.CUT_BASALT.get());
        dropSelf(ModBlocks.POLISHED_DRIPSTONE.get());
        dropSelf(ModBlocks.POLISHED_CALCITE.get());
        dropSelf(ModBlocks.CUT_DRIPSTONE.get());
        dropSelf(ModBlocks.CUT_CALCITE.get());
        dropSelf(ModBlocks.CUT_TUFF.get());
        dropSelf(ModBlocks.CUT_DEEPSLATE.get());
        dropSelf(ModBlocks.DRIPSTONE_BRICKS.get());
        dropSelf(ModBlocks.CALCITE_BRICKS.get());
        dropSelf(ModBlocks.CUT_GOLD_BLOCK.get());
        dropSelf(ModBlocks.CUT_GOLDEN_BRICKS.get());
        dropSelf(ModBlocks.CUT_QUARTZ_BLOCK.get());
        dropSelf(ModBlocks.TUFF_TILES.get());
        dropSelf(ModBlocks.QUARTZ_TILES.get());
        dropSelf(ModBlocks.CALCITE_TILES.get());
        dropSelf(ModBlocks.DRIPSTONE_TILES.get());
        dropSelf(ModBlocks.ANDESITE_TILES.get());
        dropSelf(ModBlocks.GRANITE_TILES.get());
        dropSelf(ModBlocks.DIORITE_TILES.get());
        dropSelf(ModBlocks.POLISHED_BLACKSTONE_TILES.get());
        dropSelf(ModBlocks.POLISHED_PRISMARINE.get());
        dropSelf(ModBlocks.CUT_PRISMARINE.get());
        dropSelf(ModBlocks.PRISMARINE_TILES.get());
        dropSelf(ModBlocks.PRISMARINE_BRICK_PILLAR.get());
        dropSelf(ModBlocks.POLISHED_BLACKSTONE_BRICK_PILLAR.get());
        dropSelf(ModBlocks.BASALT_BRICK_PILLAR.get());
        dropSelf(ModBlocks.TUFF_BRICK_PILLAR.get());
        dropSelf(ModBlocks.ANDESITE_BRICK_PILLAR.get());
        dropSelf(ModBlocks.GRANITE_BRICK_PILLAR.get());
        dropSelf(ModBlocks.DIORITE_BRICK_PILLAR.get());
        dropSelf(ModBlocks.CALCITE_BRICK_PILLAR.get());
        dropSelf(ModBlocks.DRIPSTONE_BRICK_PILLAR.get());
        dropSelf(ModBlocks.STONE_BRICK_PILLAR.get());
        dropSelf(ModBlocks.COPPER_CHAIN.get());
        dropSelf(ModBlocks.EXPOSED_COPPER_CHAIN.get());
        dropSelf(ModBlocks.WEATHERED_COPPER_CHAIN.get());
        dropSelf(ModBlocks.OXIDIZED_COPPER_CHAIN.get());
        dropSelf(ModBlocks.WAXED_COPPER_CHAIN.get());
        dropSelf(ModBlocks.WAXED_EXPOSED_COPPER_CHAIN.get());
        dropSelf(ModBlocks.WAXED_WEATHERED_COPPER_CHAIN.get());
        dropSelf(ModBlocks.WAXED_OXIDIZED_COPPER_CHAIN.get());
        dropSelf(ModBlocks.GOLDEN_CHAIN.get());
        dropSelf(ModBlocks.ROSE_GOLDEN_CHAIN.get());
        ModBlocks.SOUL_LANTERNS.values().forEach(b -> dropSelf(b.get()));
        ModBlocks.LANTERNS.values().forEach(b -> dropSelf(b.get()));
        dropSelf(ModBlocks.STONE_BRICK_TILES.get());
        dropSelf(ModBlocks.CUT_POLISHED_BLACKSTONE.get());
        dropSelf(ModBlocks.SANDSTONE_TILES.get());
        dropSelf(ModBlocks.RED_SANDSTONE_TILES.get());
        dropSelf(ModBlocks.SANDSTONE_BRICK_PILLAR.get());
        dropSelf(ModBlocks.RED_SANDSTONE_BRICK_PILLAR.get());
    }


    // ================================================================
    // REQUIRED: Return full list of known blocks
    // ================================================================
    @Override
    protected Iterable<Block> getKnownBlocks() {
        Set<Block> blocks = new LinkedHashSet<>();

        // Only mod blocks here — NEVER vanilla blocks
        ModBlocks.TERRACOTTA_COBBLED.values().forEach(b -> blocks.add(b.get()));
        ModBlocks.TERRACOTTA_BRICKS.values().forEach(b -> blocks.add(b.get()));
        blocks.add(ModBlocks.COBBLED_SANDSTONE.get());
        blocks.add(ModBlocks.COBBLED_RED_SANDSTONE.get());
        blocks.add(ModBlocks.SANDSTONE_BRICKS.get());
        blocks.add(ModBlocks.RED_SANDSTONE_BRICKS.get());
        ModBlocks.SLIME_BLOCKS.values().forEach(b -> blocks.add(b.get()));
        ModBlocks.STAINED_TINTED_GLASS.values().forEach(b -> blocks.add(b.get()));
        blocks.add(ModBlocks.AQUANDA_SLIME_BLOCK.get());
        blocks.add(ModBlocks.BASALT_BRICKS.get());
        blocks.add(ModBlocks.CHISELED_BASALT.get());
        blocks.add(ModBlocks.ANDESITE_BRICKS.get());
        blocks.add(ModBlocks.GRANITE_BRICKS.get());
        blocks.add(ModBlocks.DIORITE_BRICKS.get());
        blocks.add(ModBlocks.CUT_ANDESITE.get());
        blocks.add(ModBlocks.CUT_DIORITE.get());
        blocks.add(ModBlocks.CUT_GRANITE.get());
        blocks.add(ModBlocks.CHISELED_BASALT.get());
        blocks.add(ModBlocks.BASALT_TILES.get());
        blocks.add(ModBlocks.CUT_BASALT.get());
        blocks.add(ModBlocks.POLISHED_DRIPSTONE.get());
        blocks.add(ModBlocks.POLISHED_CALCITE.get());
        blocks.add(ModBlocks.CUT_DRIPSTONE.get());
        blocks.add(ModBlocks.CUT_CALCITE.get());
        blocks.add(ModBlocks.CUT_TUFF.get());
        blocks.add(ModBlocks.CUT_DEEPSLATE.get());
        blocks.add(ModBlocks.DRIPSTONE_BRICKS.get());
        blocks.add(ModBlocks.CALCITE_BRICKS.get());
        blocks.add(ModBlocks.CUT_GOLD_BLOCK.get());
        blocks.add(ModBlocks.CUT_GOLDEN_BRICKS.get());
        blocks.add(ModBlocks.CUT_QUARTZ_BLOCK.get());
        blocks.add(ModBlocks.TUFF_TILES.get());
        blocks.add(ModBlocks.QUARTZ_TILES.get());
        blocks.add(ModBlocks.CALCITE_TILES.get());
        blocks.add(ModBlocks.DRIPSTONE_TILES.get());
        blocks.add(ModBlocks.ANDESITE_TILES.get());
        blocks.add(ModBlocks.GRANITE_TILES.get());
        blocks.add(ModBlocks.DIORITE_TILES.get());
        blocks.add(ModBlocks.POLISHED_BLACKSTONE_TILES.get());
        blocks.add(ModBlocks.POLISHED_PRISMARINE.get());
        blocks.add(ModBlocks.CUT_PRISMARINE.get());
        blocks.add(ModBlocks.PRISMARINE_TILES.get());
        blocks.add(ModBlocks.PRISMARINE_BRICK_PILLAR.get());
        blocks.add(ModBlocks.POLISHED_BLACKSTONE_BRICK_PILLAR.get());
        blocks.add(ModBlocks.BASALT_BRICK_PILLAR.get());
        blocks.add(ModBlocks.TUFF_BRICK_PILLAR.get());
        blocks.add(ModBlocks.ANDESITE_BRICK_PILLAR.get());
        blocks.add(ModBlocks.GRANITE_BRICK_PILLAR.get());
        blocks.add(ModBlocks.DIORITE_BRICK_PILLAR.get());
        blocks.add(ModBlocks.CALCITE_BRICK_PILLAR.get());
        blocks.add(ModBlocks.DRIPSTONE_BRICK_PILLAR.get());
        blocks.add(ModBlocks.STONE_BRICK_PILLAR.get());
        blocks.add(ModBlocks.COPPER_CHAIN.get());
        blocks.add(ModBlocks.EXPOSED_COPPER_CHAIN.get());
        blocks.add(ModBlocks.WEATHERED_COPPER_CHAIN.get());
        blocks.add(ModBlocks.OXIDIZED_COPPER_CHAIN.get());
        blocks.add(ModBlocks.WAXED_COPPER_CHAIN.get());
        blocks.add(ModBlocks.WAXED_EXPOSED_COPPER_CHAIN.get());
        blocks.add(ModBlocks.WAXED_WEATHERED_COPPER_CHAIN.get());
        blocks.add(ModBlocks.WAXED_OXIDIZED_COPPER_CHAIN.get());
        blocks.add(ModBlocks.GOLDEN_CHAIN.get());
        blocks.add(ModBlocks.ROSE_GOLDEN_CHAIN.get());
        ModBlocks.SOUL_LANTERNS.values().forEach(b -> blocks.add(b.get()));
        ModBlocks.LANTERNS.values().forEach(b -> blocks.add(b.get()));
        blocks.add(ModBlocks.STONE_BRICK_TILES.get());
        blocks.add(ModBlocks.CUT_POLISHED_BLACKSTONE.get());
        blocks.add(ModBlocks.SANDSTONE_TILES.get());
        blocks.add(ModBlocks.RED_SANDSTONE_TILES.get());
        blocks.add(ModBlocks.SANDSTONE_BRICK_PILLAR.get());
        blocks.add(ModBlocks.RED_SANDSTONE_BRICK_PILLAR.get());

        return blocks;
    }
}
