package net.kitawa.more_stuff.blocks.custom.general;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class WeatheringLanternBlock extends Block implements SimpleWaterloggedBlock, WeatheringCopper {

    public static final MapCodec<WeatheringLanternBlock> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringLanternBlock::getAge),
                    propertiesCodec()
            ).apply(instance, WeatheringLanternBlock::new));

    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final WeatherState weatherState;

    @Override
    public MapCodec<WeatheringLanternBlock> codec() {
        return CODEC;
    }

    public WeatheringLanternBlock(WeatherState state, Properties properties) {
        super(properties);
        this.weatherState = state;

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HANGING, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public WeatherState getAge() {
        return weatherState;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HANGING, WATERLOGGED);
    }
}