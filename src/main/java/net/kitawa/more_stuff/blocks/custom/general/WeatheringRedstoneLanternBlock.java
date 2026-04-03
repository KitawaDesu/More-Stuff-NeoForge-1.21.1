package net.kitawa.more_stuff.blocks.custom.general;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public class WeatheringRedstoneLanternBlock extends RedstoneLanternBlock implements WeatheringCopper {

    public static final MapCodec<WeatheringRedstoneLanternBlock> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    WeatherState.CODEC.fieldOf("weathering_state")
                            .forGetter(WeatheringRedstoneLanternBlock::getAge),
                    propertiesCodec()
            ).apply(instance, WeatheringRedstoneLanternBlock::new));

    private final WeatherState weatherState;

    public WeatheringRedstoneLanternBlock(WeatherState state, Properties properties) {
        super(properties);
        this.weatherState = state;
    }

    @Override
    public MapCodec<? extends RedstoneLanternBlock> codec() {
        return CODEC;
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
}