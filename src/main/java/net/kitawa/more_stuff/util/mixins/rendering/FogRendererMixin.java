package net.kitawa.more_stuff.util.mixins.rendering;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.kitawa.more_stuff.util.helpers.VeilSightHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FogRenderer.class)
@OnlyIn(Dist.CLIENT)
public class FogRendererMixin {

    @Inject(
            method = "getPriorityFogFunction(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/client/renderer/FogRenderer$MobEffectFogFunction;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void veilSightNullifyFogFunction(Entity entity, float partialTick,
                                                    CallbackInfoReturnable<?> cir) {
        if (!(entity instanceof Player)) return;
        if (!VeilSightHelper.localPlayerHasVeilSight()) return;
        cir.setReturnValue(null);
    }
}