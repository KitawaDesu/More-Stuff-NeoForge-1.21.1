package net.kitawa.more_stuff.util.mixins.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SimpleBlockFeature.class)
public abstract class SimpleBlockFeatureMixin extends Feature<SimpleBlockConfiguration> {

    public SimpleBlockFeatureMixin(Codec<SimpleBlockConfiguration> codec) {
        super(codec);
    }

    /**
     * @author
     * KitawaDesu
     * @reason
     * T0 make blocks Waterloggable
     */
    @Overwrite
    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
        SimpleBlockConfiguration config = context.config();
        WorldGenLevel world = context.level();
        BlockPos pos = context.origin();
        BlockState state = config.toPlace().getState(context.random(), pos);

        // Check if the block can survive at the position
        if (!state.canSurvive(world, pos)) {
            return false;
        }

        FluidState fluid = world.getFluidState(pos);
        boolean isWaterlogged = fluid.is(Fluids.WATER);

        // Handle double plants
        if (state.getBlock() instanceof DoublePlantBlock) {
            if (!world.isEmptyBlock(pos.above())) {
                return false;
            }

            DoublePlantBlock.placeAt(world, state, pos, 2);
            return true;
        }

        // If the block supports waterlogging, set the property
        if (state.hasProperty(BlockStateProperties.WATERLOGGED) && isWaterlogged) {
            state = state.setValue(BlockStateProperties.WATERLOGGED, true);
        }

        world.setBlock(pos, state, 2);
        return true;
    }
}

