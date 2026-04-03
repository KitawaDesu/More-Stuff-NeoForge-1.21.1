package net.kitawa.more_stuff.blocks.custom.end.phantasmic;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RevealableBushBlock extends BushBlock {

    // Share the exact same property instances as RevealableBlock
    public static final int MAX_DISTANCE = RevealableBlock.MAX_DISTANCE;
    public static final IntegerProperty REVEAL_DISTANCE = RevealableBlock.REVEAL_DISTANCE;
    public static final IntegerProperty LIGHT_ABOVE = RevealableBlock.LIGHT_ABOVE;
    public static final BooleanProperty FORCED_SOLID = RevealableBlock.FORCED_SOLID;

    public static final MapCodec<RevealableBushBlock> CODEC = simpleCodec(RevealableBushBlock::new);

    public RevealableBushBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(REVEAL_DISTANCE, MAX_DISTANCE)
                .setValue(LIGHT_ABOVE, 0)
                .setValue(FORCED_SOLID, false));
    }

    @Override
    protected MapCodec<? extends RevealableBushBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(REVEAL_DISTANCE, LIGHT_ABOVE, FORCED_SOLID);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState,
                                     LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        level.scheduleTick(currentPos, this, 1);
        return !state.canSurvive(level, currentPos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState updated = RevealableBlock.updateRevealDistance(state, level, pos);
        updated = RevealableBlock.updateLightAbove(updated, level, pos);
        level.setBlock(pos, updated, Block.UPDATE_ALL);

        BlockPos abovePos = pos.above();
        BlockState above = level.getBlockState(abovePos);

        // Don't tick if blocked by a solid/opaque block, a carpet/slab, or another revealable block
        boolean allowsLight = above.propagatesSkylightDown(level, abovePos)
                && !(above.getBlock() instanceof RevealableBlock);

        if (allowsLight) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide()) level.scheduleTick(pos, this, 1);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide()) level.scheduleTick(pos, this, 1);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(REVEAL_DISTANCE) == MAX_DISTANCE
                && state.getValue(LIGHT_ABOVE) == 0
                && !state.getValue(FORCED_SOLID)) {
            BlockState updated = RevealableBlock.updateRevealDistance(state, level, pos);
            updated = RevealableBlock.updateLightAbove(updated, level, pos);
            level.setBlock(pos, updated, Block.UPDATE_ALL);
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT)
                || state.is(ModBlockTags.ENDER_NYLIUM)
                || state.getBlock() instanceof FarmBlock;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }
}