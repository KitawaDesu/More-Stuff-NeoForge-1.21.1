package net.kitawa.more_stuff.util.mixins;

import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Bogged;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Bogged.class)
public abstract class BoggedMxin extends AbstractSkeleton implements Shearable {

    protected BoggedMxin(EntityType<? extends AbstractSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @author
     * KitawaDesu
     * @reason
     * To Allow My Custom Shears to be Used on Boggeds
     */
    @Overwrite
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (false && itemstack.is(ModItemTags.SHEARS) && this.readyForShearing()) { // Neo: Shear logic is handled by IShearable
            this.shear(SoundSource.PLAYERS);
            this.gameEvent(GameEvent.SHEAR, player);
            if (!this.level().isClientSide) {
                itemstack.hurtAndBreak(1, player, getSlotForHand(hand));
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(player, hand);
        }
    }
}
