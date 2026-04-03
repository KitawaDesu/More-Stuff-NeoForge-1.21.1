package net.kitawa.more_stuff.items.util.weapons.dynamarrow;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DynamicArrowItem extends ArrowItem implements ProjectileItem {

    public DynamicArrowItem(Item.Properties properties) {
        super(properties);
    }

    // === Default instance ===
    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.set(ArrowDataComponents.HAS_POTION, false);
        return stack;
    }

    // === Tooltip display ===
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Potion info
        boolean hasPotion = stack.getOrDefault(ArrowDataComponents.HAS_POTION, false);
        if (hasPotion) {
            PotionContents potionContents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            if (!potionContents.equals(PotionContents.EMPTY)) {
                // mimic PotionItem tooltip behavior
                potionContents.addPotionTooltip(
                        line -> tooltip.add(line.copy()),
                        1.0F, // same as PotionItem (1.0F)
                        context.tickRate()
                );
            }
        } // fixed
        // === Basic tooltips (always shown) ===
        addDefaultStringTooltip(stack, tooltip, "tip", ArrowDataComponents.TIP);
        addDefaultStringTooltip(stack, tooltip, "shaft", ArrowDataComponents.SHAFT);
        addDefaultStringTooltip(stack, tooltip, "fletching", ArrowDataComponents.FLETCHING);
        addDefaultResourceTooltip(stack, tooltip, "modifier", ArrowDataComponents.MODIFIER); // blank line for separation

        // Only show advanced tooltips if F3+H is enabled
        if (flag.isAdvanced()) {
            tooltip.add(Component.literal(""));
            // Other arrow stats (gray and smaller)
            addDoubleTooltip(stack, tooltip, "gravity", ArrowDataComponents.GRAVITY, true);
            addDoubleTooltip(stack, tooltip, "base_damage", ArrowDataComponents.BASE_DAMAGE, true);
            addDoubleTooltip(stack, tooltip, "damage_multiplier", ArrowDataComponents.DAMAGE_MULTIPLIER, true);
            addFloatTooltip(stack, tooltip, "water_inertia", ArrowDataComponents.WATER_INERTIA, true);
            addFloatTooltip(stack, tooltip, "crit", ArrowDataComponents.CRIT, true);
            addBooleanTooltip(stack, tooltip, "no_physics", ArrowDataComponents.NO_PHYSICS, true);
            addBooleanTooltip(stack, tooltip, "on_fire", ArrowDataComponents.ON_FIRE, true);
            addBooleanTooltip(stack, tooltip, "explosive", ArrowDataComponents.EXPLOSIVE, true);
            addIntTooltip(stack, tooltip, "pierce_level", ArrowDataComponents.PIERCE_LEVEL, true);
            addResourceTooltip(stack, tooltip, "sound_event", ArrowDataComponents.SOUND_EVENT, true);
        }
    }

    // === Overloaded helpers for gray small text in advanced tooltips ===
    private void addDoubleTooltip(ItemStack stack, List<Component> tooltip, String key, Supplier<DataComponentType<Double>> accessor, boolean advanced) {
        DataComponentType<Double> type = accessor.get();
        if (stack.has(type)) {
            Component line = Component.translatable("tooltip.more_stuff." + key, stack.get(type));
            if (advanced) line = line.copy().withStyle(ChatFormatting.GRAY);
            tooltip.add(line);
        }
    }

    private void addFloatTooltip(ItemStack stack, List<Component> tooltip, String key, Supplier<DataComponentType<Float>> accessor, boolean advanced) {
        DataComponentType<Float> type = accessor.get();
        if (stack.has(type)) {
            Component line = Component.translatable("tooltip.more_stuff." + key, stack.get(type));
            if (advanced) line = line.copy().withStyle(ChatFormatting.GRAY);
            tooltip.add(line);
        }
    }

    private void addBooleanTooltip(ItemStack stack, List<Component> tooltip, String key, Supplier<DataComponentType<Boolean>> accessor, boolean advanced) {
        DataComponentType<Boolean> type = accessor.get();
        if (stack.has(type)) {
            boolean value = stack.get(type);
            Component localizedValue = Component.translatable(value ? "tooltip.more_stuff.value.yes" : "tooltip.more_stuff.value.no");
            Component line = Component.translatable("tooltip.more_stuff." + key, localizedValue);
            if (advanced) line = line.copy().withStyle(ChatFormatting.GRAY);
            tooltip.add(line);
        }
    }

    private void addIntTooltip(ItemStack stack, List<Component> tooltip, String key, Supplier<DataComponentType<Integer>> accessor, boolean advanced) {
        DataComponentType<Integer> type = accessor.get();
        if (stack.has(type)) {
            Component line = Component.translatable("tooltip.more_stuff." + key, stack.get(type));
            if (advanced) line = line.copy().withStyle(ChatFormatting.GRAY);
            tooltip.add(line);
        }
    }

    private void addResourceTooltip(ItemStack stack, List<Component> tooltip, String key, Supplier<DataComponentType<ResourceLocation>> accessor, boolean advanced) {
        DataComponentType<ResourceLocation> type = accessor.get();
        if (stack.has(type)) {
            ResourceLocation value = stack.get(type);
            if (value != null) {
                Component line = Component.translatable("tooltip.more_stuff." + key, value.toString());
                if (advanced) line = line.copy().withStyle(ChatFormatting.GRAY);
                tooltip.add(line);
            }
        }
    }

    // === Description ID (potion display) ===
    @Override
    public String getDescriptionId(ItemStack stack) {
        boolean hasPotion = stack.getOrDefault(ArrowDataComponents.HAS_POTION, false);
        if (hasPotion) {
            PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            return Potion.getName(contents.potion(), super.getDescriptionId() + ".effect.");
        }
        return super.getDescriptionId();
    }

    // === Sync potion flag ===
    private void syncPotionFlag(ItemStack stack) {
        PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        stack.set(ArrowDataComponents.HAS_POTION, !contents.equals(PotionContents.EMPTY));
    }

    // === Fired arrow ===
    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        ItemStack arrowStack = ammo.copyWithCount(1);
        syncPotionFlag(arrowStack);

        DynamicArrowEntity arrow = new DynamicArrowEntity(level, shooter, arrowStack, weapon);
        arrow.setItemStack(arrowStack); // <-- uses your applyComponentsFromStack internally
        Vec3 look = shooter.getLookAngle();
        arrow.shoot(look.x, look.y, look.z, 3.0F, 0.0F);
        return arrow;
    }

    // === Dispenser / placement ===
    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        ItemStack arrowStack = stack.copyWithCount(1);
        syncPotionFlag(arrowStack);

        DynamicArrowEntity arrow = new DynamicArrowEntity(level, pos.x(), pos.y(), pos.z(), arrowStack, null);
        arrow.setItemStack(arrowStack); // <-- applies all stored data
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }

    @Override
    public boolean isInfinite(ItemStack ammo, ItemStack bow, LivingEntity shooter) {
        return super.isInfinite(ammo, bow, shooter);
    }

    private void addStringTooltip(ItemStack stack, List<Component> tooltip, String key, Supplier<DataComponentType<String>> accessor) {
        DataComponentType<String> type = accessor.get();
        if (stack.has(type)) {
            String value = stack.get(type);
            if (value != null) tooltip.add(Component.translatable("tooltip.more_stuff." + key, value));
        }
    }

    private void addDefaultResourceTooltip(ItemStack stack, List<Component> tooltip, String key, Supplier<DataComponentType<ResourceLocation>> accessor) {
        DataComponentType<ResourceLocation> type = accessor.get();
        System.out.println("Checking tooltip for key: " + key + ", has type = " + stack.has(type));

        if (stack.has(type)) {
            ResourceLocation value = stack.get(type);
            System.out.println("Modifier value = " + value);
            if (value != null) {
                Item item = BuiltInRegistries.ITEM.get(value);
                System.out.println("Resolved item = " + item);

                if (item != null && item != net.minecraft.world.item.Items.AIR) {
                    Component itemName = new ItemStack(item).getHoverName();
                    tooltip.add(Component.translatable("tooltip.more_stuff." + key, itemName).copy());
                } else {
                    tooltip.add(Component.translatable("tooltip.more_stuff." + key, value.toString()).copy());
                }
            }
        }
    }

    private void addDefaultStringTooltip(ItemStack stack, List<Component> tooltip, String key, Supplier<DataComponentType<String>> accessor) {
        DataComponentType<String> type = accessor.get();
        if (!stack.has(type)) return;

        String value = stack.get(type);
        if (value == null || value.isEmpty()) return;

        // Try resolving as item ID (like "more_stuff:iron_tip")
        ResourceLocation maybeId = ResourceLocation.tryParse(value);
        if (maybeId != null && BuiltInRegistries.ITEM.containsKey(maybeId)) {
            Item item = BuiltInRegistries.ITEM.get(maybeId);
            if (item != null && item != Items.AIR) {
                Component itemName = new ItemStack(item).getHoverName();
                tooltip.add(Component.translatable("tooltip.more_stuff." + key, itemName));
                return;
            }
        }

        // Fallback: show raw string if it’s not an item
        tooltip.add(Component.translatable("tooltip.more_stuff." + key, value));
    }
}
