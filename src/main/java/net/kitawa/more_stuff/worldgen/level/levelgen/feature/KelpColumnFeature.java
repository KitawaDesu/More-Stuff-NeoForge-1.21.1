package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.KelpColumnFeatureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;

public class KelpColumnFeature extends Feature<KelpColumnFeatureConfiguration> {

    public KelpColumnFeature(Codec<KelpColumnFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<KelpColumnFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        KelpColumnFeatureConfiguration config = context.config();
        config.validate();

        BlockState bodyState = config.getBodyState();
        BlockState tipState = config.getTipState();

        // Pick starting Y
        int surfaceY = config.useOceanFloorHeightmap
                ? level.getHeight(Heightmap.Types.OCEAN_FLOOR, origin.getX(), origin.getZ())
                : origin.getY();
        BlockPos currentPos = new BlockPos(origin.getX(), surfaceY, origin.getZ());

        // ✅ Shift to nearest valid water position with jumping over existing tip/body
        int maxShift = 16; // max blocks to search
        boolean found = false;

        for (int i = 0; i <= maxShift; i++) {
            // try above
            BlockPos posAbove = currentPos.above(i);
            if (isValidWaterSpot(level, posAbove, bodyState, tipState)) {
                currentPos = posAbove;
                found = true;
                break;
            }

            // try below
            BlockPos posBelow = currentPos.below(i);
            if (isValidWaterSpot(level, posBelow, bodyState, tipState)) {
                currentPos = posBelow;
                found = true;
                break;
            }
        }

        if (!found) return false;

        int height = 1 + random.nextInt(config.maxHeight);
        int placedCount = 0;
        BlockPos lastPlacedBody = null;

        for (int step = 0; step < height; step++) {
            BlockState currentState = level.getBlockState(currentPos);
            BlockState belowState = level.getBlockState(currentPos.below());

            // ✅ Abort if block below is tip
            if (belowState.is(tipState.getBlock())) break;

            // Water checks
            boolean waterHere = currentState.getFluidState().is(Fluids.WATER);
            boolean waterAbove = level.getBlockState(currentPos.above()).getFluidState().is(Fluids.WATER);
            if (!waterHere || !waterAbove || !bodyState.canSurvive(level, currentPos)) break;

            // Place body
            level.setBlock(currentPos, bodyState, 2);
            lastPlacedBody = currentPos;
            placedCount++;
            currentPos = currentPos.above();
        }

        // Place tip on top
        if (lastPlacedBody != null) {
            BlockPos tipPos = lastPlacedBody.above();
            BlockState belowTip = level.getBlockState(tipPos.below());
            if (!belowTip.is(tipState.getBlock()) && tipState.canSurvive(level, tipPos)) {
                BlockState finalTip = tipState;
                if (tipState.hasProperty(KelpBlock.AGE)) {
                    finalTip = finalTip.setValue(KelpBlock.AGE, random.nextInt(4) + 20);
                }
                level.setBlock(tipPos, finalTip, 2);
                placedCount++;
            }
        }

        return placedCount > 0;
    }

    /**
     * Returns true if the position is a valid water spot for kelp placement:
     * - Current block is water
     * - Block above is water
     * - Block below has sturdy top face
     * - Current block is NOT already body or tip
     */
    private boolean isValidWaterSpot(WorldGenLevel level, BlockPos pos, BlockState bodyState, BlockState tipState) {
        BlockState current = level.getBlockState(pos);
        BlockState above = level.getBlockState(pos.above());
        BlockState below = level.getBlockState(pos.below());

        // Must be water here and above
        if (!current.getFluidState().is(Fluids.WATER)) return false;
        if (!above.getFluidState().is(Fluids.WATER)) return false;

        // Block below must have sturdy top face
        if (!below.isFaceSturdy(level, pos.below(), Direction.UP)) return false;

        // Avoid placing inside existing kelp body or tip
        if (current.is(bodyState.getBlock()) || current.is(tipState.getBlock())) return false;

        // Also avoid if block below is body or tip
        if (below.is(bodyState.getBlock()) || below.is(tipState.getBlock())) return false;

        return true;
    }
}