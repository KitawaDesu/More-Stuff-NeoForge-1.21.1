package net.kitawa.more_stuff.items;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.items.util.*;
import net.kitawa.more_stuff.items.util.scaffolding.*;
import net.kitawa.more_stuff.items.util.shears.*;
import net.kitawa.more_stuff.items.util.weapons.ModdedMaceItem;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.kitawa.more_stuff.items.util.ModSmithingTemplateItem.*;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MoreStuff.MOD_ID);

    public static final DeferredItem<Item> COPPER_NUGGET = ITEMS.register("copper_nugget",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ANCIENT_CHUNK = ITEMS.register("ancient_chunk",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ROSE_GOLD_NUGGET = ITEMS.register("rose_gold_nugget",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RED_NETHER_BRICK = ITEMS.register("red_nether_brick",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PYROLIZED_NETHER_BRICK = ITEMS.register("pyrolized_nether_brick",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WARPED_NETHER_BRICK = ITEMS.register("warped_nether_brick",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DUNGEON_KEY = ITEMS.register("dungeon_key",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> OMINOUS_DUNGEON_KEY = ITEMS.register("ominous_dungeon_key",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> NETHER_KEY = ITEMS.register("nether_key",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> OMINOUS_NETHER_KEY = ITEMS.register("ominous_nether_key",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COPPER_SCAFFOLDING = ITEMS.register("copper_scaffolding",
            () -> new CopperScaffoldingBlockItem(ModBlocks.COPPER_SCAFFOLDING.get(), new Item.Properties()));

    public static final DeferredItem<Item> IRON_SCAFFOLDING = ITEMS.register("iron_scaffolding",
            () -> new IronScaffoldingBlockItem(ModBlocks.IRON_SCAFFOLDING.get(), new Item.Properties()));

    public static final DeferredItem<Item> GOLDEN_SCAFFOLDING = ITEMS.register("golden_scaffolding",
            () -> new GoldenScaffoldingBlockItem(ModBlocks.GOLDEN_SCAFFOLDING.get(), new Item.Properties()));

    public static final DeferredItem<Item> ANCIENT_SCAFFOLDING = ITEMS.register("ancient_scaffolding",
            () -> new AncientScaffoldingBlockItem(ModBlocks.ANCIENT_SCAFFOLDING.get(), new Item.Properties()));

    public static final DeferredItem<Item> PYROLIZED_SCAFFOLDING = ITEMS.register("pyrolized_scaffolding",
            () -> new PyrolizedScaffoldingBlockItem(ModBlocks.PYROLIZED_SCAFFOLDING.get(), new Item.Properties()));

    public static final DeferredItem<Item> AQUANDA_BERRIES = ITEMS.register("aquanda_berries",
            () -> new ItemNameBlockItem(ModBlocks.AQUANDA_VINES.get(), new Item.Properties().food(ModFoods.AQUANDA_BERRIES)));
    public static final DeferredItem<Item> HANGING_GLOWMOSS = ITEMS.register("hanging_glowmoss",
            () -> new ItemNameBlockItem(ModBlocks.HANGING_GLOWMOSS.get(), new Item.Properties()));
    public static final DeferredItem<Item> AQUANDA_KELP = ITEMS.register("aquanda_kelp",
            () -> new ItemNameBlockItem(ModBlocks.AQUANDA_KELP.get(), new Item.Properties()));
    public static final DeferredItem<Item> GOLDEN_BAMBOO = ITEMS.register("golden_bamboo",
            () -> new ItemNameBlockItem(ModBlocks.GOLDEN_BAMBOO_SAPLING.get(), new Item.Properties()));
    public static final DeferredItem<Item> COPPER_BAMBOO = ITEMS.register("copper_bamboo",
            () -> new ItemNameBlockItem(ModBlocks.COPPER_BAMBOO_SAPLING.get(), new Item.Properties()));
    public static final DeferredItem<Item> IRON_BAMBOO = ITEMS.register("iron_bamboo",
            () -> new ItemNameBlockItem(ModBlocks.IRON_BAMBOO_SAPLING.get(), new Item.Properties()));
    public static final DeferredItem<Item> ANCIENT_BAMBOO = ITEMS.register("ancient_bamboo",
            () -> new ItemNameBlockItem(ModBlocks.ANCIENT_BAMBOO_SAPLING.get(), new Item.Properties()));
    public static final DeferredItem<Item> PYROLIZED_BAMBOO = ITEMS.register("pyrolized_bamboo",
            () -> new FuelBlockItem(ModBlocks.PYROLIZED_BAMBOO_SAPLING.get(), new Item.Properties(), 1600));
    public static final DeferredItem<Item> BLAZING_VINES = ITEMS.register("blazing_vines",
            () -> new ItemNameBlockItem(ModBlocks.BLAZING_VINES.get(), new Item.Properties()));
    public static final DeferredItem<Item> WARPED_WART = ITEMS.register("warped_wart",
            () -> new ItemNameBlockItem(ModBlocks.WARPED_WART.get(), new Item.Properties()));

    public static final DeferredItem<Item> ROSE_GOLD_INGOT = ITEMS.register("rose_gold_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ROSARITE_INGOT = ITEMS.register("rosarite_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ELYTRA_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("elytra_upgrade_smithing_template",
            ()-> new SmithingTemplateItem(
                    ELYTRA_UPGRADE_APPLIES_TO,
                    ELYTRA_UPGRADE_INGREDIENTS,
                    ELYTRA_UPGRADE,
                    ELYTRA_UPGRADE_BASE_SLOT_DESCRIPTION,
                    ELYTRA_UPGRADE_ADDITIONS_SLOT_DESCRIPTION,
                    createElytraUpgradeIconList(),
                    createElytraUpgradeMaterialList()
            )
    );

    public static final DeferredItem<Item> ROSARITE_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("rosarite_upgrade_smithing_template",
            ()-> new SmithingTemplateItem(
                    ROSARITE_UPGRADE_APPLIES_TO,
                    ROSARITE_UPGRADE_INGREDIENTS,
                    ROSARITE_UPGRADE,
                    ROSARITE_UPGRADE_BASE_SLOT_DESCRIPTION,
                    ROSARITE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION,
                    createRosariteUpgradeIconList(),
                    createRosariteUpgradeMaterialList()
            )
    );
    public static final DeferredItem<Item> STONE_SHIELD = ITEMS.register("stone_shield",
            () -> new ModShieldItem(new Item.Properties().durability(386).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY), ItemTags.STONE_TOOL_MATERIALS));
    public static final DeferredItem<Item> LAPIS_SHIELD = ITEMS.register("lapis_shield",
            () -> new ModShieldItem(new Item.Properties().durability(386).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY), Items.LAPIS_LAZULI));
    public static final DeferredItem<Item> QUARTZ_SHIELD = ITEMS.register("quartz_shield",
            () -> new ModShieldItem(new Item.Properties().durability(386).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY), Items.QUARTZ));
    public static final DeferredItem<Item> COPPER_SHIELD = ITEMS.register("copper_shield",
            () -> new ModShieldItem(new Item.Properties().durability(450).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY), Items.COPPER_INGOT));
    public static final DeferredItem<Item> GOLDEN_SHIELD = ITEMS.register("golden_shield",
            () -> new ModShieldItem(new Item.Properties().durability(399).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY), Items.GOLD_INGOT));
    public static final DeferredItem<Item> ROSE_GOLDEN_SHIELD = ITEMS.register("rose_golden_shield",
            () -> new ModShieldItem(new Item.Properties().durability(538).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY), ModItems.ROSE_GOLD_INGOT.get()));
    public static final DeferredItem<Item> IRON_SHIELD = ITEMS.register("iron_shield",
            () -> new ModShieldItem(new Item.Properties().durability(500).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY), Items.IRON_INGOT));
    public static final DeferredItem<Item> DIAMOND_SHIELD = ITEMS.register("diamond_shield",
            () -> new ModShieldItem(new Item.Properties().durability(727).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY), Items.DIAMOND));
    public static final DeferredItem<Item> EMERALD_SHIELD = ITEMS.register("emerald_shield",
            () -> new ModShieldItem(new Item.Properties().durability(727).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY), Items.EMERALD));
    public static final DeferredItem<Item> NETHERITE_SHIELD = ITEMS.register("netherite_shield",
            () -> new ModShieldItem(new Item.Properties().durability(1309).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY).fireResistant(), Items.NETHERITE_INGOT));
    public static final DeferredItem<Item> ROSARITE_SHIELD = ITEMS.register("rosarite_shield",
            () -> new ModShieldItem(new Item.Properties().durability(1745).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY).fireResistant(), ModItems.ROSARITE_INGOT.get()));

    public static final DeferredItem<Item> ROSE_GOLDEN_PICKAXE = ITEMS.register("rose_golden_pickaxe",
            () -> new PickaxeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_ROSE_GOLDEN_TOOL, 190+32, 17.0F, 2.0F, 13+22, () -> Ingredient.of(ModItems.ROSE_GOLD_INGOT)), new Item.Properties().attributes(PickaxeItem.createAttributes(Tiers.IRON, 1.0F, -2.8F))));
    public static final DeferredItem<Item> ROSE_GOLDEN_HOE = ITEMS.register("rose_golden_hoe",
            () -> new HoeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_ROSE_GOLDEN_TOOL, 190+32, 17.0F, 2.0F, 13+22, () -> Ingredient.of(ModItems.ROSE_GOLD_INGOT)), new Item.Properties().attributes(HoeItem.createAttributes(Tiers.IRON, -4.0F, 0.0F))));
    public static final DeferredItem<Item> ROSE_GOLDEN_SWORD = ITEMS.register("rose_golden_sword",
            () -> new SwordItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_ROSE_GOLDEN_TOOL, 190+32, 17.0F, 2.0F, 13+22, () -> Ingredient.of(ModItems.ROSE_GOLD_INGOT)), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.4F))));
    public static final DeferredItem<Item> ROSE_GOLDEN_SHOVEL = ITEMS.register("rose_golden_shovel",
            () -> new ShovelItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_ROSE_GOLDEN_TOOL, 190+32, 17.0F, 2.0F, 13+22, () -> Ingredient.of(ModItems.ROSE_GOLD_INGOT)), new Item.Properties().attributes(ShovelItem.createAttributes(Tiers.IRON, 1.5F, -3.0F))));
    public static final DeferredItem<Item> ROSE_GOLDEN_AXE = ITEMS.register("rose_golden_axe",
            () -> new AxeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_ROSE_GOLDEN_TOOL, 190+32, 17.0F, 2.0F, 13+22, () -> Ingredient.of(ModItems.ROSE_GOLD_INGOT)), new Item.Properties().attributes(AxeItem.createAttributes(Tiers.IRON, 5.0F, -3.0F))));

    public static final DeferredItem<Item> COPPER_PICKAXE = ITEMS.register("copper_pickaxe",
            () -> new PickaxeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_COPPER_TOOL, 190, 5.0F, 2.0F, 13, () -> Ingredient.of(Items.COPPER_INGOT)), new Item.Properties().attributes(PickaxeItem.createAttributes(Tiers.IRON, 1.0F, -2.8F))));
    public static final DeferredItem<Item> COPPER_HOE = ITEMS.register("copper_hoe",
            () -> new HoeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_COPPER_TOOL, 190, 5.0F, 2.0F, 13, () -> Ingredient.of(Items.COPPER_INGOT)), new Item.Properties().attributes(HoeItem.createAttributes(Tiers.IRON, -4.0F, 0.0F))));
    public static final DeferredItem<Item> COPPER_SWORD = ITEMS.register("copper_sword",
            () -> new SwordItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_COPPER_TOOL, 190, 5.0F, 2.0F, 13, () -> Ingredient.of(Items.COPPER_INGOT)), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.4F))));
    public static final DeferredItem<Item> COPPER_SHOVEL = ITEMS.register("copper_shovel",
            () -> new ShovelItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_COPPER_TOOL, 190, 5.0F, 2.0F, 13, () -> Ingredient.of(Items.COPPER_INGOT)), new Item.Properties().attributes(ShovelItem.createAttributes(Tiers.IRON, 1.5F, -3.0F))));
    public static final DeferredItem<Item> COPPER_AXE = ITEMS.register("copper_axe",
            () -> new AxeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_COPPER_TOOL, 190, 5.0F, 2.0F, 13, () -> Ingredient.of(Items.COPPER_INGOT)), new Item.Properties().attributes(AxeItem.createAttributes(Tiers.IRON, 5.0F, -3.0F))));

    public static final DeferredItem<Item> EMERALD_AXE = ITEMS.register("emerald_axe",
            () -> new AxeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_EMERALD_TOOL, 1561, 8.0F, 3.0F, 10, () -> Ingredient.of(Items.EMERALD)), new Item.Properties().attributes(AxeItem.createAttributes(Tiers.DIAMOND, 5.0F, -3.0F))));
    public static final DeferredItem<Item> EMERALD_HOE = ITEMS.register("emerald_hoe",
            () -> new HoeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_EMERALD_TOOL, 1561, 8.0F, 3.0F, 10, () -> Ingredient.of(Items.EMERALD)), new Item.Properties().attributes(HoeItem.createAttributes(Tiers.DIAMOND, -4.0F, 0.0F))));
    public static final DeferredItem<Item> EMERALD_SWORD = ITEMS.register("emerald_sword",
            () -> new SwordItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_EMERALD_TOOL, 1561, 8.0F, 3.0F, 10, () -> Ingredient.of(Items.EMERALD)), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.DIAMOND, 3, -2.4F))));
    public static final DeferredItem<Item> EMERALD_SHOVEL = ITEMS.register("emerald_shovel",
            () -> new ShovelItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_EMERALD_TOOL, 1561, 8.0F, 3.0F, 10, () -> Ingredient.of(Items.EMERALD)), new Item.Properties().attributes(ShovelItem.createAttributes(Tiers.DIAMOND, 1.5F, -3.0F))));
    public static final DeferredItem<Item> EMERALD_PICKAXE = ITEMS.register("emerald_pickaxe",
            () -> new PickaxeItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_EMERALD_TOOL, 1561, 8.0F, 3.0F, 10, () -> Ingredient.of(Items.EMERALD)), new Item.Properties().attributes(PickaxeItem.createAttributes(Tiers.DIAMOND, 1.0F, -2.8F))));

    public static final DeferredItem<Item> ROSARITE_AXE = ITEMS.register("rosarite_axe",
            () -> new AxeItem(new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 190+2031, 21.0F, 4.0F, 15+22, () -> Ingredient.of(ModItems.ROSARITE_INGOT)), new Item.Properties().fireResistant().attributes(AxeItem.createAttributes(Tiers.NETHERITE, 5.0F, -3.0F))));
    public static final DeferredItem<Item> ROSARITE_HOE = ITEMS.register("rosarite_hoe",
            () -> new HoeItem(new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 190+2031, 21.0F, 4.0F, 15+22, () -> Ingredient.of(ModItems.ROSARITE_INGOT)), new Item.Properties().fireResistant().attributes(HoeItem.createAttributes(Tiers.NETHERITE, -4.0F, 0.0F))));
    public static final DeferredItem<Item> ROSARITE_SWORD = ITEMS.register("rosarite_sword",
            () -> new SwordItem(new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 190+2031, 21.0F, 4.0F, 15+22, () -> Ingredient.of(ModItems.ROSARITE_INGOT)), new Item.Properties().fireResistant().attributes(SwordItem.createAttributes(Tiers.NETHERITE, 3, -2.4F))));
    public static final DeferredItem<Item> ROSARITE_SHOVEL = ITEMS.register("rosarite_shovel",
            () -> new ShovelItem(new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 190+2031, 21.0F, 4.0F, 15+22, () -> Ingredient.of(ModItems.ROSARITE_INGOT)), new Item.Properties().fireResistant().attributes(ShovelItem.createAttributes(Tiers.NETHERITE, 1.5F, -3.0F))));
    public static final DeferredItem<Item> ROSARITE_PICKAXE = ITEMS.register("rosarite_pickaxe",
            () -> new PickaxeItem(new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 190+2031, 21.0F, 4.0F, 15+22, () -> Ingredient.of(ModItems.ROSARITE_INGOT)), new Item.Properties().fireResistant().attributes(PickaxeItem.createAttributes(Tiers.NETHERITE, 1.0F, -2.8F))));

    public static final DeferredItem<Item> CHAINMAIL_HORSE_ARMOR = ITEMS.register("chainmail_horse_armor",
            () -> new AnimalArmorItem(ArmorMaterials.CHAIN, AnimalArmorItem.BodyType.EQUESTRIAN, false, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15))));

    public static final DeferredItem<Item> WOOD_PLATE_HELMET = ITEMS.register("wood_plate_helmet",
            () -> new ArmorItem(ModArmorMaterials.WOOD_PLATE, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(2))
                            .component(DataComponents.DYED_COLOR, new DyedItemColor(0xFFB8945F, true))));
    public static final DeferredItem<Item> WOOD_PLATE_CHESTPLATE = ITEMS.register("wood_plate_chestplate",
            () -> new ArmorItem(ModArmorMaterials.WOOD_PLATE, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(2))
                            .component(DataComponents.DYED_COLOR, new DyedItemColor(0xFFB8945F, true))));
    public static final DeferredItem<Item> WOOD_PLATE_LEGGINGS = ITEMS.register("wood_plate_leggings",
            () -> new ArmorItem(ModArmorMaterials.WOOD_PLATE, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(2))
                            .component(DataComponents.DYED_COLOR, new DyedItemColor(0xFFB8945F, true))));
    public static final DeferredItem<Item> WOOD_PLATE_BOOTS = ITEMS.register("wood_plate_boots",
            () -> new ArmorItem(ModArmorMaterials.WOOD_PLATE, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(2))
                            .component(DataComponents.DYED_COLOR, new DyedItemColor(0xFFB8945F, true))));
    public static final DeferredItem<Item> WOOD_PLATE_HORSE_ARMOR = ITEMS.register("wood_plate_horse_armor",
            () -> new AnimalArmorItem(
                    ModArmorMaterials.WOOD_PLATE,
                    AnimalArmorItem.BodyType.EQUESTRIAN,
                    false,
                    new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(2))
                            .component(DataComponents.DYED_COLOR, new DyedItemColor(0xFFB8945F, true))
            ));

    public static final DeferredItem<Item> STONE_PLATE_HELMET = ITEMS.register("stone_plate_helmet",
            () -> new ArmorItem(ModArmorMaterials.STONE_PLATE, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(6))));
    public static final DeferredItem<Item> STONE_PLATE_CHESTPLATE = ITEMS.register("stone_plate_chestplate",
            () -> new ArmorItem(ModArmorMaterials.STONE_PLATE, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(6))));
    public static final DeferredItem<Item> STONE_PLATE_LEGGINGS = ITEMS.register("stone_plate_leggings",
            () -> new ArmorItem(ModArmorMaterials.STONE_PLATE, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(6))));
    public static final DeferredItem<Item> STONE_PLATE_BOOTS = ITEMS.register("stone_plate_boots",
            () -> new ArmorItem(ModArmorMaterials.STONE_PLATE, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(6))));
    public static final DeferredItem<Item> STONE_PLATE_HORSE_ARMOR = ITEMS.register("stone_plate_horse_armor",
            () -> new AnimalArmorItem(
                    ModArmorMaterials.STONE_PLATE,
                    AnimalArmorItem.BodyType.EQUESTRIAN,
                    false,
                    new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(6))
            ));

    public static final DeferredItem<Item> COPPER_HELMET = ITEMS.register("copper_helmet",
            () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(11))));
    public static final DeferredItem<Item> COPPER_CHESTPLATE = ITEMS.register("copper_chestplate",
            () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(11))));
    public static final DeferredItem<Item> COPPER_LEGGINGS = ITEMS.register("copper_leggings",
            () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(11))));
    public static final DeferredItem<Item> COPPER_BOOTS = ITEMS.register("copper_boots",
            () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(11))));
    public static final DeferredItem<Item> COPPER_HORSE_ARMOR = ITEMS.register("copper_horse_armor",
            () -> new AnimalArmorItem(ModArmorMaterials.COPPER, AnimalArmorItem.BodyType.EQUESTRIAN, false, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(11))));

    public static final DeferredItem<Item> ROSE_GOLDEN_HELMET = ITEMS.register("rose_golden_helmet",
            () -> new ArmorItem(ModArmorMaterials.ROSE_GOLDEN, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(18))));
    public static final DeferredItem<Item> ROSE_GOLDEN_CHESTPLATE = ITEMS.register("rose_golden_chestplate",
            () -> new ArmorItem(ModArmorMaterials.ROSE_GOLDEN, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(18))));
    public static final DeferredItem<Item> ROSE_GOLDEN_LEGGINGS = ITEMS.register("rose_golden_leggings",
            () -> new ArmorItem(ModArmorMaterials.ROSE_GOLDEN, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(18))));
    public static final DeferredItem<Item> ROSE_GOLDEN_BOOTS = ITEMS.register("rose_golden_boots",
            () -> new ArmorItem(ModArmorMaterials.ROSE_GOLDEN, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(18))));
    public static final DeferredItem<Item> ROSE_GOLDEN_HORSE_ARMOR = ITEMS.register("rose_golden_horse_armor",
            () -> new AnimalArmorItem(ModArmorMaterials.ROSE_GOLDEN, AnimalArmorItem.BodyType.EQUESTRIAN, false, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(18))));

    public static final DeferredItem<Item> EMERALD_HELMET = ITEMS.register("emerald_helmet",
            () -> new ArmorItem(ModArmorMaterials.EMERALD, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(33))));
    public static final DeferredItem<Item> EMERALD_CHESTPLATE = ITEMS.register("emerald_chestplate",
            () -> new ArmorItem(ModArmorMaterials.EMERALD, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(33))));
    public static final DeferredItem<Item> EMERALD_LEGGINGS = ITEMS.register("emerald_leggings",
            () -> new ArmorItem(ModArmorMaterials.EMERALD, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(33))));
    public static final DeferredItem<Item> EMERALD_BOOTS = ITEMS.register("emerald_boots",
            () -> new ArmorItem(ModArmorMaterials.EMERALD, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(33))));
    public static final DeferredItem<Item> EMERALD_HORSE_ARMOR = ITEMS.register("emerald_horse_armor",
            () -> new AnimalArmorItem(ModArmorMaterials.EMERALD, AnimalArmorItem.BodyType.EQUESTRIAN, false, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(33))));

    public static final DeferredItem<Item> NETHERITE_HORSE_ARMOR = ITEMS.register("netherite_horse_armor",
            () -> new AnimalArmorItem(ArmorMaterials.NETHERITE, AnimalArmorItem.BodyType.EQUESTRIAN, false, new Item.Properties().fireResistant().durability(ArmorItem.Type.BODY.getDurability(37))));

    public static final DeferredItem<Item> ROSARITE_HELMET = ITEMS.register("rosarite_helmet",
            () -> new ArmorItem(ModArmorMaterials.ROSARITE, ArmorItem.Type.HELMET, new Item.Properties().fireResistant().durability(ArmorItem.Type.HELMET.getDurability(37+11))));
    public static final DeferredItem<Item> ROSARITE_CHESTPLATE = ITEMS.register("rosarite_chestplate",
            () -> new ArmorItem(ModArmorMaterials.ROSARITE, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(37+11))));
    public static final DeferredItem<Item> ROSARITE_LEGGINGS = ITEMS.register("rosarite_leggings",
            () -> new ArmorItem(ModArmorMaterials.ROSARITE, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(37+11))));
    public static final DeferredItem<Item> ROSARITE_BOOTS = ITEMS.register("rosarite_boots",
            () -> new ArmorItem(ModArmorMaterials.ROSARITE, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(37+11))));
    public static final DeferredItem<Item> ROSARITE_HORSE_ARMOR = ITEMS.register("rosarite_horse_armor",
            () -> new AnimalArmorItem(ModArmorMaterials.ROSARITE, AnimalArmorItem.BodyType.EQUESTRIAN, false, new Item.Properties().fireResistant().durability(ArmorItem.Type.BODY.getDurability(37+11))));

    public static final DeferredItem<Item> LEATHER_GLIDER = ITEMS.register("leather_glider",
            () -> new TeiredElytraItem(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/leather.png"), new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(4))));
    public static final DeferredItem<Item> CHAINED_ELYTRA = ITEMS.register("chained_elytra",
            () -> new TeiredElytraItem(ArmorMaterials.CHAIN, ArmorItem.Type.CHESTPLATE, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"textures/entity/wings/chainmail.png"), new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15+27)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> GOLDEN_ELYTRA = ITEMS.register("golden_elytra",
            () -> new TeiredElytraItem(ArmorMaterials.GOLD, ArmorItem.Type.CHESTPLATE, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"textures/entity/wings/gold.png"), new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(7+27)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> IRON_ELYTRA = ITEMS.register("iron_elytra",
            () -> new TeiredElytraItem(ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"textures/entity/wings/iron.png"), new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15+27)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> COPPER_ELYTRA = ITEMS.register("copper_elytra",
            () -> new TeiredElytraItem(ModArmorMaterials.COPPER, ArmorItem.Type.CHESTPLATE, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"textures/entity/wings/copper.png"), new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(11+27)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> ROSE_GOLDEN_ELYTRA = ITEMS.register("rose_golden_elytra",
            () -> new TeiredElytraItem(ModArmorMaterials.ROSE_GOLDEN, ArmorItem.Type.CHESTPLATE, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"textures/entity/wings/rose_gold.png"), new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15+27)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> DIAMOND_ELYTRA = ITEMS.register("diamond_elytra",
            () -> new TeiredElytraItem(ArmorMaterials.DIAMOND, ArmorItem.Type.CHESTPLATE, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"textures/entity/wings/diamond.png"), new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(33+27)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> EMERALD_ELYTRA = ITEMS.register("emerald_elytra",
            () -> new TeiredElytraItem(ModArmorMaterials.EMERALD, ArmorItem.Type.CHESTPLATE, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"textures/entity/wings/emerald.png"), new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(33+27)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> NETHERITE_ELYTRA = ITEMS.register("netherite_elytra",
            () -> new TeiredElytraItem(ArmorMaterials.NETHERITE, ArmorItem.Type.CHESTPLATE, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"textures/entity/wings/diamond.png"), new Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(37+27)).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> ROSARITE_ELYTRA = ITEMS.register("rosarite_elytra",
            () -> new TeiredElytraItem(ModArmorMaterials.ROSARITE, ArmorItem.Type.CHESTPLATE, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"textures/entity/wings/rosarite.png"), new Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(37+27+11)).rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> WOOD_PLATE_WOLF_ARMOR = ITEMS.register("wood_plate_wolf_armor",
            () -> new ModdedAnimalArmorItem(ModArmorMaterials.WOOD_PLATE, ModdedAnimalArmorItem.BodyType.WOODEN_CANINE, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(2))
                    .component(DataComponents.DYED_COLOR, new DyedItemColor(0xFFB8945F, true))));
    public static final DeferredItem<Item> LEATHER_WOLF_ARMOR = ITEMS.register("leather_wolf_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.LEATHER, ModdedAnimalArmorItem.BodyType.LEATHER_CANINE, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(5))));
    public static final DeferredItem<Item> STONE_PLATE_WOLF_ARMOR = ITEMS.register("stone_plate_wolf_armor",
            () -> new ModdedAnimalArmorItem(ModArmorMaterials.STONE_PLATE, ModdedAnimalArmorItem.BodyType.LEATHER_CANINE, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(6))));
    public static final DeferredItem<Item> CHAINMAIL_WOLF_ARMOR = ITEMS.register("chainmail_wolf_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.CHAIN, ModdedAnimalArmorItem.BodyType.CHAIN_CANINE, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15))));
    public static final DeferredItem<Item> GOLDEN_WOLF_ARMOR = ITEMS.register("golden_wolf_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.GOLD, ModdedAnimalArmorItem.BodyType.GOLDEN_CANINE, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(7))));
    public static final DeferredItem<Item> IRON_WOLF_ARMOR = ITEMS.register("iron_wolf_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.IRON, ModdedAnimalArmorItem.BodyType.IRON_CANINE, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15))));
    public static final DeferredItem<Item> COPPER_WOLF_ARMOR = ITEMS.register("copper_wolf_armor",
            () -> new ModdedAnimalArmorItem(ModArmorMaterials.COPPER, ModdedAnimalArmorItem.BodyType.COPPER_CANINE, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(11))));
    public static final DeferredItem<Item> ROSE_GOLDEN_WOLF_ARMOR = ITEMS.register("rose_golden_wolf_armor",
            () -> new ModdedAnimalArmorItem(ModArmorMaterials.COPPER, ModdedAnimalArmorItem.BodyType.ROSE_GOLEN_CANINE, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15))));
    public static final DeferredItem<Item> DIAMOND_WOLF_ARMOR = ITEMS.register("diamond_wolf_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.DIAMOND, ModdedAnimalArmorItem.BodyType.DIAMOND_CANINE, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(33))));
    public static final DeferredItem<Item> EMERALD_WOLF_ARMOR = ITEMS.register("emerald_wolf_armor",
            () -> new ModdedAnimalArmorItem(ModArmorMaterials.EMERALD, ModdedAnimalArmorItem.BodyType.EMERALD_CANINE, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(33))));
    public static final DeferredItem<Item> NETHERITE_WOLF_ARMOR = ITEMS.register("netherite_wolf_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.NETHERITE, ModdedAnimalArmorItem.BodyType.NETHERITE_CANINE, true, new Item.Properties().fireResistant().durability(ArmorItem.Type.BODY.getDurability(37))));
    public static final DeferredItem<Item> TURTLE_SCUTE_WOLF_ARMOR = ITEMS.register("turtle_scute_wolf_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.TURTLE, ModdedAnimalArmorItem.BodyType.TURTLE_SCUTE_CANINE, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(25))));
    public static final DeferredItem<Item> ROSARITE_WOLF_ARMOR = ITEMS.register("rosarite_wolf_armor",
            () -> new ModdedAnimalArmorItem(ModArmorMaterials.ROSARITE, ModdedAnimalArmorItem.BodyType.ROSARITE_CANINE, true, new Item.Properties().fireResistant().durability(ArmorItem.Type.BODY.getDurability(37+11))));

    public static final DeferredItem<Item> HOGLIN_ARMOR = ITEMS.register("hoglin_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.ARMADILLO, ModdedAnimalArmorItem.BodyType.HOG, true, new Item.Properties().fireResistant().durability(ArmorItem.Type.BODY.getDurability(4))));
    public static final DeferredItem<Item> LEATHER_HOGLIN_ARMOR = ITEMS.register("leather_hoglin_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.LEATHER, ModdedAnimalArmorItem.BodyType.LEATHER_HOG, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(5))));
    public static final DeferredItem<Item> CHAINMAIL_HOGLIN_ARMOR = ITEMS.register("chainmail_hoglin_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.CHAIN, ModdedAnimalArmorItem.BodyType.CHAIN_HOG, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15))));
    public static final DeferredItem<Item> GOLDEN_HOGLIN_ARMOR = ITEMS.register("golden_hoglin_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.GOLD, ModdedAnimalArmorItem.BodyType.GOLDEN_HOG, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(7))));
    public static final DeferredItem<Item> IRON_HOGLIN_ARMOR = ITEMS.register("iron_hoglin_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.IRON, ModdedAnimalArmorItem.BodyType.IRON_HOG, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15))));
    public static final DeferredItem<Item> COPPER_HOGLIN_ARMOR = ITEMS.register("copper_hoglin_armor",
            () -> new ModdedAnimalArmorItem(ModArmorMaterials.COPPER, ModdedAnimalArmorItem.BodyType.COPPER_HOG, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(11))));
    public static final DeferredItem<Item> ROSE_GOLDEN_HOGLIN_ARMOR = ITEMS.register("rose_golden_hoglin_armor",
            () -> new ModdedAnimalArmorItem(ModArmorMaterials.COPPER, ModdedAnimalArmorItem.BodyType.ROSE_GOLEN_HOG, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15))));
    public static final DeferredItem<Item> DIAMOND_HOGLIN_ARMOR = ITEMS.register("diamond_hoglin_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.DIAMOND, ModdedAnimalArmorItem.BodyType.DIAMOND_HOG, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(33))));
    public static final DeferredItem<Item> EMERALD_HOGLIN_ARMOR = ITEMS.register("emerald_hoglin_armor",
            () -> new ModdedAnimalArmorItem(ModArmorMaterials.EMERALD, ModdedAnimalArmorItem.BodyType.EMERALD_HOG, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(33))));
    public static final DeferredItem<Item> NETHERITE_HOGLIN_ARMOR = ITEMS.register("netherite_hoglin_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.NETHERITE, ModdedAnimalArmorItem.BodyType.NETHERITE_HOG, true, new Item.Properties().fireResistant().durability(ArmorItem.Type.BODY.getDurability(37))));
    public static final DeferredItem<Item> TURTLE_SCUTE_HOGLIN_ARMOR = ITEMS.register("turtle_scute_hoglin_armor",
            () -> new ModdedAnimalArmorItem(ArmorMaterials.TURTLE, ModdedAnimalArmorItem.BodyType.TURTLE_SCUTE_HOG, true, new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(25))));
    public static final DeferredItem<Item> ROSARITE_HOGLIN_ARMOR = ITEMS.register("rosarite_hoglin_armor",
            () -> new ModdedAnimalArmorItem(ModArmorMaterials.ROSARITE, ModdedAnimalArmorItem.BodyType.ROSARITE_HOG, true, new Item.Properties().fireResistant().durability(ArmorItem.Type.BODY.getDurability(37+11))));

    public static final DeferredItem<Item> NETHERRACK_STONES = ITEMS.register("netherrack_stones",
            () -> new PlaceOnLiquidBlockItem(ModBlocks.NETHERRACK_STONES.get(), new Item.Properties()));
    public static final DeferredItem<Item> STONES = ITEMS.register("stones",
            () -> new PlaceOnLiquidBlockItem(ModBlocks.STONES.get(), new Item.Properties()));
    public static final DeferredItem<Item> DEEPSLATE_STONES = ITEMS.register("deepslate_stones",
            () -> new PlaceOnLiquidBlockItem(ModBlocks.DEEPSLATE_STONES.get(), new Item.Properties()));
    public static final DeferredItem<Item> PYROLIZED_STONES = ITEMS.register("pyrolized_stones",
            () -> new PlaceOnLiquidBlockItem(ModBlocks.PYROLIZED_STONES.get(), new Item.Properties()));
    public static final DeferredItem<Item> BASALT_STONES = ITEMS.register("basalt_stones",
            () -> new PlaceOnLiquidBlockItem(ModBlocks.BASALT_STONES.get(), new Item.Properties()));
    public static final DeferredItem<Item> ICE_SHEET = ITEMS.register("ice_sheet",
            () -> new PlaceOnLiquidBlockItem(ModBlocks.ICE_SHEET.get(), new Item.Properties()));

    public static final DeferredItem<Item> WOODEN_MACE = ITEMS.register("wooden_mace",
            () -> new ModdedMaceItem(Tiers.WOOD, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.WOOD, 3 * 0.8333333333F, -3.4F)).durability(Tiers.WOOD.getUses())));
    public static final DeferredItem<Item> STONE_MACE = ITEMS.register("stone_mace",
            () -> new ModdedMaceItem(Tiers.STONE, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.STONE, 3 * 0.8333333333F, -3.4F)).durability(Tiers.STONE.getUses())));
    public static final DeferredItem<Item> IRON_MACE = ITEMS.register("iron_mace",
            () -> new ModdedMaceItem(Tiers.IRON, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 3 * 0.8333333333F, -3.4F)).durability(Tiers.IRON.getUses())));
    public static final DeferredItem<Item> COPPER_MACE = ITEMS.register("copper_mace",
            () -> new ModdedMaceItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_COPPER_TOOL, 190, 6.0F, 2.0F, 14, () -> Ingredient.of(Items.COPPER_INGOT)), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 3 * 0.8333333333F, -3.4F)).durability(190)));
    public static final DeferredItem<Item> ROSE_GOLDEN_MACE = ITEMS.register("rose_golden_mace",
            () -> new ModdedMaceItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_ROSE_GOLDEN_TOOL, 190+32, 18.0F, 2.0F, 36, () -> Ingredient.of(ModItems.ROSE_GOLD_INGOT)), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 3 * 0.8333333333F, -3.4F)).durability(190+32)));
    public static final DeferredItem<Item> GOLDEN_MACE = ITEMS.register("golden_mace",
            () -> new ModdedMaceItem(Tiers.GOLD, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.GOLD, 3 * 0.8333333333F, -3.4F)).durability(Tiers.GOLD.getUses())));
    public static final DeferredItem<Item> DIAMOND_MACE = ITEMS.register("diamond_mace",
            () -> new ModdedMaceItem(Tiers.DIAMOND, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.DIAMOND, 3 * 0.8333333333F, -3.4F)).durability(Tiers.DIAMOND.getUses())));
    public static final DeferredItem<Item> EMERALD_MACE = ITEMS.register("emerald_mace",
            () -> new ModdedMaceItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_EMERALD_TOOL, 1561, 8.0F, 3.0F, 10, () -> Ingredient.of(Items.EMERALD)), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.DIAMOND, 3 * 0.8333333333F, -3.4F)).durability(Tiers.DIAMOND.getUses())));
    public static final DeferredItem<Item> NETHERITE_MACE = ITEMS.register("netherite_mace",
            () -> new ModdedMaceItem(Tiers.NETHERITE, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.NETHERITE, 3 * 0.8333333333F, -3.4F)).fireResistant().durability(Tiers.NETHERITE.getUses())));
    public static final DeferredItem<Item> ROSARITE_MACE = ITEMS.register("rosarite_mace",
            () -> new ModdedMaceItem(new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 190+2031, 21.0F, 4.0F, 37, () -> Ingredient.of(ModItems.ROSARITE_INGOT)), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.NETHERITE, 3 * 0.8333333333F, -3.4F)).fireResistant().durability(190+2031)));

    public static final DeferredItem<Item> LAPIS_PICKAXE = ITEMS.register("lapis_pickaxe",
            () -> new PickaxeItem(new SimpleTier(BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0F, 1.0F, 60, () -> Ingredient.of(Items.LAPIS_LAZULI)), new Item.Properties().attributes(PickaxeItem.createAttributes(Tiers.STONE, 1.0F, -2.8F))));
    public static final DeferredItem<Item> LAPIS_HOE = ITEMS.register("lapis_hoe",
            () -> new HoeItem(new SimpleTier(BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0F, 1.0F, 60, () -> Ingredient.of(Items.LAPIS_LAZULI)), new Item.Properties().attributes(HoeItem.createAttributes(Tiers.STONE, -4.0F, 0.0F))));
    public static final DeferredItem<Item> LAPIS_SWORD = ITEMS.register("lapis_sword",
            () -> new SwordItem(new SimpleTier(BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0F, 1.0F, 60, () -> Ingredient.of(Items.LAPIS_LAZULI)), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.STONE, 3, -2.4F))));
    public static final DeferredItem<Item> LAPIS_SHOVEL = ITEMS.register("lapis_shovel",
            () -> new ShovelItem(new SimpleTier(BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0F, 1.0F, 60, () -> Ingredient.of(Items.LAPIS_LAZULI)), new Item.Properties().attributes(ShovelItem.createAttributes(Tiers.STONE, 1.5F, -3.0F))));
    public static final DeferredItem<Item> LAPIS_AXE = ITEMS.register("lapis_axe",
            () -> new AxeItem(new SimpleTier(BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0F, 1.0F, 60, () -> Ingredient.of(Items.LAPIS_LAZULI)), new Item.Properties().attributes(AxeItem.createAttributes(Tiers.STONE, 5.0F, -3.0F))));
    public static final DeferredItem<Item> LAPIS_MACE = ITEMS.register("lapis_mace",
            () -> new ModdedMaceItem(new SimpleTier(BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0F, 1.0F, 60, () -> Ingredient.of(Items.LAPIS_LAZULI)), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.STONE, 3 * 0.8333333333F, -3.4F)).durability(Tiers.STONE.getUses())));

    public static final DeferredItem<Item> QUARTZ_PICKAXE = ITEMS.register("quartz_pickaxe",
            () -> new PickaxeItem(new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 131, 4.0F, 1.0F, 5, () -> Ingredient.of(Items.QUARTZ)), new Item.Properties().attributes(PickaxeItem.createAttributes(Tiers.STONE, 1.0F, -2.8F))));
    public static final DeferredItem<Item> QUARTZ_HOE = ITEMS.register("quartz_hoe",
            () -> new HoeItem(new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 131, 4.0F, 1.0F, 5, () -> Ingredient.of(Items.QUARTZ)), new Item.Properties().attributes(HoeItem.createAttributes(Tiers.STONE, -4.0F, 0.0F))));
    public static final DeferredItem<Item> QUARTZ_SWORD = ITEMS.register("quartz_sword",
            () -> new SwordItem(new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 131, 4.0F, 1.0F, 5, () -> Ingredient.of(Items.QUARTZ)), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.STONE, 3, -2.4F))));
    public static final DeferredItem<Item> QUARTZ_SHOVEL = ITEMS.register("quartz_shovel",
            () -> new ShovelItem(new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 131, 4.0F, 1.0F, 5, () -> Ingredient.of(Items.QUARTZ)), new Item.Properties().attributes(ShovelItem.createAttributes(Tiers.STONE, 1.5F, -3.0F))));
    public static final DeferredItem<Item> QUARTZ_AXE = ITEMS.register("quartz_axe",
            () -> new AxeItem(new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 131, 4.0F, 1.0F, 5, () -> Ingredient.of(Items.QUARTZ)), new Item.Properties().attributes(AxeItem.createAttributes(Tiers.STONE, 5.0F, -3.0F))));
    public static final DeferredItem<Item> QUARTZ_MACE = ITEMS.register("quartz_mace",
            () -> new ModdedMaceItem(new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 131, 4.0F, 1.0F, 5, () -> Ingredient.of(Items.QUARTZ)), new Item.Properties().attributes(SwordItem.createAttributes(Tiers.STONE, 3 * 0.8333333333F, -3.4F)).durability(Tiers.STONE.getUses())));

    public static final DeferredItem<Item> WOODEN_SHEARS = ITEMS.register("wooden_shears",
            () -> new WoodenShearsItem(Tiers.WOOD, new Item.Properties().durability((int) (Tiers.WOOD.getUses() * 0.952)).component(DataComponents.TOOL, WoodenShearsItem.createToolProperties())));
    public static final DeferredItem<Item> STONE_SHEARS = ITEMS.register("stone_shears",
            () -> new StoneShearsItem(Tiers.STONE, new Item.Properties().durability((int) (Tiers.STONE.getUses() * 0.952)).component(DataComponents.TOOL, StoneShearsItem.createToolProperties())));
    public static final DeferredItem<Item> LAPIS_SHEARS = ITEMS.register("lapis_shears",
            () -> new StoneShearsItem(new SimpleTier(BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0F, 1.0F, 60, () -> Ingredient.of(Items.LAPIS_LAZULI)), new Item.Properties().durability((int) (Tiers.STONE.getUses() * 0.952)).component(DataComponents.TOOL, StoneShearsItem.createToolProperties())));
    public static final DeferredItem<Item> QUARTZ_SHEARS = ITEMS.register("quartz_shears",
            () -> new StoneShearsItem(new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 131, 4.0F, 1.0F, 5, () -> Ingredient.of(Items.QUARTZ)), new Item.Properties().durability((int) (Tiers.STONE.getUses() * 0.952)).component(DataComponents.TOOL, StoneShearsItem.createToolProperties())));
    public static final DeferredItem<Item> COPPER_SHEARS = ITEMS.register("copper_shears",
            () -> new IronShearsItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_COPPER_TOOL, 190, 6.0F, 2.0F, 14, () -> Ingredient.of(Items.COPPER_INGOT)), new Item.Properties().durability((int) (190 * 0.952)).component(DataComponents.TOOL, IronShearsItem.createToolProperties())));
    public static final DeferredItem<Item> ROSE_GOLDEN_SHEARS = ITEMS.register("rose_golden_shears",
            () -> new RoseGoldenShearsItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_ROSE_GOLDEN_TOOL, 190+32, 18.0F, 2.0F, 36, () -> Ingredient.of(ModItems.ROSE_GOLD_INGOT)), new Item.Properties().durability((int) (190+32 * 0.952)).component(DataComponents.TOOL, RoseGoldenShearsItem.createToolProperties())));
    public static final DeferredItem<Item> DIAMOND_SHEARS = ITEMS.register("diamond_shears",
            () -> new DiamondShearsItem(Tiers.DIAMOND, new Item.Properties().durability((int) (Tiers.DIAMOND.getUses() * 0.952)).component(DataComponents.TOOL, DiamondShearsItem.createToolProperties())));
    public static final DeferredItem<Item> EMERALD_SHEARS = ITEMS.register("emerald_shears",
            () -> new DiamondShearsItem(new SimpleTier(ModBlockTags.INCORRECT_FOR_EMERALD_TOOL, 1561, 8.0F, 3.0F, 10, () -> Ingredient.of(Items.EMERALD)), new Item.Properties().durability((int) (Tiers.DIAMOND.getUses() * 0.952)).component(DataComponents.TOOL, DiamondShearsItem.createToolProperties())));
    public static final DeferredItem<Item> NETHERITE_SHEARS = ITEMS.register("netherite_shears",
            () -> new NetheriteShearsItem(Tiers.NETHERITE, new Item.Properties().durability((int) (Tiers.NETHERITE.getUses() * 0.952)).component(DataComponents.TOOL, NetheriteShearsItem.createToolProperties()).fireResistant()));
    public static final DeferredItem<Item> ROSARITE_SHEARS = ITEMS.register("rosarite_shears",
            () -> new RosariteShearsItem(new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2063, 21.0F, 4.0F, 37, () -> Ingredient.of(ModItems.ROSARITE_INGOT)), new Item.Properties().durability((int) ((Tiers.NETHERITE.getUses()+190) * 0.952)).component(DataComponents.TOOL, RosariteShearsItem.createToolProperties()).fireResistant()));
    public static final DeferredItem<Item> GOLDEN_SHEARS = ITEMS.register("golden_shears",
            () -> new GoldenShearsItem(Tiers.GOLD, new Item.Properties().durability((int) (Tiers.GOLD.getUses() * 0.952)).component(DataComponents.TOOL, GoldenShearsItem.createToolProperties())));

    public static final String[] SHULKER_COLORS = {
            "white","orange","magenta","light_blue","yellow","lime","pink","gray",
            "light_gray","cyan","purple","blue","brown","green","red","black","normal"
    };

    static {
        for (String color : SHULKER_COLORS) {
            // If "normal", drop the color prefix
            String prefix = color.equals("normal") ? "shulker_shell" : color + "_shulker_shell";

            // Helmet
            ITEMS.register(prefix + "_helmet",
                    () -> new ArmorItem(ModArmorMaterials.SHULKER_ARMORS.get(color),
                            ArmorItem.Type.HELMET,
                            new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));

            // Chestplate
            ITEMS.register(prefix + "_chestplate",
                    () -> new ArmorItem(ModArmorMaterials.SHULKER_ARMORS.get(color),
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15))));

            // Leggings
            ITEMS.register(prefix + "_leggings",
                    () -> new ArmorItem(ModArmorMaterials.SHULKER_ARMORS.get(color),
                            ArmorItem.Type.LEGGINGS,
                            new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(15))));

            // Boots
            ITEMS.register(prefix + "_boots",
                    () -> new ArmorItem(ModArmorMaterials.SHULKER_ARMORS.get(color),
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(15))));

            // Horse Armor
            ITEMS.register(prefix + "_horse_armor",
                    () -> new AnimalArmorItem(ModArmorMaterials.SHULKER_ARMORS.get(color),
                            AnimalArmorItem.BodyType.EQUESTRIAN,
                            false,
                            new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15))));

            // Wolf Armor
            ModdedAnimalArmorItem.BodyType wolfBodyType = switch (color) {
                case "white" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_WHITE;
                case "orange" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_ORANGE;
                case "magenta" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_MAGENTA;
                case "light_blue" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_LIGHT_BLUE;
                case "yellow" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_YELLOW;
                case "lime" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_LIME;
                case "pink" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_PINK;
                case "gray" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_GRAY;
                case "light_gray" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_LIGHT_GRAY;
                case "cyan" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_CYAN;
                case "purple" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_PURPLE;
                case "blue" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_BLUE;
                case "brown" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_BROWN;
                case "green" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_GREEN;
                case "red" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_RED;
                case "black" -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_BLACK;
                default -> ModdedAnimalArmorItem.BodyType.SHULKER_CANINE_NORMAL; // covers "normal"
            };

            ITEMS.register(prefix + "_wolf_armor",
                    () -> new ModdedAnimalArmorItem(ModArmorMaterials.SHULKER_ARMORS.get(color),
                            wolfBodyType,
                            true,
                            new Item.Properties().durability(ArmorItem.Type.BODY.getDurability(15))));
        }
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
