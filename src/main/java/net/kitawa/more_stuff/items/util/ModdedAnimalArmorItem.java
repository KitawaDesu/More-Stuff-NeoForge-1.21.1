package net.kitawa.more_stuff.items.util;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Function;

public class ModdedAnimalArmorItem extends ArmorItem {
    private final ResourceLocation textureLocation;
    @Nullable
    private final ResourceLocation overlayTextureLocation;
    private final ModdedAnimalArmorItem.BodyType bodyType;

    public ModdedAnimalArmorItem(Holder<ArmorMaterial> armorMaterial, BodyType bodyType, boolean hasOverlay, Properties properties) {
        super(armorMaterial, ArmorItem.Type.BODY, properties);
        this.bodyType = bodyType;
        ResourceLocation resourcelocation = bodyType.textureLocator.apply(armorMaterial.unwrapKey().orElseThrow().location());
        this.textureLocation = resourcelocation.withSuffix(".png");
        if (hasOverlay) {
            this.overlayTextureLocation = resourcelocation.withSuffix("_overlay.png");
        } else {
            this.overlayTextureLocation = null;
        }
    }

    public ResourceLocation getTexture() {
        return this.textureLocation;
    }

    @Nullable
    public ResourceLocation getOverlayTexture() {
        return this.overlayTextureLocation;
    }

    public ModdedAnimalArmorItem.BodyType getBodyType() {
        return this.bodyType;
    }

    @Override
    public @NotNull SoundEvent getBreakingSound() {
        return this.bodyType.breakingSound;
    }

    /**
     * Checks isDamagable and if it cannot be stacked
     */
    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return true;
    }

    public enum BodyType {
        LEATHER_CANINE(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wolf/armor/leather_wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        WOODEN_CANINE(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wolf/armor/wood_plate_wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        CHAIN_CANINE(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wolf/armor/chainmail_wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        IRON_CANINE(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wolf/armor/iron_wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        TURTLE_SCUTE_CANINE(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wolf/armor/turtle_scute_wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        COPPER_CANINE(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wolf/armor/copper_wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        ROSE_GOLEN_CANINE(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wolf/armor/rose_golden_wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        GOLDEN_CANINE(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wolf/armor/golden_wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        DIAMOND_CANINE(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wolf/armor/diamond_wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        EMERALD_CANINE(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wolf/armor/emerald_wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        NETHERITE_CANINE(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wolf/armor/netherite_wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        ROSARITE_CANINE(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wolf/armor/rosarite_wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        HOG(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/hoglin_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        LEATHER_HOG(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/leather_hoglin_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        CHAIN_HOG(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/chainmail_hoglin_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        IRON_HOG(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/iron_hoglin_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        TURTLE_SCUTE_HOG(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/turtle_scute_hoglin_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        COPPER_HOG(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/copper_hoglin_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        ROSE_GOLEN_HOG(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/rose_golden_hoglin_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        GOLDEN_HOG(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/golden_hoglin_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        DIAMOND_HOG(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/diamond_hoglin_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        EMERALD_HOG(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/emerald_hoglin_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        NETHERITE_HOG(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/netherite_hoglin_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        ROSARITE_HOG(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/rosarite_hoglin_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_WHITE(p -> shulkerTexture("white"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_ORANGE(p -> shulkerTexture("orange"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_MAGENTA(p -> shulkerTexture("magenta"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_LIGHT_BLUE(p -> shulkerTexture("light_blue"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_YELLOW(p -> shulkerTexture("yellow"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_LIME(p -> shulkerTexture("lime"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_PINK(p -> shulkerTexture("pink"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_GRAY(p -> shulkerTexture("gray"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_LIGHT_GRAY(p -> shulkerTexture("light_gray"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_CYAN(p -> shulkerTexture("cyan"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_PURPLE(p -> shulkerTexture("purple"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_BLUE(p -> shulkerTexture("blue"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_BROWN(p -> shulkerTexture("brown"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_GREEN(p -> shulkerTexture("green"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_RED(p -> shulkerTexture("red"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_BLACK(p -> shulkerTexture("black"), SoundEvents.WOLF_ARMOR_BREAK),
        SHULKER_CANINE_NORMAL(p -> shulkerTexture("normal"), SoundEvents.WOLF_ARMOR_BREAK);

        final Function<ResourceLocation, ResourceLocation> textureLocator;
        final SoundEvent breakingSound;

        BodyType(Function<ResourceLocation, ResourceLocation> textureLocator, SoundEvent breakingSound) {
            this.textureLocator = textureLocator;
            this.breakingSound = breakingSound;
        }

        private static ResourceLocation shulkerTexture(String color) {
            return ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,
                    "textures/entity/wolf/armor/" + color + "_shulker_wolf_armor");
        }
    }
}