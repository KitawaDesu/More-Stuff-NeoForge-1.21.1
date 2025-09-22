package net.kitawa.more_stuff.util.helpers.shield;

import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.items.util.ModModelBakery;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;

import java.util.HashMap;
import java.util.Map;

public class ModShieldTextureHelper {

    public static final Map<Item, ShieldTextures> SHIELD_TEXTURES = new HashMap<>();

    static {
        SHIELD_TEXTURES.put(ModItems.STONE_SHIELD.get(), new ShieldTextures(ModModelBakery.STONE_SHIELD_BASE, ModModelBakery.STONE_NO_PATTERN_SHIELD));
        SHIELD_TEXTURES.put(ModItems.LAPIS_SHIELD.get(), new ShieldTextures(ModModelBakery.LAPIS_SHIELD_BASE, ModModelBakery.LAPIS_NO_PATTERN_SHIELD));
        SHIELD_TEXTURES.put(ModItems.QUARTZ_SHIELD.get(), new ShieldTextures(ModModelBakery.QUARTZ_SHIELD_BASE, ModModelBakery.QUARTZ_NO_PATTERN_SHIELD));
        SHIELD_TEXTURES.put(ModItems.COPPER_SHIELD.get(), new ShieldTextures(ModModelBakery.COPPER_SHIELD_BASE, ModModelBakery.COPPER_NO_PATTERN_SHIELD));
        SHIELD_TEXTURES.put(ModItems.GOLDEN_SHIELD.get(), new ShieldTextures(ModModelBakery.GOLDEN_SHIELD_BASE, ModModelBakery.GOLDEN_NO_PATTERN_SHIELD));
        SHIELD_TEXTURES.put(ModItems.ROSE_GOLDEN_SHIELD.get(), new ShieldTextures(ModModelBakery.ROSE_GOLDEN_SHIELD_BASE, ModModelBakery.ROSE_GOLDEN_NO_PATTERN_SHIELD));
        SHIELD_TEXTURES.put(ModItems.IRON_SHIELD.get(), new ShieldTextures(ModModelBakery.IRON_SHIELD_BASE, ModModelBakery.IRON_NO_PATTERN_SHIELD));
        SHIELD_TEXTURES.put(ModItems.DIAMOND_SHIELD.get(), new ShieldTextures(ModModelBakery.DIAMOND_SHIELD_BASE, ModModelBakery.DIAMOND_NO_PATTERN_SHIELD));
        SHIELD_TEXTURES.put(ModItems.EMERALD_SHIELD.get(), new ShieldTextures(ModModelBakery.EMERALD_SHIELD_BASE, ModModelBakery.EMERALD_NO_PATTERN_SHIELD));
        SHIELD_TEXTURES.put(ModItems.NETHERITE_SHIELD.get(), new ShieldTextures(ModModelBakery.NETHERITE_SHIELD_BASE, ModModelBakery.NETHERITE_NO_PATTERN_SHIELD));
        SHIELD_TEXTURES.put(ModItems.ROSARITE_SHIELD.get(), new ShieldTextures(ModModelBakery.ROSARITE_SHIELD_BASE, ModModelBakery.ROSARITE_NO_PATTERN_SHIELD));

        // Conditionally add Create shields if Create is loaded
        if (ModList.get().isLoaded("create")) {
            if (ModModelBakery.ZINC_SHIELD_BASE != null) {
                SHIELD_TEXTURES.put(CreateCompatItems.ZINC_SHIELD.get(), new ShieldTextures(ModModelBakery.ZINC_SHIELD_BASE, ModModelBakery.ZINC_NO_PATTERN_SHIELD));
            }
            if (ModModelBakery.BRASS_SHIELD_BASE != null) {
                SHIELD_TEXTURES.put(CreateCompatItems.BRASS_SHIELD.get(), new ShieldTextures(ModModelBakery.BRASS_SHIELD_BASE, ModModelBakery.BRASS_NO_PATTERN_SHIELD));
            }
        }
    }
}
