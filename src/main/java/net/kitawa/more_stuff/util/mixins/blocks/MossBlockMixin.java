package net.kitawa.more_stuff.util.mixins.blocks;

import net.kitawa.more_stuff.worldgen.level.levelgen.feature.WaterloggedVegetationPatchFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MossBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MossBlock.class)
public abstract class MossBlockMixin implements BonemealableBlock {

    @Inject(method = "isValidBonemealTarget", at = @At("HEAD"), cancellable = true)
    private void allowUnderwaterBonemeal(LevelReader level, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        BlockState above = level.getBlockState(pos.above());
        if (above.isAir() || above.getFluidState().is(Fluids.WATER)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "performBonemeal", at = @At("HEAD"), cancellable = true)
    private void performWaterloggedBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state, CallbackInfo ci) {
        level.registryAccess()
                .registry(Registries.CONFIGURED_FEATURE)
                .flatMap(reg -> reg.getHolder(CaveFeatures.MOSS_PATCH_BONEMEAL))
                .ifPresent(configuredFeature -> {
                    BlockPos placementPos = pos.above();
                    BlockState targetState = level.getBlockState(placementPos);

                    // Call place() on the configured feature — it will handle water/air correctly
                    configuredFeature.value().place(level, level.getChunkSource().getGenerator(), random, placementPos);
                });
        ci.cancel(); // prevent original method
    }
}
