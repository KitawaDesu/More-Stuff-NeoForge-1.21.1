package net.kitawa.more_stuff.blocks.custom.general;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class ModdedPointedDripstoneBlock extends Block implements Fallable, SimpleWaterloggedBlock {

    public static final MapCodec<ModdedPointedDripstoneBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.BLOCK.byNameCodec().listOf()
                            .optionalFieldOf("grow_blocks", List.of())
                            .forGetter(b -> b.growBlocks),
                    propertiesCodec()
            ).apply(instance, ModdedPointedDripstoneBlock::new)
    );

    public static final DirectionProperty TIP_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;
    public static final EnumProperty<DripstoneThickness> THICKNESS = BlockStateProperties.DRIPSTONE_THICKNESS;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final List<Block> growBlocks;

    private static final int MAX_SEARCH_LENGTH_WHEN_CHECKING_DRIP_TYPE = 11;
    private static final int DELAY_BEFORE_FALLING = 2;
    private static final float DRIP_PROBABILITY_PER_ANIMATE_TICK = 0.02F;
    private static final float DRIP_PROBABILITY_PER_ANIMATE_TICK_IF_UNDER_LIQUID_SOURCE = 0.12F;
    private static final int MAX_SEARCH_LENGTH_BETWEEN_STALACTITE_TIP_AND_CAULDRON = 11;
    public static final float WATER_TRANSFER_PROBABILITY_PER_RANDOM_TICK = 0.17578125F;
    public static final float LAVA_TRANSFER_PROBABILITY_PER_RANDOM_TICK = 0.05859375F;
    private static final double MIN_TRIDENT_VELOCITY_TO_BREAK_DRIPSTONE = 0.6;
    private static final float STALACTITE_DAMAGE_PER_FALL_DISTANCE_AND_SIZE = 1.0F;
    private static final int STALACTITE_MAX_DAMAGE = 40;
    private static final int MAX_STALACTITE_HEIGHT_FOR_DAMAGE_CALCULATION = 6;
    private static final float STALAGMITE_FALL_DISTANCE_OFFSET = 2.0F;
    private static final int STALAGMITE_FALL_DAMAGE_MODIFIER = 2;
    private static final float AVERAGE_DAYS_PER_GROWTH = 5.0F;
    private static final float GROWTH_PROBABILITY_PER_RANDOM_TICK = 0.011377778F;
    private static final int MAX_GROWTH_LENGTH = 7;
    private static final int MAX_STALAGMITE_SEARCH_RANGE_WHEN_GROWING = 10;
    private static final float STALACTITE_DRIP_START_PIXEL = 0.6875F;
    private static final VoxelShape TIP_MERGE_SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
    private static final VoxelShape TIP_SHAPE_UP = Block.box(5.0, 0.0, 5.0, 11.0, 11.0, 11.0);
    private static final VoxelShape TIP_SHAPE_DOWN = Block.box(5.0, 5.0, 5.0, 11.0, 16.0, 11.0);
    private static final VoxelShape FRUSTUM_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);
    private static final VoxelShape MIDDLE_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    private static final VoxelShape BASE_SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    private static final float MAX_HORIZONTAL_OFFSET = 0.125F;
    private static final VoxelShape REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    @Override
    public MapCodec<ModdedPointedDripstoneBlock> codec() {
        return CODEC;
    }

    public ModdedPointedDripstoneBlock(List<Block> growBlocks, BlockBehaviour.Properties properties) {
        super(properties);
        this.growBlocks = growBlocks;
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(TIP_DIRECTION, Direction.UP)
                        .setValue(THICKNESS, DripstoneThickness.TIP)
                        .setValue(WATERLOGGED, false)
        );
    }

    public ModdedPointedDripstoneBlock(Block growBlock, BlockBehaviour.Properties properties) {
        this(List.of(growBlock), properties);
    }

    public ModdedPointedDripstoneBlock(BlockBehaviour.Properties properties) {
        this(List.of(), properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TIP_DIRECTION, THICKNESS, WATERLOGGED);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return isValidPointedDripstonePlacement(level, pos, state.getValue(TIP_DIRECTION));
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED))
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

        if (direction != Direction.UP && direction != Direction.DOWN) return state;

        Direction tipDirection = state.getValue(TIP_DIRECTION);
        if (tipDirection == Direction.DOWN && level.getBlockTicks().hasScheduledTick(pos, this)) return state;

        if (direction == tipDirection.getOpposite() && !this.canSurvive(state, level, pos)) {
            level.scheduleTick(pos, this, tipDirection == Direction.DOWN ? 2 : 1);
            return state;
        }

        boolean tipMerge = state.getValue(THICKNESS) == DripstoneThickness.TIP_MERGE;
        return state.setValue(THICKNESS, calculateDripstoneThickness(level, pos, tipDirection, tipMerge));
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!level.isClientSide) {
            BlockPos pos = hit.getBlockPos();
            if (projectile.mayInteract(level, pos)
                    && projectile.mayBreak(level)
                    && projectile instanceof ThrownTrident
                    && projectile.getDeltaMovement().length() > 0.6) {
                level.destroyBlock(pos, true);
            }
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (state.getValue(TIP_DIRECTION) == Direction.UP && state.getValue(THICKNESS) == DripstoneThickness.TIP) {
            entity.causeFallDamage(fallDistance + 2.0F, 2.0F, level.damageSources().stalagmite());
        } else {
            super.fallOn(level, state, pos, entity, fallDistance);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (canDrip(state)) {
            float f = random.nextFloat();
            if (!(f > 0.12F)) {
                getFluidAboveStalactite(level, pos, state)
                        .filter(info -> f < 0.02F || canFillCauldron(info.fluid))
                        .ifPresent(info -> spawnDripParticle(level, pos, state, info.fluid));
            }
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isStalagmite(state) && !this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        } else {
            spawnFallingStalactite(state, level, pos);
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        maybeTransferFluid(state, level, pos, random.nextFloat());
        if (random.nextFloat() < GROWTH_PROBABILITY_PER_RANDOM_TICK && isStalactiteStartPos(state, level, pos)) {
            growStalactiteOrStalagmiteIfPossible(state, level, pos, random);
        }
    }

    public static void maybeTransferFluid(BlockState state, ServerLevel level, BlockPos pos, float randChance) {
        if (isStalactiteStartPos(state, level, pos)) {
            Optional<FluidInfo> optional = getFluidAboveStalactite(level, pos, state);
            if (optional.isEmpty()) return;

            Fluid fluid = optional.get().fluid;
            net.neoforged.neoforge.fluids.FluidType.DripstoneDripInfo dripInfo = fluid.getFluidType().getDripInfo();
            if (dripInfo == null || randChance >= dripInfo.chance()) return;

            BlockPos tip = findTip(state, level, pos, 11, false);
            if (tip == null) return;

            if (optional.get().sourceState.is(Blocks.MUD) && fluid == Fluids.WATER) {
                BlockState clay = Blocks.CLAY.defaultBlockState();
                level.setBlockAndUpdate(optional.get().pos, clay);
                Block.pushEntitiesUp(optional.get().sourceState, clay, level, optional.get().pos);
                level.gameEvent(GameEvent.BLOCK_CHANGE, optional.get().pos, GameEvent.Context.of(clay));
                level.levelEvent(1504, tip, 0);
            } else {
                BlockPos cauldron = findFillableCauldronBelowStalactiteTip(level, tip, fluid);
                if (cauldron != null) {
                    level.levelEvent(1504, tip, 0);
                    int dist = tip.getY() - cauldron.getY();
                    BlockState cauldronState = level.getBlockState(cauldron);
                    level.scheduleTick(cauldron, cauldronState.getBlock(), 50 + dist);
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction dir = context.getNearestLookingVerticalDirection().getOpposite();
        Direction tipDir = calculateTipDirection(level, pos, dir);
        if (tipDir == null) return null;

        boolean tipMerge = !context.isSecondaryUseActive();
        DripstoneThickness thickness = calculateDripstoneThickness(level, pos, tipDir, tipMerge);
        if (thickness == null) return null;

        return this.defaultBlockState()
                .setValue(TIP_DIRECTION, tipDir)
                .setValue(THICKNESS, thickness)
                .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        DripstoneThickness thickness = state.getValue(THICKNESS);
        VoxelShape shape = switch (thickness) {
            case TIP_MERGE -> TIP_MERGE_SHAPE;
            case TIP -> state.getValue(TIP_DIRECTION) == Direction.DOWN ? TIP_SHAPE_DOWN : TIP_SHAPE_UP;
            case FRUSTUM -> FRUSTUM_SHAPE;
            case MIDDLE -> MIDDLE_SHAPE;
            default -> BASE_SHAPE;
        };
        Vec3 offset = state.getOffset(level, pos);
        return shape.move(offset.x, 0.0, offset.z);
    }

    @Override
    protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    protected float getMaxHorizontalOffset() {
        return 0.125F;
    }

    @Override
    public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity fallingBlock) {
        if (!fallingBlock.isSilent()) level.levelEvent(1045, pos, 0);
    }

    @Override
    public DamageSource getFallDamageSource(Entity entity) {
        return entity.damageSources().fallingStalactite(entity);
    }

    private static void spawnFallingStalactite(BlockState state, ServerLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        BlockState current = state;
        while (isStalactite(current)) {
            FallingBlockEntity falling = FallingBlockEntity.fall(level, mutable, current);
            if (isTip(current, true)) {
                int i = Math.max(1 + pos.getY() - mutable.getY(), 6);
                falling.setHurtsEntities(1.0F * i, 40);
                break;
            }
            mutable.move(Direction.DOWN);
            current = level.getBlockState(mutable);
        }
    }

    public static void growStalactiteOrStalagmiteIfPossible(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Resolve self first, before any use
        ModdedPointedDripstoneBlock self = (ModdedPointedDripstoneBlock) state.getBlock();

        BlockState above1 = level.getBlockState(pos.above(1));
        BlockState above2 = level.getBlockState(pos.above(2));
        if (self.canGrow(above1, above2)) {
            BlockPos tip = findTip(state, level, pos, 7, false);
            if (tip != null) {
                BlockState tipState = level.getBlockState(tip);
                if (canDrip(tipState) && canTipGrow(tipState, level, tip)) {
                    if (random.nextBoolean()) {
                        grow(self, level, tip, Direction.DOWN);
                    } else {
                        growStalagmiteBelow(self, level, tip);
                    }
                }
            }
        }
    }

    private static void growStalagmiteBelow(ModdedPointedDripstoneBlock self, ServerLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        for (int i = 0; i < 10; i++) {
            mutable.move(Direction.DOWN);
            BlockState state = level.getBlockState(mutable);
            if (!state.getFluidState().isEmpty()) return;
            if (isUnmergedTipWithDirection(state, Direction.UP) && canTipGrow(state, level, mutable)) {
                grow(self, level, mutable, Direction.UP);
                return;
            }
            if (isValidPointedDripstonePlacement(level, mutable, Direction.UP) && !level.isWaterAt(mutable.below())) {
                grow(self, level, mutable.below(), Direction.UP);
                return;
            }
            if (!canDripThrough(level, mutable, state)) return;
        }
    }

    private static void grow(ModdedPointedDripstoneBlock self, ServerLevel level, BlockPos pos, Direction direction) {
        BlockPos target = pos.relative(direction);
        BlockState targetState = level.getBlockState(target);
        if (isUnmergedTipWithDirection(targetState, direction.getOpposite())) {
            self.createMergedTips(targetState, level, target);
        } else if (targetState.isAir() || targetState.is(Blocks.WATER)) {
            self.createDripstone(level, target, direction, DripstoneThickness.TIP);
        }
    }

    private void createDripstone(LevelAccessor level, BlockPos pos, Direction direction, DripstoneThickness thickness) {
        BlockState state = this.defaultBlockState()
                .setValue(TIP_DIRECTION, direction)
                .setValue(THICKNESS, thickness)
                .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
        level.setBlock(pos, state, 3);
    }

    private void createMergedTips(BlockState state, LevelAccessor level, BlockPos pos) {
        BlockPos upper, lower;
        if (state.getValue(TIP_DIRECTION) == Direction.UP) {
            lower = pos;
            upper = pos.above();
        } else {
            upper = pos;
            lower = pos.below();
        }
        createDripstone(level, upper, Direction.DOWN, DripstoneThickness.TIP_MERGE);
        createDripstone(level, lower, Direction.UP, DripstoneThickness.TIP_MERGE);
    }

    public static void spawnDripParticle(Level level, BlockPos pos, BlockState state) {
        getFluidAboveStalactite(level, pos, state).ifPresent(info -> spawnDripParticle(level, pos, state, info.fluid));
    }

    private static void spawnDripParticle(Level level, BlockPos pos, BlockState state, Fluid fluid) {
        Vec3 offset = state.getOffset(level, pos);
        double x = pos.getX() + 0.5 + offset.x;
        double y = (float)(pos.getY() + 1) - 0.6875F - 0.0625;
        double z = pos.getZ() + 0.5 + offset.z;
        Fluid dripFluid = getDripFluid(level, fluid);
        ParticleOptions particle = dripFluid.getFluidType().getDripInfo() != null
                ? dripFluid.getFluidType().getDripInfo().dripParticle()
                : ParticleTypes.DRIPPING_DRIPSTONE_WATER;
        if (particle != null) level.addParticle(particle, x, y, z, 0.0, 0.0, 0.0);
    }

    @Nullable
    private static BlockPos findTip(BlockState state, LevelAccessor level, BlockPos pos, int maxIterations, boolean tipMerge) {
        if (isTip(state, tipMerge)) return pos;
        Direction direction = state.getValue(TIP_DIRECTION);
        BiPredicate<BlockPos, BlockState> predicate = (p, s) ->
                s.getBlock() instanceof ModdedPointedDripstoneBlock && s.getValue(TIP_DIRECTION) == direction;
        return findBlockVertical(level, pos, direction.getAxisDirection(), predicate,
                s -> isTip(s, tipMerge), maxIterations).orElse(null);
    }

    @Nullable
    private static Direction calculateTipDirection(LevelReader level, BlockPos pos, Direction dir) {
        if (isValidPointedDripstonePlacement(level, pos, dir)) return dir;
        if (isValidPointedDripstonePlacement(level, pos, dir.getOpposite())) return dir.getOpposite();
        return null;
    }

    private static DripstoneThickness calculateDripstoneThickness(LevelReader level, BlockPos pos,
                                                                  Direction dir, boolean tipMerge) {
        Direction opposite = dir.getOpposite();
        BlockState inDir = level.getBlockState(pos.relative(dir));
        if (isPointedDripstoneWithDirection(inDir, opposite)) {
            return !tipMerge && inDir.getValue(THICKNESS) != DripstoneThickness.TIP_MERGE
                    ? DripstoneThickness.TIP : DripstoneThickness.TIP_MERGE;
        }
        if (!isPointedDripstoneWithDirection(inDir, dir)) return DripstoneThickness.TIP;

        DripstoneThickness thickness = inDir.getValue(THICKNESS);
        if (thickness != DripstoneThickness.TIP && thickness != DripstoneThickness.TIP_MERGE) {
            BlockState inOpposite = level.getBlockState(pos.relative(opposite));
            return !isPointedDripstoneWithDirection(inOpposite, dir) ? DripstoneThickness.BASE : DripstoneThickness.MIDDLE;
        }
        return DripstoneThickness.FRUSTUM;
    }

    public static boolean canDrip(BlockState state) {
        return isStalactite(state) && state.getValue(THICKNESS) == DripstoneThickness.TIP && !state.getValue(WATERLOGGED);
    }

    private static boolean canTipGrow(BlockState state, ServerLevel level, BlockPos pos) {
        Direction direction = state.getValue(TIP_DIRECTION);
        BlockPos target = pos.relative(direction);
        BlockState targetState = level.getBlockState(target);
        if (!targetState.getFluidState().isEmpty()) return false;
        return targetState.isAir() || isUnmergedTipWithDirection(targetState, direction.getOpposite());
    }

    private static Optional<BlockPos> findRootBlock(Level level, BlockPos pos, BlockState state, int maxIterations) {
        Direction direction = state.getValue(TIP_DIRECTION);
        BiPredicate<BlockPos, BlockState> predicate = (p, s) ->
                s.getBlock() instanceof ModdedPointedDripstoneBlock && s.getValue(TIP_DIRECTION) == direction;
        return findBlockVertical(level, pos, direction.getOpposite().getAxisDirection(), predicate,
                s -> !(s.getBlock() instanceof ModdedPointedDripstoneBlock), maxIterations);
    }

    private static boolean isValidPointedDripstonePlacement(LevelReader level, BlockPos pos, Direction dir) {
        BlockPos opposite = pos.relative(dir.getOpposite());
        BlockState oppositeState = level.getBlockState(opposite);
        return oppositeState.isFaceSturdy(level, opposite, dir) || isPointedDripstoneWithDirection(oppositeState, dir);
    }

    private static boolean isTip(BlockState state, boolean tipMerge) {
        if (!(state.getBlock() instanceof ModdedPointedDripstoneBlock)) return false;
        DripstoneThickness thickness = state.getValue(THICKNESS);
        return thickness == DripstoneThickness.TIP || (tipMerge && thickness == DripstoneThickness.TIP_MERGE);
    }

    private static boolean isUnmergedTipWithDirection(BlockState state, Direction dir) {
        return isTip(state, false) && state.getValue(TIP_DIRECTION) == dir;
    }

    private static boolean isStalactite(BlockState state) {
        return isPointedDripstoneWithDirection(state, Direction.DOWN);
    }

    private static boolean isStalagmite(BlockState state) {
        return isPointedDripstoneWithDirection(state, Direction.UP);
    }

    private static boolean isStalactiteStartPos(BlockState state, LevelReader level, BlockPos pos) {
        return isStalactite(state) && !(level.getBlockState(pos.above()).getBlock() instanceof ModdedPointedDripstoneBlock);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    private static boolean isPointedDripstoneWithDirection(BlockState state, Direction dir) {
        return state.getBlock() instanceof ModdedPointedDripstoneBlock && state.getValue(TIP_DIRECTION) == dir;
    }

    @Nullable
    private static BlockPos findFillableCauldronBelowStalactiteTip(Level level, BlockPos pos, Fluid fluid) {
        Predicate<BlockState> predicate = s -> s.getBlock() instanceof AbstractCauldronBlock
                && ((AbstractCauldronBlock) s.getBlock()).canReceiveStalactiteDrip(fluid);
        BiPredicate<BlockPos, BlockState> traversable = (p, s) -> canDripThrough(level, p, s);
        return findBlockVertical(level, pos, Direction.DOWN.getAxisDirection(), traversable, predicate, 11).orElse(null);
    }

    @Nullable
    public static BlockPos findStalactiteTipAboveCauldron(Level level, BlockPos pos) {
        BiPredicate<BlockPos, BlockState> traversable = (p, s) -> canDripThrough(level, p, s);
        return findBlockVertical(level, pos, Direction.UP.getAxisDirection(), traversable,
                ModdedPointedDripstoneBlock::canDrip, 11).orElse(null);
    }

    public static Fluid getCauldronFillFluidType(ServerLevel level, BlockPos pos) {
        return getFluidAboveStalactite(level, pos, level.getBlockState(pos))
                .map(info -> info.fluid)
                .filter(ModdedPointedDripstoneBlock::canFillCauldron)
                .orElse(Fluids.EMPTY);
    }

    private static Optional<FluidInfo> getFluidAboveStalactite(Level level, BlockPos pos, BlockState state) {
        if (!isStalactite(state)) return Optional.empty();
        return findRootBlock(level, pos, state, 11).map(root -> {
            BlockPos above = root.above();
            BlockState aboveState = level.getBlockState(above);
            Fluid fluid;
            if (aboveState.is(Blocks.MUD) && !level.dimensionType().ultraWarm()) {
                fluid = Fluids.WATER;
            } else {
                fluid = level.getFluidState(above).getType();
            }
            return new FluidInfo(above, fluid, aboveState);
        });
    }

    private static boolean canFillCauldron(Fluid fluid) {
        return fluid.getFluidType().getDripInfo() != null;
    }

    private boolean canGrow(BlockState dripstoneState, BlockState above) {
        if (growBlocks.isEmpty()) return false;
        return growBlocks.stream().anyMatch(dripstoneState::is)
                && above.is(Blocks.WATER)
                && above.getFluidState().isSource();
    }

    private static Fluid getDripFluid(Level level, Fluid fluid) {
        if (fluid.isSame(Fluids.EMPTY))
            return level.dimensionType().ultraWarm() ? Fluids.LAVA : Fluids.WATER;
        return fluid;
    }

    private static Optional<BlockPos> findBlockVertical(
            LevelAccessor level, BlockPos pos, Direction.AxisDirection axis,
            BiPredicate<BlockPos, BlockState> traversable,
            Predicate<BlockState> target, int maxIterations) {
        Direction direction = Direction.get(axis, Direction.Axis.Y);
        BlockPos.MutableBlockPos mutable = pos.mutable();
        for (int i = 1; i < maxIterations; i++) {
            mutable.move(direction);
            BlockState state = level.getBlockState(mutable);
            if (target.test(state)) return Optional.of(mutable.immutable());
            if (level.isOutsideBuildHeight(mutable.getY()) || !traversable.test(mutable, state))
                return Optional.empty();
        }
        return Optional.empty();
    }

    private static boolean canDripThrough(BlockGetter level, BlockPos pos, BlockState state) {
        if (state.isAir()) return true;
        if (state.isSolidRender(level, pos)) return false;
        if (!state.getFluidState().isEmpty()) return false;
        VoxelShape shape = state.getCollisionShape(level, pos);
        return !Shapes.joinIsNotEmpty(REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK, shape, BooleanOp.AND);
    }

    record FluidInfo(BlockPos pos, Fluid fluid, BlockState sourceState) {}
}