package net.kitawa.more_stuff.blocks.custom.aquanda_biome_blocks;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class AquandaKelpBlock extends GrowingPlantHeadBlock implements LiquidBlockContainer {
    public static final MapCodec<AquandaKelpBlock> CODEC = simpleCodec(AquandaKelpBlock::new);
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);
    private static final double GROW_PER_TICK_PROBABILITY = 0.14;

    @Override
    public MapCodec<AquandaKelpBlock> codec() {
        return CODEC;
    }

    public AquandaKelpBlock(BlockBehaviour.Properties p_54300_) {
        super(p_54300_, Direction.UP, SHAPE, true, 0.14);
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.is(Blocks.WATER);
    }

    @Override
    protected Block getBodyBlock() {
        return ModBlocks.AQUANDA_KELP_PLANT.get();
    }

    @Override
    protected boolean canAttachTo(BlockState state) {
        return !state.is(ModBlockTags.CREATES_DOWNWARDS_BUBBLE_COLUMNS);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader p_304590_, BlockPos p_152967_, BlockState p_152968_) {
        return new ItemStack(ModItems.AQUANDA_KELP.get());
    }

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return 1;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8 ? super.getStateForPlacement(context) : null;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getSource(false);
    }
}
