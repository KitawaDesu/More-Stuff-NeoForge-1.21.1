package net.kitawa.more_stuff.util.mixins.items;

import net.kitawa.more_stuff.blocks.custom.general.entities.ModdedBrushableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrushItem.class)
public abstract class BrushItemMixin {

    @Shadow
    private HitResult calculateHitResult(Player player) {
        throw new AbstractMethodError();
    }

    @Inject(
            method = "onUseTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;I)V",
            at = @At("HEAD")
    )
    private void callModdedBrushable(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration, CallbackInfo ci) {
        if (!(livingEntity instanceof Player player)) return;

        HitResult hitResult = this.calculateHitResult(player);
        if (!(hitResult instanceof BlockHitResult blockHitResult)) return;

        BlockPos pos = blockHitResult.getBlockPos();
        if (level.isClientSide()) return;

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ModdedBrushableBlockEntity modded) {
            boolean completed = modded.brush(level.getGameTime(), player, blockHitResult.getDirection());
            if (completed) {
                EquipmentSlot slot = stack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND))
                        ? EquipmentSlot.OFFHAND
                        : EquipmentSlot.MAINHAND;
                stack.hurtAndBreak(1, livingEntity, slot);
            }
        }
    }
}