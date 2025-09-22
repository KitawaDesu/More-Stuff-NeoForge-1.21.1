package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.VoltaicPatchConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

public class VoltaicPatch extends Feature<VoltaicPatchConfig> {
    public VoltaicPatch(Codec<VoltaicPatchConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<VoltaicPatchConfig> ctx) {
        WorldGenLevel level = ctx.level();
        RandomSource random = ctx.random();
        BlockPos origin = ctx.origin();
        VoltaicPatchConfig config = ctx.config();

        Predicate<BlockState> replaceable = state ->
                state.isAir() || state.getFluidState().isSource() || state.is(config.replaceable);

        int radius = config.radius.sample(random) + 2; // make bigger
        Set<BlockPos> positions = placeCoreBlob(level, config, random, origin, replaceable, radius);
        positions.addAll(placeTendrils(level, config, random, positions, config.tendrilCount.sample(random), config.tendrilLength.sample(random), replaceable));

        return !positions.isEmpty();
    }

    private Set<BlockPos> placeCoreBlob(WorldGenLevel level, VoltaicPatchConfig config, RandomSource random,
                                        BlockPos origin, Predicate<BlockState> replaceable, int radius) {
        Set<BlockPos> positions = new HashSet<>();
        BlockPos.MutableBlockPos mutable = origin.mutable();
        SimplexNoise noise = new SimplexNoise(random);

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                double distSq = dx * dx + dz * dz;
                if (distSq <= radius * radius && random.nextFloat() < 0.8F - distSq / (float)(radius*radius)) {
                    mutable.setWithOffset(origin, dx, 0, dz);

                    int heightOffset = (int)Math.round(noise.getValue(mutable.getX() * 0.2, mutable.getZ() * 0.2) * 3);
                    mutable.move(Direction.UP, heightOffset);

                    // Find nearest full block in the tag to push into
                    BlockPos target = findNearestTaggedBlock(level, mutable, config.replaceable);
                    if (target != null && isNextToAirOrFluid(level, target)) {
                        if (tryPlace(level, config, random, target)) {
                            positions.add(target.immutable());
                        }
                    }
                }
            }
        }
        return positions;
    }

    private Set<BlockPos> placeTendrils(WorldGenLevel level, VoltaicPatchConfig config, RandomSource random,
                                        Set<BlockPos> core, int tendrilCount, int maxLength, Predicate<BlockState> replaceable) {
        Set<BlockPos> positions = new HashSet<>();
        if (core.isEmpty()) return positions;

        for (int i = 0; i < tendrilCount; i++) {
            BlockPos start = core.stream().skip(random.nextInt(core.size())).findFirst().orElse(null);
            if (start == null) continue;

            Direction dir = Direction.getRandom(random);
            BlockPos current = start;

            for (int step = 0; step < maxLength; step++) {
                current = current.relative(dir);
                BlockPos target = findNearestTaggedBlock(level, current, config.replaceable);

                if (target != null && isNextToAirOrFluid(level, target) && tryPlace(level, config, random, target)) {
                    positions.add(target.immutable());

                    if (random.nextFloat() < 0.2F) {
                        Direction branch = Direction.getRandom(random);
                        BlockPos branchPos = target.relative(branch);
                        BlockPos branchTarget = findNearestTaggedBlock(level, branchPos, config.replaceable);
                        if (branchTarget != null && isNextToAirOrFluid(level, branchTarget) &&
                                tryPlace(level, config, random, branchTarget)) {
                            positions.add(branchTarget.immutable());
                        }
                    }
                }

                if (random.nextFloat() < 0.3F) dir = Direction.getRandom(random);
            }
        }
        return positions;
    }

    private BlockPos findNearestTaggedBlock(WorldGenLevel level, BlockPos startPos, TagKey<Block> tag) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            if (level.getBlockState(current).is(tag)) {
                return current;
            }

            // Add all 6 directions to the queue
            for (Direction dir : Direction.values()) {
                BlockPos neighbor = current.relative(dir);
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return null; // nothing found
    }

    private boolean isNextToAirOrFluid(WorldGenLevel level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockState state = level.getBlockState(pos.relative(dir));
            if (state.isAir() || state.getFluidState().isSource()) return true;
        }
        return false;
    }

    private boolean tryPlace(WorldGenLevel level, VoltaicPatchConfig config, RandomSource random, BlockPos pos) {
        BlockState state = config.block.getState(random, pos);
        level.setBlock(pos, state, 2);
        return true;
    }
}