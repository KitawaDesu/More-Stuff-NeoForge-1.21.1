package net.kitawa.more_stuff.experimentals.items;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.experimentals.items.util.ToggleableItem;
import net.kitawa.more_stuff.experimentals.items.util.ToggleableJavelinItem;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.items.util.weapons.JavelinItem;
import net.kitawa.more_stuff.util.configs.ExperimentalUpdatesConfig;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.kitawa.more_stuff.items.util.ModSmithingTemplateItem.Properties;

public class ExperimentalCombatItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MoreStuff.MOD_ID);

    public static final DeferredItem<Item> STURDY_STICK = ITEMS.register("sturdy_stick",
            () -> new ToggleableItem(new Properties(),
                    () -> ExperimentalUpdatesConfig.isCombatUpdateAllowed, // dynamic check
                    "§7Experimental Combat Update"));

    public static final DeferredItem<Item> WOODEN_JAVELIN = ITEMS.register("wooden_javelin",
            () -> new ToggleableJavelinItem("wooden_javelin",
                    Tiers.WOOD,
                    ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/item/javelin/wood.png"),
                    new Properties()
                    .durability((int) (Tiers.WOOD.getUses()*1.5))
                    .attributes(JavelinItem.createAttributes(Tiers.WOOD))
                    .component(DataComponents.TOOL, JavelinItem.createToolProperties()),
                    () -> ExperimentalUpdatesConfig.isCombatUpdateAllowed, // dynamic check
                    "§7Experimental Combat Update"));

    public static final DeferredItem<Item> STONE_JAVELIN = ITEMS.register("stone_javelin",
            () -> new ToggleableJavelinItem("stone_javelin",
                    Tiers.STONE,
                    ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/item/javelin/stone.png"),
                    new Properties()
                    .durability((int) (Tiers.STONE.getUses()*1.5))
                    .attributes(JavelinItem.createAttributes(Tiers.STONE))
                    .component(DataComponents.TOOL, JavelinItem.createToolProperties()),
                    () -> ExperimentalUpdatesConfig.isCombatUpdateAllowed, // dynamic check
                    "§7Experimental Combat Update"));

    public static final DeferredItem<Item> QUARTZ_JAVELIN = ITEMS.register("quartz_javelin",
            () -> new ToggleableJavelinItem("quartz_javelin",
                    new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 131, 4.0F, 1.0F, 5, () -> Ingredient.of(Items.QUARTZ)),
                    ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/item/javelin/quartz.png"),
                    new Properties()
                            .durability((int) (Tiers.STONE.getUses()*1.5))
                            .attributes(JavelinItem.createAttributes(Tiers.STONE))
                            .component(DataComponents.TOOL, JavelinItem.createToolProperties()),
                    () -> ExperimentalUpdatesConfig.isCombatUpdateAllowed, // dynamic check
                    "§7Experimental Combat Update"));

    public static final DeferredItem<Item> LAPIS_JAVELIN = ITEMS.register("lapis_javelin",
            () -> new ToggleableJavelinItem("lapis_javelin",
                    new SimpleTier(BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0F, 1.0F, 60, () -> Ingredient.of(Items.LAPIS_LAZULI)),
                    ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/item/javelin/lapis.png"),
                    new Properties()
                            .durability((int) (Tiers.STONE.getUses()*1.5))
                            .attributes(JavelinItem.createAttributes(Tiers.STONE))
                            .component(DataComponents.TOOL, JavelinItem.createToolProperties()),
                    () -> ExperimentalUpdatesConfig.isCombatUpdateAllowed, // dynamic check
                    "§7Experimental Combat Update"));

    public static final DeferredItem<Item> COPPER_JAVELIN = ITEMS.register("copper_javelin",
            () -> new ToggleableJavelinItem("copper_javelin",
                    new SimpleTier(ModBlockTags.INCORRECT_FOR_COPPER_TOOL, 190, 5.0F, 2.0F, 13, () -> Ingredient.of(Items.COPPER_INGOT)),
                    ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/item/javelin/copper.png"),
                    new Properties()
                    .durability((int) (190*1.5))
                    .attributes(JavelinItem.createAttributes(new SimpleTier(ModBlockTags.INCORRECT_FOR_COPPER_TOOL, 190, 5.0F, 2.0F, 13, () -> Ingredient.of(Items.COPPER_INGOT))))
                    .component(DataComponents.TOOL, JavelinItem.createToolProperties()),
                    () -> ExperimentalUpdatesConfig.isCombatUpdateAllowed, // dynamic check
                    "§7Experimental Combat Update"));

    public static final DeferredItem<Item> IRON_JAVELIN = ITEMS.register("iron_javelin",
            () -> new ToggleableJavelinItem("iron_javelin",
                    Tiers.IRON,
                    ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/item/javelin/iron.png"),
                    new Properties()
                    .durability((int) (Tiers.IRON.getUses()*1.5))
                    .attributes(JavelinItem.createAttributes(Tiers.IRON))
                    .component(DataComponents.TOOL, JavelinItem.createToolProperties()),
                    () -> ExperimentalUpdatesConfig.isCombatUpdateAllowed, // dynamic check
                    "§7Experimental Combat Update"));

    public static final DeferredItem<Item> ROSE_GOLDEN_JAVELIN = ITEMS.register("rose_golden_javelin",
            () -> new ToggleableJavelinItem("rose_golden_javelin",
                    new SimpleTier(ModBlockTags.INCORRECT_FOR_ROSE_GOLDEN_TOOL, 190+32, 17.0F, 2.0F, 13+22, () -> Ingredient.of(ModItems.ROSE_GOLD_INGOT)),
                    ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/item/javelin/rose_gold.png"),
                    new Properties()
                    .durability((int) (190*1.5))
                    .attributes(JavelinItem.createAttributes(new SimpleTier(ModBlockTags.INCORRECT_FOR_ROSE_GOLDEN_TOOL, 190+32, 17.0F, 2.0F, 13+22, () -> Ingredient.of(ModItems.ROSE_GOLD_INGOT))))
                    .component(DataComponents.TOOL, JavelinItem.createToolProperties()),
                    () -> ExperimentalUpdatesConfig.isCombatUpdateAllowed, // dynamic check
                    "§7Experimental Combat Update"));

    public static final DeferredItem<Item> GOLDEN_JAVELIN = ITEMS.register("golden_javelin",
            () -> new ToggleableJavelinItem("golden_javelin",
                    Tiers.GOLD,
                    ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/item/javelin/gold.png"),
                    new Properties()
                    .durability((int) (Tiers.GOLD.getUses()*1.5))
                    .attributes(JavelinItem.createAttributes(Tiers.GOLD))
                    .component(DataComponents.TOOL, JavelinItem.createToolProperties()),
                    () -> ExperimentalUpdatesConfig.isCombatUpdateAllowed, // dynamic check
                    "§7Experimental Combat Update"));

    public static final DeferredItem<Item> EMERALD_JAVELIN = ITEMS.register("emerald_javelin",
            () -> new ToggleableJavelinItem("emerald_javelin",
                    new SimpleTier(ModBlockTags.INCORRECT_FOR_EMERALD_TOOL, 1561, 8.0F, 3.0F, 10, () -> Ingredient.of(Items.EMERALD)),
                    ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/item/javelin/emerald.png"),
                    new Properties()
                    .durability((int) (Tiers.DIAMOND.getUses()*1.5))
                    .attributes(JavelinItem.createAttributes(Tiers.DIAMOND))
                    .component(DataComponents.TOOL, JavelinItem.createToolProperties()),
                    () -> ExperimentalUpdatesConfig.isCombatUpdateAllowed, // dynamic check
                    "§7Experimental Combat Update"));

    public static final DeferredItem<Item> DIAMOND_JAVELIN = ITEMS.register("diamond_javelin",
            () -> new ToggleableJavelinItem("diamond_javelin",
                    Tiers.DIAMOND,
                    ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/item/javelin/diamond.png"),
                    new Properties()
                    .durability((int) (Tiers.DIAMOND.getUses()*1.5))
                    .attributes(JavelinItem.createAttributes(Tiers.DIAMOND))
                    .component(DataComponents.TOOL, JavelinItem.createToolProperties()),
                    () -> ExperimentalUpdatesConfig.isCombatUpdateAllowed, // dynamic check
                    "§7Experimental Combat Update"));

    public static final DeferredItem<Item> NETHERITE_JAVELIN = ITEMS.register("netherite_javelin",
            () -> new ToggleableJavelinItem("netherite_javelin",
                    Tiers.NETHERITE,
                    ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/item/javelin/netherite.png"),
                    new Properties()
                    .durability((int) (Tiers.NETHERITE.getUses()*1.5))
                    .attributes(JavelinItem.createAttributes(Tiers.NETHERITE))
                    .component(DataComponents.TOOL, JavelinItem.createToolProperties())
                            .fireResistant(),
                    () -> ExperimentalUpdatesConfig.isCombatUpdateAllowed, // dynamic check
                    "§7Experimental Combat Update"));

    public static final DeferredItem<Item> ROSARITE_JAVELIN = ITEMS.register("rosarite_javelin",
            () -> new ToggleableJavelinItem("rosarite_javelin",
                    new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 190+2031, 21.0F, 4.0F, 15+22, () -> Ingredient.of(ModItems.ROSARITE_INGOT)),
                    ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/item/javelin/rosarite.png"),
                    new Properties()
                    .durability((int) ((190+2031)*1.5))
                    .attributes(JavelinItem.createAttributes(new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 190+2031, 21.0F, 4.0F, 15+22, () -> Ingredient.of(ModItems.ROSARITE_INGOT))))
                    .component(DataComponents.TOOL, JavelinItem.createToolProperties())
                            .fireResistant(),
                    () -> ExperimentalUpdatesConfig.isCombatUpdateAllowed, // dynamic check
                    "§7Experimental Combat Update"));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
