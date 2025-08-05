package net.kitawa.more_stuff.blocks.custom.frozen_valley_biome_blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathType;

import javax.annotation.Nullable;

public class FreezingMagmaBlock extends Block {
    public static final MapCodec<FreezingMagmaBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.FLOAT.fieldOf("damage").forGetter(block -> block.Damage),
                    propertiesCodec()
            ).apply(instance, FreezingMagmaBlock::new)
    );

    private static final int BUBBLE_COLUMN_CHECK_DELAY = 20;

    public final float Damage;

    @Override
    public MapCodec<FreezingMagmaBlock> codec() {
        return CODEC;
    }

    public FreezingMagmaBlock(float Damage, Properties properties) {
        super(properties);
        this.Damage = Damage;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.isSteppingCarefully() && entity instanceof LivingEntity living) {
            if (!canEntityWalkOnPowderSnow(living)) {
                entity.hurt(level.damageSources().freeze(), this.Damage);
                living.setTicksFrozen(142);
            }
        }

        super.stepOn(level, pos, state, entity);
    }

    public static boolean canEntityWalkOnPowderSnow(Entity entity) {
        if (entity.getType().is(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS) || entity.getType().is(EntityTypeTags.SKELETONS) || entity.getType().equals(EntityType.POLAR_BEAR)) {
            return true;
        } else {
            return entity instanceof LivingEntity ? ((LivingEntity)entity).getItemBySlot(EquipmentSlot.FEET).canWalkOnPowderedSnow((LivingEntity)entity) || ((LivingEntity)entity).getItemBySlot(EquipmentSlot.BODY).is(ItemTags.FREEZE_IMMUNE_WEARABLES) : false;
        }
    }


    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BubbleColumnBlock.updateColumn(level, pos.above(), state);
    }

    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */
    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.UP && facingState.is(Blocks.WATER)) {
            level.scheduleTick(currentPos, this, 20);
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        level.scheduleTick(pos, this, 20);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }
}

