package net.kitawa.more_stuff.compat.create.items.util;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class CreateCompatArmorMaterials {
    public static final Holder<ArmorMaterial> ZINC = register("zinc",
            Util.make(new EnumMap<>(ArmorItem.Type.class), attribute -> {
                attribute.put(ArmorItem.Type.BOOTS, 2);
                attribute.put(ArmorItem.Type.LEGGINGS, 5);
                attribute.put(ArmorItem.Type.CHESTPLATE, 6);
                attribute.put(ArmorItem.Type.HELMET, 2);
                attribute.put(ArmorItem.Type.BODY, 5);
            }), 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F,
            () -> BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot")));
    public static final Holder<ArmorMaterial> BRASS = register("brass",
            Util.make(new EnumMap<>(ArmorItem.Type.class), attribute -> {
                attribute.put(ArmorItem.Type.BOOTS, 2);
                attribute.put(ArmorItem.Type.LEGGINGS, 5);
                attribute.put(ArmorItem.Type.CHESTPLATE, 6);
                attribute.put(ArmorItem.Type.HELMET, 2);
                attribute.put(ArmorItem.Type.BODY, 5);
            }), 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F,
            () -> BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "brass_ingot")));

    private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> typeProtection, int enchantability, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Item> ingredientItem) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name);
        Supplier<Ingredient> ingredient = () -> Ingredient.of(ingredientItem.get());
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(location));

        EnumMap<ArmorItem.Type, Integer> typeMap = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            typeMap.put(type, typeProtection.get(type));
        }

        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, location,
                new ArmorMaterial(typeProtection, enchantability, equipSound, ingredient, layers, toughness, knockbackResistance));
    }
}