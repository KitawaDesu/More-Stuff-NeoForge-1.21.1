package net.kitawa.more_stuff.util.mixins.blocks.fluidlogging;

import net.kitawa.more_stuff.blocks.util.ModdedBlockStateProperties;
import net.kitawa.more_stuff.blocks.util.SimpleFluidLoggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(BedBlock.class)
public abstract class BedBlockFluidMixin extends HorizontalDirectionalBlock implements SimpleFluidLoggedBlock {
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final BooleanProperty LAVALOGGED = ModdedBlockStateProperties.LAVALOGGED;

    protected BedBlockFluidMixin(Properties properties) {
        super(properties);
    }

    // Inject default state
    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectDefaultState(DyeColor color, Properties properties, CallbackInfo ci) {
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(BedBlock.PART, BedPart.FOOT)
                        .setValue(BedBlock.OCCUPIED, Boolean.valueOf(false))
                        .setValue(WATERLOGGED, false)
                        .setValue(LAVALOGGED, false)
        );
    }

    // Inject LAVALOGGED and WATERLOGGED into block state
    @Inject(method = "createBlockStateDefinition", at = @At("RETURN"))
    private void addFluidProperties(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(WATERLOGGED, LAVALOGGED);
    }

    // Inject default fluid values when block is placed
    @Inject(method = "setPlacedBy", at = @At("RETURN"))
    private void injectFluidState(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        FluidState fluid = level.getFluidState(pos);
        BlockState newState = state
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(LAVALOGGED, fluid.getType() == Fluids.LAVA);
        level.setBlock(pos, newState, 3);

        // Also update the head part if this is the foot
        if (state.getValue(BedBlock.PART) == BedPart.FOOT) {
            BlockPos headPos = pos.relative(state.getValue(BedBlock.FACING));
            BlockState headState = level.getBlockState(headPos);
            if (headState.is(state.getBlock())) {
                FluidState headFluid = level.getFluidState(headPos);
                level.setBlock(headPos, headState
                        .setValue(WATERLOGGED, headFluid.getType() == Fluids.WATER)
                        .setValue(LAVALOGGED, headFluid.getType() == Fluids.LAVA), 3);
            }
        }
    }

    // Inject lava/water tick scheduling into updateShape
    @Inject(method = "updateShape", at = @At("HEAD"))
    private void scheduleFluidTick(BlockState state, Direction facing, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        } else if (state.getValue(LAVALOGGED)) {
            level.scheduleTick(pos, Fluids.LAVA, Fluids.LAVA.getTickDelay(level));
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction direction = ctx.getHorizontalDirection();
        BlockPos footPos = ctx.getClickedPos();
        BlockPos headPos = footPos.relative(direction);
        Level level = ctx.getLevel();

        // Validate head position
        if (!level.getBlockState(headPos).canBeReplaced(ctx) || !level.getWorldBorder().isWithinBounds(headPos)) {
            return null;
        }

        // Foot fluid
        FluidState fluid = level.getFluidState(footPos);

        return this.defaultBlockState()
                .setValue(FACING, direction)
                .setValue(BedBlock.PART, BedPart.FOOT)
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(LAVALOGGED, fluid.getType() == Fluids.LAVA);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (state.getValue(BedBlock.PART) == BedPart.FOOT) {
            BlockPos headPos = pos.relative(state.getValue(FACING));

            // Head fluid
            FluidState headFluid = level.getFluidState(headPos);

            BlockState headState = this.defaultBlockState()
                    .setValue(FACING, state.getValue(FACING))
                    .setValue(BedBlock.PART, BedPart.HEAD)
                    .setValue(WATERLOGGED, headFluid.getType() == Fluids.WATER)
                    .setValue(LAVALOGGED, headFluid.getType() == Fluids.LAVA);

            level.setBlock(headPos, headState, 3);
        }
    }


    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED)) return Fluids.WATER.getSource(false);
        if (state.getValue(LAVALOGGED)) return Fluids.LAVA.getSource(false);
        return super.getFluidState(state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            if (state.getValue(BedBlock.PART) != BedPart.HEAD) {
                pos = pos.relative(state.getValue(FACING));
                state = level.getBlockState(pos);
                if (!state.is(this)) {
                    return InteractionResult.CONSUME;
                }
            }

            // 🌊 Extra check: if waterlogged, require water breathing/conduit power
            FluidState fluid = level.getFluidState(pos);
            if (fluid.is(Fluids.WATER)) {
                boolean hasWaterBreathing = player.hasEffect(MobEffects.WATER_BREATHING);
                boolean hasConduitPower = player.hasEffect(MobEffects.CONDUIT_POWER);

                if (!hasWaterBreathing && !hasConduitPower) {
                    player.displayClientMessage(
                            Component.translatable("block.minecraft.bed.needs_breathing_or_conduit_power"),
                            true
                    );
                    return InteractionResult.FAIL;
                }
            }

            // 🔥 Extra check: if lavalogged, require fire resistance
            if (state.hasProperty(LAVALOGGED) && state.getValue(LAVALOGGED)) {
                boolean hasFireRes = player.hasEffect(MobEffects.FIRE_RESISTANCE);

                if (!hasFireRes) {
                    player.displayClientMessage(
                            Component.translatable("block.minecraft.bed.needs_fire_resistance"),
                            true
                    );
                    return InteractionResult.FAIL;
                }
            }

            if (!BedBlock.canSetSpawn(level)) {
                level.removeBlock(pos, false);
                BlockPos blockpos = pos.relative(state.getValue(FACING).getOpposite());
                if (level.getBlockState(blockpos).is(this)) {
                    level.removeBlock(blockpos, false);
                }

                Vec3 vec3 = pos.getCenter();
                level.explode(null, level.damageSources().badRespawnPointExplosion(vec3), null, vec3, 5.0F, true, Level.ExplosionInteraction.BLOCK);
                return InteractionResult.SUCCESS;
            } else if (state.getValue(BedBlock.OCCUPIED)) {
                if (!this.kickVillagerOutOfBed(level, pos)) {
                    player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
                }

                return InteractionResult.SUCCESS;
            } else {
                player.startSleepInBed(pos).ifLeft(problem -> {
                    if (problem.getMessage() != null) {
                        player.displayClientMessage(problem.getMessage(), true);
                    }
                });
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Inject(method = "playerWillDestroy", at = @At("HEAD"))
    private void onFootDestroyed(Level level, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<BlockState> cir) {
        if (!level.isClientSide && state.getValue(BedBlock.PART) == BedPart.FOOT) {
            // Find head block
            BlockPos headPos = pos.relative(state.getValue(BedBlock.FACING));
            BlockState headState = level.getBlockState(headPos);

            if (headState.getBlock() instanceof BedBlock) {
                // If head is waterlogged -> place water
                if (headState.hasProperty(WATERLOGGED) && headState.getValue(WATERLOGGED)) {
                    level.setBlock(headPos, Blocks.WATER.defaultBlockState(), 3);
                }
                // If head is lavalogged -> place lava
                else if (headState.hasProperty(LAVALOGGED) && headState.getValue(LAVALOGGED)) {
                    level.setBlock(headPos, Blocks.LAVA.defaultBlockState(), 3);
                }
            }
        }
    }

    @Shadow
    private boolean kickVillagerOutOfBed(Level level, BlockPos pos) {
        List<Villager> list = level.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping);
        if (list.isEmpty()) {
            return false;
        } else {
            list.get(0).stopSleeping();
            return true;
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        int baseLight = super.getLightEmission(state, level, pos); // call Block's method directly
        return SimpleFluidLoggedBlock.super.getFluidLightEmission(state, baseLight);
    }
}
