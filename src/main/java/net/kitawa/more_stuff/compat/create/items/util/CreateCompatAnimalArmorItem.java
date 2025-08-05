package net.kitawa.more_stuff.compat.create.items.util;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Function;

public class CreateCompatAnimalArmorItem extends ArmorItem {
    private final ResourceLocation textureLocation;
    @Nullable
    private final ResourceLocation overlayTextureLocation;
    private final CreateCompatAnimalArmorItem.BodyType bodyType;

    public CreateCompatAnimalArmorItem(Holder<ArmorMaterial> armorMaterial, BodyType bodyType, boolean hasOverlay, Properties properties) {
        super(armorMaterial, Type.BODY, properties);
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

    public CreateCompatAnimalArmorItem.BodyType getBodyType() {
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
        ZINC_CANINE(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wolf/armor/zinc_wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        BRASS_CANINE(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wolf/armor/brass_wolf_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        ZINC_HOG(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/zinc_hoglin_armor"), SoundEvents.WOLF_ARMOR_BREAK),
        BRASS_HOG(p_323678_ -> ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/brass_hoglin_armor"), SoundEvents.WOLF_ARMOR_BREAK);

        final Function<ResourceLocation, ResourceLocation> textureLocator;
        final SoundEvent breakingSound;

        BodyType(Function<ResourceLocation, ResourceLocation> textureLocator, SoundEvent breakingSound) {
            this.textureLocator = textureLocator;
            this.breakingSound = breakingSound;
        }
    }
}