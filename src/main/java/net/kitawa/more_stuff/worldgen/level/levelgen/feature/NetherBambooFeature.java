package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.NetherBambooFeatureConfiguration;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Random;

public class NetherBambooFeature extends Feature<NetherBambooFeatureConfiguration> {

    public NetherBambooFeature(Codec<NetherBambooFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NetherBambooFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        NetherBambooFeatureConfiguration config = context.config();

        BlockPos.MutableBlockPos pos = origin.mutable();
        BlockPos.MutableBlockPos below = origin.mutable().move(Direction.DOWN);

        // Pick a shoot block from the list
        BlockState shootBaseState = config.pickShootBlock(random, below);
        if (shootBaseState == null) return false;

        // ensure first block above is air
        if (!level.isEmptyBlock(pos)) {
            return false;
        }

        int height = random.nextInt(12) + 5;

        // === Place ground block directly below shoot (must pass RuleTest AND air above) ===
        BlockState belowState = level.getBlockState(below);

        // ⭐ FIX: Only place ground if block above is air
        if (!level.getBlockState(origin).isAir()) return false;

        if (config.groundCanReplace.test(belowState, random)) {
            BlockState centerGroundState = config.pickGroundBlock(random, below);
            if (centerGroundState == null) return false;
            level.setBlock(below, centerGroundState, 2);
        } else {
            return false;
        }

        // recheck survival
        if (!shootBaseState.canSurvive(level, pos)) {
            return false;
        }

        // === Replace surrounding ground probabilistically ===
        int radius = random.nextInt(4) + 1;
        for (int x = origin.getX() - radius; x <= origin.getX() + radius; x++) {
            for (int z = origin.getZ() - radius; z <= origin.getZ() + radius; z++) {
                int dx = x - origin.getX();
                int dz = z - origin.getZ();
                if (dx * dx + dz * dz <= radius * radius) {
                    BlockPos checkPos = new BlockPos(x, below.getY(), z);
                    if (checkPos.equals(below)) continue;

                    BlockState targetState = level.getBlockState(checkPos);

                    if (random.nextFloat() < config.probability && config.groundCanReplace.test(targetState, random)) {

                        // ⭐ FIX: skip if ABOVE block is not air
                        if (!level.getBlockState(checkPos.above()).isAir()) {
                            continue;
                        }

                        BlockState groundState = config.pickGroundBlock(random, checkPos);
                        if (groundState != null) {
                            level.setBlock(checkPos, groundState, 2);
                        }
                    }
                }
            }
        }

        // === Build stalk upwards ===
        int built = 0;
        while (built < height && level.isEmptyBlock(pos)) {
            if (level.getBlockState(pos.above()).isSolid()) break;

            level.setBlock(pos, shootBaseState
                    .setValue(BambooStalkBlock.AGE, 1)
                    .setValue(BambooStalkBlock.LEAVES, BambooLeaves.NONE)
                    .setValue(BambooStalkBlock.STAGE, 0), 2);

            pos.move(Direction.UP);
            built++;
        }

        if (built < 4) {
            for (int i = 0; i < built; i++) {
                pos.move(Direction.DOWN);
                level.removeBlock(pos, false);
            }
            return false;
        }

        // === Add leafy top ===
        pos.move(Direction.DOWN);
        level.setBlock(pos, shootBaseState
                .setValue(BambooStalkBlock.AGE, 1)
                .setValue(BambooStalkBlock.LEAVES, BambooLeaves.LARGE)
                .setValue(BambooStalkBlock.STAGE, 1), 2);
        pos.move(Direction.DOWN);
        level.setBlock(pos, shootBaseState
                .setValue(BambooStalkBlock.AGE, 1)
                .setValue(BambooStalkBlock.LEAVES, BambooLeaves.LARGE), 2);
        pos.move(Direction.DOWN);
        level.setBlock(pos, shootBaseState
                .setValue(BambooStalkBlock.AGE, 1)
                .setValue(BambooStalkBlock.LEAVES, BambooLeaves.SMALL), 2);

        return true;
    }
}
