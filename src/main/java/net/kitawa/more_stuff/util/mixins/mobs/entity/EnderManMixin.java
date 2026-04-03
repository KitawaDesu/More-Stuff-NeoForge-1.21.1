package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.enchantments.ModEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.StreamSupport;

@Mixin(EnderMan.class)
public class EnderManMixin {

    @Inject(method = "isLookingAtMe", at = @At("HEAD"), cancellable = true)
    private void veilSightPreventsAnger(Player player, CallbackInfoReturnable<Boolean> cir) {
        var registry = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        boolean hasVeilSight = StreamSupport.stream(player.getArmorSlots().spliterator(), false)
                .anyMatch(stack -> stack.getEnchantmentLevel(
                        registry.getOrThrow(ModEnchantments.VEIL_SIGHT)) > 0);

        if (hasVeilSight) {
            cir.setReturnValue(false);
        }
    }
}