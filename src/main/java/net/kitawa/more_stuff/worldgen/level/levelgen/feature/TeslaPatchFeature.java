package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.blocks.custom.electricity.OmniBlock;
import net.kitawa.more_stuff.blocks.custom.electricity.TeslaCoilBlock;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.TeslaPatchConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.List;

public class TeslaPatchFeature extends Feature<TeslaPatchConfig> {
    public TeslaPatchFeature(Codec<TeslaPatchConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<TeslaPatchConfig> context) {
        LevelAccessor level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();
        TeslaPatchConfig config = context.config();

        if (!(level instanceof ServerLevel server)) return false;

        // Collect all block placements
        List<Pair<BlockPos, BlockState>> placements = new ArrayList<>();

        // --- Step 1: Place the central anchor at bottom ---
        BlockPos centerAnchorPos = pos;
        BlockState anchorState = ModBlocks.ANCHOR_BLOCK.get().defaultBlockState();
        placements.add(Pair.of(centerAnchorPos, anchorState));

        // --- Step 2: Place the Tesla Coil on top ---
        BlockPos teslaPos = centerAnchorPos.above();
        BlockState teslaState = ModBlocks.TESLA_COIL.get()
                .defaultBlockState()
                .setValue(TeslaCoilBlock.TOP, false);
        placements.add(Pair.of(teslaPos, teslaState));

        // --- Step 3: Place optional surrounding anchors ---
        List<Direction> sides = List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

        for (Direction side : sides) {
            if (random.nextFloat() <= config.sideAnchorChance) {
                BlockPos sidePos = centerAnchorPos.relative(side);
                placements.add(Pair.of(sidePos, anchorState));

                // --- Optional stormvein on top of side anchor ---
                if (random.nextFloat() <= config.stormveinChance) {
                    BlockPos stormveinPos = sidePos.above();
                    BlockState stormveinState = ModBlocks.STORMVEIN.get().defaultBlockState()
                            .setValue(OmniBlock.ANCHOR_DOWN, true)
                            .setValue(OmniBlock.CORE, true);

                    // Set directional arm property based on side
                    switch (side) {
                        case NORTH -> stormveinState = stormveinState.setValue(OmniBlock.ANCHOR_NORTH, true);
                        case SOUTH -> stormveinState = stormveinState.setValue(OmniBlock.ANCHOR_SOUTH, true);
                        case EAST  -> stormveinState = stormveinState.setValue(OmniBlock.ANCHOR_EAST, true);
                        case WEST  -> stormveinState = stormveinState.setValue(OmniBlock.ANCHOR_WEST, true);
                        default -> { /* no action needed */ }
                    }

                    placements.add(Pair.of(stormveinPos, stormveinState));
                }
            }
        }

        // --- Step 4: Apply all placements shifted down by 1 ---
        for (Pair<BlockPos, BlockState> pair : placements) {
            BlockPos shiftedPos = pair.getFirst().below();
            level.setBlock(shiftedPos, pair.getSecond(), 3);
        }

        return true;
    }
}