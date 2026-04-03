package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kitawa.more_stuff.worldgen.tree.ModTrunkPlacers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.*;
import java.util.function.BiConsumer;

public class HybernatusTrunkPlacer extends TrunkPlacer {

    public static final MapCodec<HybernatusTrunkPlacer> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    trunkPlacerParts(instance)
                            .and(instance.group(
                                    Codec.INT.fieldOf("max_spread").forGetter(p -> p.maxSpread),
                                    Codec.FLOAT.fieldOf("branch_chance").forGetter(p -> p.branchChance),
                                    Codec.FLOAT.fieldOf("branch_recursion_chance").forGetter(p -> p.branchRecursionChance),
                                    Codec.INT.fieldOf("trunk_grow_before_branch").forGetter(p -> p.trunkGrowBeforeBranch)
                            ))
                            .apply(instance, HybernatusTrunkPlacer::new)
            );

    private final int maxSpread;
    private final float branchChance;
    private final float branchRecursionChance;
    private final int trunkGrowBeforeBranch;

    public HybernatusTrunkPlacer(
            int baseHeight, int heightRandA, int heightRandB,
            int maxSpread, float branchChance, float branchRecursionChance,
            int trunkGrowBeforeBranch
    ) {
        super(baseHeight, heightRandA, heightRandB);
        this.maxSpread = maxSpread;
        this.branchChance = branchChance;
        this.branchRecursionChance = branchRecursionChance;
        this.trunkGrowBeforeBranch = trunkGrowBeforeBranch;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacers.HYBERNATUS_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(
            LevelSimulatedReader world,
            BiConsumer<BlockPos, BlockState> setter,
            RandomSource random,
            int height,
            BlockPos pos,
            TreeConfiguration config
    ) {

        List<FoliagePlacer.FoliageAttachment> foliage = new ArrayList<>();
        Set<BlockPos> endpoints = new HashSet<>();

        BlockPos current = pos;

// FIX: place the very first trunk block at the base
        placeLog(world, setter, random, current, config);

/* --------------------------
   1. INITIAL TRUNK GROWTH
   -------------------------- */
        for (int i = 0; i < trunkGrowBeforeBranch && i < height; i++) {
            BlockPos up = current.above();
            if (!world.isStateAtPosition(up, s -> s.isAir()))
                break;

            placeLog(world, setter, random, up, config);
            current = up;
        }

        endpoints.add(current);

        /* --------------------------
           2. CHAOTIC LEVEL-BY-LEVEL GROWTH
           -------------------------- */
        for (int y = trunkGrowBeforeBranch; y < height; y++) {
            Set<BlockPos> nextEndpoints = new HashSet<>();

            for (BlockPos tip : endpoints) {

                // Continue trunk upward
                BlockPos up = tip.above();
                if (world.isStateAtPosition(up, s -> s.isAir())) {
                    placeLog(world, setter, random, up, config);
                    nextEndpoints.add(up);
                }

                // Attempt branch creation
                List<Direction> dirs = new ArrayList<>(List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST));
                Collections.shuffle(dirs, new java.util.Random(random.nextLong()));

                int attempts = 1 + random.nextInt(2); // 1 or 2 directions
                for (int i = 0; i < attempts; i++) {
                    Direction dir = dirs.get(i);
                    if (random.nextFloat() < branchChance) {
                        // call the version that collects foliage attachments
                        BlockPos branchEnd = createBranch(world, setter, random, tip, dir, config, foliage, 0);
                        if (branchEnd != null) {
                            // createBranch already adds foliage for sub-branches, but keep this to ensure main-branch endpoint gets leaves
                            foliage.add(new FoliagePlacer.FoliageAttachment(branchEnd.above(), 0, false));
                        }
                    }
                }
            }

            endpoints = nextEndpoints;
        }

        // Foliage at top trunk tips
        for (BlockPos p : endpoints) {
            foliage.add(new FoliagePlacer.FoliageAttachment(p.above(), 0, false));
        }

        return foliage;
    }


    /* ---------------------------------------------------------
       FIXED: Branches now grow relative to their origin, not
              the base trunk. This allows >1 block growth.
       --------------------------------------------------------- */
    private BlockPos createBranch(
            LevelSimulatedReader world,
            BiConsumer<BlockPos, BlockState> setter,
            RandomSource random,
            BlockPos origin,
            Direction dir,
            TreeConfiguration config,
            List<FoliagePlacer.FoliageAttachment> foliage,
            int recursion
    ) {

        BlockPos current = origin;

        int length = 2 + random.nextInt(3);

        for (int i = 0; i < length; i++) {
            BlockPos next = current.relative(dir);

            if (next.distManhattan(origin) > maxSpread)
                return null;

            if (!world.isStateAtPosition(next, s -> s.isAir()))
                return null;

            placeLog(world, setter, random, next, config);
            current = next;
        }

        // upward turn
        BlockPos up = current.above();
        if (world.isStateAtPosition(up, s -> s.isAir())) {
            placeLog(world, setter, random, up, config);
            current = up;
        }

        // foliage at the end of this branch
        foliage.add(new FoliagePlacer.FoliageAttachment(current.above(), 0, false));

        /* recursive sub-branches */
        float chance = branchRecursionChance * (float) Math.pow(0.7, recursion);

        if (chance > 0.05f) {

            List<Direction> dirs = new ArrayList<>(List.of(
                    Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST
            ));
            Collections.shuffle(dirs, new Random(random.nextLong()));

            for (Direction newDir : dirs) {
                if (newDir == dir.getOpposite()) continue; // prevent backtracking
                if (random.nextFloat() < chance) {
                    createBranch(world, setter, random, current, newDir, config, foliage, recursion + 1);
                }
            }
        }

        return current;
    }
}