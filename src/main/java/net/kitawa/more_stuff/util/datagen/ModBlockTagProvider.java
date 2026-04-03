package net.kitawa.more_stuff.util.datagen;

import net.kitawa.more_stuff.compat.create.blocks.CreateCompatBlocks;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.compat.create.blocks.CreateIronworksCompatBlocks;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MoreStuff.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(ModBlockTags.UNSUITABLE_CEILINGS)
                .addTag(BlockTags.CLIMBABLE);

        tag(BlockTags.MUSHROOM_GROW_BLOCK)
                .add(ModBlocks.GLOWMOSS_BLOCK.get());

        tag(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)
                .add(ModBlocks.ICE_SHEET.get())
                .addTag(ModBlockTags.STONES);

        tag(BlockTags.BASE_STONE_OVERWORLD)
                .add(ModBlocks.VOLTAIC_SLATE.get());

        tag(ModBlockTags.METALLIC_BAMBOOS)
                .add(ModBlocks.IRON_BAMBOO_SAPLING.get())
                .add(ModBlocks.IRON_BAMBOO_STALK.get())
                .add(ModBlocks.GOLDEN_BAMBOO_SAPLING.get())
                .add(ModBlocks.GOLDEN_BAMBOO_STALK.get())
                .add(ModBlocks.COPPER_BAMBOO_SAPLING.get())
                .add(ModBlocks.COPPER_BAMBOO_STALK.get())
                .add(ModBlocks.ANCIENT_BAMBOO_SAPLING.get())
                .add(ModBlocks.ANCIENT_BAMBOO_STALK.get())
                .add(ModBlocks.PYROLIZED_BAMBOO_SAPLING.get())
                .add(ModBlocks.PYROLIZED_BAMBOO_STALK.get())
                .add(ModBlocks.PALLADIUM_BAMBOO_SAPLING.get())
                .add(ModBlocks.PALLADIUM_BAMBOO_STALK.get())
                .add(ModBlocks.BRASS_BAMBOO_SAPLING.get())
                .add(ModBlocks.BRASS_BAMBOO_STALK.get())
                .add(ModBlocks.BRONZE_BAMBOO_SAPLING.get())
                .add(ModBlocks.BRONZE_BAMBOO_STALK.get())
                .add(ModBlocks.CINCINNASITE_BAMBOO_SAPLING.get())
                .add(ModBlocks.CINCINNASITE_BAMBOO_STALK.get())
                .add(ModBlocks.TIN_BAMBOO_SAPLING.get())
                .add(ModBlocks.TIN_BAMBOO_STALK.get())
                .add(ModBlocks.ZINC_BAMBOO_SAPLING.get())
                .add(ModBlocks.ZINC_BAMBOO_STALK.get())
                .add(ModBlocks.STEEL_BAMBOO_SAPLING.get())
                .add(ModBlocks.STEEL_BAMBOO_STALK.get());

        tag(ModBlockTags.ELECTRICITY_CAN_TRAVEL_THROUGH)
                // Copper blocks with all oxidation stages and waxed variants
                .add(Blocks.COPPER_BLOCK)
                .add(Blocks.EXPOSED_COPPER)
                .add(Blocks.WEATHERED_COPPER)
                .add(Blocks.OXIDIZED_COPPER)
                .add(Blocks.WAXED_COPPER_BLOCK)
                .add(Blocks.WAXED_EXPOSED_COPPER)
                .add(Blocks.WAXED_WEATHERED_COPPER)
                .add(Blocks.WAXED_OXIDIZED_COPPER)

                .add(Blocks.CHISELED_COPPER)
                .add(Blocks.EXPOSED_CHISELED_COPPER)
                .add(Blocks.WEATHERED_CHISELED_COPPER)
                .add(Blocks.OXIDIZED_CHISELED_COPPER)
                .add(Blocks.WAXED_CHISELED_COPPER)
                .add(Blocks.WAXED_EXPOSED_CHISELED_COPPER)
                .add(Blocks.WAXED_WEATHERED_CHISELED_COPPER)
                .add(Blocks.WAXED_OXIDIZED_CHISELED_COPPER)

                .add(Blocks.CUT_COPPER)
                .add(Blocks.EXPOSED_CUT_COPPER)
                .add(Blocks.WEATHERED_CUT_COPPER)
                .add(Blocks.OXIDIZED_CUT_COPPER)
                .add(Blocks.WAXED_CUT_COPPER)
                .add(Blocks.WAXED_EXPOSED_CUT_COPPER)
                .add(Blocks.WAXED_WEATHERED_CUT_COPPER)
                .add(Blocks.WAXED_OXIDIZED_CUT_COPPER)

                .add(Blocks.CUT_COPPER_SLAB)
                .add(Blocks.EXPOSED_CUT_COPPER_SLAB)
                .add(Blocks.WEATHERED_CUT_COPPER_SLAB)
                .add(Blocks.OXIDIZED_CUT_COPPER_SLAB)
                .add(Blocks.WAXED_CUT_COPPER_SLAB)
                .add(Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB)
                .add(Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB)
                .add(Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB)

                .add(Blocks.CUT_COPPER_STAIRS)
                .add(Blocks.EXPOSED_CUT_COPPER_STAIRS)
                .add(Blocks.WEATHERED_CUT_COPPER_STAIRS)
                .add(Blocks.OXIDIZED_CUT_COPPER_STAIRS)
                .add(Blocks.WAXED_CUT_COPPER_STAIRS)
                .add(Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS)
                .add(Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS)
                .add(Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS)

                // Copper-related blocks with oxidation stages and waxed variants
                .add(Blocks.COPPER_DOOR)
                .add(Blocks.EXPOSED_COPPER_DOOR)
                .add(Blocks.WEATHERED_COPPER_DOOR)
                .add(Blocks.OXIDIZED_COPPER_DOOR)
                .add(Blocks.WAXED_COPPER_DOOR)
                .add(Blocks.WAXED_EXPOSED_COPPER_DOOR)
                .add(Blocks.WAXED_WEATHERED_COPPER_DOOR)
                .add(Blocks.WAXED_OXIDIZED_COPPER_DOOR)

                .add(Blocks.COPPER_TRAPDOOR)
                .add(Blocks.EXPOSED_COPPER_TRAPDOOR)
                .add(Blocks.WEATHERED_COPPER_TRAPDOOR)
                .add(Blocks.OXIDIZED_COPPER_TRAPDOOR)
                .add(Blocks.WAXED_COPPER_TRAPDOOR)
                .add(Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR)
                .add(Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR)
                .add(Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR)

                .add(Blocks.COPPER_BULB)
                .add(Blocks.EXPOSED_COPPER_BULB)
                .add(Blocks.WEATHERED_COPPER_BULB)
                .add(Blocks.OXIDIZED_COPPER_BULB)
                .add(Blocks.WAXED_COPPER_BULB)
                .add(Blocks.WAXED_EXPOSED_COPPER_BULB)
                .add(Blocks.WAXED_WEATHERED_COPPER_BULB)
                .add(Blocks.WAXED_OXIDIZED_COPPER_BULB)

                .add(Blocks.COPPER_GRATE)
                .add(Blocks.EXPOSED_COPPER_GRATE)
                .add(Blocks.WEATHERED_COPPER_GRATE)
                .add(Blocks.OXIDIZED_COPPER_GRATE)
                .add(Blocks.WAXED_COPPER_GRATE)
                .add(Blocks.WAXED_EXPOSED_COPPER_GRATE)
                .add(Blocks.WAXED_WEATHERED_COPPER_GRATE)
                .add(Blocks.WAXED_OXIDIZED_COPPER_GRATE)

                .add(Blocks.ANCIENT_DEBRIS)
                .add(Blocks.NETHERITE_BLOCK)
                .add(Blocks.ANVIL)
                .add(Blocks.LIGHTNING_ROD)
                .add(Blocks.IRON_BLOCK)
                .add(Blocks.GOLD_BLOCK)
                .add(Blocks.IRON_DOOR)
                .add(ModBlocks.ANCHOR_BLOCK.get())
                .add(ModBlocks.STORMVEIN.get())
                .addTag(ModBlockTags.METALLIC_BAMBOOS)
                .addTag(ModBlockTags.POWER_SOURCES)
                .addTag(ModBlockTags.ANCHOR_BLOCKS)
                .add(Blocks.WATER)
                .add(Blocks.LAVA)
                .add(Blocks.POWDER_SNOW)
                .addTag(BlockTags.REPLACEABLE)
                .addTag(BlockTags.SAPLINGS)
                .addTag(BlockTags.FLOWERS)
                .addTag(BlockTags.FLOWER_POTS)
                .addTag(BlockTags.AIR)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.ENCHANTMENT_POWER_TRANSMITTER);

        tag(ModBlockTags.ANCHOR_BLOCKS)
                .addTag(ModBlockTags.POWER_SOURCES)
                .add(ModBlocks.TESLA_COIL.get());

        tag(BlockTags.LEAVES)
                .add(ModBlocks.AQUANDA_GEL.get())
                .add(ModBlocks.GLOWING_AQUANDA_GEL.get())
                .add(ModBlocks.HYBERNATUS_LEAVES.get());

        tag(BlockTags.IMPERMEABLE)
                .add(ModBlocks.AQUANDA_GEL.get())
                .add(ModBlocks.GLOWING_AQUANDA_GEL.get())
                .add(ModBlocks.HYBERNATUS_LEAVES.get())
                .add(ModBlocks.HYBERNATIC_CRYSTAL_BLOCK.get())
                .add(ModBlocks.PHANTASMIC_GRASS.get())
                .add(ModBlocks.PHANTASMIC_NYLIUM.get())
                .add(ModBlocks.PHANTASMIC_ENDSTONE.get())
                .add(ModBlocks.PHANTASMIC_TALL_GRASS.get());

        tag(ModBlockTags.POWER_SOURCES)
                .add(ModBlocks.ANCHOR_BLOCK.get())
                .add(ModBlocks.REDSTONIC_BLOCK.get())
                .add(Blocks.REDSTONE_BLOCK)
                .add(Blocks.REDSTONE_TORCH)
                .add(Blocks.REDSTONE_WALL_TORCH);

        tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .add(ModBlocks.ICE_SHEET.get())
                .addTag(ModBlockTags.STONES);

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.COPPER_NYLIUM.get())
                .add(ModBlocks.IRON_NYLIUM.get())
                .add(ModBlocks.GOLDEN_NYLIUM.get())
                .add(ModBlocks.ANCIENT_NYLIUM.get())
                .add(ModBlocks.PALLADIUM_NYLIUM.get())
                .add(ModBlocks.BRASS_NYLIUM.get())
                .add(ModBlocks.BRONZE_NYLIUM.get())
                .add(ModBlocks.CINCINNASITE_NYLIUM.get())
                .add(ModBlocks.TIN_NYLIUM.get())
                .add(ModBlocks.ZINC_NYLIUM.get())
                .add(ModBlocks.STEEL_NYLIUM.get())
                .add(ModBlocks.BLAZING_MAGMA_BLOCK.get())
                .add(ModBlocks.PYROLIZED_EXPERIENCE_ORE.get())
                .add(ModBlocks.NETHER_EXPERIENCE_ORE.get())
                .add(ModBlocks.PYROLIZED_COPPER_ORE.get())
                .add(ModBlocks.NETHER_COPPER_ORE.get())
                .add(ModBlocks.PYROLIZED_IRON_ORE.get())
                .add(ModBlocks.NETHER_IRON_ORE.get())
                .add(ModBlocks.PYROLIZED_QUARTZ_ORE.get())
                .add(ModBlocks.PYROLIZED_GOLD_ORE.get())
                .add(ModBlocks.ROSARITE_BLOCK.get())
                .add(ModBlocks.ROSE_GOLD_BLOCK.get())
                .add(ModBlocks.PYROLIZED_NETHERRACK.get())
                .add(ModBlocks.FROZEN_NETHERRACK.get())
                .add(ModBlocks.PACKED_FROZEN_NETHERRACK.get())
                .add(ModBlocks.BLUE_FROZEN_NETHERRACK.get())
                .add(ModBlocks.FREEZING_MAGMA_BLOCK.get())
                .add(ModBlocks.PACKED_FREEZING_MAGMA_BLOCK.get())
                .add(ModBlocks.BLUE_FREEZING_MAGMA_BLOCK.get())
                .add(ModBlocks.FROZEN_COPPER_ORE.get())
                .add(ModBlocks.FROZEN_IRON_ORE.get())
                .add(ModBlocks.FROZEN_GOLD_ORE.get())
                .add(ModBlocks.FROZEN_QUARTZ_ORE.get())
                .add(ModBlocks.FROZEN_EXPERIENCE_ORE.get())
                .add(ModBlocks.NETHER_BRICK_PILLAR.get())
                .add(ModBlocks.CRACKED_NETHER_BRICK_PILLAR.get())
                .add(ModBlocks.CRACKED_NETHER_BRICK_SLAB.get())
                .add(ModBlocks.CRACKED_NETHER_BRICK_STAIRS.get())
                .add(ModBlocks.CRACKED_NETHER_BRICK_WALL.get())
                .add(ModBlocks.CRACKED_NETHER_BRICK_FENCE.get())
                .add(ModBlocks.BLAZE_BRICKS.get())
                .add(ModBlocks.BLAZE_BRICK_PILLAR.get())
                .add(ModBlocks.BLAZE_BRICK_SLAB.get())
                .add(ModBlocks.BLAZE_BRICK_STAIRS.get())
                .add(ModBlocks.BLAZE_BRICK_FENCE.get())
                .add(ModBlocks.BLAZE_BRICK_WALL.get())
                .add(ModBlocks.PYROLIZED_NETHER_BRICKS.get())
                .add(ModBlocks.PYROLIZED_NETHER_BRICK_PILLAR.get())
                .add(ModBlocks.PYROLIZED_NETHER_BRICK_SLAB.get())
                .add(ModBlocks.PYROLIZED_NETHER_BRICK_STAIRS.get())
                .add(ModBlocks.PYROLIZED_NETHER_BRICK_WALL.get())
                .add(ModBlocks.PYROLIZED_NETHER_BRICK_FENCE.get())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICKS.get())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_PILLAR.get())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_SLAB.get())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_STAIRS.get())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_WALL.get())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_FENCE.get())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICKS.get())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICK_PILLAR.get())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICK_SLAB.get())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICK_STAIRS.get())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICK_FENCE.get())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICK_WALL.get())
                .add(ModBlocks.RED_NETHER_BRICK_PILLAR.get())
                .add(ModBlocks.RED_NETHER_BRICK_FENCE.get())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICKS.get())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICK_PILLAR.get())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICK_SLAB.get())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICK_STAIRS.get())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICK_WALL.get())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICK_FENCE.get())
                .add(ModBlocks.RED_BLAZE_BRICKS.get())
                .add(ModBlocks.RED_BLAZE_BRICK_PILLAR.get())
                .add(ModBlocks.RED_BLAZE_BRICK_SLAB.get())
                .add(ModBlocks.RED_BLAZE_BRICK_STAIRS.get())
                .add(ModBlocks.RED_BLAZE_BRICK_FENCE.get())
                .add(ModBlocks.RED_BLAZE_BRICK_WALL.get())
                .add(ModBlocks.WARPED_NETHER_BRICKS.get())
                .add(ModBlocks.WARPED_NETHER_BRICK_PILLAR.get())
                .add(ModBlocks.WARPED_NETHER_BRICK_SLAB.get())
                .add(ModBlocks.WARPED_NETHER_BRICK_STAIRS.get())
                .add(ModBlocks.WARPED_NETHER_BRICK_WALL.get())
                .add(ModBlocks.WARPED_NETHER_BRICK_FENCE.get())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICKS.get())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICK_PILLAR.get())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICK_SLAB.get())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICK_STAIRS.get())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICK_WALL.get())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICK_FENCE.get())
                .add(ModBlocks.WARPED_BLAZE_BRICKS.get())
                .add(ModBlocks.WARPED_BLAZE_BRICK_PILLAR.get())
                .add(ModBlocks.WARPED_BLAZE_BRICK_SLAB.get())
                .add(ModBlocks.WARPED_BLAZE_BRICK_STAIRS.get())
                .add(ModBlocks.WARPED_BLAZE_BRICK_FENCE.get())
                .add(ModBlocks.WARPED_BLAZE_BRICK_WALL.get())
                .add(ModBlocks.VOLTAIC_SLATE.get())
                .add(ModBlocks.STORMVEIN.get())
                .add(ModBlocks.TESLA_COIL.get())
                .add(ModBlocks.POINTED_REDSTONIC.get())
                .add(ModBlocks.REDSTONIC_BLOCK.get())
                .add(ModBlocks.ICICLE.get())
                .add(ModBlocks.ICE_SHEET.get())
                .add(ModBlocks.ANCHOR_BLOCK.get())
                .add(ModBlocks.ROSE_GOLD_DOOR.get())
                .add(ModBlocks.ROSE_GOLD_GRATE.get())
                .add(ModBlocks.ROSE_GOLD_PILLAR.get())
                .add(ModBlocks.ROSE_GOLD_TRAPDOOR.get())
                .add(ModBlocks.CUT_ROSE_GOLD.get())
                .add(ModBlocks.CUT_ROSE_GOLD_BRICKS.get())
                .add(ModBlocks.CUT_ROSE_GOLD_SLAB.get())
                .add(ModBlocks.CUT_ROSE_GOLD_STAIRS.get())
                .add(ModBlocks.CHISELED_ROSE_GOLD.get())
                .add(ModBlocks.CUT_COPPER_BRICKS.get())
                .add(ModBlocks.EXPOSED_CUT_COPPER_BRICKS.get())
                .add(ModBlocks.OXIDIZED_CUT_COPPER_BRICKS.get())
                .add(ModBlocks.WEATHERED_CUT_COPPER_BRICKS.get())
                .add(ModBlocks.WAXED_CUT_COPPER_BRICKS.get())
                .add(ModBlocks.WAXED_EXPOSED_CUT_COPPER_BRICKS.get())
                .add(ModBlocks.WAXED_OXIDIZED_CUT_COPPER_BRICKS.get())
                .add(ModBlocks.WAXED_WEATHERED_CUT_COPPER_BRICKS.get())
                .add(ModBlocks.COPPER_PILLAR.get())
                .add(ModBlocks.EXPOSED_COPPER_PILLAR.get())
                .add(ModBlocks.OXIDIZED_COPPER_PILLAR.get())
                .add(ModBlocks.WEATHERED_COPPER_PILLAR.get())
                .add(ModBlocks.WAXED_COPPER_PILLAR.get())
                .add(ModBlocks.WAXED_EXPOSED_COPPER_PILLAR.get())
                .add(ModBlocks.WAXED_OXIDIZED_COPPER_PILLAR.get())
                .add(ModBlocks.WAXED_WEATHERED_COPPER_PILLAR.get())
                .add(CreateCompatBlocks.PYROLIZED_ZINC_ORE.get())
                .add(CreateCompatBlocks.NETHER_ZINC_ORE.get())
                .add(CreateCompatBlocks.FROZEN_ZINC_ORE.get())
                .add(CreateIronworksCompatBlocks.PYROLIZED_TIN_ORE.get())
                .add(CreateIronworksCompatBlocks.NETHER_TIN_ORE.get())
                .add(CreateIronworksCompatBlocks.FROZEN_TIN_ORE.get())
                .add(ModBlocks.COBBLED_SANDSTONE.get())
                .add(ModBlocks.COBBLED_RED_SANDSTONE.get())
                .add(ModBlocks.SANDSTONE_BRICKS.get())
                .add(ModBlocks.RED_SANDSTONE_BRICKS.get())
                .add(ModBlocks.ANDESITE_BRICKS.get())
                .add(ModBlocks.GRANITE_BRICKS.get())
                .add(ModBlocks.DIORITE_BRICKS.get())
                .add(ModBlocks.CUT_ANDESITE.get())
                .add(ModBlocks.CUT_GRANITE.get())
                .add(ModBlocks.CUT_DIORITE.get())
                .add(ModBlocks.BASALT_BRICKS.get())
                .add(ModBlocks.CHISELED_BASALT.get())
                .add(ModBlocks.CUT_GOLD_BLOCK.get())
                .add(ModBlocks.CUT_GOLDEN_BRICKS.get())
                .add(ModBlocks.BASALT_TILES.get())
                .add(ModBlocks.CUT_BASALT.get())
                .add(ModBlocks.POLISHED_DRIPSTONE.get())
                .add(ModBlocks.POLISHED_DRIPSTONE.get())
                .add(ModBlocks.POLISHED_CALCITE.get())
                .add(ModBlocks.CUT_DRIPSTONE.get())
                .add(ModBlocks.CUT_CALCITE.get())
                .add(ModBlocks.CUT_TUFF.get())
                .add(ModBlocks.CUT_DEEPSLATE.get())
                .add(ModBlocks.DRIPSTONE_BRICKS.get())
                .add(ModBlocks.CALCITE_BRICKS.get())
                .add(ModBlocks.CUT_GOLD_BLOCK.get())
                .add(ModBlocks.CUT_GOLDEN_BRICKS.get())
                .add(ModBlocks.TUFF_TILES.get())
                .add(ModBlocks.CUT_QUARTZ_BLOCK.get())
                .add(ModBlocks.QUARTZ_TILES.get())
                .add(ModBlocks.CALCITE_TILES.get())
                .add(ModBlocks.DRIPSTONE_TILES.get())
                .add(ModBlocks.ANDESITE_TILES.get())
                .add(ModBlocks.DIORITE_TILES.get())
                .add(ModBlocks.GRANITE_TILES.get())
                .add(ModBlocks.POLISHED_BLACKSTONE_TILES.get())
                .add(ModBlocks.POLISHED_PRISMARINE.get())
                .add(ModBlocks.CUT_PRISMARINE.get())
                .add(ModBlocks.PRISMARINE_TILES.get())
                .add(ModBlocks.PRISMARINE_BRICK_PILLAR.get())
                .add(ModBlocks.POLISHED_BLACKSTONE_BRICK_PILLAR.get())
                .add(ModBlocks.BASALT_BRICK_PILLAR.get())
                .add(ModBlocks.TUFF_BRICK_PILLAR.get())
                .add(ModBlocks.ANDESITE_BRICK_PILLAR.get())
                .add(ModBlocks.GRANITE_BRICK_PILLAR.get())
                .add(ModBlocks.DIORITE_BRICK_PILLAR.get())
                .add(ModBlocks.CALCITE_BRICK_PILLAR.get())
                .add(ModBlocks.DRIPSTONE_BRICK_PILLAR.get())
                .add(ModBlocks.STONE_BRICK_PILLAR.get())
                .add(ModBlocks.COPPER_CHAIN.get())
                .add(ModBlocks.EXPOSED_COPPER_CHAIN.get())
                .add(ModBlocks.WEATHERED_COPPER_CHAIN.get())
                .add(ModBlocks.OXIDIZED_COPPER_CHAIN.get())
                .add(ModBlocks.WAXED_COPPER_CHAIN.get())
                .add(ModBlocks.WAXED_EXPOSED_COPPER_CHAIN.get())
                .add(ModBlocks.WAXED_WEATHERED_COPPER_CHAIN.get())
                .add(ModBlocks.WAXED_OXIDIZED_COPPER_CHAIN.get())
                .add(ModBlocks.GOLDEN_CHAIN.get())
                .add(ModBlocks.ROSE_GOLDEN_CHAIN.get())
                .add(ModBlocks.GOLDEN_SPAWNER.get())
                .add(ModBlocks.STONE_BRICK_TILES.get())
                .add(ModBlocks.CUT_POLISHED_BLACKSTONE.get())
                .add(ModBlocks.SANDSTONE_TILES.get())
                .add(ModBlocks.SANDSTONE_BRICK_PILLAR.get())
                .add(ModBlocks.RED_SANDSTONE_TILES.get())
                .add(ModBlocks.RED_SANDSTONE_BRICK_PILLAR.get())
                .add(ModBlocks.PHANTASMIC_ENDSTONE.get())
                .add(ModBlocks.PHANTASMIC_NYLIUM.get())
                .add(ModBlocks.HYBERNATIC_NYLIUM.get())
                .add(ModBlocks.HYBERNATIC_CRYSTAL_BLOCK.get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.COPPER_BAMBOO_SAPLING.get())
                .add(ModBlocks.COPPER_BAMBOO_STALK.get())
                .add(ModBlocks.IRON_BAMBOO_SAPLING.get())
                .add(ModBlocks.IRON_BAMBOO_STALK.get())
                .add(ModBlocks.GOLDEN_BAMBOO_SAPLING.get())
                .add(ModBlocks.GOLDEN_BAMBOO_STALK.get())
                .add(ModBlocks.ANCIENT_BAMBOO_SAPLING.get())
                .add(ModBlocks.ANCIENT_BAMBOO_STALK.get())
                .add(ModBlocks.PYROLIZED_BAMBOO_SAPLING.get())
                .add(ModBlocks.PYROLIZED_BAMBOO_STALK.get());

        tag(BlockTags.NETHER_CARVER_REPLACEABLES)
                .add(ModBlocks.SOUL_SNOW_BLOCK.get())
                .add(ModBlocks.POWDER_SOUL_SNOW.get())
                .add(ModBlocks.PYROLIZED_NETHERRACK.get())
                .add(ModBlocks.ANCIENT_NYLIUM.get())
                .add(ModBlocks.GOLDEN_NYLIUM.get())
                .add(ModBlocks.IRON_NYLIUM.get())
                .add(ModBlocks.COPPER_NYLIUM.get())
                .add(ModBlocks.BLAZING_NYLIUM.get())
                .add(ModBlocks.FROZEN_NETHERRACK.get());

        tag(BlockTags.MINEABLE_WITH_HOE)
                .add(ModBlocks.AQUANDA_MOSS_BLOCK.get())
                .add(ModBlocks.AQUANDA_MOSS_CARPET.get())
                .add(ModBlocks.GLOWMOSS_BLOCK.get())
                .add(ModBlocks.GLOWMOSS_CARPET.get())
                .add(ModBlocks.HANGING_GLOWMOSS.get())
                .add(ModBlocks.HANGING_GLOWMOSS_PLANT.get())
                .add(ModBlocks.HYBERNATUS_LEAVES.get());

        tag(BlockTags.SOUL_SPEED_BLOCKS)
                .add(ModBlocks.SOUL_SNOW_BLOCK.get())
                .add(ModBlocks.POWDER_SOUL_SNOW.get());

        tag(BlockTags.SOUL_FIRE_BASE_BLOCKS)
                .add(ModBlocks.SOUL_SNOW_BLOCK.get())
                .add(ModBlocks.POWDER_SOUL_SNOW.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.POWDER_SOUL_SNOW.get())
                .add(ModBlocks.SOUL_SNOW_BLOCK.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.COPPER_NYLIUM.get())
                .add(ModBlocks.IRON_NYLIUM.get())
                .add(ModBlocks.PALLADIUM_NYLIUM.get())
                .add(ModBlocks.ZINC_NYLIUM.get())
                .add(ModBlocks.TIN_NYLIUM.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.GOLDEN_NYLIUM.get())
                .add(ModBlocks.CUT_GOLD_BLOCK.get())
                .add(ModBlocks.CUT_GOLDEN_BRICKS.get())
                .add(ModBlocks.CINCINNASITE_NYLIUM.get())
                .add(ModBlocks.BRASS_NYLIUM.get())
                .add(ModBlocks.BRONZE_NYLIUM.get())
                .add(ModBlocks.STEEL_NYLIUM.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.ANCIENT_NYLIUM.get())
                .add(ModBlocks.ROSARITE_BLOCK.get());

        tag(BlockTags.FENCES)
                .add(ModBlocks.EBONY_FENCE.get())
                .add(ModBlocks.AZALEA_FENCE.get())
                .add(ModBlocks.AQUANDA_FENCE.get())
                .add(ModBlocks.CRACKED_NETHER_BRICK_FENCE.get())
                .add(ModBlocks.BLAZE_BRICK_FENCE.get())
                .add(ModBlocks.PYROLIZED_NETHER_BRICK_FENCE.get())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_FENCE.get())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICK_FENCE.get())
                .add(ModBlocks.RED_NETHER_BRICK_FENCE.get())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICK_FENCE.get())
                .add(ModBlocks.RED_BLAZE_BRICK_FENCE.get())
                .add(ModBlocks.WARPED_NETHER_BRICK_FENCE.get())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICK_FENCE.get())
                .add(ModBlocks.WARPED_BLAZE_BRICK_FENCE.get());

        tag(BlockTags.WALLS)
                .add(ModBlocks.CRACKED_NETHER_BRICK_WALL.get())
                .add(ModBlocks.BLAZE_BRICK_WALL.get())
                .add(ModBlocks.PYROLIZED_NETHER_BRICK_WALL.get())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_WALL.get())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICK_WALL.get())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICK_WALL.get())
                .add(ModBlocks.RED_BLAZE_BRICK_WALL.get())
                .add(ModBlocks.WARPED_NETHER_BRICK_WALL.get())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICK_WALL.get())
                .add(ModBlocks.WARPED_BLAZE_BRICK_WALL.get());

        tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.EBONY_FENCE_GATE.get())
                .add(ModBlocks.AZALEA_FENCE_GATE.get())
                .add(ModBlocks.AQUANDA_FENCE_GATE.get());

        tag(ModBlockTags.CREATES_DOWNWARDS_BUBBLE_COLUMNS)
                .add(Blocks.MAGMA_BLOCK)
                .add(ModBlocks.BLAZING_MAGMA_BLOCK.get())
                .add(ModBlocks.FREEZING_MAGMA_BLOCK.get());

        tag(ModBlockTags.CREATES_UPWARDS_BUBBLE_COLUMNS)
                .add(Blocks.SOUL_SAND)
                .add(ModBlocks.POWDER_SOUL_SNOW.get());

        tag(ModBlockTags.INCORRECT_FOR_EMERALD_TOOL)
                .addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL);

        tag(ModBlockTags.INCORRECT_FOR_COPPER_TOOL)
                .addTag(BlockTags.NEEDS_IRON_TOOL)
                .addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL);

        tag(ModBlockTags.INCORRECT_FOR_ROSE_GOLDEN_TOOL)
                .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL);

            tag(ModBlockTags.INCORRECT_FOR_BRASS_TOOL)
                    .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL);

            tag(ModBlockTags.INCORRECT_FOR_ZINC_TOOL)
                    .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL);

            tag(BlockTags.NYLIUM)
                    .add(ModBlocks.ZINC_NYLIUM.get())
                    .add(ModBlocks.BRASS_NYLIUM.get())
                    .add(ModBlocks.TIN_NYLIUM.get());

        tag(ModBlockTags.AQUANDA_STEMS)
                .add(ModBlocks.AQUANDA_STEM.get())
                .add(ModBlocks.STRIPPED_AQUANDA_STEM.get())
                .add(ModBlocks.AQUANDA_HYPHAE.get())
                .add(ModBlocks.STRIPPED_AQUANDA_HYPHAE.get());

        tag(ModBlockTags.EBONY_STEMS)
                .add(ModBlocks.EBONY_STEM.get())
                .add(ModBlocks.STRIPPED_EBONY_STEM.get())
                .add(ModBlocks.EBONY_HYPHAE.get())
                .add(ModBlocks.STRIPPED_EBONY_HYPHAE.get());

        tag(ModBlockTags.AZALEA_LOGS)
                .add(ModBlocks.AZALEA_LOG.get())
                .add(ModBlocks.STRIPPED_AZALEA_LOG.get())
                .add(ModBlocks.AZALEA_WOOD.get())
                .add(ModBlocks.STRIPPED_AZALEA_WOOD.get());

        tag(ModBlockTags.HYBERNATUS_LOGS)
                .add(ModBlocks.HYBERNATUS_LOG.get())
                .add(ModBlocks.STRIPPED_HYBERNATUS_LOG.get())
                .add(ModBlocks.HYBERNATUS_WOOD.get())
                .add(ModBlocks.STRIPPED_HYBERNATUS_WOOD.get());

        tag(BlockTags.LOGS)
                .addTag(ModBlockTags.AQUANDA_STEMS)
                .addTag(ModBlockTags.EBONY_STEMS)
                .addTag(ModBlockTags.AZALEA_LOGS)
                .addTag(ModBlockTags.HYBERNATUS_LOGS);

        tag(ModBlockTags.MOSS_CAN_GENERATE_UNDER)
                .addTag(BlockTags.AIR)
                .add(Blocks.WATER)
                .addTag(BlockTags.ALL_SIGNS)
                .addTag(BlockTags.REPLACEABLE_BY_TREES)
                .addTag(BlockTags.REPLACEABLE)
                .addTag(BlockTags.FLOWERS);

        tag(BlockTags.CLIMBABLE)
                .add(ModBlocks.AQUANDA_VINES.get())
                .add(ModBlocks.AQUANDA_VINES_PLANT.get())
                .add(ModBlocks.BLAZING_VINES.get())
                .add(ModBlocks.BLAZING_VINES_PLANT.get())
                .add(ModBlocks.HANGING_GLOWMOSS.get())
                .add(ModBlocks.HANGING_GLOWMOSS_PLANT.get())
                .addTag(ModBlockTags.SCAFFOLDING);

        tag(ModBlockTags.SCAFFOLDING)
                .add(Blocks.SCAFFOLDING)

                .add(ModBlocks.PYROLIZED_SCAFFOLDING.get())
                .add(ModBlocks.COPPER_SCAFFOLDING.get())
                .add(ModBlocks.IRON_SCAFFOLDING.get())
                .add(ModBlocks.GOLDEN_SCAFFOLDING.get())
                .add(ModBlocks.ANCIENT_SCAFFOLDING.get())

                .add(ModBlocks.ZINC_SCAFFOLDING.get())
                .add(ModBlocks.TIN_SCAFFOLDING.get())
                .add(ModBlocks.PALLADIUM_SCAFFOLDING.get())
                .add(ModBlocks.CINCINNASITE_SCAFFOLDING.get());

        tag(ModBlockTags.KELP_LIKE)
                .add(Blocks.KELP)
                .add(ModBlocks.AQUANDA_KELP.get());

        tag(ModBlockTags.KELP_PLANT_LIKE)
                .add(Blocks.KELP_PLANT)
                .add(ModBlocks.AQUANDA_KELP_PLANT.get());

        tag(ModBlockTags.CAN_BE_ALTERED_GROUND)
                .addTag(BlockTags.SCULK_REPLACEABLE);

        tag(BlockTags.MOSS_REPLACEABLE)
                .add(ModBlocks.AQUANDA_MOSS_BLOCK.get());

        tag(BlockTags.DIRT)
                .add(ModBlocks.AQUANDA_MOSS_BLOCK.get());

        tag(ModBlockTags.GOLDEN_BAMBOO_PLANTABLE_ON)
                .addTag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(ModBlocks.GOLDEN_BAMBOO_SAPLING.get())
                .add(ModBlocks.GOLDEN_NYLIUM.get())
                .add(ModBlocks.GOLDEN_BAMBOO_STALK.get());

        tag(ModBlockTags.COPPER_BAMBOO_PLANTABLE_ON)
                .addTag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(ModBlocks.COPPER_BAMBOO_SAPLING.get())
                .add(ModBlocks.COPPER_NYLIUM.get())
                .add(ModBlocks.COPPER_BAMBOO_STALK.get());

        tag(ModBlockTags.IRON_BAMBOO_PLANTABLE_ON)
                .addTag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(ModBlocks.IRON_BAMBOO_SAPLING.get())
                .add(ModBlocks.IRON_NYLIUM.get())
                .add(ModBlocks.IRON_BAMBOO_STALK.get());

        tag(ModBlockTags.ANCIENT_BAMBOO_PLANTABLE_ON)
                .addTag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(ModBlocks.ANCIENT_BAMBOO_SAPLING.get())
                .add(ModBlocks.ANCIENT_NYLIUM.get())
                .add(ModBlocks.ANCIENT_BAMBOO_STALK.get());

        tag(ModBlockTags.PYROLIZED_BAMBOO_PLANTABLE_ON)
                .addTag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(ModBlocks.PYROLIZED_BAMBOO_SAPLING.get())
                .addTag(BlockTags.NYLIUM)
                .add(ModBlocks.PYROLIZED_BAMBOO_STALK.get());

        tag(BlockTags.NYLIUM)
                .add(ModBlocks.ANCIENT_NYLIUM.get())
                .add(ModBlocks.COPPER_NYLIUM.get())
                .add(ModBlocks.GOLDEN_NYLIUM.get())
                .add(ModBlocks.IRON_NYLIUM.get())
                .add(ModBlocks.BLAZING_NYLIUM.get())
                .add(ModBlocks.ZINC_NYLIUM.get())
                .add(ModBlocks.TIN_NYLIUM.get())
                .add(ModBlocks.STEEL_NYLIUM.get())
                .add(ModBlocks.ROSE_GOLDEN_NYLIUM.get())
                .add(ModBlocks.BRASS_NYLIUM.get())
                .add(ModBlocks.BRONZE_NYLIUM.get())
                .add(ModBlocks.CINCINNASITE_NYLIUM.get())
                .add(ModBlocks.PALLADIUM_NYLIUM.get());

        tag(ModBlockTags.ZINC_BAMBOO_PLANTABLE_ON)
                .addTag(BlockTags.NYLIUM)
                .addTag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(ModBlocks.ZINC_BAMBOO_SAPLING.get())
                .add(ModBlocks.ZINC_NYLIUM.get())
                .add(ModBlocks.ZINC_BAMBOO_STALK.get());

        tag(ModBlockTags.ZINC_BAMBOO_GROWABLE_ON)
                .add(ModBlocks.ZINC_BAMBOO_SAPLING.get())
                .add(ModBlocks.ZINC_NYLIUM.get())
                .add(ModBlocks.ZINC_BAMBOO_STALK.get());

        tag(ModBlockTags.ROSE_GOLDEN_BAMBOO_PLANTABLE_ON)
                .addTag(BlockTags.NYLIUM)
                .addTag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(ModBlocks.ROSE_GOLDEN_BAMBOO_SAPLING.get())
                .add(ModBlocks.ROSE_GOLDEN_NYLIUM.get())
                .add(ModBlocks.ROSE_GOLDEN_BAMBOO_STALK.get());

        tag(ModBlockTags.ROSE_GOLDEN_BAMBOO_GROWABLE_ON)
                .add(ModBlocks.ROSE_GOLDEN_BAMBOO_SAPLING.get())
                .add(ModBlocks.ROSE_GOLDEN_NYLIUM.get())
                .add(ModBlocks.ROSE_GOLDEN_BAMBOO_STALK.get());

        tag(ModBlockTags.BRASS_BAMBOO_PLANTABLE_ON)
                .addTag(BlockTags.NYLIUM)
                .addTag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(ModBlocks.BRASS_BAMBOO_SAPLING.get())
                .add(ModBlocks.BRASS_NYLIUM.get())
                .add(ModBlocks.BRASS_BAMBOO_STALK.get());

        tag(ModBlockTags.BRASS_BAMBOO_GROWABLE_ON)
                .add(ModBlocks.BRASS_BAMBOO_SAPLING.get())
                .add(ModBlocks.BRASS_NYLIUM.get())
                .add(ModBlocks.BRASS_BAMBOO_STALK.get());

        tag(ModBlockTags.TIN_BAMBOO_PLANTABLE_ON)
                .addTag(BlockTags.NYLIUM)
                .addTag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(ModBlocks.TIN_BAMBOO_SAPLING.get())
                .add(ModBlocks.TIN_NYLIUM.get())
                .add(ModBlocks.TIN_BAMBOO_STALK.get());

        tag(ModBlockTags.TIN_BAMBOO_GROWABLE_ON)
                .add(ModBlocks.TIN_BAMBOO_SAPLING.get())
                .add(ModBlocks.TIN_NYLIUM.get())
                .add(ModBlocks.TIN_BAMBOO_STALK.get());

        tag(ModBlockTags.STEEL_BAMBOO_PLANTABLE_ON)
                .addTag(BlockTags.NYLIUM)
                .addTag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(ModBlocks.STEEL_BAMBOO_SAPLING.get())
                .add(ModBlocks.STEEL_NYLIUM.get())
                .add(ModBlocks.STEEL_BAMBOO_STALK.get());

        tag(ModBlockTags.STEEL_BAMBOO_GROWABLE_ON)
                .add(ModBlocks.STEEL_BAMBOO_SAPLING.get())
                .add(ModBlocks.STEEL_NYLIUM.get())
                .add(ModBlocks.STEEL_BAMBOO_STALK.get());

        tag(ModBlockTags.BRONZE_BAMBOO_PLANTABLE_ON)
                .addTag(BlockTags.NYLIUM)
                .addTag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(ModBlocks.BRONZE_BAMBOO_SAPLING.get())
                .add(ModBlocks.BRONZE_NYLIUM.get())
                .add(ModBlocks.BRONZE_BAMBOO_STALK.get());

        tag(ModBlockTags.BRONZE_BAMBOO_GROWABLE_ON)
                .add(ModBlocks.BRONZE_BAMBOO_SAPLING.get())
                .add(ModBlocks.BRONZE_NYLIUM.get())
                .add(ModBlocks.BRONZE_BAMBOO_STALK.get());

        tag(ModBlockTags.PALLADIUM_BAMBOO_PLANTABLE_ON)
                .addTag(BlockTags.NYLIUM)
                .addTag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(ModBlocks.PALLADIUM_BAMBOO_SAPLING.get())
                .add(ModBlocks.PALLADIUM_NYLIUM.get())
                .add(ModBlocks.PALLADIUM_BAMBOO_STALK.get());

        tag(ModBlockTags.PALLADIUM_BAMBOO_GROWABLE_ON)
                .add(ModBlocks.PALLADIUM_BAMBOO_SAPLING.get())
                .add(ModBlocks.PALLADIUM_NYLIUM.get())
                .add(ModBlocks.PALLADIUM_BAMBOO_STALK.get());

        tag(ModBlockTags.CINCINNASITE_BAMBOO_PLANTABLE_ON)
                .addTag(BlockTags.NYLIUM)
                .addTag(BlockTags.BAMBOO_PLANTABLE_ON)
                .add(ModBlocks.CINCINNASITE_BAMBOO_SAPLING.get())
                .add(ModBlocks.CINCINNASITE_NYLIUM.get())
                .add(ModBlocks.CINCINNASITE_BAMBOO_STALK.get());

        tag(ModBlockTags.CINCINNASITE_BAMBOO_GROWABLE_ON)
                .add(ModBlocks.CINCINNASITE_BAMBOO_SAPLING.get())
                .add(ModBlocks.CINCINNASITE_NYLIUM.get())
                .add(ModBlocks.CINCINNASITE_BAMBOO_STALK.get());

        ModBlocks.TERRACOTTA_BRICKS.values().forEach(block ->
                tag(BlockTags.MINEABLE_WITH_PICKAXE)
                        .add(block.get())
        );
        ModBlocks.TERRACOTTA_COBBLED.values().forEach(block ->
                tag(BlockTags.MINEABLE_WITH_PICKAXE)
                        .add(block.get())
        );
        tag(BlockTags.GUARDED_BY_PIGLINS)
                .add(ModBlocks.GOLDEN_SPAWNER.get())
                .add(ModBlocks.GOLDEN_CHAIN.get())
                .add(ModBlocks.GOLDEN_NYLIUM.get())
                .add(ModBlocks.GOLDEN_BAMBOO_STALK.get())
                .add(ModBlocks.GOLDEN_BAMBOO_SAPLING.get())
                .add(ModBlocks.CHISELED_ROSE_GOLD.get())
                .add(ModBlocks.ROSE_GOLDEN_CHAIN.get())
                .add(ModBlocks.ROSE_GOLDEN_NYLIUM.get())
                .add(ModBlocks.ROSE_GOLD_TRAPDOOR.get())
                .add(ModBlocks.ROSE_GOLD_BLOCK.get())
                .add(ModBlocks.ROSE_GOLD_DOOR.get())
                .add(ModBlocks.ROSE_GOLD_GRATE.get())
                .add(ModBlocks.ROSE_GOLD_PILLAR.get())
                .add(ModBlocks.ROSE_GOLDEN_BAMBOO_STALK.get())
                .add(ModBlocks.ROSE_GOLDEN_BAMBOO_SAPLING.get());

        ModBlocks.SOUL_LANTERNS.forEach((color, block) -> {

            // Always true
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block.get());
            tag(BlockTags.PIGLIN_REPELLENTS).add(block.get());

            // Only gold or rose_gold get piglin guarded
            if (color.equals("golden") || color.equals("rose_golden")) {
                tag(BlockTags.GUARDED_BY_PIGLINS).add(block.get());
            }
        });
        ModBlocks.LANTERNS.forEach((color, block) -> {

            // Always mineable
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block.get());

            // Only gold or rose_gold get piglin guarded
            if (color.equals("golden") || color.equals("rose_golden")) {
                tag(BlockTags.GUARDED_BY_PIGLINS).add(block.get());
            }
        });
    }
}
