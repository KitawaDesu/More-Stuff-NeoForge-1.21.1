package net.kitawa.more_stuff.blocks.custom.aquanda_biome_blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

public class WaterloggablePressurePlate extends BasePressurePlateBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<WaterloggablePressurePlate> CODEC = RecordCodecBuilder.mapCodec(
            p_308833_ -> p_308833_.group(BlockSetType.CODEC.fieldOf("block_set_type").forGetter(p_304917_ -> p_304917_.type), propertiesCodec())
                    .apply(p_308833_, WaterloggablePressurePlate::new)
    );
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    @Override
    public MapCodec<WaterloggablePressurePlate> codec() {
        return CODEC;
    }

    public WaterloggablePressurePlate(BlockSetType type, BlockBehaviour.Properties properties) {
        super(properties, type);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    /**
     * Returns the signal encoded in the given block state.
     */
    @Override
    protected int getSignalForState(BlockState state) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    /**
     * Returns the block state that encodes the given signal.
     */
    @Override
    protected BlockState setSignalForState(BlockState state, int strength) {
        return state.setValue(POWERED, Boolean.valueOf(strength > 0));
    }

    /**
     * Calculates what the signal strength of a pressure plate at the given location should be.
     */
    @Override
    protected int getSignalStrength(Level level, BlockPos pos) {
        Class<? extends Entity> oclass = switch (this.type.pressurePlateSensitivity()) {
            case EVERYTHING -> Entity.class;
            case MOBS -> LivingEntity.class;
        };
        return getEntityCount(level, TOUCH_AABB.move(pos), oclass) > 0 ? 15 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, WATERLOGGED);
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
