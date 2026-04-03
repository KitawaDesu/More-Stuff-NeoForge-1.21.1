package net.kitawa.more_stuff.worldgen.level.levelgen.feature.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.neoforged.fml.ModList;

import java.util.Optional;
import java.util.Random;

public class ConditionalBlockStateProvider extends BlockStateProvider {

    public static final Codec<ConditionalBlockStateProvider> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockStateProvider.CODEC.fieldOf("block").forGetter(cb -> cb.block),
                    Codec.STRING.optionalFieldOf("required_mod").forGetter(cb -> Optional.ofNullable(cb.requiredMod))
            ).apply(instance, ConditionalBlockStateProvider::new)
    );

    private final BlockStateProvider block;
    private final String requiredMod;

    public ConditionalBlockStateProvider(BlockStateProvider block) {
        this(block, Optional.empty());
    }

    public ConditionalBlockStateProvider(BlockStateProvider block, Optional<String> requiredMod) {
        super();
        this.block = block;
        this.requiredMod = requiredMod.orElse(null);
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.SIMPLE_STATE_PROVIDER;
    }

    /**
     * Vanilla call path (normal worldgen)
     */
    @Override
    public BlockState getState(RandomSource random, BlockPos pos) {
        return getState(random, pos, false);
    }

    /**
     * Debug-aware version used by NetherBambooFeatureConfiguration
     */
    public BlockState getState(RandomSource random, BlockPos pos, boolean debug) {

        // If debug is enabled, pretend all mods are loaded
        if (debug) {
            return block.getState(random, pos);
        }

        if (requiredMod == null || ModList.get().isLoaded(requiredMod)) {
            return block.getState(random, pos);
        }

        return null;
    }
}