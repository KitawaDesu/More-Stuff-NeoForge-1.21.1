package net.kitawa.more_stuff.blocks.custom.end.phantasmic;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;


public class VeilOrchidBlock extends DoublePlantBlock implements BonemealableBlock {

    public static final MapCodec<VeilOrchidBlock> CODEC = simpleCodec(VeilOrchidBlock::new);
    public static final IntegerProperty EMIT_LIGHT = IntegerProperty.create("emit_light", 3, 10);
    public static final IntegerProperty REVEAL_RADIUS = IntegerProperty.create("reveal_radius", 2, 5);

    public VeilOrchidBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(EMIT_LIGHT, 3)
                .setValue(REVEAL_RADIUS, 3));
    }

    @Override
    public MapCodec<VeilOrchidBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EMIT_LIGHT, REVEAL_RADIUS);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            LivingEntity placer, ItemStack stack) {
        int randomLight = 3 + level.random.nextInt(8);
        int randomRadius = 2 + level.random.nextInt(4);
        BlockState newState = state.setValue(EMIT_LIGHT, randomLight).setValue(REVEAL_RADIUS, randomRadius);
        level.setBlock(pos, newState, 3);
        super.setPlacedBy(level, pos, newState, placer, stack);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos,
                        BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide() && state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            BlockPos above = pos.above();
            BlockState upperState = level.getBlockState(above);
            if (upperState.is(this) && upperState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                level.setBlock(above, upperState
                        .setValue(EMIT_LIGHT, state.getValue(EMIT_LIGHT))
                        .setValue(REVEAL_RADIUS, state.getValue(REVEAL_RADIUS)), 3);
            }
            forceRevealNearby(level, pos, state.getValue(REVEAL_RADIUS), true);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos,
                         BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide() && state.getValue(HALF) == DoubleBlockHalf.LOWER
                && !newState.is(this)) {
            // Use the stored radius so we unforce exactly the same blocks we forced
            forceRevealNearby(level, pos, state.getValue(REVEAL_RADIUS), false);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    private void forceRevealNearby(Level level, BlockPos pos, int radius, boolean solid) {
        BlockPos.betweenClosedStream(
                pos.offset(-radius, -radius, -radius),
                pos.offset(radius, radius, radius)
        ).forEach(nearPos -> {
            if (pos.distSqr(nearPos) <= (double)(radius * radius)) {
                BlockPos immutable = nearPos.immutable();
                BlockState state = level.getBlockState(immutable);
                if (state.hasProperty(RevealableBlock.FORCED_SOLID)) {
                    level.setBlock(immutable, state.setValue(RevealableBlock.FORCED_SOLID, solid), Block.UPDATE_ALL);
                }
            }
        });
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Only initialize once — if still at default values it hasn't been set up yet (worldgen)
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER
                && state.getValue(EMIT_LIGHT) == 3
                && state.getValue(REVEAL_RADIUS) == 3) {

            int randomLight = 3 + random.nextInt(8);
            int randomRadius = 2 + level.random.nextInt(4);
            BlockState newState = state.setValue(EMIT_LIGHT, randomLight).setValue(REVEAL_RADIUS, randomRadius);
            level.setBlock(pos, newState, 3);

            // Sync upper half
            BlockPos above = pos.above();
            BlockState upperState = level.getBlockState(above);
            if (upperState.is(this) && upperState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                level.setBlock(above, upperState
                        .setValue(EMIT_LIGHT, randomLight)
                        .setValue(REVEAL_RADIUS, randomRadius), 3);
            }

            forceRevealNearby(level, pos, randomRadius, true);
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT)
                || state.is(ModBlockTags.ENDER_NYLIUM)
                || state.getBlock() instanceof FarmBlock;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        // Always resolve to the lower half so we only ever drop one item
        BlockPos lowerPos = state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
        Block.popResource(level, lowerPos, new ItemStack(this.asItem()));
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }
}