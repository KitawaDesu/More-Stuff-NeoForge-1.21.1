package net.kitawa.more_stuff.blocks.custom.end.phantasmic;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.util.configs.ExperimentalUpdatesConfig;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RevealableBlock extends Block {

    public static final int MAX_DISTANCE = 7;
    public static final IntegerProperty REVEAL_DISTANCE = IntegerProperty.create("reveal_distance", 1, MAX_DISTANCE);
    public static final IntegerProperty LIGHT_ABOVE = IntegerProperty.create("light_above", 0, 15);
    public static final BooleanProperty FORCED_SOLID = BooleanProperty.create("forced_solid");

    public RevealableBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(REVEAL_DISTANCE, MAX_DISTANCE)
                .setValue(LIGHT_ABOVE, 0)
                .setValue(FORCED_SOLID, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return simpleCodec(Block::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(REVEAL_DISTANCE, LIGHT_ABOVE, FORCED_SOLID);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState,
                                     LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        level.scheduleTick(currentPos, this, 1);
        return state;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState updated = updateRevealDistance(state, level, pos);
        updated = updateLightAbove(updated, level, pos);
        level.setBlock(pos, updated, Block.UPDATE_ALL);

        BlockPos abovePos = pos.above();
        BlockState above = level.getBlockState(abovePos);

        // If a VeilOrchid sits above, nudge it to re-evaluate its reveal radius
        if (above.getBlock() instanceof VeilOrchidBlock) {
            level.scheduleTick(abovePos, above.getBlock(), 1);
        }

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
            BlockState updated = updateRevealDistance(state, level, pos);
            updated = updateLightAbove(updated, level, pos);
            level.setBlock(pos, updated, Block.UPDATE_ALL);
        }
    }

    // Shared across all revealable block types — uses hasProperty so it works for any class
    public static void setForcedSolid(Level level, BlockPos pos, boolean solid) {
        BlockState state = level.getBlockState(pos);
        if (state.hasProperty(FORCED_SOLID)) {
            level.setBlock(pos, state.setValue(FORCED_SOLID, solid), Block.UPDATE_ALL);
        }
    }

    static BlockState updateRevealDistance(BlockState state, LevelAccessor level, BlockPos pos) {
        int minDist = MAX_DISTANCE;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            mutable.setWithOffset(pos, dir);
            BlockState neighbor = level.getBlockState(mutable);
            if (neighbor.is(ModBlockTags.REVEALS_HIDDEN_BLOCKS)) {
                minDist = 1;
                break;
            } else if (neighbor.hasProperty(FORCED_SOLID) && neighbor.getValue(FORCED_SOLID)) {
                minDist = 1;
                break;
            } else if (neighbor.hasProperty(REVEAL_DISTANCE)) {
                minDist = Math.min(minDist, neighbor.getValue(REVEAL_DISTANCE) + 1);
            }
        }
        return state.setValue(REVEAL_DISTANCE, Math.min(minDist, MAX_DISTANCE));
    }

    static BlockState updateLightAbove(BlockState state, LevelAccessor level, BlockPos pos) {
        BlockPos above = pos.above();
        int blockLight = level.getBrightness(LightLayer.BLOCK, above);
        int skyLight = level.getBrightness(LightLayer.SKY, above);

        float skyFraction = skyLight / 15.0f;

        float timeLight = 0;
        if (level instanceof Level l) {
            long time = l.getDayTime() % 24000;

            if (time >= 1000 && time <= 11000) {
                timeLight = 15;
            } else if (time > 11000 && time < 13700) {
                timeLight = 15.0f * (1.0f - (float)(time - 11000) / (13700 - 11000));
            } else if (time >= 13700 && time <= 22300) {
                timeLight = 0;
            } else if (time > 22300) {
                timeLight = 15.0f * ((float)(time - 22300) / (1000 + (24000 - 22300)));
            } else {
                timeLight = 15.0f * ((float)(time + (24000 - 22300)) / (1000 + (24000 - 22300)));
            }
        }

        float skyContribution = timeLight * skyFraction;
        int directLight = Math.round(Math.max(skyContribution, blockLight));

        // Propagate LIGHT_ABOVE from neighbors — same diamond pattern as REVEAL_DISTANCE
        int maxNeighborLight = 0;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            mutable.setWithOffset(pos, dir);
            BlockState neighbor = level.getBlockState(mutable);
            if (neighbor.hasProperty(LIGHT_ABOVE)) {
                maxNeighborLight = Math.max(maxNeighborLight, neighbor.getValue(LIGHT_ABOVE) - 1);
            }
        }

        // Use whichever is brightest — direct measurement or propagated from neighbor
        int result = Math.max(directLight, maxNeighborLight);
        result = Math.max(0, Math.min(15, result));

        return state.setValue(LIGHT_ABOVE, result);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    public enum RenderMode { CURRENT, DISTANCE_STATES, LIGHT_STATES, SHADER_FRIENDLY }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        RenderMode mode      = ExperimentalUpdatesConfig.phantasmicRenderType;
        int        tolerance = ExperimentalUpdatesConfig.phantasmicRenderDistance;

        return switch (mode) {
            case CURRENT ->
                    adjacentBlockState == state
                            || super.skipRendering(state, adjacentBlockState, side);

            case DISTANCE_STATES -> {
                if (adjacentBlockState.getBlock() != this)
                    yield super.skipRendering(state, adjacentBlockState, side);
                int dist    = state.getValue(REVEAL_DISTANCE);
                int adjDist = adjacentBlockState.getValue(REVEAL_DISTANCE);
                yield Math.abs(dist - adjDist) < tolerance
                        || super.skipRendering(state, adjacentBlockState, side);
            }

            case LIGHT_STATES -> {
                if (adjacentBlockState.getBlock() != this)
                    yield super.skipRendering(state, adjacentBlockState, side);
                int light    = state.getValue(LIGHT_ABOVE);
                int adjLight = adjacentBlockState.getValue(LIGHT_ABOVE);
                yield Math.abs(light - adjLight) < tolerance
                        || super.skipRendering(state, adjacentBlockState, side);
            }

            case SHADER_FRIENDLY ->
                    super.skipRendering(state, adjacentBlockState, side);
        };
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState p_309084_, BlockGetter p_309133_, BlockPos p_309097_) {
        return true;
    }

    @Override
    protected float getShadeBrightness(BlockState p_308911_, BlockGetter p_308952_, BlockPos p_308918_) {
        return 1.0F;
    }
}
