package net.kitawa.more_stuff.blocks.custom.general;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BrushableSoulSandBlock extends BrushableBlockNoFall {

    public static final MapCodec<ModdedBrushableBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("turns_into").forGetter(ModdedBrushableBlock::getTurnsInto),
                    BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("brush_sound").forGetter(ModdedBrushableBlock::getBrushSound),
                    BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("brush_completed_sound").forGetter(ModdedBrushableBlock::getBrushCompletedSound),
                    propertiesCodec()
            ).apply(instance, ModdedBrushableBlock::new)
    );

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);

    public BrushableSoulSandBlock(Block turnsInto, SoundEvent brushSound, SoundEvent brushCompletedSound, BlockBehaviour.Properties properties) {
        super(turnsInto, brushSound, brushCompletedSound, properties);
    }

    @Override
    public MapCodec<? extends BrushableBlockNoFall> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
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
