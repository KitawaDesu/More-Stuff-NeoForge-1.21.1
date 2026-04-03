package net.kitawa.more_stuff.blocks.custom.nether.metallic_forest_blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.util.TriState;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ModdedBambooStalkBlock extends Block implements BonemealableBlock {
    public static final MapCodec<ModdedBambooStalkBlock> CODEC = simpleCodec(ModdedBambooStalkBlock::new);

    private final Supplier<Block> sapling;
    private final Supplier<Block> stalkSelf;

    protected static final VoxelShape SMALL_SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
    protected static final VoxelShape LARGE_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    protected static final VoxelShape COLLISION_SHAPE = Block.box(6.5, 0.0, 6.5, 9.5, 16.0, 9.5);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    public static final EnumProperty<BambooLeaves> LEAVES = BlockStateProperties.BAMBOO_LEAVES;
    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
    public static final int MAX_HEIGHT = 16;

    @Override
    public MapCodec<ModdedBambooStalkBlock> codec() { return CODEC; }

    public ModdedBambooStalkBlock(Supplier<Block> sapling, Supplier<Block> stalkSelf,
                                  BlockBehaviour.Properties properties) {
        super(properties);
        this.sapling = sapling;
        this.stalkSelf = stalkSelf;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 0).setValue(LEAVES, BambooLeaves.NONE).setValue(STAGE, 0));
    }

    public ModdedBambooStalkBlock(BlockBehaviour.Properties properties) {
        this(() -> Blocks.BAMBOO_SAPLING, () -> Blocks.BAMBOO, properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, LEAVES, STAGE);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = state.getValue(LEAVES) == BambooLeaves.LARGE ? LARGE_SHAPE : SMALL_SHAPE;
        Vec3 vec3 = state.getOffset(level, pos);
        return shape.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(level, pos);
        return COLLISION_SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) { return false; }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        if (!fluid.isEmpty()) return null;

        BlockState below = context.getLevel().getBlockState(context.getClickedPos().below());
        TriState soilDecision = below.canSustainPlant(context.getLevel(),
                context.getClickedPos().below(), Direction.UP, this.defaultBlockState());

        if (soilDecision.isDefault() ? below.is(BlockTags.BAMBOO_PLANTABLE_ON) : soilDecision.isTrue()) {
            if (below.is(sapling.get())) {
                return this.defaultBlockState().setValue(AGE, 0);
            } else if (below.is(stalkSelf.get())) {
                return this.defaultBlockState().setValue(AGE, below.getValue(AGE) > 0 ? 1 : 0);
            } else {
                BlockState above = context.getLevel().getBlockState(context.getClickedPos().above());
                return above.is(stalkSelf.get())
                        ? this.defaultBlockState().setValue(AGE, above.getValue(AGE))
                        : sapling.get().defaultBlockState();
            }
        }
        return null;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) level.destroyBlock(pos, true);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) { return state.getValue(STAGE) == 0; }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(STAGE) == 0 && level.isEmptyBlock(pos.above())
                && level.getRawBrightness(pos.above(), 0) >= 0) {
            int height = getHeightBelowUpToMax(level, pos) + 1;
            if (height < MAX_HEIGHT && CommonHooks.canCropGrow(level, pos, state, random.nextInt(3) == 0)) {
                growBamboo(state, level, pos, random, height);
                CommonHooks.fireCropGrowPost(level, pos, state);
            }
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
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) level.scheduleTick(pos, this, 1);

        if (direction == Direction.UP && neighborState.is(stalkSelf.get())
                && neighborState.getValue(AGE) > state.getValue(AGE)) {
            level.setBlock(pos, state.cycle(AGE), 2);
        }
        if (direction == Direction.UP && neighborState.is(sapling.get())) {
            level.setBlock(pos.above(), stalkSelf.get().defaultBlockState(), 2);
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        int above = getHeightAboveUpToMax(level, pos);
        int below = getHeightBelowUpToMax(level, pos);
        return above + below + 1 < MAX_HEIGHT
                && level.getBlockState(pos.above(above)).getValue(STAGE) != 1;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int above = getHeightAboveUpToMax(level, pos);
        int below = getHeightBelowUpToMax(level, pos);
        int total = above + below + 1;
        int growth = 1 + random.nextInt(2);

        for (int i = 0; i < growth; i++) {
            BlockPos topPos = pos.above(above);
            BlockState topState = level.getBlockState(topPos);
            if (total >= MAX_HEIGHT || topState.getValue(STAGE) == 1 || !level.isEmptyBlock(topPos.above())) return;
            growBamboo(topState, level, topPos, random, total);
            above++;
            total++;
        }
    }

    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return player.getMainHandItem().canPerformAction(ItemAbilities.SWORD_DIG)
                ? 1.0F : super.getDestroyProgress(state, player, level, pos);
    }

    protected void growBamboo(BlockState state, Level level, BlockPos pos, RandomSource random, int age) {
        BlockState below = level.getBlockState(pos.below());
        BlockPos below2Pos = pos.below(2);
        BlockState below2 = level.getBlockState(below2Pos);
        BambooLeaves leaves = BambooLeaves.NONE;

        if (age >= 1) {
            if (!below.is(stalkSelf.get()) || below.getValue(LEAVES) == BambooLeaves.NONE) {
                leaves = BambooLeaves.SMALL;
            } else if (below.is(stalkSelf.get()) && below.getValue(LEAVES) != BambooLeaves.NONE) {
                leaves = BambooLeaves.LARGE;
                if (below2.is(stalkSelf.get())) {
                    level.setBlock(pos.below(), below.setValue(LEAVES, BambooLeaves.SMALL), 3);
                    level.setBlock(below2Pos, below2.setValue(LEAVES, BambooLeaves.NONE), 3);
                }
            }
        }

        int newAge = state.getValue(AGE) != 1 && !below2.is(stalkSelf.get()) ? 0 : 1;
        int stage = (age < 11 || !(random.nextFloat() < 0.25F)) && age != 15 ? 0 : 1;
        level.setBlock(pos.above(), this.defaultBlockState()
                .setValue(AGE, newAge).setValue(LEAVES, leaves).setValue(STAGE, stage), 3);
    }

    protected int getHeightAboveUpToMax(BlockGetter level, BlockPos pos) {
        int i = 0;
        while (i < MAX_HEIGHT && level.getBlockState(pos.above(i + 1)).is(stalkSelf.get())) i++;
        return i;
    }

    protected int getHeightBelowUpToMax(BlockGetter level, BlockPos pos) {
        int i = 0;
        while (i < MAX_HEIGHT && level.getBlockState(pos.below(i + 1)).is(stalkSelf.get())) i++;
        return i;
    }
}