package net.kitawa.more_stuff.blocks.custom.general;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kitawa.more_stuff.blocks.custom.general.entities.ModdedBrushableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModdedBrushableBlock extends BrushableBlockNoFall implements Fallable {

    public static final MapCodec<ModdedBrushableBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("turns_into").forGetter(ModdedBrushableBlock::getTurnsInto),
                    BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("brush_sound").forGetter(ModdedBrushableBlock::getBrushSound),
                    BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("brush_completed_sound").forGetter(ModdedBrushableBlock::getBrushCompletedSound),
                    propertiesCodec()
            ).apply(instance, ModdedBrushableBlock::new)
    );

    public ModdedBrushableBlock(Block turnsInto, SoundEvent brushSound, SoundEvent brushCompletedSound, BlockBehaviour.Properties properties) {
        super(turnsInto, brushSound, brushCompletedSound, properties);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof ModdedBrushableBlockEntity moddedbrushableblockentity) {
            moddedbrushableblockentity.checkReset();
        }

        if (FallingBlock.isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight()) {
            FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(level, pos, state);
            fallingblockentity.disableDrop();
        }
    }

    @Override
    public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity fallingBlock) {
        Vec3 vec3 = fallingBlock.getBoundingBox().getCenter();
        level.levelEvent(2001, BlockPos.containing(vec3), Block.getId(fallingBlock.getBlockState()));
        level.gameEvent(fallingBlock, GameEvent.BLOCK_DESTROY, vec3);
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles).
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(16) == 0) {
            BlockPos blockpos = pos.below();
            if (FallingBlock.isFree(level.getBlockState(blockpos))) {
                double d0 = (double)pos.getX() + random.nextDouble();
                double d1 = (double)pos.getY() - 0.05;
                double d2 = (double)pos.getZ() + random.nextDouble();
                level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), d0, d1, d2, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public MapCodec<? extends BrushableBlockNoFall> codec() {
        return CODEC;
    }
}