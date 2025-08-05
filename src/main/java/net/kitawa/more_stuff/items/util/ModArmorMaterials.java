package net.kitawa.more_stuff.items.util;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.util.tags.ModItemTags;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {
    public static final Holder<ArmorMaterial> COPPER = register("copper",
            Util.make(new EnumMap<>(ArmorItem.Type.class), attribute -> {
                attribute.put(ArmorItem.Type.BOOTS, 2);
                attribute.put(ArmorItem.Type.LEGGINGS, 5);
                attribute.put(ArmorItem.Type.CHESTPLATE, 6);
                attribute.put(ArmorItem.Type.HELMET, 2);
                attribute.put(ArmorItem.Type.BODY, 5);
            }), 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> Items.COPPER_INGOT);
    public static final Holder<ArmorMaterial> ROSE_GOLDEN = register("rose_gold",
            Util.make(new EnumMap<>(ArmorItem.Type.class), attribute -> {
                attribute.put(ArmorItem.Type.BOOTS, 2);
                attribute.put(ArmorItem.Type.LEGGINGS, 5);
                attribute.put(ArmorItem.Type.CHESTPLATE, 6);
                attribute.put(ArmorItem.Type.HELMET, 2);
                attribute.put(ArmorItem.Type.BODY, 5);
            }), 34, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, () -> ModItems.ROSE_GOLD_INGOT.asItem());
    public static final Holder<ArmorMaterial> EMERALD = register("emerald",
            Util.make(new EnumMap<>(ArmorItem.Type.class), attribute -> {
                attribute.put(ArmorItem.Type.BOOTS, 3);
                attribute.put(ArmorItem.Type.LEGGINGS, 6);
                attribute.put(ArmorItem.Type.CHESTPLATE, 8);
                attribute.put(ArmorItem.Type.HELMET, 3);
                attribute.put(ArmorItem.Type.BODY, 11);
            }), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, () -> Items.EMERALD);
    public static final Holder<ArmorMaterial> ROSARITE = register("rosarite",
            Util.make(new EnumMap<>(ArmorItem.Type.class), attribute -> {
                attribute.put(ArmorItem.Type.BOOTS, 4);
                attribute.put(ArmorItem.Type.LEGGINGS, 9);
                attribute.put(ArmorItem.Type.CHESTPLATE, 13);
                attribute.put(ArmorItem.Type.HELMET, 5);
                attribute.put(ArmorItem.Type.BODY, 18);
            }), 40, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, () -> ModItems.ROSARITE_INGOT.asItem());


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