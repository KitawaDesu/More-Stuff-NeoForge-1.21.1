package net.kitawa.more_stuff.blocks.custom.end.phantasmic;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.TriState;

import javax.annotation.Nullable;

public class RevealableDoublePlantBlock extends DoublePlantBlock {

    // Share the exact same property instances as RevealableBlock
    public static final int MAX_DISTANCE = RevealableBlock.MAX_DISTANCE;
    public static final IntegerProperty REVEAL_DISTANCE = RevealableBlock.REVEAL_DISTANCE;
    public static final IntegerProperty LIGHT_ABOVE = RevealableBlock.LIGHT_ABOVE;
    public static final BooleanProperty FORCED_SOLID = RevealableBlock.FORCED_SOLID;

    public static final MapCodec<RevealableDoublePlantBlock> CODEC = simpleCodec(RevealableDoublePlantBlock::new);

    public RevealableDoublePlantBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(REVEAL_DISTANCE, MAX_DISTANCE)
                .setValue(LIGHT_ABOVE, 0)
                .setValue(FORCED_SOLID, false));
    }

    @Override
    public MapCodec<RevealableDoublePlantBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(REVEAL_DISTANCE, LIGHT_ABOVE, FORCED_SOLID);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState,
                                     LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        level.scheduleTick(currentPos, this, 1);
        DoubleBlockHalf half = state.getValue(HALF);
        if (facing.getAxis() != Direction.Axis.Y
                || half == DoubleBlockHalf.LOWER != (facing == Direction.UP)
                || facingState.is(this) && facingState.getValue(HALF) != half) {
            return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !state.canSurvive(level, currentPos)
                    ? Blocks.AIR.defaultBlockState()
                    : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        return pos.getY() < level.getMaxBuildHeight() - 1
                && level.getBlockState(pos.above()).canBeReplaced(context)
                ? super.getStateForPlacement(context)
                : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            LivingEntity placer, ItemStack stack) {
        BlockPos above = pos.above();
        level.setBlock(above, DoublePlantBlock.copyWaterloggedFrom(level, above,
                this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER)), 3);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockState below = level.getBlockState(pos.below());
            if (state.getBlock() != this) return super.canSurvive(state, level, pos);
            return below.is(this) && below.getValue(HALF) == DoubleBlockHalf.LOWER;
        } else {
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);
            TriState soilDecision = belowState.canSustainPlant(level, belowPos, Direction.UP, state);
            if (!soilDecision.isDefault()) return soilDecision.isTrue();
            return belowState.is(BlockTags.DIRT)
                    || belowState.is(ModBlockTags.ENDER_NYLIUM)
                    || belowState.getBlock() instanceof FarmBlock;
        }
    }

    public static void placeAt(LevelAccessor level, BlockState state, BlockPos pos, int flags) {
        BlockPos above = pos.above();
        level.setBlock(pos, DoublePlantBlock.copyWaterloggedFrom(level, pos,
                state.setValue(HALF, DoubleBlockHalf.LOWER)), flags);
        level.setBlock(above, DoublePlantBlock.copyWaterloggedFrom(level, above,
                state.setValue(HALF, DoubleBlockHalf.UPPER)), flags);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            if (player.isCreative()) {
                preventDropFromBottomPart(level, pos, state, player);
            } else {
                dropResources(state, level, pos, null, player, player.getMainHandItem());
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
                              @Nullable BlockEntity te, ItemStack stack) {
        super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), te, stack);
    }

    protected static void preventDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player) {
        DoubleBlockHalf half = state.getValue(HALF);
        if (half == DoubleBlockHalf.UPPER) {
            BlockPos below = pos.below();
            BlockState belowState = level.getBlockState(below);
            if (belowState.is(state.getBlock()) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState replacement = belowState.getFluidState().is(Fluids.WATER)
                        ? Blocks.WATER.defaultBlockState()
                        : Blocks.AIR.defaultBlockState();
                level.setBlock(below, replacement, 35);
                level.levelEvent(player, 2001, below, Block.getId(belowState));
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected long getSeed(BlockState state, BlockPos pos) {
        return Mth.getSeed(pos.getX(),
                pos.below(state.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(),
                pos.getZ());
    }
}
