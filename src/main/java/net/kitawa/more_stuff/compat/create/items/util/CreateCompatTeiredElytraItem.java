package net.kitawa.more_stuff.compat.create.items.util;

import com.google.common.base.Suppliers;
import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CreateCompatTeiredElytraItem extends ElytraItem {
    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        @Override
        protected ItemStack execute(BlockSource p_302434_, ItemStack p_40409_) {
            return ArmorItem.dispenseArmor(p_302434_, p_40409_) ? p_40409_ : super.execute(p_302434_, p_40409_);
        }
    };
    protected final Holder<ArmorMaterial> material;
    private final ResourceLocation overlayTextureLocation;
    private final Supplier<ItemAttributeModifiers> defaultModifiers;

    public CreateCompatTeiredElytraItem(Holder<ArmorMaterial> material, ArmorItem.Type type, ResourceLocation overlayTextureLocation, Properties properties) {
        super(properties);
        this.material = material;
        this.overlayTextureLocation = overlayTextureLocation;
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
        this.defaultModifiers = Suppliers.memoize(
                () -> {
                    int i = material.value().getDefense(type);
                    float f = material.value().toughness();
                    ItemAttributeModifiers.Builder itemattributemodifiers$builder = ItemAttributeModifiers.builder();
                    EquipmentSlotGroup equipmentslotgroup = EquipmentSlotGroup.bySlot(type.getSlot());
                    ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace("armor." + type.getName());
                    itemattributemodifiers$builder.add(
                            Attributes.ARMOR, new AttributeModifier(resourcelocation, (double)i, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup
                    );
                    itemattributemodifiers$builder.add(
                            Attributes.ARMOR_TOUGHNESS, new AttributeModifier(resourcelocation, (double)f, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup
                    );
                    float f1 = material.value().knockbackResistance();
                    if (f1 > 0.0F) {
                        itemattributemodifiers$builder.add(
                                Attributes.KNOCKBACK_RESISTANCE,
                                new AttributeModifier(resourcelocation, (double)f1, AttributeModifier.Operation.ADD_VALUE),
                                equipmentslotgroup
                        );
                    }

                    return itemattributemodifiers$builder.build();
                }
        );
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
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (!entity.level().isClientSide) {
            int nextFlightTick = flightTicks + 1;
            if (nextFlightTick % 10 == 0) {
                if (nextFlightTick % 20 == 0) {
                    stack.hurtAndBreak(1, entity, EquipmentSlot.CHEST);
                }
                entity.gameEvent(net.minecraft.world.level.gameevent.GameEvent.ELYTRA_GLIDE);
            }
        }
        return true;
    }
}
