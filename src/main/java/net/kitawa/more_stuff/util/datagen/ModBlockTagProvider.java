package net.kitawa.more_stuff.util.datagen;

import net.kitawa.more_stuff.compat.create.blocks.CreateCompatBlocks;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.ModList;
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
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.COPPER_NYLIUM.get())
                .add(ModBlocks.IRON_NYLIUM.get())
                .add(ModBlocks.GOLDEN_NYLIUM.get())
                .add(ModBlocks.ANCIENT_NYLIUM.get())
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
                .add(ModBlocks.NETHER_BRICK_PILLAR.getKey())
                .add(ModBlocks.CRACKED_NETHER_BRICK_PILLAR.getKey())
                .add(ModBlocks.CRACKED_NETHER_BRICK_SLAB.getKey())
                .add(ModBlocks.CRACKED_NETHER_BRICK_STAIRS.getKey())
                .add(ModBlocks.CRACKED_NETHER_BRICK_WALL.getKey())
                .add(ModBlocks.CRACKED_NETHER_BRICK_FENCE.getKey())
                .add(ModBlocks.BLAZE_BRICKS.getKey())
                .add(ModBlocks.BLAZE_BRICK_PILLAR.getKey())
                .add(ModBlocks.BLAZE_BRICK_SLAB.getKey())
                .add(ModBlocks.BLAZE_BRICK_STAIRS.getKey())
                .add(ModBlocks.BLAZE_BRICK_FENCE.getKey())
                .add(ModBlocks.BLAZE_BRICK_WALL.getKey())
                .add(ModBlocks.PYROLIZED_NETHER_BRICKS.getKey())
                .add(ModBlocks.PYROLIZED_NETHER_BRICK_PILLAR.getKey())
                .add(ModBlocks.PYROLIZED_NETHER_BRICK_SLAB.getKey())
                .add(ModBlocks.PYROLIZED_NETHER_BRICK_STAIRS.getKey())
                .add(ModBlocks.PYROLIZED_NETHER_BRICK_WALL.getKey())
                .add(ModBlocks.PYROLIZED_NETHER_BRICK_FENCE.getKey())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICKS.getKey())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_PILLAR.getKey())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_SLAB.getKey())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_STAIRS.getKey())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_WALL.getKey())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_FENCE.getKey())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICKS.getKey())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICK_PILLAR.getKey())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICK_SLAB.getKey())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICK_STAIRS.getKey())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICK_FENCE.getKey())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICK_WALL.getKey())
                .add(ModBlocks.RED_NETHER_BRICK_PILLAR.getKey())
                .add(ModBlocks.RED_NETHER_BRICK_FENCE.getKey())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICKS.getKey())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICK_PILLAR.getKey())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICK_SLAB.getKey())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICK_STAIRS.getKey())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICK_WALL.getKey())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICK_FENCE.getKey())
                .add(ModBlocks.RED_BLAZE_BRICKS.getKey())
                .add(ModBlocks.RED_BLAZE_BRICK_PILLAR.getKey())
                .add(ModBlocks.RED_BLAZE_BRICK_SLAB.getKey())
                .add(ModBlocks.RED_BLAZE_BRICK_STAIRS.getKey())
                .add(ModBlocks.RED_BLAZE_BRICK_FENCE.getKey())
                .add(ModBlocks.RED_BLAZE_BRICK_WALL.getKey())
                .add(ModBlocks.WARPED_NETHER_BRICKS.getKey())
                .add(ModBlocks.WARPED_NETHER_BRICK_PILLAR.getKey())
                .add(ModBlocks.WARPED_NETHER_BRICK_SLAB.getKey())
                .add(ModBlocks.WARPED_NETHER_BRICK_STAIRS.getKey())
                .add(ModBlocks.WARPED_NETHER_BRICK_WALL.getKey())
                .add(ModBlocks.WARPED_NETHER_BRICK_FENCE.getKey())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICKS.getKey())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICK_PILLAR.getKey())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICK_SLAB.getKey())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICK_STAIRS.getKey())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICK_WALL.getKey())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICK_FENCE.getKey())
                .add(ModBlocks.WARPED_BLAZE_BRICKS.getKey())
                .add(ModBlocks.WARPED_BLAZE_BRICK_PILLAR.getKey())
                .add(ModBlocks.WARPED_BLAZE_BRICK_SLAB.getKey())
                .add(ModBlocks.WARPED_BLAZE_BRICK_STAIRS.getKey())
                .add(ModBlocks.WARPED_BLAZE_BRICK_FENCE.getKey())
                .add(ModBlocks.WARPED_BLAZE_BRICK_WALL.getKey());

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
                .add(ModBlocks.POWDER_SOUL_SNOW.get());

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
                .add(ModBlocks.IRON_NYLIUM.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.GOLDEN_NYLIUM.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.ANCIENT_NYLIUM.get())
                .add(ModBlocks.ROSARITE_BLOCK.get());

        tag(BlockTags.FENCES)
                .add(ModBlocks.EBONY_FENCE.get())
                .add(ModBlocks.AZALEA_FENCE.get())
                .add(ModBlocks.AQUANDA_FENCE.get())
                .add(ModBlocks.CRACKED_NETHER_BRICK_FENCE.getKey())
                .add(ModBlocks.BLAZE_BRICK_FENCE.getKey())
                .add(ModBlocks.PYROLIZED_NETHER_BRICK_FENCE.getKey())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_FENCE.getKey())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICK_FENCE.getKey())
                .add(ModBlocks.RED_NETHER_BRICK_FENCE.getKey())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICK_FENCE.getKey())
                .add(ModBlocks.RED_BLAZE_BRICK_FENCE.getKey())
                .add(ModBlocks.WARPED_NETHER_BRICK_FENCE.getKey())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICK_FENCE.getKey())
                .add(ModBlocks.WARPED_BLAZE_BRICK_FENCE.getKey());

        tag(BlockTags.WALLS)
                .add(ModBlocks.CRACKED_NETHER_BRICK_WALL.getKey())
                .add(ModBlocks.BLAZE_BRICK_WALL.getKey())
                .add(ModBlocks.PYROLIZED_NETHER_BRICK_WALL.getKey())
                .add(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_WALL.getKey())
                .add(ModBlocks.PYROLIZED_BLAZE_BRICK_WALL.getKey())
                .add(ModBlocks.CRACKED_RED_NETHER_BRICK_WALL.getKey())
                .add(ModBlocks.RED_BLAZE_BRICK_WALL.getKey())
                .add(ModBlocks.WARPED_NETHER_BRICK_WALL.getKey())
                .add(ModBlocks.CRACKED_WARPED_NETHER_BRICK_WALL.getKey())
                .add(ModBlocks.WARPED_BLAZE_BRICK_WALL.getKey());

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
                .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL);

        tag(ModBlockTags.INCORRECT_FOR_ROSE_GOLDEN_TOOL)
                .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL);

        if (ModList.get().isLoaded("create")) {
            tag(ModBlockTags.INCORRECT_FOR_BRASS_TOOL)
                    .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL);

            tag(ModBlockTags.INCORRECT_FOR_ZINC_TOOL)
                    .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL);

            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(CreateCompatBlocks.PYROLIZED_ZINC_ORE.get())
                    .add(CreateCompatBlocks.NETHER_ZINC_ORE.get())
                    .add(CreateCompatBlocks.FROZEN_ZINC_ORE.get());
        }

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

        tag(BlockTags.LOGS)
                .addTag(ModBlockTags.AQUANDA_STEMS)
                .addTag(ModBlockTags.EBONY_STEMS)
                .addTag(ModBlockTags.AZALEA_LOGS);

        tag(BlockTags.LEAVES)
                .add(ModBlocks.AQUANDA_GEL.get())
                .add(ModBlocks.GLOWING_AQUANDA_GEL.get());

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
                .add(ModBlocks.BLAZING_VINES_PLANT.get());

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
                .add(ModBlocks.BLAZING_NYLIUM.get());
    }
}
