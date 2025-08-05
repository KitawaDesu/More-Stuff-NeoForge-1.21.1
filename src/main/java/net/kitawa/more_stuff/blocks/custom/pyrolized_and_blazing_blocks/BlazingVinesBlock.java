package net.kitawa.more_stuff.blocks.custom.pyrolized_and_blazing_blocks;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlazingVinesBlock extends GrowingPlantHeadBlock {
    public static final MapCodec<BlazingVinesBlock> CODEC = simpleCodec(BlazingVinesBlock::new);
    public static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 15.0, 12.0);

    @Override
    public MapCodec<BlazingVinesBlock> codec() {
        return CODEC;
    }

    public BlazingVinesBlock(BlockBehaviour.Properties p_154864_) {
        super(p_154864_, Direction.UP, SHAPE, false, 0.1);
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource p_222649_) {
        return NetherVines.getBlocksToGrowWhenBonemealed(p_222649_);
    }

    @Override
    protected Block getBodyBlock() {
        return ModBlocks.BLAZING_VINES_PLANT.get();
    }

    @Override
    protected boolean canGrowInto(BlockState p_154869_) {
        return NetherVines.isValidGrowthState(p_154869_);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader p_304590_, BlockPos p_152967_, BlockState p_152968_) {
        return new ItemStack(ModItems.BLAZING_VINES.get());
    }
}