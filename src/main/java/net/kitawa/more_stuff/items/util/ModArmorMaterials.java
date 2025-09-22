package net.kitawa.more_stuff.items.util;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.items.ModItems;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {
    public static final Holder<ArmorMaterial> WOOD_PLATE = register(
            "wood_plate",
            Util.make(new EnumMap<>(ArmorItem.Type.class), attributes -> {
                attributes.put(ArmorItem.Type.BOOTS, 1);
                attributes.put(ArmorItem.Type.LEGGINGS, 2);
                attributes.put(ArmorItem.Type.CHESTPLATE, 2);
                attributes.put(ArmorItem.Type.HELMET, 1);
                attributes.put(ArmorItem.Type.BODY, 2);
            }),
            1,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0.0F,
            0.0F,
            () -> Ingredient.of(ItemTags.PLANKS),
            List.of(
                    new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "wood_plate")
                    ),
                    new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "wood_plate"), "_overlay", true
                    )
            )
    );
    public static final Holder<ArmorMaterial> STONE_PLATE = register("plate",
            Util.make(new EnumMap<>(ArmorItem.Type.class), attributes -> {
                attributes.put(ArmorItem.Type.BOOTS, 2);
                attributes.put(ArmorItem.Type.LEGGINGS, 3);
                attributes.put(ArmorItem.Type.CHESTPLATE, 3);
                attributes.put(ArmorItem.Type.HELMET, 1);
                attributes.put(ArmorItem.Type.BODY, 3);
            }), 2, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F,
            () -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS)); // Uses item tag

    public static final Holder<ArmorMaterial> COPPER = register("copper",
            Util.make(new EnumMap<>(ArmorItem.Type.class), attribute -> {
                attribute.put(ArmorItem.Type.BOOTS, 1);
                attribute.put(ArmorItem.Type.LEGGINGS, 3);
                attribute.put(ArmorItem.Type.CHESTPLATE, 4);
                attribute.put(ArmorItem.Type.HELMET, 2);
                attribute.put(ArmorItem.Type.BODY, 4);
            }), 8, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F,
            () -> Ingredient.of(Items.COPPER_INGOT)); // Uses single item

    public static final Holder<ArmorMaterial> ROSE_GOLDEN = register("rose_gold",
            Util.make(new EnumMap<>(ArmorItem.Type.class), attribute -> {
                attribute.put(ArmorItem.Type.BOOTS, 2);
                attribute.put(ArmorItem.Type.LEGGINGS, 5);
                attribute.put(ArmorItem.Type.CHESTPLATE, 6);
                attribute.put(ArmorItem.Type.HELMET, 2);
                attribute.put(ArmorItem.Type.BODY, 5);
            }), 33, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F,
            () -> Ingredient.of(ModItems.ROSE_GOLD_INGOT.get()));

    public static final Holder<ArmorMaterial> EMERALD = register("emerald",
            Util.make(new EnumMap<>(ArmorItem.Type.class), attribute -> {
                attribute.put(ArmorItem.Type.BOOTS, 3);
                attribute.put(ArmorItem.Type.LEGGINGS, 6);
                attribute.put(ArmorItem.Type.CHESTPLATE, 8);
                attribute.put(ArmorItem.Type.HELMET, 3);
                attribute.put(ArmorItem.Type.BODY, 11);
            }), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F,
            () -> Ingredient.of(Items.EMERALD));

    public static final Holder<ArmorMaterial> ROSARITE = register("rosarite",
            Util.make(new EnumMap<>(ArmorItem.Type.class), attribute -> {
                attribute.put(ArmorItem.Type.BOOTS, 3);
                attribute.put(ArmorItem.Type.LEGGINGS, 6);
                attribute.put(ArmorItem.Type.CHESTPLATE, 8);
                attribute.put(ArmorItem.Type.HELMET, 3);
                attribute.put(ArmorItem.Type.BODY, 11);
            }), 40, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F,
            () -> Ingredient.of(ModItems.ROSARITE_INGOT.get()));

    // ✨ Modified register method to accept Ingredient supplier
    private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> typeProtection, int enchantability,
                                                  Holder<SoundEvent> equipSound, float toughness, float knockbackResistance,
                                                  Supplier<Ingredient> ingredientSupplier) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name);
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(location));

        EnumMap<ArmorItem.Type, Integer> typeMap = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            typeMap.put(type, typeProtection.get(type));
        }

        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, location,
                new ArmorMaterial(typeMap, enchantability, equipSound, ingredientSupplier, layers, toughness, knockbackResistance));
    }

    private static Holder<ArmorMaterial> register(
            String name,
            EnumMap<ArmorItem.Type, Integer> typeProtection,
            int enchantability,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> ingredientSupplier,
            List<ArmorMaterial.Layer> layers
    ) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name);

        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, location,
                new ArmorMaterial(typeProtection, enchantability, equipSound, ingredientSupplier, layers, toughness, knockbackResistance));
    }
}