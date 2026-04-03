package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SnowGolem.class, priority = 500)
public abstract class SnowGolemMixin extends AbstractGolem implements Shearable, RangedAttackMob {

    protected SnowGolemMixin(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @author KitawaDesu
     * @reason Inject shear logic for custom shears without overwriting vanilla behavior
     */
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void moreStuff$customShearInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack.is(ModItemTags.SHEARS) && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            this.gameEvent(GameEvent.SHEAR, player);
            if (!this.level().isClientSide) {
                itemstack.hurtAndBreak(1, player, getSlotForHand(hand));
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
        }
    }
}