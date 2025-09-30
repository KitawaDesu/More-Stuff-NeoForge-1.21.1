package net.kitawa.more_stuff.compat.create.items;

import net.kitawa.more_stuff.compat.create.items.util.CreateCompatArmorMaterials;
import net.kitawa.more_stuff.compat.create.items.util.CreateCompatTeiredElytraItem;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.compat.create.items.util.CreateCompatAnimalArmorItem;
import net.kitawa.more_stuff.compat.create.items.util.shears.BrassShearsItem;
import net.kitawa.more_stuff.items.util.ModShieldItem;
import net.kitawa.more_stuff.items.util.weapons.ModdedMaceItem;
import net.kitawa.more_stuff.items.util.shears.IronShearsItem;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreateCompatItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MoreStuff.MOD_ID);

    public static final DeferredItem<Item> COPPER_DUST = ITEMS.register("copper_dust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ANCIENT_DUST = ITEMS.register("ancient_dust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GOLDEN_DUST = ITEMS.register("golden_dust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> IRON_DUST = ITEMS.register("iron_dust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRUSHED_ANCIENT_DEBRIS = ITEMS.register("crushed_ancient_debris",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BRASS_DUST = ITEMS.register("brass_dust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ZINC_DUST = ITEMS.register("zinc_dust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CRUSHED_BRASS = ITEMS.register("crushed_brass",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ZINC_SHIELD = ITEMS.register("zinc_shield",
            () -> new ModShieldItem(new Item.Properties().durability(500).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY), BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot"))));
    public static final DeferredItem<Item> BRASS_SHIELD = ITEMS.register("brass_shield",
            () -> new ModShieldItem(new Item.Properties().durability(536).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY), BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "brass_ingot"))));

    public static final DeferredItem<Item> ZINC_HELMET = ITEMS.register("zinc_helmet",
            () -> new ArmorItem(CreateCompatArmorMaterials.ZINC, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
    public static final DeferredItem<Item> ZINC_CHESTPLATE = ITEMS.register("zinc_chestplate",
            () -> new ArmorItem(CreateCompatArmorMaterials.ZINC, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
    public static final DeferredItem<Item> ZINC_LEGGINGS = ITEMS.register("zinc_leggings",
            () -> new ArmorItem(CreateCompatArmorMaterials.ZINC, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
    public static final DeferredItem<Item> ZINC_BOOTS = ITEMS.register("zinc_boots",
            () -> new ArmorItem(CreateCompatArmorMaterials.ZINC, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));

    public static final DeferredItem<Item> BRASS_HELMET = ITEMS.register("brass_helmet",
            () -> new ArmorItem(CreateCompatArmorMaterials.BRASS, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15+11))));
    public static final DeferredItem<Item> BRASS_CHESTPLATE = ITEMS.register("brass_chestplate",
            () -> new ArmorItem(CreateCompatArmorMaterials.BRASS, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15+11))));
    public static final DeferredItem<Item> BRASS_LEGGINGS = ITEMS.register("brass_leggings",
            () -> new ArmorItem(CreateCompatArmorMaterials.BRASS, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15+11))));
    public static final DeferredItem<Item> BRASS_BOOTS = ITEMS.register("brass_boots",
            () -> new ArmorItem(CreateCompatArmorMaterials.BRASS, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15+11))));

    public static final DeferredItem<Item> BRASS_PICKAXE = ITEMS.register("brass_pickaxe",
            () -> new PickaxeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_BRASS_TOOL, 190+250, 11.0F, 2.0F, 14, () -> Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "brass_ingot")))), new Item.Properties().attributes(PickaxeItem.createAttributes(Tiers.IRON, 1.0F, -2.8F))));
    public static final DeferredItem<Item> BRASS_HOE = ITEMS.register("brass_hoe",
            () -> new HoeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_BRASS_TOOL, 190+250, 11.0F, 2.0F, 14, () -> Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "brass_ingot")))), new Item.Properties().attributes(HoeItem.createAttributes(Tiers.IRON, -4.0F, 0.0F))));
    public static final DeferredItem<Item> BRASS_SWORD = ITEMS.register("brass_sword",
            () -> new SwordItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_BRASS_TOOL, 190+250, 11.0F, 2.0F, 14, () -> Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "brass_ingot")))), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.4F))));
    public static final DeferredItem<Item> BRASS_SHOVEL = ITEMS.register("brass_shovel",
            () -> new ShovelItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_BRASS_TOOL, 190+250, 11.0F, 2.0F, 14, () -> Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "brass_ingot")))), new Item.Properties().attributes(ShovelItem.createAttributes(Tiers.IRON, 1.5F, -3.0F))));
    public static final DeferredItem<Item> BRASS_AXE = ITEMS.register("brass_axe",
            () -> new AxeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_BRASS_TOOL, 190+250, 11.0F, 2.0F, 14, () -> Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "brass_ingot")))), new Item.Properties().attributes(AxeItem.createAttributes(Tiers.IRON, 5.0F, -3.0F))));

    public static final DeferredItem<Item> ZINC_PICKAXE = ITEMS.register("zinc_pickaxe",
            () -> new PickaxeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_ZINC_TOOL, 250, 6.0F, 2.0F, 14, () -> Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot")))), new Item.Properties().attributes(PickaxeItem.createAttributes(Tiers.IRON, 1.0F, -2.8F))));
    public static final DeferredItem<Item> ZINC_HOE = ITEMS.register("zinc_hoe",
            () -> new HoeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_ZINC_TOOL, 250, 6.0F, 2.0F, 14, () -> Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot")))), new Item.Properties().attributes(HoeItem.createAttributes(Tiers.IRON, -4.0F, 0.0F))));
    public static final DeferredItem<Item> ZINC_SWORD = ITEMS.register("zinc_sword",
            () -> new SwordItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_ZINC_TOOL, 250, 6.0F, 2.0F, 14, () -> Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot")))), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.4F))));
    public static final DeferredItem<Item> ZINC_SHOVEL = ITEMS.register("zinc_shovel",
            () -> new ShovelItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_ZINC_TOOL, 250, 6.0F, 2.0F, 14, () -> Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot")))), new Item.Properties().attributes(ShovelItem.createAttributes(Tiers.IRON, 1.5F, -3.0F))));
    public static final DeferredItem<Item> ZINC_AXE = ITEMS.register("zinc_axe",
            () -> new AxeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_ZINC_TOOL, 250, 6.0F, 2.0F, 14, () -> Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot")))), new Item.Properties().attributes(AxeItem.createAttributes(Tiers.IRON, 5.0F, -3.0F))));

    public static final DeferredItem<Item> ZINC_ELYTRA = ITEMS.register("zinc_elytra",
            () -> new CreateCompatTeiredElytraItem(CreateCompatArmorMaterials.ZINC, ArmorItem.Type.CHESTPLATE, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"textures/entity/wings/zinc.png"), new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15+27)).rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> BRASS_ELYTRA = ITEMS.register("brass_elytra",
            () -> new CreateCompatTeiredElytraItem(CreateCompatArmorMaterials.BRASS, ArmorItem.Type.CHESTPLATE, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"textures/entity/wings/brass.png"), new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(26+27)).rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> ZINC_WOLF_ARMOR = ITEMS.register("zinc_wolf_armor",
            () -> new CreateCompatAnimalArmorItem(CreateCompatArmorMaterials.ZINC, CreateCompatAnimalArmorItem.BodyType.ZINC_CANINE, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15))));

    public static final DeferredItem<Item> ZINC_HOGLIN_ARMOR = ITEMS.register("zinc_hoglin_armor",
            () -> new CreateCompatAnimalArmorItem(CreateCompatArmorMaterials.ZINC, CreateCompatAnimalArmorItem.BodyType.ZINC_HOG, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15))));

    public static final DeferredItem<Item> ZINC_HORSE_ARMOR = ITEMS.register("zinc_horse_armor",
            () -> new AnimalArmorItem(CreateCompatArmorMaterials.ZINC, AnimalArmorItem.BodyType.EQUESTRIAN, false, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15))));

    public static final DeferredItem<Item> BRASS_WOLF_ARMOR = ITEMS.register("brass_wolf_armor",
            () -> new CreateCompatAnimalArmorItem(CreateCompatArmorMaterials.BRASS, CreateCompatAnimalArmorItem.BodyType.BRASS_CANINE, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15+11))));

    public static final DeferredItem<Item> BRASS_HOGLIN_ARMOR = ITEMS.register("brass_hoglin_armor",
            () -> new CreateCompatAnimalArmorItem(CreateCompatArmorMaterials.BRASS, CreateCompatAnimalArmorItem.BodyType.BRASS_HOG, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15+11))));

    public static final DeferredItem<Item> BRASS_HORSE_ARMOR = ITEMS.register("brass_horse_armor",
            () -> new AnimalArmorItem(CreateCompatArmorMaterials.BRASS, AnimalArmorItem.BodyType.EQUESTRIAN, false, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15+11))));

    public static final DeferredItem<Item> BRASS_MACE = ITEMS.register("brass_mace",
            () -> new ModdedMaceItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_BRASS_TOOL, 190+250, 9.0F, 2.0F, 14, () -> Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "brass_ingot")))), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 3 * 0.8333333333F, -3.4F))));
    public static final DeferredItem<Item> ZINC_MACE = ITEMS.register("zinc_mace",
            () -> new ModdedMaceItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_ZINC_TOOL, 250, 6.0F, 2.0F, 14, () -> Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot")))), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 3 * 0.8333333333F, -3.4F))));

    public static final DeferredItem<Item> BRASS_SHEARS = ITEMS.register("brass_shears",
            () -> new BrassShearsItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_BRASS_TOOL, 190+250, 9.0F, 2.0F, 14, () -> Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "brass_ingot")))), new Item.Properties().durability((int) ((190+250) * 0.952)).component(DataComponents.TOOL, BrassShearsItem.createToolProperties())));
    public static final DeferredItem<Item> ZINC_SHEARS = ITEMS.register("zinc_shears",
            () -> new IronShearsItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_ZINC_TOOL, 250, 6.0F, 2.0F, 14, () -> Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot")))), new Item.Properties().durability((int) (250 * 0.952)).component(DataComponents.TOOL, IronShearsItem.createToolProperties())));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
