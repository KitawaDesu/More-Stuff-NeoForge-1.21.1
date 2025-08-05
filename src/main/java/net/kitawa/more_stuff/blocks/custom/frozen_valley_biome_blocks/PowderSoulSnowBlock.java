package net.kitawa.more_stuff.blocks.custom.frozen_valley_biome_blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class PowderSoulSnowBlock extends Block {
    public static final MapCodec<PowderSoulSnowBlock> CODEC = simpleCodec(PowderSoulSnowBlock::new);
    private static final float HORIZONTAL_PARTICLE_MOMENTUM_FACTOR = 0.083333336F;
    private static final float IN_BLOCK_HORIZONTAL_SPEED_MULTIPLIER = 0.9F;
    private static final float IN_BLOCK_VERTICAL_SPEED_MULTIPLIER = 1.5F;
    private static final float NUM_BLOCKS_TO_FALL_INTO_BLOCK = 2.5F;
    private static final VoxelShape FALLING_COLLISION_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 0.9F, 1.0);
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);
    private static final double MINIMUM_FALL_DISTANCE_FOR_SOUND = 4.0;
    private static final double MINIMUM_FALL_DISTANCE_FOR_BIG_SOUND = 7.0;

    @Override
    public MapCodec<PowderSoulSnowBlock> codec() {
        return CODEC;
    }

    public PowderSoulSnowBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }


    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        boolean isImmune = canEntityWalkOnPowderSnow(entity);

        // Only apply velocity effects if the entity is NOT immune
        if (!isImmune && entity.getInBlockState().is(this)) {
            entity.makeStuckInBlock(state, new Vec3(0.9F, 1.5, 0.9F));
        }

        // Always show snowflake particles on client
        if (level.isClientSide) {
            RandomSource randomsource = level.getRandom();
            boolean isMoving = entity.xOld != entity.getX() || entity.zOld != entity.getZ();
            if (isMoving && randomsource.nextBoolean()) {
                level.addParticle(
                        ParticleTypes.SNOWFLAKE,
                        entity.getX(),
                        pos.getY() + 1.0,
                        entity.getZ(),
                        Mth.randomBetween(randomsource, -1.0F, 1.0F) * 0.083333336F,
                        0.05F,
                        Mth.randomBetween(randomsource, -1.0F, 1.0F) * 0.083333336F
                );
            }
        }

        entity.setIsInPowderSnow(true);

        // Always handle fire extinguishing and block destruction on server
        if (!level.isClientSide) {
            if (entity.isOnFire()
                    && (level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || entity instanceof Player)
                    && entity.mayInteract(level, pos)) {
                level.destroyBlock(pos, false);
            }

            entity.setSharedFlagOnFire(false);
        }
    }

    public static boolean canEntityWalkOnPowderSnow(Entity entity) {
        if (entity.getType().is(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
            return true;
        } else {
            return entity instanceof LivingEntity ? ((LivingEntity)entity).getItemBySlot(EquipmentSlot.FEET).canWalkOnPowderedSnow((LivingEntity)entity) || ((LivingEntity)entity).getItemBySlot(EquipmentSlot.BODY).is(ItemTags.FREEZE_IMMUNE_WEARABLES) : false;
        }
    }


    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!((double)fallDistance < 4.0) && entity instanceof LivingEntity livingentity) {
            LivingEntity.Fallsounds $$7 = livingentity.getFallSounds();
            SoundEvent soundevent = (double)fallDistance < 7.0 ? $$7.small() : $$7.big();
            entity.playSound(soundevent, 1.0F, 1.0F);
        }
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }
}
