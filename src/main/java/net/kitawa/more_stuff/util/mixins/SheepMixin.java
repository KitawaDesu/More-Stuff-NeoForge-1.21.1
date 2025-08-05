package net.kitawa.more_stuff.util.mixins;

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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Sheep.class)
public abstract class SheepMixin extends Animal implements Shearable {
    protected SheepMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @author
     * KitawaDesu
     * @reason
     * To Allow My Custom Shears to be Used on Sheep
     */
    @Overwrite
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (false && itemstack.is(ModItemTags.SHEARS)) { // Neo: Shear logic is handled by IShearable
            if (!this.level().isClientSide && this.readyForShearing()) {
                this.shear(SoundSource.PLAYERS);
                this.gameEvent(GameEvent.SHEAR, player);
                itemstack.hurtAndBreak(1, player, getSlotForHand(hand));
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else {
            return super.mobInteract(player, hand);
        }
    }
}
