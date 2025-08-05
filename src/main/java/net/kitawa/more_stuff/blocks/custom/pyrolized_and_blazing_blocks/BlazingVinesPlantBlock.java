package net.kitawa.more_stuff.blocks.custom.pyrolized_and_blazing_blocks;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlazingVinesPlantBlock extends GrowingPlantBodyBlock {
    public static final MapCodec<BlazingVinesPlantBlock> CODEC = simpleCodec(BlazingVinesPlantBlock::new);
    public static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    @Override
    public MapCodec<BlazingVinesPlantBlock> codec() {
        return CODEC;
    }

    public BlazingVinesPlantBlock(BlockBehaviour.Properties p_154873_) {
        super(p_154873_, Direction.UP, SHAPE, false);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) ModBlocks.BLAZING_VINES.get();
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader p_304590_, BlockPos p_152967_, BlockState p_152968_) {
        return new ItemStack(ModItems.BLAZING_VINES.get());
    }
}
