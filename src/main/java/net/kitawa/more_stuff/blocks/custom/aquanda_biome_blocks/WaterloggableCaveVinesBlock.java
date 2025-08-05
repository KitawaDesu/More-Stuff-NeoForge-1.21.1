package net.kitawa.more_stuff.blocks.custom.aquanda_biome_blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.blocks.util.WaterloggableVines;
import net.kitawa.more_stuff.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class WaterloggableCaveVinesBlock extends GrowingPlantHeadBlock implements BonemealableBlock, WaterloggableVines, SimpleWaterloggedBlock {
    public static final MapCodec<WaterloggableCaveVinesBlock> CODEC = simpleCodec(WaterloggableCaveVinesBlock::new);
    private static final float CHANCE_OF_BERRIES_ON_GROWTH = 0.11F;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    @Override
    public MapCodec<WaterloggableCaveVinesBlock> codec() {
        return CODEC;
    }

    public WaterloggableCaveVinesBlock(Properties p_152959_) {
        super(p_152959_, Direction.DOWN, SHAPE, false, 0.1);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)).setValue(BERRIES, Boolean.valueOf(false)).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource p_220928_) {
        return 1;
    }

    @Override
    protected boolean canGrowInto(BlockState p_152998_) {
        return p_152998_.isAir() || p_152998_.is(Blocks.WATER);
    }

    @Override
    protected Block getBodyBlock() {
        return ModBlocks.AQUANDA_VINES_PLANT.get();
    }

    @Override
    protected BlockState updateBodyAfterConvertedFromHead(BlockState fromState, BlockState toState) {
        return toState
                .setValue(BERRIES, fromState.getValue(BERRIES))
                .setValue(WATERLOGGED, fromState.getValue(WATERLOGGED));
    }

    @Override
    protected BlockState getGrowIntoState(BlockState currentState, RandomSource random) {
        boolean isTargetWater = currentState.getFluidState().getType() == Fluids.WATER;

        return super.getGrowIntoState(currentState, random)
                .setValue(BERRIES, random.nextFloat() < CHANCE_OF_BERRIES_ON_GROWTH)
                .setValue(WATERLOGGED, isTargetWater);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader p_304590_, BlockPos p_152967_, BlockState p_152968_) {
        return new ItemStack(ModItems.AQUANDA_BERRIES.get());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player entity, BlockHitResult blockHitResultr) {
        if (state.getValue(BERRIES).equals(Boolean.TRUE)) {
            Block.popResource(level, pos, new ItemStack(ModItems.AQUANDA_BERRIES.get(), 1));
            float f = Mth.randomBetween(level.random, 0.8F, 1.2F);
            level.playSound(null, pos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, f);
            BlockState blockstate = state.setValue(BERRIES, Boolean.valueOf(false));
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, blockstate));
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_152993_) {
        super.createBlockStateDefinition(p_152993_);
        p_152993_.add(BERRIES, WATERLOGGED);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader p_256026_, BlockPos p_152971_, BlockState p_152972_) {
        return !p_152972_.getValue(BERRIES);
    }

    @Override
    public boolean isBonemealSuccess(Level p_220930_, RandomSource p_220931_, BlockPos p_220932_, BlockState p_220933_) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel p_220923_, RandomSource p_220924_, BlockPos p_220925_, BlockState p_220926_) {
        p_220923_.setBlock(p_220925_, p_220926_.setValue(BERRIES, Boolean.valueOf(true)), 2);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        return SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return SimpleWaterloggedBlock.super.canPlaceLiquid(player, level, pos, state, fluid);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }
}
