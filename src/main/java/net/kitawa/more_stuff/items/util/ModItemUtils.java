package net.kitawa.more_stuff.items.util;

import net.kitawa.more_stuff.util.helpers.shield.ModShieldTextureHelper;
import net.minecraft.world.item.ItemStack;

public class ModItemUtils {
    public static boolean isCustomShield(ItemStack stack) {
        return ModShieldTextureHelper.SHIELD_TEXTURES.containsKey(stack.getItem());
    }
}
