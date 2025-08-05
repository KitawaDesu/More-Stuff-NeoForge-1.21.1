package net.kitawa.more_stuff.util.mixins;

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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SnowGolem.class)
public abstract class SnowGolemMixin extends AbstractGolem implements Shearable, RangedAttackMob {

    protected SnowGolemMixin(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @author
     * KitawaDesu
     * @reason
     * To Allow My Custom Shears to be Used on Snow Golems
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
            return InteractionResult.PASS;
        }
    }
}
