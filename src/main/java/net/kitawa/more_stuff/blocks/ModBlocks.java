package net.kitawa.more_stuff.blocks;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.blocks.custom.aquanda_biome_blocks.*;
import net.kitawa.more_stuff.blocks.custom.frozen_valley_biome_blocks.FreezingMagmaBlock;
import net.kitawa.more_stuff.blocks.custom.frozen_valley_biome_blocks.PowderSoulSnowBlock;
import net.kitawa.more_stuff.blocks.custom.general.*;
import net.kitawa.more_stuff.blocks.custom.metallic_forest_blocks.*;
import net.kitawa.more_stuff.blocks.custom.pyrolized_and_blazing_blocks.*;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.worldgen.ModConfiguredFeatures;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
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
                            .sound(SoundType.METAL)
            )
    );

    public static final DeferredBlock<Block> ROSARITE_BLOCK = registerBlock("rosarite_block",
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
            () -> new ModdedMossBlock(ModConfiguredFeatures.AQUANDA_MOSS_PATCH_BONEMEAL, ModConfiguredFeatures.AQUANDA_MOSS_PATCH_BONEMEAL_UNDERWATER,
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
            () -> new ModdedNyliumBlock(NetherFeatures.CRIMSON_FOREST_VEGETATION, ModBlocks.PYROLIZED_NETHERRACK.get(), BlockBehaviour.Properties.of()
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

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
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
