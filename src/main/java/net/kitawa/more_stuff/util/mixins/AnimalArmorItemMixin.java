package net.kitawa.more_stuff.util.mixins;

import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AnimalArmorItem.class)
public class AnimalArmorItemMixin {
    /**
     * @author
     * KitawaDesu
     * @reason
     * To Allow for more Items to be Enchanted
     */
    @Overwrite
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
}
