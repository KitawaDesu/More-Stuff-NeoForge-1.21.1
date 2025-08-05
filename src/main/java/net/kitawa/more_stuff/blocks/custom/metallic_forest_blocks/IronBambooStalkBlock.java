package net.kitawa.more_stuff.blocks.custom.metallic_forest_blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
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

import javax.annotation.Nullable;

public class IronBambooStalkBlock extends Block implements BonemealableBlock {
    public static final MapCodec<IronBambooStalkBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Block.CODEC.fieldOf("sapling_block").forGetter(block -> block.saplingblock),
                    propertiesCodec() // helper to get block properties
            ).apply(instance, IronBambooStalkBlock::new)
    );

    private final Block saplingblock;

    protected static final float SMALL_LEAVES_AABB_OFFSET = 3.0F;
    protected static final float LARGE_LEAVES_AABB_OFFSET = 5.0F;
    protected static final float COLLISION_AABB_OFFSET = 1.5F;
    protected static final VoxelShape SMALL_SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
    protected static final VoxelShape LARGE_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    protected static final VoxelShape COLLISION_SHAPE = Block.box(6.5, 0.0, 6.5, 9.5, 16.0, 9.5);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    public static final EnumProperty<BambooLeaves> LEAVES = BlockStateProperties.BAMBOO_LEAVES;
    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
    public static final int MAX_HEIGHT = 16;
    public static final int STAGE_GROWING = 0;
    public static final int STAGE_DONE_GROWING = 1;
    public static final int AGE_THIN_BAMBOO = 0;
    public static final int AGE_THICK_BAMBOO = 1;

    @Override
    public MapCodec<IronBambooStalkBlock> codec() {
        return CODEC;
    }

    public IronBambooStalkBlock(Block saplingblock, Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)).setValue(LEAVES, BambooLeaves.NONE).setValue(STAGE, Integer.valueOf(0))
        );
        this.saplingblock = saplingblock;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.IRON_BAMBOO.get());
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
        VoxelShape voxelshape = state.getValue(LEAVES) == BambooLeaves.LARGE ? LARGE_SHAPE : SMALL_SHAPE;
        Vec3 vec3 = state.getOffset(level, pos);
        return voxelshape.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(level, pos);
        return COLLISION_SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        if (!fluidstate.isEmpty()) {
            return null;
        } else {
            BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().below());
            net.neoforged.neoforge.common.util.TriState soilDecision = blockstate.canSustainPlant(context.getLevel(), context.getClickedPos().below(), Direction.UP, this.defaultBlockState());
            if (soilDecision.isDefault() ? blockstate.is(ModBlockTags.IRON_BAMBOO_GROWABLE_ON) : soilDecision.isTrue()) {
                if (blockstate.is(this.saplingblock)) {
                    return this.defaultBlockState().setValue(AGE, Integer.valueOf(0));
                } else if (blockstate.is(ModBlocks.IRON_BAMBOO_STALK.get())) {
                    int i = blockstate.getValue(AGE) > 0 ? 1 : 0;
                    return this.defaultBlockState().setValue(AGE, Integer.valueOf(i));
                } else {
                    BlockState blockstate1 = context.getLevel().getBlockState(context.getClickedPos().above());
                    return blockstate1.is(ModBlocks.IRON_BAMBOO_STALK.get())
                            ? this.defaultBlockState().setValue(AGE, blockstate1.getValue(AGE))
                            : this.saplingblock.defaultBlockState();
                }
            } else {
                return null;
            }
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(STAGE) == 0;
    }

    /**
     * Performs a random tick on a block.
     */
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            if (level.isEmptyBlock(pos.above()) && level.getRawBrightness(pos.above(), 0) >= 0) {
                int i = this.getHeightBelowUpToMax(level, pos) + 1;
                if (i < 16 && net.neoforged.neoforge.common.CommonHooks.canCropGrow(level, pos, state, random.nextInt(3) == 0)) {
                    this.growBamboo(state, level, pos, random, i);
                    net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(level, pos, state);
                }
            }
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        net.neoforged.neoforge.common.util.TriState soilDecision = level.getBlockState(pos.below()).canSustainPlant(level, pos.below(), Direction.UP, state);
        if (!soilDecision.isDefault()) return soilDecision.isTrue();
        return level.getBlockState(pos.below()).is(ModBlockTags.IRON_BAMBOO_PLANTABLE_ON);
    }

    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */
    @Override
    protected BlockState updateShape(
            BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos
    ) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }

        if (direction == Direction.UP && neighborState.is(ModBlocks.IRON_BAMBOO_STALK.get()) && neighborState.getValue(AGE) > state.getValue(AGE)) {
            level.setBlock(pos, state.cycle(AGE), 2);
        }

        if (direction == Direction.UP && neighborState.is(ModBlocks.IRON_BAMBOO_SAPLING.get())) {
            level.setBlock(pos.above(), ModBlocks.IRON_BAMBOO_STALK.get().defaultBlockState(), 2);
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        int i = this.getHeightAboveUpToMax(level, pos);
        int j = this.getHeightBelowUpToMax(level, pos);
        return i + j + 1 < 16 && level.getBlockState(pos.above(i)).getValue(STAGE) != 1;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int i = this.getHeightAboveUpToMax(level, pos);
        int j = this.getHeightBelowUpToMax(level, pos);
        int k = i + j + 1;
        int l = 1 + random.nextInt(2);

        for (int i1 = 0; i1 < l; i1++) {
            BlockPos blockpos = pos.above(i);
            BlockState blockstate = level.getBlockState(blockpos);
            if (k >= 16 || blockstate.getValue(STAGE) == 1 || !level.isEmptyBlock(blockpos.above())) {
                return;
            }

            this.growBamboo(blockstate, level, blockpos, random, k);
            i++;
            k++;
        }
    }

    /**
     * Get the hardness of this Block relative to the ability of the given player
     */
    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return player.getMainHandItem().canPerformAction(net.neoforged.neoforge.common.ItemAbilities.SWORD_DIG) ? 1.0F : super.getDestroyProgress(state, player, level, pos);
    }

    protected void growBamboo(BlockState state, Level level, BlockPos pos, RandomSource random, int age) {
        BlockState blockstate = level.getBlockState(pos.below());
        BlockPos blockpos = pos.below(2);
        BlockState blockstate1 = level.getBlockState(blockpos);
        BambooLeaves bambooleaves = BambooLeaves.NONE;
        if (age >= 1) {
            if (!blockstate.is(ModBlocks.IRON_BAMBOO_STALK.get()) || blockstate.getValue(LEAVES) == BambooLeaves.NONE) {
                bambooleaves = BambooLeaves.SMALL;
            } else if (blockstate.is(ModBlocks.IRON_BAMBOO_STALK.get()) && blockstate.getValue(LEAVES) != BambooLeaves.NONE) {
                bambooleaves = BambooLeaves.LARGE;
                if (blockstate1.is(ModBlocks.IRON_BAMBOO_STALK.get())) {
                    level.setBlock(pos.below(), blockstate.setValue(LEAVES, BambooLeaves.SMALL), 3);
                    level.setBlock(blockpos, blockstate1.setValue(LEAVES, BambooLeaves.NONE), 3);
                }
            }
        }

        int i = state.getValue(AGE) != 1 && !blockstate1.is(ModBlocks.IRON_BAMBOO_STALK.get()) ? 0 : 1;
        int j = (age < 11 || !(random.nextFloat() < 0.25F)) && age != 15 ? 0 : 1;
        level.setBlock(
                pos.above(), this.defaultBlockState().setValue(AGE, Integer.valueOf(i)).setValue(LEAVES, bambooleaves).setValue(STAGE, Integer.valueOf(j)), 3
        );
    }

    protected int getHeightAboveUpToMax(BlockGetter level, BlockPos pos) {
        int i = 0;

        while (i < 16 && level.getBlockState(pos.above(i + 1)).is(ModBlocks.IRON_BAMBOO_STALK.get())) {
            i++;
        }

        return i;
    }

    protected int getHeightBelowUpToMax(BlockGetter level, BlockPos pos) {
        int i = 0;

        while (i < 16 && level.getBlockState(pos.below(i + 1)).is(ModBlocks.IRON_BAMBOO_STALK.get())) {
            i++;
        }

        return i;
    }
}
