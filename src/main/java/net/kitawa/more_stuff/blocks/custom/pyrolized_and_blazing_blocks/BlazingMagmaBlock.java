package net.kitawa.more_stuff.blocks.custom.pyrolized_and_blazing_blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlazingMagmaBlock extends Block {
    public static final MapCodec<BlazingMagmaBlock> CODEC = simpleCodec(BlazingMagmaBlock::new);
    private static final int BUBBLE_COLUMN_CHECK_DELAY = 20;

    @Override
    public MapCodec<BlazingMagmaBlock> codec() {
        return CODEC;
    }

    public BlazingMagmaBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.isSteppingCarefully() && entity instanceof LivingEntity) {
            entity.hurt(level.damageSources().hotFloor(), 2.0F);
        }

        super.stepOn(level, pos, state, entity);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BubbleColumnBlock.updateColumn(level, pos.above(), state);
    }

    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */
    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.UP && facingState.is(Blocks.WATER)) {
            level.scheduleTick(currentPos, this, 20);
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        level.scheduleTick(pos, this, 20);
    }
}

