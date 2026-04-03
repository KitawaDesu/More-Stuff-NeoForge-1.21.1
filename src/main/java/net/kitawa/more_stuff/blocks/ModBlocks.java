package net.kitawa.more_stuff.blocks;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.blocks.custom.end.hybernatus.*;
import net.kitawa.more_stuff.blocks.custom.end.phantasmic.*;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.betternether.CincinnasiteBambooSaplingBlock;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.betternether.CincinnasiteBambooStalkBlock;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.betternether.scaffolding.CincinnasiteScaffolding;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.create.scaffolding.BrassScaffolding;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.create.scaffolding.ZincScaffolding;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.create_ironworks.scaffolding.BronzeScaffolding;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.create_ironworks.scaffolding.SteelScaffolding;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.create_ironworks.scaffolding.TinScaffolding;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.gallosphere.PalladiumBambooSaplingBlock;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.gallosphere.PalladiumBambooStalkBlock;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.gallosphere.scaffolding.PalladiumScaffolding;
import net.kitawa.more_stuff.blocks.custom.overworld.aquanda_biome_blocks.*;
import net.kitawa.more_stuff.blocks.custom.overworld.electricity.OmniBlock;
import net.kitawa.more_stuff.blocks.custom.overworld.electricity.TeslaCoilBlock;
import net.kitawa.more_stuff.blocks.custom.overworld.frostbitten_caverns.IceSheetBlock;
import net.kitawa.more_stuff.blocks.custom.nether.frozen_valley_biome_blocks.FreezingMagmaBlock;
import net.kitawa.more_stuff.blocks.custom.nether.frozen_valley_biome_blocks.PowderSoulSnowBlock;
import net.kitawa.more_stuff.blocks.custom.general.*;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.*;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.create.BrassBambooSaplingBlock;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.create.BrassBambooStalkBlock;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.create_ironworks.*;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.scaffolding.*;
import net.kitawa.more_stuff.blocks.custom.nether.pyrolized_and_blazing_blocks.*;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.create.ZincBambooSaplingBlock;
import net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks.create.ZincBambooStalkBlock;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.worldgen.ModConfiguredFeatures;
import net.kitawa.more_stuff.worldgen.tree.ModTreeGrowers;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MoreStuff.MOD_ID);

    public static final DeferredBlock<Block> STRIPPED_AZALEA_LOG = registerBlock("stripped_azalea_log",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GRASS)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()
            )
    );

    public static final DeferredBlock<Block> AZALEA_LOG = registerBlock("azalea_log",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WOOD)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()
            ) {
                @Nullable
                @Override
                public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
                    if (itemAbility == ItemAbilities.AXE_STRIP) {
                        return STRIPPED_AZALEA_LOG.get().defaultBlockState()
                                .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
                    }
                    return super.getToolModifiedState(state, context, itemAbility, simulate);
                }
            }
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_AZALEA_WOOD = registerBlock("stripped_azalea_wood",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GRASS)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> AZALEA_WOOD = registerBlock("azalea_wood",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WOOD)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()
            ) {
                @Nullable
                @Override
                public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
                    if (itemAbility == ItemAbilities.AXE_STRIP) {
                        return STRIPPED_AZALEA_WOOD.get().defaultBlockState()
                                .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
                    }
                    return super.getToolModifiedState(state, context, itemAbility, simulate);
                }
            }
    );

    public static final DeferredBlock<Block> AZALEA_PLANKS = registerBlock("azalea_planks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GRASS)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()
            )
    );

    public static final DeferredBlock<StairBlock> AZALEA_STAIRS = registerBlock("azalea_stairs",
            () -> new StairBlock(ModBlocks.AZALEA_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GRASS)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()
            )
    );

    public static final DeferredBlock<SlabBlock> AZALEA_SLAB = registerBlock("azalea_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GRASS)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
            )
    );

    public static final DeferredBlock<PressurePlateBlock> AZALEA_PRESSURE_PLATE = registerBlock("azalea_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GRASS)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noCollission()
                    .ignitedByLava()
            )
    );

    public static final DeferredBlock<ButtonBlock> AZALEA_BUTTON = registerBlock("azalea_button",
            () -> new ButtonBlock(BlockSetType.OAK, 20, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GRASS)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noCollission()
                    .ignitedByLava()
            )
    );

    public static final DeferredBlock<FenceBlock> AZALEA_FENCE = registerBlock("azalea_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GRASS)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
            )
    );

    public static final DeferredBlock<FenceGateBlock> AZALEA_FENCE_GATE = registerBlock("azalea_fence_gate",
            () -> new FenceGateBlock(WoodType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GRASS)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
            )
    );

    public static final DeferredBlock<DoorBlock> AZALEA_DOOR = registerBlock("azalea_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GRASS)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .ignitedByLava()
            )
    );

    public static final DeferredBlock<TrapDoorBlock> AZALEA_TRAPDOOR = registerBlock("azalea_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GRASS)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
            )
    );

    public static final DeferredBlock<Block> STRIPPED_EBONY_STEM = registerBlock("stripped_ebony_stem",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DEEPSLATE)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<Block> EBONY_STEM = registerBlock("ebony_stem",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.FIRE)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.NETHER_WOOD)
            ) {
                @Nullable
                @Override
                public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
                    if (itemAbility == ItemAbilities.AXE_STRIP) {
                        return STRIPPED_EBONY_STEM.get().defaultBlockState()
                                .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
                    }
                    return super.getToolModifiedState(state, context, itemAbility, simulate);
                }
            }
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_EBONY_HYPHAE = registerBlock("stripped_ebony_hyphae",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DEEPSLATE)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> EBONY_HYPHAE = registerBlock("ebony_hyphae",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.FIRE)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.NETHER_WOOD)
            ) {
                @Nullable
                @Override
                public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
                    if (itemAbility == ItemAbilities.AXE_STRIP) {
                        return STRIPPED_EBONY_HYPHAE.get().defaultBlockState()
                                .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
                    }
                    return super.getToolModifiedState(state, context, itemAbility, simulate);
                }
            }
    );

    public static final DeferredBlock<Block> EBONY_PLANKS = registerBlock("ebony_planks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DEEPSLATE)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<StairBlock> EBONY_STAIRS = registerBlock("ebony_stairs",
            () -> new StairBlock(ModBlocks.EBONY_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DEEPSLATE)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<SlabBlock> EBONY_SLAB = registerBlock("ebony_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<PressurePlateBlock> EBONY_PRESSURE_PLATE = registerBlock("ebony_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD)
                    .noCollission()
            )
    );

    public static final DeferredBlock<ButtonBlock> EBONY_BUTTON = registerBlock("ebony_button",
            () -> new ButtonBlock(BlockSetType.OAK, 20, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD)
                    .noCollission()
            )
    );

    public static final DeferredBlock<FenceBlock> EBONY_FENCE = registerBlock("ebony_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<FenceGateBlock> EBONY_FENCE_GATE = registerBlock("ebony_fence_gate",
            () -> new FenceGateBlock(WoodType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<DoorBlock> EBONY_DOOR = registerBlock("ebony_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD)
                    .noOcclusion()
            )
    );

    public static final DeferredBlock<TrapDoorBlock> EBONY_TRAPDOOR = registerBlock("ebony_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<Block> STRIPPED_AQUANDA_STEM = registerBlock("stripped_aquanda_stem",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<Block> AQUANDA_STEM = registerBlock("aquanda_stem",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.NETHER_WOOD)
            ) {
                @Nullable
                @Override
                public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
                    if (itemAbility == ItemAbilities.AXE_STRIP) {
                        return STRIPPED_AQUANDA_STEM.get().defaultBlockState()
                                .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
                    }
                    return super.getToolModifiedState(state, context, itemAbility, simulate);
                }
            }
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_AQUANDA_HYPHAE = registerBlock("stripped_aquanda_hyphae",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> AQUANDA_HYPHAE = registerBlock("aquanda_hyphae",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.NETHER_WOOD)
            ) {
                @Nullable
                @Override
                public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
                    if (itemAbility == ItemAbilities.AXE_STRIP) {
                        return STRIPPED_AQUANDA_HYPHAE.get().defaultBlockState()
                                .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
                    }
                    return super.getToolModifiedState(state, context, itemAbility, simulate);
                }
            }
    );

    public static final DeferredBlock<Block> AQUANDA_PLANKS = registerBlock("aquanda_planks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<StairBlock> AQUANDA_STAIRS = registerBlock("aquanda_stairs",
            () -> new StairBlock(ModBlocks.AQUANDA_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<SlabBlock> AQUANDA_SLAB = registerBlock("aquanda_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<WaterloggablePressurePlate> AQUANDA_PRESSURE_PLATE = registerBlock("aquanda_pressure_plate",
            () -> new WaterloggablePressurePlate(BlockSetType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD)
                    .noCollission()
            )
    );

    public static final DeferredBlock<WaterloggableButton> AQUANDA_BUTTON = registerBlock("aquanda_button",
            () -> new WaterloggableButton(BlockSetType.OAK, 20, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD)
                    .noCollission()
            )
    );

    public static final DeferredBlock<FenceBlock> AQUANDA_FENCE = registerBlock("aquanda_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<WaterloggableFenceGate> AQUANDA_FENCE_GATE = registerBlock("aquanda_fence_gate",
            () -> new WaterloggableFenceGate(WoodType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<DoorBlock> AQUANDA_DOOR = registerBlock("aquanda_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD)
                    .noOcclusion()
            )
    );

    public static final DeferredBlock<TrapDoorBlock> AQUANDA_TRAPDOOR = registerBlock("aquanda_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD)
            )
    );

    public static final DeferredBlock<Block> ROSE_GOLD_BLOCK = registerBlock("rose_gold_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PINK)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .sound(SoundType.COPPER)
            )
    );

    public static final DeferredBlock<Block> CHISELED_ROSE_GOLD = registerBlock("chiseled_rose_gold",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PINK)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .sound(SoundType.COPPER)
            )
    );

    public static final DeferredBlock<Block> ROSE_GOLD_GRATE = registerBlock("rose_gold_grate",
            () -> new WaterloggedTransparentBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PINK)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .sound(SoundType.COPPER_GRATE)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> CUT_ROSE_GOLD = registerBlock("cut_rose_gold",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PINK)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .sound(SoundType.COPPER)
            )
    );

    public static final DeferredBlock<Block> CUT_ROSE_GOLD_STAIRS = registerBlock("cut_rose_gold_stairs",
            () -> new StairBlock(CUT_ROSE_GOLD.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PINK)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .sound(SoundType.COPPER)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> CUT_ROSE_GOLD_SLAB = registerBlock("cut_rose_gold_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PINK)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .sound(SoundType.COPPER)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> ROSE_GOLD_DOOR = registerBlock("rose_gold_door",
            () -> new DoorBlock(BlockSetType.COPPER,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PINK)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .sound(SoundType.COPPER)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> ROSE_GOLD_TRAPDOOR = registerBlock("rose_gold_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.COPPER,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PINK)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .sound(SoundType.COPPER)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> ROSE_GOLD_PILLAR = registerBlock("rose_gold_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PINK)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .sound(SoundType.COPPER)
            )
    );

    public static final DeferredBlock<Block> CUT_ROSE_GOLD_BRICKS = registerBlock("cut_rose_gold_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PINK)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .sound(SoundType.COPPER)
            )
    );

    public static final DeferredBlock<Block> ROSARITE_BLOCK = registerBlockNoItem("rosarite_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .requiresCorrectToolForDrops()
                            .strength(50.0F, 1200.0F)
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );

    public static final DeferredBlock<GelBlock> AQUANDA_GEL = registerBlock("aquanda_gel",
            () -> new GelBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .friction(0.8F)
                            .sound(SoundType.SLIME_BLOCK)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<GelBlock> GLOWING_AQUANDA_GEL = registerBlock("glowing_aquanda_gel",
            () -> new GelBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .friction(0.8F)
                            .sound(SoundType.SLIME_BLOCK)
                            .noOcclusion()
                            .lightLevel(p_50872_ -> 15)
            )
    );

    public static final DeferredBlock<WaterloggableCaveVinesBlock> AQUANDA_VINES = registerBlockNoItem("aquanda_vines",
            () -> new WaterloggableCaveVinesBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .friction(0.8F)
                            .sound(SoundType.CAVE_VINES)
                            .noOcclusion()
                            .lightLevel(CaveVines.emission(14))
                            .noCollission()
            )
    );

    public static final DeferredBlock<WaterloggableCaveVinesPlantBlock> AQUANDA_VINES_PLANT = registerBlockNoItem("aquanda_vines_plant",
            () -> new WaterloggableCaveVinesPlantBlock(ModBlocks.AQUANDA_VINES.get(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .friction(0.8F)
                            .sound(SoundType.CAVE_VINES)
                            .noOcclusion()
                            .lightLevel(CaveVines.emission(14))
                            .noCollission()
            )
    );

    public static final DeferredBlock<WaterloggableCarpetBlock> AQUANDA_MOSS_CARPET = registerBlock("aquanda_moss_carpet",
            () -> new WaterloggableCarpetBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .strength(0.1F)
                            .sound(SoundType.MOSS_CARPET)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<AquandaBlock> AQUANDA = registerBlock("aquanda",
            () -> new AquandaBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .friction(0.8F)
                            .sound(SoundType.AZALEA)
            )
    );

    public static final DeferredBlock<AquandaBlock> GLOWING_AQUANDA = registerBlock("glowing_aquanda",
            () -> new AquandaBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .friction(0.8F)
                            .sound(SoundType.AZALEA)
                            .lightLevel(p_50872_ -> 8)
            )
    );

    public static final DeferredBlock<ModdedMossBlock> AQUANDA_MOSS_BLOCK = registerBlock("aquanda_moss_block",
            () -> new ModdedMossBlock(ModConfiguredFeatures.AQUANDA_MOSS_PATCH_BONEMEAL, ModConfiguredFeatures.AQUANDA_MOSS_PATCH_BONEMEAL,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .strength(0.1F)
                            .sound(SoundType.MOSS)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<AquandaKelpBlock> AQUANDA_KELP = registerBlockNoItem("aquanda_kelp",
            () -> new AquandaKelpBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.WET_GRASS)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<AquandaKelpPlantBlock> AQUANDA_KELP_PLANT = registerBlockNoItem("aquanda_kelp_plant",
            () -> new AquandaKelpPlantBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .noCollission()
                            .instabreak()
                            .sound(SoundType.WET_GRASS)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<ModdedNyliumBlock> GOLDEN_NYLIUM = registerBlock("golden_nylium",
            () -> new ModdedNyliumBlock(NetherFeatures.CRIMSON_FOREST_VEGETATION, Blocks.NETHERRACK, BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GOLD)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(0.4F)
                            .sound(SoundType.NYLIUM)
                            .randomTicks()
            )
    );

    public static final DeferredBlock<ModdedNyliumBlock> COPPER_NYLIUM = registerBlock("copper_nylium",
            () -> new ModdedNyliumBlock(NetherFeatures.CRIMSON_FOREST_VEGETATION, Blocks.NETHERRACK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.NYLIUM)
                    .randomTicks()
            )
    );

    public static final DeferredBlock<ModdedNyliumBlock> IRON_NYLIUM = registerBlock("iron_nylium",
            () -> new ModdedNyliumBlock(NetherFeatures.CRIMSON_FOREST_VEGETATION, Blocks.NETHERRACK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.NYLIUM)
                    .randomTicks()
            )
    );

    public static final DeferredBlock<ModdedNyliumBlock> ANCIENT_NYLIUM = registerBlock("ancient_nylium",
            () -> new ModdedNyliumBlock(NetherFeatures.CRIMSON_FOREST_VEGETATION, Blocks.NETHERRACK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.NYLIUM)
                    .randomTicks()
            )
    );

    public static final DeferredBlock<GoldenBambooSaplingBlock> GOLDEN_BAMBOO_SAPLING = registerBlockNoItem("golden_bamboo_sapling",
            () -> new GoldenBambooSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO_SAPLING)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<GoldenBambooStalkBlock> GOLDEN_BAMBOO_STALK = registerBlockNoItem("golden_bamboo_stalk",
            () -> new GoldenBambooStalkBlock(ModBlocks.GOLDEN_BAMBOO_SAPLING.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO)
                    .noOcclusion()
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<CopperBambooSaplingBlock> COPPER_BAMBOO_SAPLING = registerBlockNoItem("copper_bamboo_sapling",
            () -> new CopperBambooSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO_SAPLING)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<CopperBambooStalkBlock> COPPER_BAMBOO_STALK = registerBlockNoItem("copper_bamboo_stalk",
            () -> new CopperBambooStalkBlock(ModBlocks.COPPER_BAMBOO_SAPLING.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO)
                    .noOcclusion()
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<IronBambooSaplingBlock> IRON_BAMBOO_SAPLING = registerBlockNoItem("iron_bamboo_sapling",
            () -> new IronBambooSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO_SAPLING)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<IronBambooStalkBlock> IRON_BAMBOO_STALK = registerBlockNoItem("iron_bamboo_stalk",
            () -> new IronBambooStalkBlock(ModBlocks.IRON_BAMBOO_SAPLING.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO)
                    .noOcclusion()
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<AncientBambooSaplingBlock> ANCIENT_BAMBOO_SAPLING = registerBlockNoItem("ancient_bamboo_sapling",
            () -> new AncientBambooSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO_SAPLING)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<AncientBambooStalkBlock> ANCIENT_BAMBOO_STALK = registerBlockNoItem("ancient_bamboo_stalk",
            () -> new AncientBambooStalkBlock(ModBlocks.ANCIENT_BAMBOO_SAPLING.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO)
                    .noOcclusion()
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<PyrolizedBambooSaplingBlock> PYROLIZED_BAMBOO_SAPLING = registerBlockNoItem("pyrolized_bamboo_sapling",
            () -> new PyrolizedBambooSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO_SAPLING)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<PyrolizedBambooStalkBlock> PYROLIZED_BAMBOO_STALK = registerBlockNoItem("pyrolized_bamboo_stalk",
            () -> new PyrolizedBambooStalkBlock(ModBlocks.PYROLIZED_BAMBOO_SAPLING.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO)
                    .noOcclusion()
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<PyrolizedNetherrack> PYROLIZED_NETHERRACK = registerBlock("pyrolized_netherrack",
            () -> new PyrolizedNetherrack(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.NETHERRACK)
            )
    );

    public static final DeferredBlock<ModdedNyliumBlock> BLAZING_NYLIUM = registerBlock("blazing_nylium",
            () -> new ModdedNyliumBlock(
                    ModConfiguredFeatures.BLAZING_FOREST_VEGETATION_BONEMEAL,
                    Optional.of(ModConfiguredFeatures.BLAZING_VINES_BONEMEAL),
                    ModBlocks.PYROLIZED_NETHERRACK.get(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.FIRE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(0.4F)
                            .sound(SoundType.NYLIUM)
                            .randomTicks()
            )
    );

    public static final DeferredBlock<DropExperienceBlock> PYROLIZED_GOLD_ORE = registerBlock("pyrolized_gold_ore",
            () -> new DropExperienceBlock(UniformInt.of(0, 1), BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 3.0F)
                            .sound(SoundType.NETHER_GOLD_ORE)
            )
    );

    public static final DeferredBlock<DropExperienceBlock> PYROLIZED_QUARTZ_ORE = registerBlock("pyrolized_quartz_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 5), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_ORE)
            )
    );

    public static final DeferredBlock<DropExperienceBlock> NETHER_COPPER_ORE = registerBlock("nether_copper_ore",
            () -> new DropExperienceBlock(UniformInt.of(0, 1), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NETHER)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_ORE)
            )
    );

    public static final DeferredBlock<DropExperienceBlock> PYROLIZED_COPPER_ORE = registerBlock("pyrolized_copper_ore",
            () -> new DropExperienceBlock(UniformInt.of(0, 1), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_ORE)
            )
    );

    public static final DeferredBlock<DropExperienceBlock> NETHER_IRON_ORE = registerBlock("nether_iron_ore",
            () -> new DropExperienceBlock(UniformInt.of(0, 1), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NETHER)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_ORE)
            )
    );

    public static final DeferredBlock<DropExperienceBlock> PYROLIZED_IRON_ORE = registerBlock("pyrolized_iron_ore",
            () -> new DropExperienceBlock(UniformInt.of(0, 1), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_ORE)
            )
    );

    public static final DeferredBlock<DropExperienceBlock> NETHER_EXPERIENCE_ORE = registerBlock("nether_experience_ore",
            () -> new DropExperienceBlock(UniformInt.of(4, 20), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NETHER)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_ORE)
            )
    );

    public static final DeferredBlock<DropExperienceBlock> PYROLIZED_EXPERIENCE_ORE = registerBlock("pyrolized_experience_ore",
            () -> new DropExperienceBlock(UniformInt.of(4, 20), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_ORE)
            )
    );

    public static final DeferredBlock<RootsBlock> BLAZING_ROOTS = registerBlock("blazing_roots",
            () -> new RootsBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.FIRE)
                            .replaceable()
                            .noCollission()
                            .instabreak()
                            .sound(SoundType.ROOTS)
                            .offsetType(BlockBehaviour.OffsetType.XZ)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<FungusBlock> EBONY_FUNGUS = registerBlock("ebony_fungus",
            () -> new FungusBlock(ModConfiguredFeatures.EBONY_FUNGUS_PLANTED, ModBlocks.BLAZING_NYLIUM.get(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instabreak()
                            .noCollission()
                            .sound(SoundType.FUNGUS)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<NetherSproutsBlock> BLAZING_SPROUTS = registerBlock("blazing_sprouts",
            () -> new NetherSproutsBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.FIRE)
                            .replaceable()
                            .noCollission()
                            .instabreak()
                            .sound(SoundType.NETHER_SPROUTS)
                            .offsetType(BlockBehaviour.OffsetType.XZ)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> EBONY_WART_BLOCK = registerBlock("ebony_wart_block",
            () ->  new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(1.0F)
                    .sound(SoundType.WART_BLOCK)
            )
    );

    public static final DeferredBlock<BlazingVinesBlock> BLAZING_VINES = registerBlockNoItem("blazing_vines",
            () ->  new BlazingVinesBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.FIRE)
                    .randomTicks()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.WEEPING_VINES)
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<BlazingVinesPlantBlock> BLAZING_VINES_PLANT = registerBlockNoItem("blazing_vines_plant",
            () ->  new BlazingVinesPlantBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.FIRE)
                    .randomTicks()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.WEEPING_VINES)
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<BlazingMagmaBlock> BLAZING_MAGMA_BLOCK = registerBlock("blazing_magma_block",
            () ->  new BlazingMagmaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MAGMA_BLOCK)
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .lightLevel(p_152684_ -> 3)
                    .emissiveRendering(ModBlocks::always)
                    .strength(0.5F)
                    .isValidSpawn((p_187421_, p_187422_, p_187423_, p_187424_) -> p_187424_.fireImmune())
            )
    );

    public static final DeferredBlock<BlazingReedsBlock> BLAZING_REEDS = registerBlock("blazing_reeds",
            () ->  new BlazingReedsBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.FIRE)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<PowderSoulSnowBlock> POWDER_SOUL_SNOW = registerBlock("powder_soul_snow",
            () ->  new PowderSoulSnowBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.SNOW)
                    .strength(0.5F)
                    .speedFactor(0.4F)
                    .sound(SoundType.POWDER_SNOW)
                    .isValidSpawn(Blocks::always)
            )
    );

    public static final DeferredBlock<Block> SOUL_SNOW_BLOCK = registerBlock("soul_snow_block",
            () ->  new Block(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.SNOW))
    );

    public static final DeferredBlock<FreezingMagmaBlock> FREEZING_MAGMA_BLOCK = registerBlock("freezing_magma_block",
            () ->  new FreezingMagmaBlock(1.0F, BlockBehaviour.Properties.ofFullCopy(Blocks.MAGMA_BLOCK)
                    .mapColor(MapColor.ICE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .friction(0.98F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(p_152684_ -> 3)
                    .emissiveRendering(ModBlocks::always)
                    .strength(0.5F)
                    .isValidSpawn((p_187421_, p_187422_, p_187423_, p_187424_) -> p_187424_.is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES) || p_187424_.is(EntityTypeTags.SKELETONS))
            )
    );

    public static final DeferredBlock<FreezingMagmaBlock> PACKED_FREEZING_MAGMA_BLOCK = registerBlock("packed_freezing_magma_block",
            () ->  new FreezingMagmaBlock(2.0F, BlockBehaviour.Properties.ofFullCopy(Blocks.MAGMA_BLOCK)
                    .mapColor(MapColor.ICE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .friction(0.98F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(p_152684_ -> 3)
                    .emissiveRendering(ModBlocks::always)
                    .strength(0.5F)
                    .isValidSpawn((p_187421_, p_187422_, p_187423_, p_187424_) -> p_187424_.is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES) || p_187424_.is(EntityTypeTags.SKELETONS))
            )
    );

    public static final DeferredBlock<FreezingMagmaBlock> BLUE_FREEZING_MAGMA_BLOCK = registerBlock("blue_freezing_magma_block",
            () ->  new FreezingMagmaBlock(3.0F, BlockBehaviour.Properties.ofFullCopy(Blocks.MAGMA_BLOCK)
                    .mapColor(MapColor.ICE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .friction(0.989F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(p_152684_ -> 3)
                    .emissiveRendering(ModBlocks::always)
                    .strength(0.5F)
                    .isValidSpawn((p_187421_, p_187422_, p_187423_, p_187424_) -> p_187424_.is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES) || p_187424_.is(EntityTypeTags.SKELETONS))
            )
    );

    public static final DeferredBlock<StonesBlock> NETHERRACK_STONES = registerBlockNoItem("netherrack_stones",
            () ->  new StonesBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NETHER)
                    .noOcclusion()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.NETHERRACK)
            )
    );

    public static final DeferredBlock<StonesBlock> STONES = registerBlockNoItem("stones",
            () ->  new StonesBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .noOcclusion()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<StonesBlock> DEEPSLATE_STONES = registerBlockNoItem("deepslate_stones",
            () ->  new StonesBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .noOcclusion()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.DEEPSLATE)
            )
    );

    public static final DeferredBlock<StonesBlock> PYROLIZED_STONES = registerBlockNoItem("pyrolized_stones",
            () ->  new StonesBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .noOcclusion()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.NETHERRACK)
            )
    );

    public static final DeferredBlock<StonesBlock> BASALT_STONES = registerBlockNoItem("basalt_stones",
            () ->  new StonesBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .noOcclusion()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.BASALT)
            )
    );

    public static final DeferredBlock<ModdedPointedDripstoneBlock> POINTED_NETHERRACK = registerBlock("pointed_netherrack",
            () ->  new ModdedPointedDripstoneBlock(Blocks.NETHERRACK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NETHER)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .noOcclusion()
                    .sound(SoundType.POINTED_DRIPSTONE)
                    .randomTicks()
                    .strength(1.5F, 3.0F)
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor(ModBlocks::never)
            )
    );

    public static final DeferredBlock<Block> FROZEN_NETHERRACK = registerBlock("frozen_netherrack",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.ICE)
                            .friction(0.98F)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(0.4F)
                            .sound(SoundType.NETHERRACK)
            )
    );

    public static final DeferredBlock<Block> PACKED_FROZEN_NETHERRACK = registerBlock("packed_frozen_netherrack",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.ICE)
                            .friction(0.98F)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(0.4F)
                            .sound(SoundType.NETHERRACK)
            )
    );

    public static final DeferredBlock<Block> BLUE_FROZEN_NETHERRACK = registerBlock("blue_frozen_netherrack",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.ICE)
                            .friction(0.989F)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(0.4F)
                            .sound(SoundType.NETHERRACK)
            )
    );

    public static final DeferredBlock<DropExperienceBlock> FROZEN_GOLD_ORE = registerBlock("frozen_gold_ore",
            () -> new DropExperienceBlock(UniformInt.of(0, 1), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.ICE)
                    .friction(0.98F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_GOLD_ORE)
            )
    );

    public static final DeferredBlock<DropExperienceBlock> FROZEN_QUARTZ_ORE = registerBlock("frozen_quartz_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 5), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.ICE)
                    .friction(0.98F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_ORE)
            )
    );

    public static final DeferredBlock<DropExperienceBlock> FROZEN_COPPER_ORE = registerBlock("frozen_copper_ore",
            () -> new DropExperienceBlock(UniformInt.of(0, 1), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.ICE)
                    .friction(0.98F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_ORE)
            )
    );

    public static final DeferredBlock<DropExperienceBlock> FROZEN_IRON_ORE = registerBlock("frozen_iron_ore",
            () -> new DropExperienceBlock(UniformInt.of(0, 1), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.ICE)
                    .friction(0.98F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_ORE)
            )
    );

    public static final DeferredBlock<DropExperienceBlock> FROZEN_EXPERIENCE_ORE = registerBlock("frozen_experience_ore",
            () -> new DropExperienceBlock(UniformInt.of(4, 20), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.ICE)
                    .friction(0.98F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_ORE)
            )
    );

    public static final DeferredBlock<BlazeRodBlock> BLAZE_ROD = registerBlockNoItem("blaze_rod",
            () -> new BlazeRodBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.FIRE)
                    .instabreak()
                    .sound(SoundType.DEEPSLATE)
                    .lightLevel(p_50872_ -> 8)
            )
    );

    public static final DeferredBlock<BreezeRodBlock> BREEZE_ROD = registerBlockNoItem("breeze_rod",
            () -> new BreezeRodBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .instabreak()
                    .sound(SoundType.DEEPSLATE)
                    .lightLevel(p_50872_ -> 8)
            )
    );

    public static final DeferredBlock<Block> CRACKED_NETHER_BRICK_STAIRS = registerBlock("cracked_nether_brick_stairs",
            () -> new StairBlock(Blocks.CRACKED_NETHER_BRICKS.defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_NETHER_BRICK_SLAB = registerBlock("cracked_nether_brick_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_NETHER_BRICK_WALL = registerBlock("cracked_nether_brick_wall",
            () -> new WallBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_NETHER_BRICK_FENCE = registerBlock("cracked_nether_brick_fence",
            () -> new FenceBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_NETHER_BRICK_PILLAR = registerBlock("cracked_nether_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> NETHER_BRICK_PILLAR = registerBlock("nether_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> RED_NETHER_BRICK_FENCE = registerBlock("red_nether_brick_fence",
            () -> new FenceBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_RED_NETHER_BRICKS = registerBlock("cracked_red_nether_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_RED_NETHER_BRICK_STAIRS = registerBlock("cracked_red_nether_brick_stairs",
            () -> new StairBlock(ModBlocks.CRACKED_RED_NETHER_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_RED_NETHER_BRICK_SLAB = registerBlock("cracked_red_nether_brick_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_RED_NETHER_BRICK_WALL = registerBlock("cracked_red_nether_brick_wall",
            () -> new WallBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_RED_NETHER_BRICK_FENCE = registerBlock("cracked_red_nether_brick_fence",
            () -> new FenceBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> RED_NETHER_BRICK_PILLAR = registerBlock("red_nether_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_RED_NETHER_BRICK_PILLAR = registerBlock("cracked_red_nether_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> PYROLIZED_NETHER_BRICKS = registerBlock("pyrolized_nether_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> PYROLIZED_NETHER_BRICK_STAIRS = registerBlock("pyrolized_nether_brick_stairs",
            () -> new StairBlock(ModBlocks.PYROLIZED_NETHER_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> PYROLIZED_NETHER_BRICK_SLAB = registerBlock("pyrolized_nether_brick_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> PYROLIZED_NETHER_BRICK_WALL = registerBlock("pyrolized_nether_brick_wall",
            () -> new WallBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> PYROLIZED_NETHER_BRICK_FENCE = registerBlock("pyrolized_nether_brick_fence",
            () -> new FenceBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_PYROLIZED_NETHER_BRICKS = registerBlock("cracked_pyrolized_nether_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_PYROLIZED_NETHER_BRICK_STAIRS = registerBlock("cracked_pyrolized_nether_brick_stairs",
            () -> new StairBlock(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_PYROLIZED_NETHER_BRICK_SLAB = registerBlock("cracked_pyrolized_nether_brick_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_PYROLIZED_NETHER_BRICK_WALL = registerBlock("cracked_pyrolized_nether_brick_wall",
            () -> new WallBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_PYROLIZED_NETHER_BRICK_FENCE = registerBlock("cracked_pyrolized_nether_brick_fence",
            () -> new FenceBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> PYROLIZED_NETHER_BRICK_PILLAR = registerBlock("pyrolized_nether_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_PYROLIZED_NETHER_BRICK_PILLAR = registerBlock("cracked_pyrolized_nether_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> BLAZE_BRICKS = registerBlock("blaze_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 15)
            )
    );

    public static final DeferredBlock<Block> BLAZE_BRICK_PILLAR = registerBlock("blaze_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 15)
            )
    );

    public static final DeferredBlock<Block> BLAZE_BRICK_STAIRS = registerBlock("blaze_brick_stairs",
            () -> new StairBlock(ModBlocks.BLAZE_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 11)
            )
    );

    public static final DeferredBlock<Block> BLAZE_BRICK_SLAB = registerBlock("blaze_brick_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(state -> {
                                SlabType type = state.getValue(SlabBlock.TYPE);
                                return switch (type) {
                                    case DOUBLE -> 15;
                                    case TOP, BOTTOM -> 8;
                                };
                            })
            )
    );

    public static final DeferredBlock<Block> BLAZE_BRICK_WALL = registerBlock("blaze_brick_wall",
            () -> new WallBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 8)
            )
    );

    public static final DeferredBlock<Block> BLAZE_BRICK_FENCE = registerBlock("blaze_brick_fence",
            () -> new FenceBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 8)
            )
    );

    public static final DeferredBlock<Block> RED_BLAZE_BRICKS = registerBlock("red_blaze_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 15)
            )
    );

    public static final DeferredBlock<Block> RED_BLAZE_BRICK_PILLAR = registerBlock("red_blaze_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 15)
            )
    );

    public static final DeferredBlock<Block> RED_BLAZE_BRICK_STAIRS = registerBlock("red_blaze_brick_stairs",
            () -> new StairBlock(ModBlocks.RED_BLAZE_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 11)
            )
    );

    public static final DeferredBlock<Block> RED_BLAZE_BRICK_SLAB = registerBlock("red_blaze_brick_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(state -> {
                                SlabType type = state.getValue(SlabBlock.TYPE);
                                return switch (type) {
                                    case DOUBLE -> 15;
                                    case TOP, BOTTOM -> 8;
                                };
                            })
            )
    );

    public static final DeferredBlock<Block> RED_BLAZE_BRICK_WALL = registerBlock("red_blaze_brick_wall",
            () -> new WallBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 8)
            )
    );

    public static final DeferredBlock<Block> RED_BLAZE_BRICK_FENCE = registerBlock("red_blaze_brick_fence",
            () -> new FenceBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 8)
            )
    );

    public static final DeferredBlock<Block> PYROLIZED_BLAZE_BRICKS = registerBlock("pyrolized_blaze_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 15)
            )
    );

    public static final DeferredBlock<Block> PYROLIZED_BLAZE_BRICK_PILLAR = registerBlock("pyrolized_blaze_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 15)
            )
    );

    public static final DeferredBlock<Block> PYROLIZED_BLAZE_BRICK_STAIRS = registerBlock("pyrolized_blaze_brick_stairs",
            () -> new StairBlock(ModBlocks.PYROLIZED_BLAZE_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 11)
            )
    );

    public static final DeferredBlock<Block> PYROLIZED_BLAZE_BRICK_SLAB = registerBlock("pyrolized_blaze_brick_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(state -> {
                                SlabType type = state.getValue(SlabBlock.TYPE);
                                return switch (type) {
                                    case DOUBLE -> 15;
                                    case TOP, BOTTOM -> 8;
                                };
                            })
            )
    );

    public static final DeferredBlock<Block> PYROLIZED_BLAZE_BRICK_WALL = registerBlock("pyrolized_blaze_brick_wall",
            () -> new WallBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 8)
            )
    );

    public static final DeferredBlock<Block> PYROLIZED_BLAZE_BRICK_FENCE = registerBlock("pyrolized_blaze_brick_fence",
            () -> new FenceBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 8)
            )
    );

    public static final DeferredBlock<Block> WARPED_BLAZE_BRICKS = registerBlock("warped_blaze_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 15)
            )
    );

    public static final DeferredBlock<Block> WARPED_BLAZE_BRICK_PILLAR = registerBlock("warped_blaze_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 15)
            )
    );

    public static final DeferredBlock<Block> WARPED_BLAZE_BRICK_STAIRS = registerBlock("warped_blaze_brick_stairs",
            () -> new StairBlock(ModBlocks.WARPED_BLAZE_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 11)
            )
    );

    public static final DeferredBlock<Block> WARPED_BLAZE_BRICK_SLAB = registerBlock("warped_blaze_brick_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(state -> {
                                SlabType type = state.getValue(SlabBlock.TYPE);
                                return switch (type) {
                                    case DOUBLE -> 15;
                                    case TOP, BOTTOM -> 8;
                                };
                            })
            )
    );

    public static final DeferredBlock<Block> WARPED_BLAZE_BRICK_WALL = registerBlock("warped_blaze_brick_wall",
            () -> new WallBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 8)
            )
    );

    public static final DeferredBlock<Block> WARPED_BLAZE_BRICK_FENCE = registerBlock("warped_blaze_brick_fence",
            () -> new FenceBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
                            .lightLevel(p_50872_ -> 8)
            )
    );

    public static final DeferredBlock<Block> WARPED_NETHER_BRICKS = registerBlock("warped_nether_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> WARPED_NETHER_BRICK_STAIRS = registerBlock("warped_nether_brick_stairs",
            () -> new StairBlock(ModBlocks.WARPED_NETHER_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> WARPED_NETHER_BRICK_SLAB = registerBlock("warped_nether_brick_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> WARPED_NETHER_BRICK_WALL = registerBlock("warped_nether_brick_wall",
            () -> new WallBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> WARPED_NETHER_BRICK_FENCE = registerBlock("warped_nether_brick_fence",
            () -> new FenceBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_WARPED_NETHER_BRICKS = registerBlock("cracked_warped_nether_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_WARPED_NETHER_BRICK_STAIRS = registerBlock("cracked_warped_nether_brick_stairs",
            () -> new StairBlock(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_WARPED_NETHER_BRICK_SLAB = registerBlock("cracked_warped_nether_brick_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_WARPED_NETHER_BRICK_WALL = registerBlock("cracked_warped_nether_brick_wall",
            () -> new WallBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_WARPED_NETHER_BRICK_FENCE = registerBlock("cracked_warped_nether_brick_fence",
            () -> new FenceBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> WARPED_NETHER_BRICK_PILLAR = registerBlock("warped_nether_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CRACKED_WARPED_NETHER_BRICK_PILLAR = registerBlock("cracked_warped_nether_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.NETHER_BRICKS)
            )
    );

    public static final DeferredBlock<Block> WARPED_WART = registerBlockNoItem("warped_wart",
            () -> new WarpedWartBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_CYAN)
                            .noCollission()
                            .randomTicks()
                            .sound(SoundType.NETHER_WART)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> MOSSY_AQUANDA_COBBLESTONE = registerBlock("mossy_aquanda_cobblestone",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> MOSSY_AQUANDA_COBBLESTONE_STAIRS = registerBlock("mossy_aquanda_cobblestone_stairs",
            () -> new StairBlock(ModBlocks.MOSSY_AQUANDA_COBBLESTONE.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> MOSSY_AQUANDA_COBBLESTONE_SLAB = registerBlock("mossy_aquanda_cobblestone_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> MOSSY_AQUANDA_COBBLESTONE_WALL = registerBlock("mossy_aquanda_cobblestone_wall",
            () -> new WallBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> MOSSY_AQUANDA_STONE_BRICKS = registerBlock("mossy_aquanda_stone_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> MOSSY_AQUANDA_STONE_BRICK_STAIRS = registerBlock("mossy_aquanda_stone_brick_stairs",
            () -> new StairBlock(ModBlocks.MOSSY_AQUANDA_STONE_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> MOSSY_AQUANDA_STONE_BRICK_SLAB = registerBlock("mossy_aquanda_stone_brick_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> MOSSY_AQUANDA_STONE_BRICK_WALL = registerBlock("mossy_aquanda_stone_brick_wall",
            () -> new WallBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> REDSTONIC_BLOCK = registerBlock("redstonic_block",
            () -> new PoweredBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.FIRE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .sound(SoundType.DRIPSTONE_BLOCK)
                            .requiresCorrectToolForDrops()
                            .strength(1.5F, 1.0F)
            )
    );

    public static final DeferredBlock<Block> POINTED_REDSTONIC = registerBlock("pointed_redstonic",
            () -> new ModdedPointedDripstoneBlock(ModBlocks.REDSTONIC_BLOCK.get(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_RED)
                            .forceSolidOn()
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .noOcclusion()
                            .sound(SoundType.POINTED_DRIPSTONE)
                            .randomTicks()
                            .strength(1.5F, 3.0F)
                            .dynamicShape()
                            .offsetType(BlockBehaviour.OffsetType.XZ)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> ICE_SHEET = registerBlockNoItem("ice_sheet",
            () -> new IceSheetBlock(0.98F,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.ICE)
                            .friction(0.98F)
                            .randomTicks()
                            .strength(0.5F)
                            .sound(SoundType.GLASS)
                            .noOcclusion()
                            .isValidSpawn((p_187426_, p_187427_, p_187428_, p_187429_) -> true)
            )
    );

    public static final DeferredBlock<Block> ICICLE = registerBlock("icicle",
            () -> new ModdedPointedDripstoneBlock(List.of(ModBlocks.FROZEN_NETHERRACK.get(), Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.ICE)
                            .friction(0.98F)
                            .forceSolidOn()
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .noOcclusion()
                            .sound(SoundType.GLASS)
                            .randomTicks()
                            .strength(1.5F, 3.0F)
                            .dynamicShape()
                            .offsetType(BlockBehaviour.OffsetType.XZ)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> STORMVEIN = registerBlock("stormvein",
            () -> new OmniBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .sound(SoundType.NETHERITE_BLOCK)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> ANCHOR_BLOCK = registerBlock("anchor_block",
            () -> new PoweredBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .requiresCorrectToolForDrops()
                            .strength(50.0F)
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );

    public static final DeferredBlock<Block> TESLA_COIL = registerBlock("tesla_coil",
            () -> new TeslaCoilBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .sound(SoundType.NETHERITE_BLOCK)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> VOLTAIC_SLATE = registerBlock("voltaic_slate",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );

    public static final DeferredBlock<Block> CUT_COPPER_BRICKS = registerBlock("cut_copper_bricks",
            () -> new WeatheringCopperFullBlock(WeatheringCopper.WeatherState.UNAFFECTED,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_ORANGE)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .randomTicks()
                            .sound(SoundType.COPPER)
            )
    );

    public static final DeferredBlock<Block> EXPOSED_CUT_COPPER_BRICKS = registerBlock("exposed_cut_copper_bricks",
            () -> new WeatheringCopperFullBlock(WeatheringCopper.WeatherState.EXPOSED,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .randomTicks()
                            .sound(SoundType.COPPER)
            )
    );

    public static final DeferredBlock<Block> WEATHERED_CUT_COPPER_BRICKS = registerBlock("weathered_cut_copper_bricks",
            () -> new WeatheringCopperFullBlock(WeatheringCopper.WeatherState.WEATHERED,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WARPED_STEM)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .randomTicks()
                            .sound(SoundType.COPPER)
            )
    );

    public static final DeferredBlock<Block> OXIDIZED_CUT_COPPER_BRICKS = registerBlock("oxidized_cut_copper_bricks",
            () -> new WeatheringCopperFullBlock(WeatheringCopper.WeatherState.OXIDIZED,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WARPED_NYLIUM)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .randomTicks()
                            .sound(SoundType.COPPER)
            )
    );

    public static final DeferredBlock<Block> WAXED_CUT_COPPER_BRICKS = registerBlock("waxed_cut_copper_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(ModBlocks.CUT_COPPER_BRICKS.get())
            )
    );

    public static final DeferredBlock<Block> WAXED_EXPOSED_CUT_COPPER_BRICKS = registerBlock("waxed_exposed_cut_copper_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(ModBlocks.EXPOSED_CUT_COPPER_BRICKS.get())
            )
    );

    public static final DeferredBlock<Block> WAXED_WEATHERED_CUT_COPPER_BRICKS = registerBlock("waxed_weathered_cut_copper_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(ModBlocks.WEATHERED_CUT_COPPER_BRICKS.get())
            )
    );

    public static final DeferredBlock<Block> WAXED_OXIDIZED_CUT_COPPER_BRICKS = registerBlock("waxed_oxidized_cut_copper_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(ModBlocks.OXIDIZED_CUT_COPPER_BRICKS.get())
            )
    );

    // Unaffected pillar
    public static final DeferredBlock<Block> COPPER_PILLAR = registerBlock("copper_pillar",
            () -> new WeatheringCopperPillarBlock(WeatheringCopper.WeatherState.UNAFFECTED,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_ORANGE)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .randomTicks()
                            .sound(SoundType.COPPER)
            )
    );

    // Exposed pillar
    public static final DeferredBlock<Block> EXPOSED_COPPER_PILLAR = registerBlock("exposed_copper_pillar",
            () -> new WeatheringCopperPillarBlock(WeatheringCopper.WeatherState.EXPOSED,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .randomTicks()
                            .sound(SoundType.COPPER)
            )
    );

    // Weathered pillar
    public static final DeferredBlock<Block> WEATHERED_COPPER_PILLAR = registerBlock("weathered_copper_pillar",
            () -> new WeatheringCopperPillarBlock(WeatheringCopper.WeatherState.WEATHERED,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WARPED_STEM)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .randomTicks()
                            .sound(SoundType.COPPER)
            )
    );

    // Oxidized pillar
    public static final DeferredBlock<Block> OXIDIZED_COPPER_PILLAR = registerBlock("oxidized_copper_pillar",
            () -> new WeatheringCopperPillarBlock(WeatheringCopper.WeatherState.OXIDIZED,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WARPED_NYLIUM)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 6.0F)
                            .randomTicks()
                            .sound(SoundType.COPPER)
            )
    );


    // Waxed pillars (just copies, no weathering)
    public static final DeferredBlock<Block> WAXED_COPPER_PILLAR = registerBlock("waxed_copper_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.COPPER_PILLAR.get()))
    );

    public static final DeferredBlock<Block> WAXED_EXPOSED_COPPER_PILLAR = registerBlock("waxed_exposed_copper_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.EXPOSED_COPPER_PILLAR.get()))
    );

    public static final DeferredBlock<Block> WAXED_WEATHERED_COPPER_PILLAR = registerBlock("waxed_weathered_copper_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.WEATHERED_COPPER_PILLAR.get()))
    );

    public static final DeferredBlock<Block> WAXED_OXIDIZED_COPPER_PILLAR = registerBlock("waxed_oxidized_copper_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.OXIDIZED_COPPER_PILLAR.get()))
    );

    public static final DeferredBlock<Block> GLOWSHROOM = registerBlock("glowshroom",
            () -> new WaterloggedMushroomBlock(
                    ModConfiguredFeatures.HUGE_GLOWSHROOM,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GLOW_LICHEN)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
                            .lightLevel(p_50892_ -> 3)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> GLOWSHROOM_BLOCK = registerBlock("glowshroom_block",
            () -> new HugeMushroomBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).instrument(NoteBlockInstrument.BASS).strength(0.2F).sound(SoundType.WOOD).lightLevel(p_50892_ -> 7)
            )
    );

    public static final DeferredBlock<Block> HANGING_GLOWMOSS = registerBlockNoItem("hanging_glowmoss",
            () -> new HangingGlowmossBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GLOW_LICHEN)
                            .randomTicks()
                            .noCollission()
                            .instabreak()
                            .sound(SoundType.MOSS)
                            .pushReaction(PushReaction.DESTROY)
                            .lightLevel(p_50892_ -> 7)
                            .offsetType(BlockBehaviour.OffsetType.XZ)
            )
    );

    public static final DeferredBlock<Block> HANGING_GLOWMOSS_PLANT = registerBlockNoItem("hanging_glowmoss_plant",
            () -> new HangingGlowmossPlantBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GLOW_LICHEN)
                            .randomTicks()
                            .noCollission()
                            .instabreak()
                            .sound(SoundType.MOSS)
                            .pushReaction(PushReaction.DESTROY)
                            .lightLevel(p_50892_ -> 7)
                            .offsetType(BlockBehaviour.OffsetType.XZ)
            )
    );

    public static final DeferredBlock<GlowmossCarpetBlock> GLOWMOSS_CARPET = registerBlock("glowmoss_carpet",
            () -> new GlowmossCarpetBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GLOW_LICHEN)
                            .strength(0.1F)
                            .sound(SoundType.MOSS_CARPET)
                            .pushReaction(PushReaction.DESTROY)
                            .lightLevel(GlowmossCarpetBlock.LIGHT_EMISSION)
            )
    );

    public static final DeferredBlock<ModdedMossBlock> GLOWMOSS_BLOCK = registerBlock("glowmoss_block",
            () -> new ModdedMossBlock(ModConfiguredFeatures.GLOWMOSS_PATCH_BONEMEAL, ModConfiguredFeatures.GLOWMOSS_PATCH_BONEMEAL,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GLOW_LICHEN)
                            .strength(0.1F)
                            .sound(SoundType.MOSS)
                            .pushReaction(PushReaction.DESTROY)
                            .lightLevel(p_50892_ -> 7)
            )
    );

    public static final DeferredBlock<Block> COPPER_SCAFFOLDING = registerBlockNoItem("copper_scaffolding",
            () -> new CopperScaffolding(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_ORANGE)
                            .noCollission()
                            .sound(SoundType.SCAFFOLDING)
                            .dynamicShape()
                            .isValidSpawn(Blocks::never)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> IRON_SCAFFOLDING = registerBlockNoItem("iron_scaffolding",
            () -> new IronScaffolding(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .noCollission()
                            .sound(SoundType.SCAFFOLDING)
                            .dynamicShape()
                            .isValidSpawn(Blocks::never)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> ZINC_SCAFFOLDING = registerBlockNoItem("zinc_scaffolding",
            () -> new ZincScaffolding(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .noCollission()
                            .sound(SoundType.SCAFFOLDING)
                            .dynamicShape()
                            .isValidSpawn(Blocks::never)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> TIN_SCAFFOLDING = registerBlockNoItem("tin_scaffolding",
            () -> new TinScaffolding(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .noCollission()
                            .sound(SoundType.SCAFFOLDING)
                            .dynamicShape()
                            .isValidSpawn(Blocks::never)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> BRONZE_SCAFFOLDING = registerBlockNoItem("bronze_scaffolding",
            () -> new BronzeScaffolding(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .noCollission()
                            .sound(SoundType.SCAFFOLDING)
                            .dynamicShape()
                            .isValidSpawn(Blocks::never)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> STEEL_SCAFFOLDING = registerBlockNoItem("steel_scaffolding",
            () -> new SteelScaffolding(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .noCollission()
                            .sound(SoundType.SCAFFOLDING)
                            .dynamicShape()
                            .isValidSpawn(Blocks::never)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> BRASS_SCAFFOLDING = registerBlockNoItem("brass_scaffolding",
            () -> new BrassScaffolding(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .noCollission()
                            .sound(SoundType.SCAFFOLDING)
                            .dynamicShape()
                            .isValidSpawn(Blocks::never)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> GOLDEN_SCAFFOLDING = registerBlockNoItem("golden_scaffolding",
            () -> new GoldenScaffolding(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GOLD)
                            .noCollission()
                            .sound(SoundType.SCAFFOLDING)
                            .dynamicShape()
                            .isValidSpawn(Blocks::never)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> CINCINNASITE_SCAFFOLDING = registerBlockNoItem("cincinnasite_scaffolding",
            () -> new CincinnasiteScaffolding(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_YELLOW)
                            .noCollission()
                            .sound(SoundType.SCAFFOLDING)
                            .dynamicShape()
                            .isValidSpawn(Blocks::never)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> PALLADIUM_SCAFFOLDING = registerBlockNoItem("palladium_scaffolding",
            () -> new PalladiumScaffolding(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.SNOW)
                            .noCollission()
                            .sound(SoundType.SCAFFOLDING)
                            .dynamicShape()
                            .isValidSpawn(Blocks::never)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> ANCIENT_SCAFFOLDING = registerBlockNoItem("ancient_scaffolding",
            () -> new AncientScaffolding(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GOLD)
                            .noCollission()
                            .sound(SoundType.SCAFFOLDING)
                            .dynamicShape()
                            .isValidSpawn(Blocks::never)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> PYROLIZED_SCAFFOLDING = registerBlockNoItem("pyrolized_scaffolding",
            () -> new PyrolizedScaffolding(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GOLD)
                            .noCollission()
                            .sound(SoundType.SCAFFOLDING)
                            .dynamicShape()
                            .isValidSpawn(Blocks::never)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<BrushableBlockNoFall> SUSPICIOUS_DIRT = registerBlock("suspicious_dirt",
            () -> new BrushableBlockNoFall(
                    Blocks.DIRT, // turns into normal dirt when fully brushed
                    SoundEvents.BRUSH_GRAVEL, // brushing sound
                    SoundEvents.BRUSH_GRAVEL_COMPLETED, // finished brushing sound
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DIRT)
                            .instrument(NoteBlockInstrument.SNARE)
                            .strength(0.25F)
                            .sound(SoundType.SUSPICIOUS_GRAVEL)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<BrushableBlockNoFall> SUSPICIOUS_COARSE_DIRT = registerBlock("suspicious_coarse_dirt",
            () -> new BrushableBlockNoFall(
                    Blocks.COARSE_DIRT, // turns into normal dirt when fully brushed
                    SoundEvents.BRUSH_GRAVEL, // brushing sound
                    SoundEvents.BRUSH_GRAVEL_COMPLETED, // finished brushing sound
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DIRT)
                            .instrument(NoteBlockInstrument.SNARE)
                            .strength(0.25F)
                            .sound(SoundType.SUSPICIOUS_GRAVEL)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<BrushableSoulSandBlock> SUSPICIOUS_SOUL_SAND = registerBlock("suspicious_soul_sand",
            () -> new BrushableSoulSandBlock(
                    Blocks.SOUL_SAND, // turns into normal dirt when fully brushed
                    SoundEvents.BRUSH_GRAVEL, // brushing sound
                    SoundEvents.BRUSH_GRAVEL_COMPLETED, // finished brushing sound
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BROWN)
                            .instrument(NoteBlockInstrument.SNARE)
                            .strength(0.25F)
                            .speedFactor(0.4F)
                            .sound(SoundType.SOUL_SAND)
                            .pushReaction(PushReaction.DESTROY)
                            .isValidSpawn(Blocks::always)
            )
    );

    public static final DeferredBlock<BrushableBlockNoFall> SUSPICIOUS_SOUL_SOIL = registerBlock("suspicious_soul_soil",
            () -> new BrushableBlockNoFall(
                    Blocks.SOUL_SOIL, // turns into normal dirt when fully brushed
                    SoundEvents.BRUSH_GRAVEL, // brushing sound
                    SoundEvents.BRUSH_GRAVEL_COMPLETED, // finished brushing sound
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BROWN)
                            .instrument(NoteBlockInstrument.SNARE)
                            .strength(0.25F)
                            .sound(SoundType.SOUL_SOIL)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<ModdedBrushableBlock> SUSPICIOUS_RED_SAND = registerBlock("suspicious_red_sand",
            () -> new ModdedBrushableBlock(
                    Blocks.RED_SAND, // turns into normal dirt when fully brushed
                    SoundEvents.BRUSH_SAND, // brushing sound
                    SoundEvents.BRUSH_SAND_COMPLETED, // finished brushing sound
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_ORANGE)
                            .instrument(NoteBlockInstrument.SNARE)
                            .strength(0.25F)
                            .sound(SoundType.SUSPICIOUS_SAND)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<ModdedNyliumBlock> ZINC_NYLIUM = registerBlock("zinc_nylium",
            () -> new ModdedNyliumBlock(NetherFeatures.CRIMSON_FOREST_VEGETATION, Blocks.NETHERRACK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.RAW_IRON)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.NYLIUM)
                    .randomTicks()
            )
    );

    public static final DeferredBlock<ZincBambooSaplingBlock> ZINC_BAMBOO_SAPLING = registerBlockNoItem("zinc_bamboo_sapling",
            () -> new ZincBambooSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.RAW_IRON)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO_SAPLING)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<ZincBambooStalkBlock> ZINC_BAMBOO_STALK = registerBlockNoItem("zinc_bamboo_stalk",
            () -> new ZincBambooStalkBlock(ModBlocks.ZINC_BAMBOO_SAPLING.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.RAW_IRON)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO)
                    .noOcclusion()
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<ModdedNyliumBlock> ROSE_GOLDEN_NYLIUM = registerBlock("rose_golden_nylium",
            () -> new ModdedNyliumBlock(NetherFeatures.CRIMSON_FOREST_VEGETATION, Blocks.NETHERRACK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_PINK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.NYLIUM)
                    .randomTicks()
            )
    );

    public static final DeferredBlock<RoseGoldenBambooSaplingBlock> ROSE_GOLDEN_BAMBOO_SAPLING = registerBlockNoItem("rose_golden_bamboo_sapling",
            () -> new RoseGoldenBambooSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_PINK)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO_SAPLING)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<RoseGoldenBambooStalkBlock> ROSE_GOLDEN_BAMBOO_STALK = registerBlockNoItem("rose_golden_bamboo_stalk",
            () -> new RoseGoldenBambooStalkBlock(ModBlocks.ROSE_GOLDEN_BAMBOO_SAPLING.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_PINK)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO)
                    .noOcclusion()
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<ModdedNyliumBlock> BRASS_NYLIUM = registerBlock("brass_nylium",
            () -> new ModdedNyliumBlock(NetherFeatures.CRIMSON_FOREST_VEGETATION, Blocks.NETHERRACK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.NYLIUM)
                    .randomTicks()
            )
    );

    public static final DeferredBlock<BrassBambooSaplingBlock> BRASS_BAMBOO_SAPLING = registerBlockNoItem("brass_bamboo_sapling",
            () -> new BrassBambooSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO_SAPLING)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<BrassBambooStalkBlock> BRASS_BAMBOO_STALK = registerBlockNoItem("brass_bamboo_stalk",
            () -> new BrassBambooStalkBlock(ModBlocks.BRASS_BAMBOO_SAPLING.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO)
                    .noOcclusion()
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<ModdedNyliumBlock> TIN_NYLIUM = registerBlock("tin_nylium",
            () -> new ModdedNyliumBlock(NetherFeatures.CRIMSON_FOREST_VEGETATION, Blocks.NETHERRACK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.NYLIUM)
                    .randomTicks()
            )
    );

    public static final DeferredBlock<TinBambooSaplingBlock> TIN_BAMBOO_SAPLING = registerBlockNoItem("tin_bamboo_sapling",
            () -> new TinBambooSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO_SAPLING)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<TinBambooStalkBlock> TIN_BAMBOO_STALK = registerBlockNoItem("tin_bamboo_stalk",
            () -> new TinBambooStalkBlock(ModBlocks.TIN_BAMBOO_SAPLING.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO)
                    .noOcclusion()
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<ModdedNyliumBlock> STEEL_NYLIUM = registerBlock("steel_nylium",
            () -> new ModdedNyliumBlock(NetherFeatures.CRIMSON_FOREST_VEGETATION, Blocks.NETHERRACK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.NYLIUM)
                    .randomTicks()
            )
    );

    public static final DeferredBlock<SteelBambooSaplingBlock> STEEL_BAMBOO_SAPLING = registerBlockNoItem("steel_bamboo_sapling",
            () -> new SteelBambooSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO_SAPLING)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<SteelBambooStalkBlock> STEEL_BAMBOO_STALK = registerBlockNoItem("steel_bamboo_stalk",
            () -> new SteelBambooStalkBlock(ModBlocks.TIN_BAMBOO_SAPLING.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO)
                    .noOcclusion()
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<ModdedNyliumBlock> BRONZE_NYLIUM = registerBlock("bronze_nylium",
            () -> new ModdedNyliumBlock(NetherFeatures.CRIMSON_FOREST_VEGETATION, Blocks.NETHERRACK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.NYLIUM)
                    .randomTicks()
            )
    );

    public static final DeferredBlock<BronzeBambooSaplingBlock> BRONZE_BAMBOO_SAPLING = registerBlockNoItem("bronze_bamboo_sapling",
            () -> new BronzeBambooSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO_SAPLING)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<BronzeBambooStalkBlock> BRONZE_BAMBOO_STALK = registerBlockNoItem("bronze_bamboo_stalk",
            () -> new BronzeBambooStalkBlock(ModBlocks.TIN_BAMBOO_SAPLING.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO)
                    .noOcclusion()
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<ModdedNyliumBlock> CINCINNASITE_NYLIUM = registerBlock("cincinnasite_nylium",
            () -> new ModdedNyliumBlock(NetherFeatures.CRIMSON_FOREST_VEGETATION, Blocks.NETHERRACK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.NYLIUM)
                    .randomTicks()
            )
    );

    public static final DeferredBlock<CincinnasiteBambooSaplingBlock> CINCINNASITE_BAMBOO_SAPLING = registerBlockNoItem("cincinnasite_bamboo_sapling",
            () -> new CincinnasiteBambooSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO_SAPLING)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<CincinnasiteBambooStalkBlock> CINCINNASITE_BAMBOO_STALK = registerBlockNoItem("cincinnasite_bamboo_stalk",
            () -> new CincinnasiteBambooStalkBlock(ModBlocks.CINCINNASITE_BAMBOO_SAPLING.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO)
                    .noOcclusion()
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<ModdedNyliumBlock> PALLADIUM_NYLIUM = registerBlock("palladium_nylium",
            () -> new ModdedNyliumBlock(NetherFeatures.CRIMSON_FOREST_VEGETATION, Blocks.NETHERRACK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(0.4F)
                    .sound(SoundType.NYLIUM)
                    .randomTicks()
            )
    );

    public static final DeferredBlock<PalladiumBambooSaplingBlock> PALLADIUM_BAMBOO_SAPLING = registerBlockNoItem("palladium_bamboo_sapling",
            () -> new PalladiumBambooSaplingBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .noCollission()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO_SAPLING)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<PalladiumBambooStalkBlock> PALLADIUM_BAMBOO_STALK = registerBlockNoItem("palladium_bamboo_stalk",
            () -> new PalladiumBambooStalkBlock(ModBlocks.PALLADIUM_BAMBOO_SAPLING.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .forceSolidOn()
                    .randomTicks()
                    .instabreak()
                    .strength(1.0F)
                    .sound(SoundType.BAMBOO)
                    .noOcclusion()
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_PYROLIZED_BAMBOO = registerBlock("potted_pyrolized_bamboo",
            () -> new FlowerPotBlock(
                    ModBlocks.PYROLIZED_BAMBOO_SAPLING.get(),
                    BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_COPPER_BAMBOO = registerBlock("potted_copper_bamboo",
            () -> new FlowerPotBlock(
                    ModBlocks.COPPER_BAMBOO_SAPLING.get(),
                    BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_IRON_BAMBOO = registerBlock("potted_iron_bamboo",
            () -> new FlowerPotBlock(
                    ModBlocks.IRON_BAMBOO_SAPLING.get(),
                    BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_GOLDEN_BAMBOO = registerBlock("potted_golden_bamboo",
            () -> new FlowerPotBlock(
                    ModBlocks.GOLDEN_BAMBOO_SAPLING.get(),
                    BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_ANCIENT_BAMBOO = registerBlock("potted_ancient_bamboo",
            () -> new FlowerPotBlock(
                    ModBlocks.ANCIENT_BAMBOO_SAPLING.get(),
                    BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_ZINC_BAMBOO = registerBlock("potted_zinc_bamboo",
            () -> new FlowerPotBlock(
                    ModBlocks.ZINC_BAMBOO_SAPLING.get(),
                    BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_ROSE_GOLDEN_BAMBOO = registerBlock("potted_rose_golden_bamboo",
            () -> new FlowerPotBlock(
                    ModBlocks.ROSE_GOLDEN_BAMBOO_SAPLING.get(),
                    BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_BRASS_BAMBOO = registerBlock("potted_brass_bamboo",
            () -> new FlowerPotBlock(
                    ModBlocks.BRASS_BAMBOO_SAPLING.get(),
                    BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_TIN_BAMBOO = registerBlock("potted_tin_bamboo",
            () -> new FlowerPotBlock(
                    ModBlocks.TIN_BAMBOO_SAPLING.get(),
                    BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_STEEL_BAMBOO = registerBlock("potted_steel_bamboo",
            () -> new FlowerPotBlock(
                    ModBlocks.STEEL_BAMBOO_SAPLING.get(),
                    BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<FlowerPotBlock> POTTED_BRONZE_BAMBOO = registerBlock("potted_bronze_bamboo",
            () -> new FlowerPotBlock(
                    ModBlocks.BRONZE_BAMBOO_SAPLING.get(),
                    BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)
            )
    );

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static final DeferredBlock<LeavesBlock> HYBERNATUS_LEAVES = registerBlock("hybernatus_leaves",
            () -> new LeavesBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GRASS)
                            .sound(SoundType.CHERRY_LEAVES)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> STRIPPED_HYBERNATUS_LOG = registerBlock("stripped_hybernatus_log",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()
            )
    );

    public static final DeferredBlock<Block> HYBERNATUS_LOG = registerBlock("hybernatus_log",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_PURPLE)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()
            ) {
                @Nullable
                @Override
                public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
                    if (itemAbility == ItemAbilities.AXE_STRIP) {
                        return STRIPPED_HYBERNATUS_LOG.get().defaultBlockState()
                                .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
                    }
                    return super.getToolModifiedState(state, context, itemAbility, simulate);
                }
            }
    );

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_HYBERNATUS_WOOD = registerBlock("stripped_hybernatus_wood",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> HYBERNATUS_WOOD = registerBlock("hybernatus_wood",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_PURPLE)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()
            ) {
                @Nullable
                @Override
                public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
                    if (itemAbility == ItemAbilities.AXE_STRIP) {
                        return STRIPPED_HYBERNATUS_WOOD.get().defaultBlockState()
                                .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
                    }
                    return super.getToolModifiedState(state, context, itemAbility, simulate);
                }
            }
    );

    public static final DeferredBlock<Block> HYBERNATUS_PLANKS = registerBlock("hybernatus_planks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()
            )
    );

    public static final DeferredBlock<StairBlock> HYBERNATUS_STAIRS = registerBlock("hybernatus_stairs",
            () -> new StairBlock(ModBlocks.HYBERNATUS_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(2.0F, 3.0F)
                            .sound(SoundType.WOOD)
                            .ignitedByLava()
            )
    );

    public static final DeferredBlock<SlabBlock> HYBERNATUS_SLAB = registerBlock("hybernatus_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
            )
    );

    public static final DeferredBlock<PressurePlateBlock> HYBERNATUS_PRESSURE_PLATE = registerBlock("hybernatus_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noCollission()
                    .ignitedByLava()
            )
    );

    public static final DeferredBlock<ButtonBlock> HYBERNATUS_BUTTON = registerBlock("hybernatus_button",
            () -> new ButtonBlock(BlockSetType.OAK, 20, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noCollission()
                    .ignitedByLava()
            )
    );

    public static final DeferredBlock<FenceBlock> HYBERNATUS_FENCE = registerBlock("hybernatus_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
            )
    );

    public static final DeferredBlock<FenceGateBlock> HYBERNATUS_FENCE_GATE = registerBlock("hybernatus_fence_gate",
            () -> new FenceGateBlock(WoodType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
            )
    );

    public static final DeferredBlock<DoorBlock> HYBERNATUS_DOOR = registerBlock("hybernatus_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .ignitedByLava()
            )
    );

    public static final DeferredBlock<TrapDoorBlock> HYBERNATUS_TRAPDOOR = registerBlock("hybernatus_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
                    .noOcclusion()
            )
    );

    public static final DeferredBlock<HybernatusSaplingBlock> HYBERNATUS_SAPLING = registerBlock("hybernatus_sapling",
            () -> new HybernatusSaplingBlock(
                    ModTreeGrowers.HYBERNATUS,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.PLANT)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<ModdedNyliumBlock> HYBERNATIC_NYLIUM = registerBlock("hybernatic_nylium",
            () -> new ModdedNyliumBlock(ModConfiguredFeatures.HYBERNATIC_FOLIAGE, Blocks.END_STONE, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .sound(SoundType.NYLIUM)
                    .randomTicks()
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 9.0F)
            )
    );

    public static final DeferredBlock<RevealableDoublePlantBlock> HYBERNATIC_TALL_GRASS = registerBlock("hybernatic_tall_grass",
            () -> new RevealableDoublePlantBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GREEN)
                            .randomTicks()
                            .dynamicShape()
                            .replaceable()
                            .noCollission()
                            .instabreak()
                            .sound(SoundType.GRASS)
                            .offsetType(BlockBehaviour.OffsetType.XZ)
                            .ignitedByLava()
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<EndTallGrassBlock> HYBERNATIC_GRASS = registerBlock("hybernatic_grass",
            () -> new EndTallGrassBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GREEN)
                            .randomTicks()
                            .dynamicShape()
                            .replaceable()
                            .noCollission()
                            .instabreak()
                            .sound(SoundType.GRASS)
                            .offsetType(BlockBehaviour.OffsetType.XYZ)
                            .ignitedByLava()
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<Block> HYBERNATIC_CRYSTAL = registerBlock("hybernatic_crystal",
            () -> new AmethystClusterBlock(
                    7.0F,
                    3.0F,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .forceSolidOn()
                            .noOcclusion()
                            .sound(SoundType.AMETHYST_CLUSTER)
                            .strength(1.5F)
                            .lightLevel(p_152632_ -> 5)
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<HybernaticCrystalBlock> HYBERNATIC_CRYSTAL_BLOCK =
            registerBlock("hybernatic_crystal_block",
                    () -> new HybernaticCrystalBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_PURPLE)
                                    .strength(1.5F)
                                    .sound(SoundType.AMETHYST)
                                    .requiresCorrectToolForDrops()
                                    .noOcclusion()
                                    .randomTicks()
                                    .lightLevel(state -> 15),
                            ModBlocks.HYBERNATIC_CRYSTAL // ✅ THIS is the Supplier
                    )
            );

    public static final String[] SLIME_COLORS = {
            "white","orange","magenta","light_blue","yellow","lime","pink","gray",
            "light_gray","cyan","purple","blue","brown","green","red","black","clear","tinted"
    };

    public static final Map<String, DeferredBlock<SlimeBlock>> SLIME_BLOCKS = new HashMap<>();

    static {
        for (String color : SLIME_COLORS) {

            DeferredBlock<SlimeBlock> block;

            if (color.equals("tinted")) {
                // Use TintedSlimeBlock for tinted color
                block = BLOCKS.register(color + "_slime_block",
                        () -> new TintedSlimeBlock(BlockBehaviour.Properties.of()
                                .strength(0.0F)
                                .friction(0.8F)
                                .sound(SoundType.SLIME_BLOCK)
                                .noOcclusion()
                        ));
            } else {
                // Regular slime block for all other colors
                block = BLOCKS.register(color + "_slime_block",
                        () -> new SlimeBlock(BlockBehaviour.Properties.of()
                                .strength(0.0F)
                                .friction(0.8F)
                                .sound(SoundType.SLIME_BLOCK)
                                .noOcclusion()
                        ));
            }

            SLIME_BLOCKS.put(color, block);

            // Register matching BlockItem
            registerBlockItem(color + "_slime_block", block);
        }
    }



    public static final String[] COLORS = {
            "white","orange","magenta","light_blue","yellow","lime","pink","gray",
            "light_gray","cyan","purple","blue","brown","green","red","black","clear","hybernatic"
    };

    public static final Map<String, DeferredBlock<TintedGlassBlock>> STAINED_TINTED_GLASS = new HashMap<>();

    // Extra hybernatic glass blocks
    public static final DeferredBlock<HybernaticGlassBlock> HYBERNATIC_GLASS = BLOCKS.register("hybernatic_glass",
            () -> new HybernaticGlassBlock(BlockBehaviour.Properties.of()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(ModBlocks::never)
                    .isSuffocating(ModBlocks::never)
                    .isViewBlocking(ModBlocks::never)
            ));

    public static final DeferredBlock<HybernaticGlassPaneBlock> HYBERNATIC_GLASS_PANE = BLOCKS.register("hybernatic_glass_pane",
            () -> new HybernaticGlassPaneBlock(BlockBehaviour.Properties.of()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor(ModBlocks::never)
                    .isSuffocating(ModBlocks::never)
                    .isViewBlocking(ModBlocks::never)
            )
    );

    static {
        for (String color : COLORS) {
            String blockName;
            if (color.equals("clear")) {
                blockName = "clear_tinted_glass";
            } else if (color.equals("hybernatic")) {
                blockName = "hybernatic_tinted_glass";  // no _stained_ for hybernatic
            } else {
                blockName = color + "_stained_tinted_glass";
            }

            DeferredBlock<TintedGlassBlock> block = BLOCKS.register(blockName,
                    () -> new TintedGlassBlock(BlockBehaviour.Properties.of()
                            .instrument(NoteBlockInstrument.HAT)
                            .strength(0.3F)
                            .sound(SoundType.GLASS)
                            .noOcclusion()
                            .isValidSpawn(Blocks::never)
                            .isRedstoneConductor(ModBlocks::never)
                            .isSuffocating(ModBlocks::never)
                            .isViewBlocking(ModBlocks::never)
                    ));

            STAINED_TINTED_GLASS.put(color, block);
            registerBlockItem(blockName, block);
        }

        // Register items for the extra hybernatic glass blocks
        registerBlockItem("hybernatic_glass", HYBERNATIC_GLASS);
        registerBlockItem("hybernatic_glass_pane", HYBERNATIC_GLASS_PANE);
    }

    public static final DeferredBlock<WaterloggedSlimeBlock> AQUANDA_SLIME_BLOCK = registerBlock("aquanda_slime_block",
            () -> new WaterloggedSlimeBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WATER)
                            .friction(0.8F)
                            .sound(SoundType.SLIME_BLOCK)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> MOSSY_DEEPSLATE = registerBlock("mossy_deepslate",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)
                            .mapColor(MapColor.GRASS)
            )
    );

    public static final DeferredBlock<Block> MOSSY_COBBLED_DEEPSLATE = registerBlock("mossy_cobbled_deepslate",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)
                            .mapColor(MapColor.GRASS)
            )
    );

    public static final DeferredBlock<Block> MOSSY_DEEPSLATE_BRICKS = registerBlock("mossy_deepslate_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS)
                            .mapColor(MapColor.GRASS)
            )
    );

    public static final DeferredBlock<Block> MOSSY_CHISELED_DEEPSLATE = registerBlock("mossy_chiseled_deepslate",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_DEEPSLATE)
                            .mapColor(MapColor.GRASS)
            )
    );

    public static final DeferredBlock<Block> MOSSY_DEEPSLATE_TILES = registerBlock("mossy_deepslate_tiles",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_TILES)
                            .mapColor(MapColor.GRASS)
            )
    );

    public static final DeferredBlock<Block> MOSSY_STONE = registerBlock("mossy_stone",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                            .mapColor(MapColor.GRASS)
            )
    );

    public static final DeferredBlock<Block> MOSSY_CHISELED_STONE_BRICKS = registerBlock("mossy_chiseled_stone_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_STONE_BRICKS)
                            .mapColor(MapColor.GRASS)
            )
    );

    public static final DeferredBlock<Block> COBBLED_SANDSTONE = registerBlock("cobbled_sandstone",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)
                            .strength(1.3F, 0.8F)
            )
    );

    public static final DeferredBlock<Block> COBBLED_RED_SANDSTONE = registerBlock("cobbled_red_sandstone",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SANDSTONE)
                            .strength(1.3F, 0.8F)
            )
    );

    public static final DeferredBlock<Block> SANDSTONE_BRICKS = registerBlock("sandstone_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)
            )
    );

    public static final DeferredBlock<Block> RED_SANDSTONE_BRICKS = registerBlock("red_sandstone_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SANDSTONE)
            )
    );

    public static final DeferredBlock<Block> BASALT_BRICKS = registerBlock("basalt_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.BASALT)
                            .sound(new SoundType(
                                    1.0F,
                                    0.7F,
                                    SoundEvents.TUFF_BRICKS_BREAK,
                                    SoundEvents.TUFF_BRICKS_STEP,
                                    SoundEvents.TUFF_BRICKS_PLACE,
                                    SoundEvents.TUFF_BRICKS_HIT,
                                    SoundEvents.TUFF_BRICKS_FALL
                            )
                    )
            )
    );

    public static final DeferredBlock<Block> CHISELED_BASALT = registerBlock("chiseled_basalt",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.BASALT)
                            .sound(new SoundType(
                                            1.0F,
                                            0.7F,
                                            SoundEvents.TUFF_BRICKS_BREAK,
                                            SoundEvents.TUFF_BRICKS_STEP,
                                            SoundEvents.TUFF_BRICKS_PLACE,
                                            SoundEvents.TUFF_BRICKS_HIT,
                                            SoundEvents.TUFF_BRICKS_FALL
                                    )
                            )
            )
    );

    public static final DeferredBlock<Block> ANDESITE_BRICKS = registerBlock("andesite_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)
            )
    );

    public static final DeferredBlock<Block> GRANITE_BRICKS = registerBlock("granite_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)
            )
    );

    public static final DeferredBlock<Block> DIORITE_BRICKS = registerBlock("diorite_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)
            )
    );

    public static final DeferredBlock<Block> CUT_ANDESITE = registerBlock("cut_andesite",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_ANDESITE)
            )
    );

    public static final DeferredBlock<Block> CUT_GRANITE = registerBlock("cut_granite",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_GRANITE)
            )
    );

    public static final DeferredBlock<Block> CUT_DIORITE = registerBlock("cut_diorite",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DIORITE)
            )
    );

    public static final DeferredBlock<Block> CUT_SMOOTH_STONE = registerBlock("cut_smooth_stone",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_STONE)
            )
    );

    public static final DeferredBlock<Block> CUT_TUFF = registerBlock("cut_tuff",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_TUFF)
            )
    );

    public static final DeferredBlock<Block> TUFF_TILES = registerBlock("tuff_tiles",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF_BRICKS)
                            .sound(new SoundType(
                                    1.0F,
                                    1.15F,
                                    SoundEvents.TUFF_BRICKS_BREAK,
                                    SoundEvents.TUFF_BRICKS_STEP,
                                    SoundEvents.TUFF_BRICKS_PLACE,
                                    SoundEvents.TUFF_BRICKS_HIT,
                                    SoundEvents.TUFF_BRICKS_FALL
                            )
                    )
            )
    );

    public static final DeferredBlock<Block> CUT_DEEPSLATE = registerBlock("cut_deepslate",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DEEPSLATE)
            )
    );

    public static final DeferredBlock<Block> POLISHED_DRIPSTONE = registerBlock("polished_dripstone",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK)
            )
    );

    public static final DeferredBlock<Block> POLISHED_CALCITE = registerBlock("polished_calcite",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE)
            )
    );

    public static final DeferredBlock<Block> CUT_DRIPSTONE = registerBlock("cut_dripstone",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK)
            )
    );

    public static final DeferredBlock<Block> CUT_CALCITE = registerBlock("cut_calcite",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE)
            )
    );

    public static final DeferredBlock<Block> DRIPSTONE_BRICKS = registerBlock("dripstone_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK)
                            .sound(new SoundType(
                                            1.0F,
                                            0.9F,
                                            SoundEvents.TUFF_BRICKS_BREAK,
                                            SoundEvents.TUFF_BRICKS_STEP,
                                            SoundEvents.TUFF_BRICKS_PLACE,
                                            SoundEvents.TUFF_BRICKS_HIT,
                                            SoundEvents.TUFF_BRICKS_FALL
                            )
                    )
            )
    );

    public static final DeferredBlock<Block> CALCITE_BRICKS = registerBlock("calcite_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE)
                            .sound(new SoundType(
                                            1.0F,
                                            1.3F,
                                            SoundEvents.TUFF_BRICKS_BREAK,
                                            SoundEvents.TUFF_BRICKS_STEP,
                                            SoundEvents.TUFF_BRICKS_PLACE,
                                            SoundEvents.TUFF_BRICKS_HIT,
                                            SoundEvents.TUFF_BRICKS_FALL
                                    )
                            )
            )
    );

    public static final DeferredBlock<Block> CUT_BASALT = registerBlock("cut_basalt",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_BASALT)
            )
    );

    public static final DeferredBlock<Block> CUT_GOLD_BLOCK = registerBlock("cut_gold_block",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK)
            )
    );

    public static final DeferredBlock<Block> CUT_GOLDEN_BRICKS = registerBlock("cut_golden_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK)
            )
    );

    public static final DeferredBlock<Block> BASALT_TILES = registerBlock("basalt_tiles",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.BASALT)
                            .sound(new SoundType(
                                            1.0F,
                                            0.85F,
                                            SoundEvents.TUFF_BRICKS_BREAK,
                                            SoundEvents.TUFF_BRICKS_STEP,
                                            SoundEvents.TUFF_BRICKS_PLACE,
                                            SoundEvents.TUFF_BRICKS_HIT,
                                            SoundEvents.TUFF_BRICKS_FALL
                                    )
                            )
            )
    );

    public static final DeferredBlock<Block> CUT_QUARTZ_BLOCK = registerBlock("cut_quartz_block",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
            )
    );

    public static final DeferredBlock<Block> QUARTZ_TILES = registerBlock("quartz_tiles",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BRICKS)
                            .sound(new SoundType(
                                    1.0F,
                                    1.15F,
                                    SoundEvents.STONE_BREAK,
                                    SoundEvents.STONE_STEP,
                                    SoundEvents.STONE_PLACE,
                                    SoundEvents.STONE_HIT,
                                    SoundEvents.STONE_FALL
                            )
                    )
            )
    );



    public static final DeferredBlock<Block> DRIPSTONE_TILES = registerBlock("dripstone_tiles",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK)
                            .sound(new SoundType(
                                            1.0F,
                                            1.05F,
                                            SoundEvents.TUFF_BRICKS_BREAK,
                                            SoundEvents.TUFF_BRICKS_STEP,
                                            SoundEvents.TUFF_BRICKS_PLACE,
                                            SoundEvents.TUFF_BRICKS_HIT,
                                            SoundEvents.TUFF_BRICKS_FALL
                                    )
                            )
            )
    );

    public static final DeferredBlock<Block> CALCITE_TILES = registerBlock("calcite_tiles",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE)
                            .sound(new SoundType(
                                            1.0F,
                                            1.45F,
                                            SoundEvents.TUFF_BRICKS_BREAK,
                                            SoundEvents.TUFF_BRICKS_STEP,
                                            SoundEvents.TUFF_BRICKS_PLACE,
                                            SoundEvents.TUFF_BRICKS_HIT,
                                            SoundEvents.TUFF_BRICKS_FALL
                                    )
                            )
            )
    );

    public static final DeferredBlock<Block> ANDESITE_TILES = registerBlock("andesite_tiles",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_ANDESITE)
                            .sound(new SoundType(
                                            1.0F,
                                            1.15F,
                                            SoundEvents.STONE_BREAK,
                                            SoundEvents.STONE_STEP,
                                            SoundEvents.STONE_PLACE,
                                            SoundEvents.STONE_HIT,
                                            SoundEvents.STONE_FALL
                                    )
                            )
            )
    );

    public static final DeferredBlock<Block> GRANITE_TILES = registerBlock("granite_tiles",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_GRANITE)
                            .sound(new SoundType(
                                            1.0F,
                                            1.15F,
                                            SoundEvents.STONE_BREAK,
                                            SoundEvents.STONE_STEP,
                                            SoundEvents.STONE_PLACE,
                                            SoundEvents.STONE_HIT,
                                            SoundEvents.STONE_FALL
                                    )
                            )
            )
    );

    public static final DeferredBlock<Block> DIORITE_TILES = registerBlock("diorite_tiles",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DIORITE)
                            .sound(new SoundType(
                                            1.0F,
                                            1.15F,
                                            SoundEvents.STONE_BREAK,
                                            SoundEvents.STONE_STEP,
                                            SoundEvents.STONE_PLACE,
                                            SoundEvents.STONE_HIT,
                                            SoundEvents.STONE_FALL
                                    )
                            )
            )
    );

    public static final DeferredBlock<Block> POLISHED_BLACKSTONE_TILES = registerBlock("polished_blackstone_tiles",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_BLACKSTONE_BRICKS)
                            .sound(new SoundType(
                                            1.0F,
                                            1.15F,
                                            SoundEvents.STONE_BREAK,
                                            SoundEvents.STONE_STEP,
                                            SoundEvents.STONE_PLACE,
                                            SoundEvents.STONE_HIT,
                                            SoundEvents.STONE_FALL
                                    )
                            )
            )
    );

    public static final DeferredBlock<Block> STONE_BRICK_TILES = registerBlock("stone_brick_tiles",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)
                            .sound(new SoundType(
                                            1.0F,
                                            1.15F,
                                            SoundEvents.STONE_BREAK,
                                            SoundEvents.STONE_STEP,
                                            SoundEvents.STONE_PLACE,
                                            SoundEvents.STONE_HIT,
                                            SoundEvents.STONE_FALL
                                    )
                            )
            )
    );

    public static final DeferredBlock<Block> POLISHED_PRISMARINE = registerBlock("polished_prismarine",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE)
            )
    );

    public static final DeferredBlock<Block> CUT_PRISMARINE = registerBlock("cut_prismarine",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE)
            )
    );

    public static final DeferredBlock<Block> PRISMARINE_TILES = registerBlock("prismarine_tiles",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE_BRICKS)
                            .sound(new SoundType(
                                            1.0F,
                                            1.15F,
                                            SoundEvents.STONE_BREAK,
                                            SoundEvents.STONE_STEP,
                                            SoundEvents.STONE_PLACE,
                                            SoundEvents.STONE_HIT,
                                            SoundEvents.STONE_FALL
                                    )
                            )
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> PRISMARINE_BRICK_PILLAR = registerBlock("prismarine_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE_BRICKS)
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> BASALT_BRICK_PILLAR = registerBlock("basalt_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.BASALT)
                            .sound(new SoundType(
                                    1.0F,
                                    0.7F,
                                    SoundEvents.TUFF_BRICKS_BREAK,
                                    SoundEvents.TUFF_BRICKS_STEP,
                                    SoundEvents.TUFF_BRICKS_PLACE,
                                    SoundEvents.TUFF_BRICKS_HIT,
                                    SoundEvents.TUFF_BRICKS_FALL
                            )
                    )
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> TUFF_BRICK_PILLAR = registerBlock("tuff_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF_BRICKS)
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> POLISHED_BLACKSTONE_BRICK_PILLAR = registerBlock("polished_blackstone_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_BLACKSTONE_BRICKS)
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> ANDESITE_BRICK_PILLAR = registerBlock("andesite_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_ANDESITE)
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> GRANITE_BRICK_PILLAR = registerBlock("granite_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_GRANITE)
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> DIORITE_BRICK_PILLAR = registerBlock("diorite_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DIORITE)
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> STONE_BRICK_PILLAR = registerBlock("stone_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> CALCITE_BRICK_PILLAR = registerBlock("calcite_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE)
                            .sound(new SoundType(
                                    1.0F,
                                    1.3F,
                                    SoundEvents.TUFF_BRICKS_BREAK,
                                    SoundEvents.TUFF_BRICKS_STEP,
                                    SoundEvents.TUFF_BRICKS_PLACE,
                                    SoundEvents.TUFF_BRICKS_HIT,
                                    SoundEvents.TUFF_BRICKS_FALL
                            )
                    )
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> DRIPSTONE_BRICK_PILLAR = registerBlock("dripstone_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK)
                            .sound(new SoundType(
                                    1.0F,
                                    0.9F,
                                    SoundEvents.TUFF_BRICKS_BREAK,
                                    SoundEvents.TUFF_BRICKS_STEP,
                                    SoundEvents.TUFF_BRICKS_PLACE,
                                    SoundEvents.TUFF_BRICKS_HIT,
                                    SoundEvents.TUFF_BRICKS_FALL
                            )
                    )
            )
    );



    public static final DeferredBlock<WeatheringCopperChainBlock> COPPER_CHAIN = registerBlock("copper_chain",
            () -> new WeatheringCopperChainBlock(WeatheringCopper.WeatherState.UNAFFECTED,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_ORANGE)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .randomTicks()
                            .sound(SoundType.CHAIN)
            )
    );

    public static final DeferredBlock<WeatheringCopperChainBlock> EXPOSED_COPPER_CHAIN = registerBlock("exposed_copper_chain",
            () -> new WeatheringCopperChainBlock(WeatheringCopper.WeatherState.EXPOSED,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .randomTicks()
                            .sound(SoundType.CHAIN)
            )
    );

    public static final DeferredBlock<WeatheringCopperChainBlock> WEATHERED_COPPER_CHAIN = registerBlock("weathered_copper_chain",
            () -> new WeatheringCopperChainBlock(WeatheringCopper.WeatherState.WEATHERED,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WARPED_STEM)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .randomTicks()
                            .sound(SoundType.CHAIN)
            )
    );

    public static final DeferredBlock<WeatheringCopperChainBlock> OXIDIZED_COPPER_CHAIN = registerBlock("oxidized_copper_chain",
            () -> new WeatheringCopperChainBlock(WeatheringCopper.WeatherState.OXIDIZED,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WARPED_NYLIUM)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .randomTicks()
                            .sound(SoundType.CHAIN)
            )
    );

    public static final DeferredBlock<ChainBlock> WAXED_COPPER_CHAIN = registerBlock("waxed_copper_chain",
            () -> new ChainBlock(
                    BlockBehaviour.Properties.ofFullCopy(ModBlocks.COPPER_CHAIN.get())
            )
    );

    public static final DeferredBlock<ChainBlock> WAXED_EXPOSED_COPPER_CHAIN = registerBlock("waxed_exposed_copper_chain",
            () -> new ChainBlock(
                    BlockBehaviour.Properties.ofFullCopy(ModBlocks.EXPOSED_COPPER_CHAIN.get())
            )
    );

    public static final DeferredBlock<ChainBlock> WAXED_WEATHERED_COPPER_CHAIN = registerBlock("waxed_weathered_copper_chain",
            () -> new ChainBlock(
                    BlockBehaviour.Properties.ofFullCopy(ModBlocks.WEATHERED_COPPER_CHAIN.get())
            )
    );

    public static final DeferredBlock<ChainBlock> WAXED_OXIDIZED_COPPER_CHAIN = registerBlock("waxed_oxidized_copper_chain",
            () -> new ChainBlock(
                    BlockBehaviour.Properties.ofFullCopy(ModBlocks.OXIDIZED_COPPER_CHAIN.get())
            )
    );

    public static final DeferredBlock<ChainBlock> GOLDEN_CHAIN = registerBlock("golden_chain",
            () -> new ChainBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CHAIN)
            )
    );

    public static final DeferredBlock<ChainBlock> ROSE_GOLDEN_CHAIN = registerBlock("rose_golden_chain",
            () -> new ChainBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CHAIN)
            )
    );

    public static final DeferredBlock<ModdedSpawnerBlock> GOLDEN_SPAWNER = registerBlock("golden_spawner",
            () -> new ModdedSpawnerBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SPAWNER)
                            .mapColor(MapColor.GOLD)
            )
    );

    public static final DeferredBlock<Block> CUT_POLISHED_BLACKSTONE = registerBlock("cut_polished_blackstone",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)
            )
    );

    public static final DeferredBlock<Block> SANDSTONE_TILES = registerBlock("sandstone_tiles",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)
                            .sound(new SoundType(
                                            1.0F,
                                            1.15F,
                                            SoundEvents.STONE_BREAK,
                                            SoundEvents.STONE_STEP,
                                            SoundEvents.STONE_PLACE,
                                            SoundEvents.STONE_HIT,
                                            SoundEvents.STONE_FALL
                                    )
                            )
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> RED_SANDSTONE_BRICK_PILLAR = registerBlock("red_sandstone_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)
            )
    );

    public static final DeferredBlock<Block> RED_SANDSTONE_TILES = registerBlock("red_sandstone_tiles",
            () -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)
                            .sound(new SoundType(
                                            1.0F,
                                            1.15F,
                                            SoundEvents.STONE_BREAK,
                                            SoundEvents.STONE_STEP,
                                            SoundEvents.STONE_PLACE,
                                            SoundEvents.STONE_HIT,
                                            SoundEvents.STONE_FALL
                                    )
                            )
            )
    );

    public static final DeferredBlock<RotatedPillarBlock> SANDSTONE_BRICK_PILLAR = registerBlock("sandstone_brick_pillar",
            () -> new RotatedPillarBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)
            )
    );

    public static final DeferredBlock<RevealableBlock> PHANTASMIC_ENDSTONE = registerBlock("phantasmic_endstone",
            () -> new RevealableBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.SAND)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .randomTicks()
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 9.0F)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<RevealableNyliumBlock> PHANTASMIC_NYLIUM = registerBlock("phantasmic_nylium",
            () -> new RevealableNyliumBlock(ModConfiguredFeatures.PHANTASMIC_FOLIAGE, ModBlocks.PHANTASMIC_ENDSTONE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .sound(SoundType.SCULK_CATALYST)
                    .randomTicks()
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 9.0F)
                    .noOcclusion()
            )
    );

    public static final DeferredBlock<RevealableTallGrassBlock> PHANTASMIC_GRASS = registerBlock("phantasmic_grass",
            () -> new RevealableTallGrassBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GREEN)
                            .randomTicks()
                            .dynamicShape()
                            .replaceable()
                            .noCollission()
                            .instabreak()
                            .sound(SoundType.SCULK)
                            .offsetType(BlockBehaviour.OffsetType.XYZ)
                            .ignitedByLava()
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<RevealableDoublePlantBlock> PHANTASMIC_TALL_GRASS = registerBlock("phantasmic_tall_grass",
            () -> new RevealableDoublePlantBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GREEN)
                            .randomTicks()
                            .dynamicShape()
                            .replaceable()
                            .noCollission()
                            .instabreak()
                            .sound(SoundType.SCULK)
                            .offsetType(BlockBehaviour.OffsetType.XZ)
                            .ignitedByLava()
                            .pushReaction(PushReaction.DESTROY)
            )
    );

    public static final DeferredBlock<VeilOrchidBlock> VEIL_ORCHID = registerBlock("veil_orchid",
            () -> new VeilOrchidBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .randomTicks()
                            .replaceable()
                            .noCollission()
                            .instabreak()
                            .sound(SoundType.SCULK)
                            .offsetType(BlockBehaviour.OffsetType.XZ)
                            .ignitedByLava()
                            .dynamicShape()
                            .pushReaction(PushReaction.DESTROY)
                            .lightLevel(state -> state.getValue(VeilOrchidBlock.EMIT_LIGHT))
            )
    );

    public record GemstoneSet(
            DeferredBlock<AmethystBlock> roughBlock,
            DeferredBlock<ModdedBuddingBlock> buddingBlock,
            DeferredBlock<AmethystClusterBlock> cluster,
            DeferredBlock<AmethystClusterBlock> largeBud,
            DeferredBlock<AmethystClusterBlock> mediumBud,
            DeferredBlock<AmethystClusterBlock> smallBud
    ) {
        public static GemstoneSet register(String name, MapColor stoneColor, MapColor buddingColor, MapColor clusterColor) {
            DeferredBlock<AmethystBlock> roughBlock = ModBlocks.registerBlock("rough_" + name + "_block",
                    () -> new AmethystBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(stoneColor)
                                    .strength(1.5F)
                                    .sound(SoundType.AMETHYST)
                                    .requiresCorrectToolForDrops()
                    ));

            DeferredBlock<AmethystClusterBlock> cluster = ModBlocks.registerBlock("rough_" + name + "_cluster",
                    () -> new AmethystClusterBlock(7.0F, 3.0F,
                            BlockBehaviour.Properties.of()
                                    .mapColor(clusterColor)
                                    .forceSolidOn()
                                    .noOcclusion()
                                    .sound(SoundType.AMETHYST_CLUSTER)
                                    .strength(1.5F)
                                    .lightLevel(s -> 5)
                                    .pushReaction(PushReaction.DESTROY)
                    ));

            DeferredBlock<AmethystClusterBlock> largeBud = ModBlocks.registerBlock("large_rough_" + name + "_bud",
                    () -> new AmethystClusterBlock(5.0F, 3.0F,
                            BlockBehaviour.Properties.of()
                                    .mapColor(clusterColor)
                                    .forceSolidOn()
                                    .noOcclusion()
                                    .sound(SoundType.MEDIUM_AMETHYST_BUD)
                                    .strength(1.5F)
                                    .lightLevel(s -> 4)
                                    .pushReaction(PushReaction.DESTROY)
                    ));

            DeferredBlock<AmethystClusterBlock> mediumBud = ModBlocks.registerBlock("medium_rough_" + name + "_bud",
                    () -> new AmethystClusterBlock(4.0F, 3.0F,
                            BlockBehaviour.Properties.of()
                                    .mapColor(clusterColor)
                                    .forceSolidOn()
                                    .noOcclusion()
                                    .sound(SoundType.LARGE_AMETHYST_BUD)
                                    .strength(1.5F)
                                    .lightLevel(s -> 2)
                                    .pushReaction(PushReaction.DESTROY)
                    ));

            DeferredBlock<AmethystClusterBlock> smallBud = ModBlocks.registerBlock("small_rough_" + name + "_bud",
                    () -> new AmethystClusterBlock(3.0F, 4.0F,
                            BlockBehaviour.Properties.of()
                                    .mapColor(clusterColor)
                                    .forceSolidOn()
                                    .noOcclusion()
                                    .sound(SoundType.SMALL_AMETHYST_BUD)
                                    .strength(1.5F)
                                    .lightLevel(s -> 1)
                                    .pushReaction(PushReaction.DESTROY)
                    ));

            DeferredBlock<ModdedBuddingBlock> buddingBlock = ModBlocks.registerBlock("budding_rough_" + name,
                    () -> new ModdedBuddingBlock(
                            cluster.get(), largeBud.get(), mediumBud.get(), smallBud.get(),
                            BlockBehaviour.Properties.of()
                                    .mapColor(buddingColor)
                                    .randomTicks()
                                    .strength(1.5F)
                                    .sound(SoundType.AMETHYST)
                                    .requiresCorrectToolForDrops()
                                    .pushReaction(PushReaction.DESTROY)
                    ));

            return new GemstoneSet(roughBlock, buddingBlock, cluster, largeBud, mediumBud, smallBud);
        }
    }

    public static final GemstoneSet DIAMOND = GemstoneSet.register("diamond",
            MapColor.COLOR_LIGHT_BLUE, MapColor.COLOR_CYAN, MapColor.COLOR_LIGHT_BLUE);

    public static final GemstoneSet EMERALD = GemstoneSet.register("emerald",
            MapColor.COLOR_GREEN, MapColor.EMERALD, MapColor.COLOR_GREEN);

    public static final GemstoneSet LAPIS = GemstoneSet.register("lapis",
            MapColor.COLOR_BLUE, MapColor.LAPIS, MapColor.COLOR_BLUE);

    public static final GemstoneSet QUARTZ = GemstoneSet.register("quartz",
            MapColor.QUARTZ, MapColor.QUARTZ, MapColor.QUARTZ);

    public static final GemstoneSet ECHO_SHARD = GemstoneSet.register("echo_shard",
            MapColor.COLOR_CYAN, MapColor.COLOR_CYAN, MapColor.COLOR_CYAN);

    public static final List<GemstoneSet> GEMSTONE_SETS = List.of(
            DIAMOND, EMERALD, LAPIS, QUARTZ, ECHO_SHARD
    );

    public static final String[] LANTERN_COLORS = {
            "golden",
            "rose_golden"
            // add more colors here!
    };

    private static MapColor getLanternMapColor(String color) {
        return switch (color) {

            case "golden"      -> MapColor.GOLD;
            case "rose_golden" -> MapColor.TERRACOTTA_PINK;

            default -> MapColor.COLOR_LIGHT_GRAY; // safe default
        };
    }

    public static final Map<String, DeferredBlock<LanternBlock>> LANTERNS = new HashMap<>();
    public static final Map<String, DeferredBlock<LanternBlock>> SOUL_LANTERNS = new HashMap<>();

    static {
        for (String color : LANTERN_COLORS) {

            // -------------------------
            // GET MAP COLOR
            // -------------------------
            MapColor mapColor = getLanternMapColor(color);

            // -------------------------
            // BUILD BLOCK NAME
            // -------------------------
            String name = color + "_lantern";
            String soulname = color + "_soul_lantern";

            // -------------------------
            // REGISTER BLOCK
            // -------------------------
            DeferredBlock<LanternBlock> lantern = BLOCKS.register(
                    name,
                    () -> new LanternBlock(
                            BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)
                                    .mapColor(mapColor)
                    )
            );

            DeferredBlock<LanternBlock> soulLantern = BLOCKS.register(
                    soulname,
                    () -> new LanternBlock(
                            BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_LANTERN)
                                    .mapColor(mapColor)
                    )
            );

            // Store in map
            LANTERNS.put(color, lantern);
            SOUL_LANTERNS.put(color, soulLantern);

            // Register block item
            registerBlockItem(name, lantern);
            registerBlockItem(soulname, soulLantern);
        }
    }

    // ============================================================================
// TERRACOTTA COLOR LIST
// ============================================================================
    public static final String[] TERRACOTTA_COLORS = {
            "white","orange","magenta","light_blue","yellow","lime","pink","gray",
            "light_gray","cyan","purple","blue","brown","green","red","black","terracotta"
    };


    // ============================================================================
// TERRACOTTA MAP COLOR LOOKUP
// ============================================================================
    private static MapColor getTerracottaMapColor(String color) {
        return switch (color) {
            case "white"      -> MapColor.TERRACOTTA_WHITE;
            case "orange"     -> MapColor.TERRACOTTA_ORANGE;
            case "magenta"    -> MapColor.TERRACOTTA_MAGENTA;
            case "light_blue" -> MapColor.TERRACOTTA_LIGHT_BLUE;
            case "yellow"     -> MapColor.TERRACOTTA_YELLOW;
            case "lime"       -> MapColor.TERRACOTTA_LIGHT_GREEN;
            case "pink"       -> MapColor.TERRACOTTA_PINK;
            case "gray"       -> MapColor.TERRACOTTA_GRAY;
            case "light_gray" -> MapColor.TERRACOTTA_LIGHT_GRAY;
            case "cyan"       -> MapColor.TERRACOTTA_CYAN;
            case "purple"     -> MapColor.TERRACOTTA_PURPLE;
            case "blue"       -> MapColor.TERRACOTTA_BLUE;
            case "brown"      -> MapColor.TERRACOTTA_BROWN;
            case "green"      -> MapColor.TERRACOTTA_GREEN;
            case "red"        -> MapColor.TERRACOTTA_RED;
            case "black"      -> MapColor.TERRACOTTA_BLACK;

            // Vanilla default terracotta is orange toned
            case "terracotta" -> MapColor.TERRACOTTA_ORANGE;

            default -> MapColor.TERRACOTTA_ORANGE;
        };
    }


    // ============================================================================
// TERRACOTTA BLOCK MAPS
// ============================================================================
    public static final Map<String, DeferredBlock<Block>> TERRACOTTA_COBBLED = new HashMap<>();
    public static final Map<String, DeferredBlock<Block>> TERRACOTTA_BRICKS = new HashMap<>();


    // ============================================================================
// BLOCK REGISTRATION
// ============================================================================
    static {
        for (String color : TERRACOTTA_COLORS) {

            // Get correct terracotta map color
            MapColor mapColor = getTerracottaMapColor(color);

            // ------------------------------------------------------------
            // COBBLED TERRACOTTA
            // ------------------------------------------------------------
            String cobbledName = color.equals("terracotta")
                    ? "cobbled_terracotta"
                    : color + "_cobbled_terracotta";

            DeferredBlock<Block> cobbled = BLOCKS.register(
                    cobbledName,
                    () -> new Block(BlockBehaviour.Properties.of()
                            .mapColor(mapColor)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(1.75F, 4.2F) // cobbled values
                    )
            );

            TERRACOTTA_COBBLED.put(color, cobbled);
            registerBlockItem(cobbledName, cobbled);


            // ------------------------------------------------------------
            // TERRACOTTA BRICKS
            // ------------------------------------------------------------
            String bricksName = color.equals("terracotta")
                    ? "terracotta_bricks"
                    : color + "_terracotta_bricks";

            DeferredBlock<Block> bricks = BLOCKS.register(
                    bricksName,
                    () -> new Block(BlockBehaviour.Properties.of()
                            .mapColor(mapColor)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(1.25F, 4.2F) // normal brick values
                    )
            );

            TERRACOTTA_BRICKS.put(color, bricks);
            registerBlockItem(bricksName, bricks);
        }
    }

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }

    private static <T extends Block> DeferredBlock<T> registerBlockNoItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
