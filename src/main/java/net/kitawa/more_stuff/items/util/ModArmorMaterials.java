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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModArmorMaterials {

    // Existing materials
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
                    new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "wood_plate")),
                    new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "wood_plate"), "_overlay", true)
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
            () -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS));

    public static final Holder<ArmorMaterial> COPPER = register("copper",
            Util.make(new EnumMap<>(ArmorItem.Type.class), attribute -> {
                attribute.put(ArmorItem.Type.BOOTS, 1);
                attribute.put(ArmorItem.Type.LEGGINGS, 3);
                attribute.put(ArmorItem.Type.CHESTPLATE, 4);
                attribute.put(ArmorItem.Type.HELMET, 2);
                attribute.put(ArmorItem.Type.BODY, 4);
            }), 8, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F,
            () -> Ingredient.of(Items.COPPER_INGOT));

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

    // ===== Shulker Armor Variants (All 16 colors + normal) =====
    public static final Map<String, Holder<ArmorMaterial>> SHULKER_ARMORS = new HashMap<>();
    private static final String[] SHULKER_COLORS = {
            "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
            "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black", "normal"
    };

    static {
        for (String color : SHULKER_COLORS) {
            SHULKER_ARMORS.put(color, registerShulkerArmor(color));
        }
    }

    private static Holder<ArmorMaterial> registerShulkerArmor(String color) {
        EnumMap<ArmorItem.Type, Integer> defenseMap = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, 2);
            map.put(ArmorItem.Type.LEGGINGS, 5);
            map.put(ArmorItem.Type.CHESTPLATE, 6);
            map.put(ArmorItem.Type.HELMET, 2);
            map.put(ArmorItem.Type.BODY, 5);
        });

        // Use just "shulker" for the normal one
        String name = color.equals("normal") ? "shulker" : "shulker_" + color;

        return register(
                name,
                defenseMap,
                8,
                SoundEvents.ARMOR_EQUIP_IRON,
                0.0F,
                0.0F,
                () -> Ingredient.of(Items.SHULKER_SHELL)
        );
    }

    // ===== Register methods =====
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
