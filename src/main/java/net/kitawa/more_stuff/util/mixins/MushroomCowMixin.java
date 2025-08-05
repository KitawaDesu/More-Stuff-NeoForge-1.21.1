package net.kitawa.more_stuff.util.mixins;

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

import java.util.Optional;

@Mixin(MushroomCow.class)
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
     * @author
     * KitawaDesu
     * @reason
     * To Allow My Custom Shears to be Used on Mooshrooms
     */
    @Overwrite
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(Items.BOWL) && !this.isBaby()) {
            boolean flag = false;
            ItemStack itemstack2;
            if (this.stewEffects != null) {
                flag = true;
                itemstack2 = new ItemStack(Items.SUSPICIOUS_STEW);
                itemstack2.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, this.stewEffects);
                this.stewEffects = null;
            } else {
                itemstack2 = new ItemStack(Items.MUSHROOM_STEW);
            }

            ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, player, itemstack2, false);
            player.setItemInHand(hand, itemstack1);
            SoundEvent soundevent;
            if (flag) {
                soundevent = SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY;
            } else {
                soundevent = SoundEvents.MOOSHROOM_MILK;
            }

            this.playSound(soundevent, 1.0F, 1.0F);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (false && itemstack.is(ModItemTags.SHEARS) && this.readyForShearing()) { // Neo: Shear logic is handled by IShearable
            this.shear(SoundSource.PLAYERS);
            this.gameEvent(GameEvent.SHEAR, player);
            if (!this.level().isClientSide) {
                itemstack.hurtAndBreak(1, player, getSlotForHand(hand));
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (this.getVariant() == MushroomCow.MushroomType.BROWN && itemstack.is(ItemTags.SMALL_FLOWERS)) {
            if (this.stewEffects != null) {
                for (int i = 0; i < 2; i++) {
                    this.level()
                            .addParticle(
                                    ParticleTypes.SMOKE,
                                    this.getX() + this.random.nextDouble() / 2.0,
                                    this.getY(0.5),
                                    this.getZ() + this.random.nextDouble() / 2.0,
                                    0.0,
                                    this.random.nextDouble() / 5.0,
                                    0.0
                            );
                }
            } else {
                Optional<SuspiciousStewEffects> optional = this.getEffectsFromItemStack(itemstack);
                if (optional.isEmpty()) {
                    return InteractionResult.PASS;
                }

                itemstack.consume(1, player);

                for (int j = 0; j < 4; j++) {
                    this.level()
                            .addParticle(
                                    ParticleTypes.EFFECT,
                                    this.getX() + this.random.nextDouble() / 2.0,
                                    this.getY(0.5),
                                    this.getZ() + this.random.nextDouble() / 2.0,
                                    0.0,
                                    this.random.nextDouble() / 5.0,
                                    0.0
                            );
                }

                this.stewEffects = optional.get();
                this.playSound(SoundEvents.MOOSHROOM_EAT, 2.0F, 1.0F);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(player, hand);
        }
    }
}
