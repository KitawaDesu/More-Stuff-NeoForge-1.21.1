package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = MushroomCow.class, priority = 500)
public abstract class MushroomCowMixin extends Cow implements Shearable, VariantHolder<MushroomCow.MushroomType> {
    @Shadow
    private SuspiciousStewEffects stewEffects;

    public MushroomCowMixin(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    private Optional<SuspiciousStewEffects> getEffectsFromItemStack(ItemStack stack) {
        SuspiciousEffectHolder suspiciouseffectholder = SuspiciousEffectHolder.tryGet(stack.getItem());
        return suspiciouseffectholder != null ? Optional.of(suspiciouseffectholder.getSuspiciousEffects()) : Optional.empty();
    }

    /**
     * @author KitawaDesu
     * @reason Replace Overwrite with Injection to support custom shears without breaking other mods
     */
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void moreStuff$customMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemstack = player.getItemInHand(hand);

        // Custom shear logic first
        if (itemstack.is(ModItemTags.SHEARS) && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            this.gameEvent(GameEvent.SHEAR, player);
            if (!this.level().isClientSide) {
                itemstack.hurtAndBreak(1, player, getSlotForHand(hand));
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
            return;
        }

        // Otherwise, don’t override vanilla – let it fall through
    }
}
