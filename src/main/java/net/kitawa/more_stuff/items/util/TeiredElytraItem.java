package net.kitawa.more_stuff.items.util;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class TeiredElytraItem extends ElytraItem {
    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        @Override
        protected ItemStack execute(BlockSource p_302434_, ItemStack p_40409_) {
            return ArmorItem.dispenseArmor(p_302434_, p_40409_) ? p_40409_ : super.execute(p_302434_, p_40409_);
        }
    };
    protected final Holder<ArmorMaterial> material;
    private final ResourceLocation overlayTextureLocation;
    private final Supplier<ItemAttributeModifiers> defaultModifiers;

    public TeiredElytraItem(Holder<ArmorMaterial> material, ArmorItem.Type type, ResourceLocation overlayTextureLocation, Properties properties) {
        super(properties); // ✅ fixed this line
        this.material = material;
        this.overlayTextureLocation = overlayTextureLocation;

        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);

        this.defaultModifiers = Suppliers.memoize(() -> {
            int i = material.value().getDefense(type);
            float f = material.value().toughness();
            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
            EquipmentSlotGroup slotGroup = EquipmentSlotGroup.bySlot(type.getSlot());
            ResourceLocation attrId = ResourceLocation.withDefaultNamespace("armor." + type.getName());

            builder.add(Attributes.ARMOR, new AttributeModifier(attrId, i, AttributeModifier.Operation.ADD_VALUE), slotGroup);
            builder.add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(attrId, f, AttributeModifier.Operation.ADD_VALUE), slotGroup);

            float knockbackRes = material.value().knockbackResistance();
            if (knockbackRes > 0.0F) {
                builder.add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(attrId, knockbackRes, AttributeModifier.Operation.ADD_VALUE), slotGroup);
            }

            return builder.build();
        });
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return this.defaultModifiers.get();
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return ElytraItem.isFlyEnabled(stack);
    }

    @Override
    public int getEnchantmentValue() {
        return this.material.value().enchantmentValue();
    }

    public @NotNull Holder<ArmorMaterial> getMaterial() {
        return this.material;
    }

    @Override
    public Holder<SoundEvent> getEquipSound() {
        return this.getMaterial().value().equipSound();
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }

    @Nullable
    public ResourceLocation getOverlayTexture() {
        return this.overlayTextureLocation;
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, net.minecraft.world.entity.LivingEntity entity, int flightTicks) {
        if (!entity.level().isClientSide) {
            int nextFlightTick = flightTicks + 1;
            if (nextFlightTick % 10 == 0) {
                if (nextFlightTick % 20 == 0) {
                    stack.hurtAndBreak(1, entity, net.minecraft.world.entity.EquipmentSlot.CHEST);
                }
                entity.gameEvent(net.minecraft.world.level.gameevent.GameEvent.ELYTRA_GLIDE);
            }
        }
        return true;
    }
}
