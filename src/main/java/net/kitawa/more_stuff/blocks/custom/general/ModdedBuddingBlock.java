package net.kitawa.more_stuff.blocks.custom.general;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class ModdedBuddingBlock extends AmethystBlock {

    public static final MapCodec<ModdedBuddingBlock> CODEC = simpleCodec(ModdedBuddingBlock::new);
    private static final Direction[] DIRECTIONS = Direction.values();

    private final Block cluster;
    private final Block largeBud;
    private final Block mediumBud;
    private final Block smallBud;

    public ModdedBuddingBlock(Block cluster, Block largeBud, Block mediumBud, Block smallBud,
                              BlockBehaviour.Properties properties) {
        super(properties);
        this.cluster  = cluster;
        this.largeBud  = largeBud;
        this.mediumBud = mediumBud;
        this.smallBud  = smallBud;
    }

    // Needed for codec — falls back to no growth blocks; override if you need serialization
    public ModdedBuddingBlock(BlockBehaviour.Properties properties) {
        this(Blocks.AMETHYST_CLUSTER, Blocks.LARGE_AMETHYST_BUD,
                Blocks.MEDIUM_AMETHYST_BUD, Blocks.SMALL_AMETHYST_BUD, properties);
    }

    @Override
    public MapCodec<ModdedBuddingBlock> codec() {
        return CODEC;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(5) != 0) return;

        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        BlockPos target = pos.relative(direction);
        BlockState targetState = level.getBlockState(target);
        Block growInto = null;

        if (canClusterGrowAtState(targetState)) {
            growInto = smallBud;
        } else if (targetState.is(smallBud) && targetState.getValue(AmethystClusterBlock.FACING) == direction) {
            growInto = mediumBud;
        } else if (targetState.is(mediumBud) && targetState.getValue(AmethystClusterBlock.FACING) == direction) {
            growInto = largeBud;
        } else if (targetState.is(largeBud) && targetState.getValue(AmethystClusterBlock.FACING) == direction) {
            growInto = cluster;
        }

        if (growInto != null) {
            BlockState newState = growInto.defaultBlockState()
                    .setValue(AmethystClusterBlock.FACING, direction)
                    .setValue(AmethystClusterBlock.WATERLOGGED,
                            targetState.getFluidState().getType() == Fluids.WATER);
            level.setBlockAndUpdate(target, newState);
        }
    }

    public static boolean canClusterGrowAtState(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
    }
}