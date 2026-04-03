package net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.util.TriState;

import java.util.function.Supplier;

public class ModdedBambooSaplingBlock extends Block implements BonemealableBlock {
    public static final MapCodec<ModdedBambooSaplingBlock> CODEC = simpleCodec(ModdedBambooSaplingBlock::new);
    protected static final VoxelShape SAPLING_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 12.0, 12.0);

    private final Supplier<Block> stalk;

    @Override
    public MapCodec<ModdedBambooSaplingBlock> codec() { return CODEC; }

    public ModdedBambooSaplingBlock(Supplier<Block> stalk, BlockBehaviour.Properties properties) {
        super(properties);
        this.stalk = stalk;
    }

    // Codec fallback
    public ModdedBambooSaplingBlock(BlockBehaviour.Properties properties) {
        this(() -> Blocks.BAMBOO, properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(level, pos);
        return SAPLING_SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(3) == 0 && level.isEmptyBlock(pos.above())
                && level.getRawBrightness(pos.above(), 0) >= 0) {
            growBamboo(level, pos);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        TriState soilDecision = level.getBlockState(pos.below())
                .canSustainPlant(level, pos.below(), Direction.UP, state);
        if (!soilDecision.isDefault()) return soilDecision.isTrue();
        return level.getBlockState(pos.below()).is(BlockTags.BAMBOO_PLANTABLE_ON);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState,
                                     LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (!state.canSurvive(level, currentPos)) return Blocks.AIR.defaultBlockState();

        if (facing == Direction.UP && facingState.is(stalk.get())) {
            level.setBlock(currentPos, stalk.get().defaultBlockState(), 2);
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
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
        growBamboo(level, pos);
    }

    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return player.getMainHandItem().canPerformAction(ItemAbilities.SWORD_DIG)
                ? 1.0F : super.getDestroyProgress(state, player, level, pos);
    }

    protected void growBamboo(Level level, BlockPos pos) {
        level.setBlock(pos.above(), stalk.get().defaultBlockState()
                .setValue(BambooStalkBlock.LEAVES, BambooLeaves.SMALL), 3);
    }
}