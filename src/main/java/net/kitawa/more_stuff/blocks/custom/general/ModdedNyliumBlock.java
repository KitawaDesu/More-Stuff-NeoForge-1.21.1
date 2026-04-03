package net.kitawa.more_stuff.blocks.custom.general;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.lighting.LightEngine;

import java.util.Optional;

public class ModdedNyliumBlock extends Block implements BonemealableBlock {

    private final ResourceKey<ConfiguredFeature<?, ?>> vegetationFeature;
    private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryFeature;
    private final Block netherrackblock;

    public ModdedNyliumBlock(
            ResourceKey<ConfiguredFeature<?, ?>> vegetationFeature,
            Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryFeature,
            Block netherrackBlock,
            Properties properties) {
        super(properties);
        this.vegetationFeature = vegetationFeature;
        this.secondaryFeature = secondaryFeature;
        this.netherrackblock = netherrackBlock;
    }

    // convenience constructor for no secondary feature
    public ModdedNyliumBlock(
            ResourceKey<ConfiguredFeature<?, ?>> vegetationFeature,
            Block netherrackBlock,
            Properties properties) {
        this(vegetationFeature, Optional.empty(), netherrackBlock, properties);
    }

    private static boolean canBeNylium(BlockState state, LevelReader reader, BlockPos pos) {
        BlockPos above = pos.above();
        BlockState aboveState = reader.getBlockState(above);
        int lightBlocked = LightEngine.getLightBlockInto(reader, state, pos, aboveState, above, Direction.UP, aboveState.getLightBlock(reader, above));
        return lightBlocked < reader.getMaxLightLevel();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canBeNylium(state, level, pos)) {
            level.setBlockAndUpdate(pos, this.netherrackblock.defaultBlockState());
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return level.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos abovePos = pos.above();
        var registry = level.registryAccess().registry(Registries.CONFIGURED_FEATURE);

        registry.flatMap(reg -> reg.getHolder(vegetationFeature))
                .ifPresent(holder -> holder.value().place(level, level.getChunkSource().getGenerator(), random, abovePos));

        secondaryFeature.ifPresent(key ->
                registry.flatMap(reg -> reg.getHolder(key))
                        .ifPresent(holder -> holder.value().place(level, level.getChunkSource().getGenerator(), random, abovePos)));
    }

    @Override
    public Type getType() {
        return Type.NEIGHBOR_SPREADER;
    }
}
