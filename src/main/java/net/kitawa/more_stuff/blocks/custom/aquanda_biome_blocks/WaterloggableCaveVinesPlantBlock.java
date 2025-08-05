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

public class WaterloggableCaveVinesPlantBlock extends GrowingPlantBodyBlock implements BonemealableBlock, WaterloggableVines, SimpleWaterloggedBlock {
    public static final MapCodec<WaterloggableCaveVinesPlantBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    BuiltInRegistries.BLOCK.holderByNameCodec().fieldOf("growing_plant_head_block").forGetter(p_304365_ -> p_304365_.growingplantheadblock.builtInRegistryHolder()),
                    propertiesCodec()
            ).apply(instance, (blockHolder, properties) -> new WaterloggableCaveVinesPlantBlock(blockHolder.value(), properties))
    );
    private final Block growingplantheadblock;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    @Override
    public MapCodec<WaterloggableCaveVinesPlantBlock> codec() {
        return CODEC;
    }

    public WaterloggableCaveVinesPlantBlock(Block growingplantheadblock, Properties p_153000_) {
        super(p_153000_, Direction.DOWN, SHAPE, false);
        this.registerDefaultState(this.stateDefinition.any().setValue(BERRIES, Boolean.valueOf(false)).setValue(WATERLOGGED, Boolean.valueOf(false)));
        this.growingplantheadblock = growingplantheadblock;
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock)this.growingplantheadblock;
    }

    @Override
    protected BlockState updateHeadAfterConvertedFromBody(BlockState oldState, BlockState newState) {
        return newState
                .setValue(BERRIES, oldState.getValue(BERRIES))
                .setValue(WATERLOGGED, oldState.getValue(WATERLOGGED));
    }


    @Override
    public ItemStack getCloneItemStack(LevelReader p_304444_, BlockPos p_153008_, BlockState p_153009_) {
        return new ItemStack(ModItems.AQUANDA_BERRIES.get());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player entity, BlockHitResult blockHitResult) {
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_153031_) {
        p_153031_.add(BERRIES, WATERLOGGED);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader p_255942_, BlockPos p_153012_, BlockState p_153013_) {
        return !p_153013_.getValue(BERRIES);
    }

    @Override
    public boolean isBonemealSuccess(Level p_220943_, RandomSource p_220944_, BlockPos p_220945_, BlockState p_220946_) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel p_220938_, RandomSource p_220939_, BlockPos p_220940_, BlockState p_220941_) {
        p_220938_.setBlock(p_220940_, p_220941_.setValue(BERRIES, Boolean.valueOf(true)), 2);
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
