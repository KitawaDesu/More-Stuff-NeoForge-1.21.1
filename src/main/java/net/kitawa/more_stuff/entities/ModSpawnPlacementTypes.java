package net.kitawa.more_stuff.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

import javax.annotation.Nullable;

public interface ModSpawnPlacementTypes {

    /**
     * Allows spawning:
     *  ✔ ON GROUND (normal vanilla behavior)
     *  ✔ UNDERWATER *if there is solid ground beneath*
     *
     *  Not allowed:
     *  ✖ Floating in water
     *  ✖ Floating in air
     */
    SpawnPlacementType ON_GROUND_OR_IN_WATER = new SpawnPlacementType() {

        @Override
        public boolean isSpawnPositionOk(LevelReader level, BlockPos pos, @Nullable EntityType<?> entityType) {
            if (entityType == null || !level.getWorldBorder().isWithinBounds(pos)) {
                return false;
            }

            BlockPos above = pos.above();
            BlockPos below = pos.below();

            BlockState belowState = level.getBlockState(below);

            // ---- Must ALWAYS have solid ground under spawn ----
            boolean hasSolidGround = belowState.isValidSpawn(level, below, entityType);
            if (!hasSolidGround) {
                return false;
            }

            // ---- Case 1: Normal ON_GROUND spawn (block above must be empty) ----
            boolean airAbove = NaturalSpawner.isValidEmptySpawnBlock(
                    level, above,
                    level.getBlockState(above),
                    level.getFluidState(above),
                    entityType
            );

            if (airAbove) {
                return true;
            }

            // ---- Case 2: Underwater spawn ----
            boolean isWaterAtPos = level.getFluidState(pos).is(FluidTags.WATER);
            if (isWaterAtPos) {
                boolean emptyEnough = NaturalSpawner.isValidEmptySpawnBlock(
                        level, pos,
                        level.getBlockState(pos),
                        level.getFluidState(pos),
                        entityType
                );
                return emptyEnough;
            }

            return false;
        }

        @Override
        public BlockPos adjustSpawnPosition(LevelReader level, BlockPos pos) {
            // Same logic as vanilla ON_GROUND
            BlockPos below = pos.below();
            return level.getBlockState(below).isPathfindable(PathComputationType.LAND)
                    ? below
                    : pos;
        }
    };
}