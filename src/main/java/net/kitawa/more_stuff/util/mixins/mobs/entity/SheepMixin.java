package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Sheep.class, priority = 500)
public abstract class SheepMixin extends Animal implements Shearable {
    protected SheepMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @author KitawaDesu
     * @reason Replace overwrite with injection so custom shears work without breaking vanilla/shear mods
     */
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void moreStuff$customShearInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack.is(ModItemTags.SHEARS) && this.readyForShearing()) {
            if (!this.level().isClientSide) {
                this.shear(SoundSource.PLAYERS);
                this.gameEvent(GameEvent.SHEAR, player);
                itemstack.hurtAndBreak(1, player, getSlotForHand(hand));
            }
            cir.setReturnValue(this.level().isClientSide ? InteractionResult.CONSUME : InteractionResult.SUCCESS);
        }
    }
}
