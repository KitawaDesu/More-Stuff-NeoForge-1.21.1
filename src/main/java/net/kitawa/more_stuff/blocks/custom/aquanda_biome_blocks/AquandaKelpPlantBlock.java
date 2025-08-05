package net.kitawa.more_stuff.blocks.custom.aquanda_biome_blocks;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.Shapes;

import javax.annotation.Nullable;

public class AquandaKelpPlantBlock extends GrowingPlantBodyBlock implements LiquidBlockContainer {
    public static final MapCodec<AquandaKelpPlantBlock> CODEC = simpleCodec(AquandaKelpPlantBlock::new);

    @Override
    public MapCodec<AquandaKelpPlantBlock> codec() {
        return CODEC;
    }

    public AquandaKelpPlantBlock(BlockBehaviour.Properties p_54323_) {
        super(p_54323_, Direction.UP, Shapes.block(), true);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return ModBlocks.AQUANDA_KELP.get();
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader p_304590_, BlockPos p_152967_, BlockState p_152968_) {
        return new ItemStack(ModItems.AQUANDA_KELP.get());
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getSource(false);
    }

    @Override
    protected boolean canAttachTo(BlockState state) {
        return !state.is(ModBlockTags.CREATES_DOWNWARDS_BUBBLE_COLUMNS);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }
}
