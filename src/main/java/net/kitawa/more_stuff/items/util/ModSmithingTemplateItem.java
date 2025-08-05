package net.kitawa.more_stuff.items.util;

import java.util.Collections;
import java.util.List;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.TooltipFlag;

public class ModSmithingTemplateItem extends Item {
    private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;
    private static final Component INGREDIENTS_TITLE = Component.translatable(
                    Util.makeDescriptionId("item", ResourceLocation.withDefaultNamespace("smithing_template.ingredients"))
            )
            .withStyle(TITLE_FORMAT);
    private static final Component APPLIES_TO_TITLE = Component.translatable(
                    Util.makeDescriptionId("item", ResourceLocation.withDefaultNamespace("smithing_template.applies_to"))
            )
            .withStyle(TITLE_FORMAT);
    public static final Component ELYTRA_UPGRADE = Component.translatable(
                    Util.makeDescriptionId("upgrade", ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"elytra_upgrade"))
            )
            .withStyle(TITLE_FORMAT);
    public static final Component ELYTRA_UPGRADE_APPLIES_TO = Component.translatable(
                    Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "smithing_template.elytra_upgrade.applies_to"))
            )
            .withStyle(DESCRIPTION_FORMAT);
    public static final Component ELYTRA_UPGRADE_INGREDIENTS = Component.translatable(
                    Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"smithing_template.elytra_upgrade.ingredients"))
            )
            .withStyle(DESCRIPTION_FORMAT);
    public static final Component ELYTRA_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(
            Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"smithing_template.elytra_upgrade.base_slot_description"))
    );
    public static final Component ELYTRA_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(
            Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"smithing_template.elytra_upgrade.additions_slot_description"))
    );

    public static final Component ROSARITE_UPGRADE = Component.translatable(
                    Util.makeDescriptionId("upgrade", ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"rosarite_upgrade"))
            )
            .withStyle(TITLE_FORMAT);
    public static final Component ROSARITE_UPGRADE_APPLIES_TO = Component.translatable(
                    Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "smithing_template.rosarite_upgrade.applies_to"))
            )
            .withStyle(DESCRIPTION_FORMAT);
    public static final Component ROSARITE_UPGRADE_INGREDIENTS = Component.translatable(
                    Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"smithing_template.rosarite_upgrade.ingredients"))
            )
            .withStyle(DESCRIPTION_FORMAT);
    public static final Component ROSARITE_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(
            Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"smithing_template.rosarite_upgrade.base_slot_description"))
    );
    public static final Component ROSARITE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(
            Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"smithing_template.rosarite_upgrade.additions_slot_description"))
    );

    private static final ResourceLocation EMPTY_SLOT_HELMET = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet");
    private static final ResourceLocation EMPTY_SLOT_CHESTPLATE = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate");
    private static final ResourceLocation EMPTY_SLOT_LEGGINGS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings");
    private static final ResourceLocation EMPTY_SLOT_BOOTS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots");
    private static final ResourceLocation EMPTY_SLOT_ELYTRA = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_elytra");
    private static final ResourceLocation EMPTY_SLOT_HOE = ResourceLocation.withDefaultNamespace("item/empty_slot_hoe");
    private static final ResourceLocation EMPTY_SLOT_AXE = ResourceLocation.withDefaultNamespace("item/empty_slot_axe");
    private static final ResourceLocation EMPTY_SLOT_SWORD = ResourceLocation.withDefaultNamespace("item/empty_slot_sword");
    private static final ResourceLocation EMPTY_SLOT_SHOVEL = ResourceLocation.withDefaultNamespace("item/empty_slot_shovel");
    private static final ResourceLocation EMPTY_SLOT_PICKAXE = ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe");
    private static final ResourceLocation EMPTY_SLOT_INGOT = ResourceLocation.withDefaultNamespace("item/empty_slot_ingot");
    private static final ResourceLocation EMPTY_SLOT_REDSTONE_DUST = ResourceLocation.withDefaultNamespace("item/empty_slot_redstone_dust");
    private static final ResourceLocation EMPTY_SLOT_QUARTZ = ResourceLocation.withDefaultNamespace("item/empty_slot_quartz");
    private static final ResourceLocation EMPTY_SLOT_EMERALD = ResourceLocation.withDefaultNamespace("item/empty_slot_emerald");
    private static final ResourceLocation EMPTY_SLOT_DIAMOND = ResourceLocation.withDefaultNamespace("item/empty_slot_diamond");
    private static final ResourceLocation EMPTY_SLOT_LAPIS_LAZULI = ResourceLocation.withDefaultNamespace("item/empty_slot_lapis_lazuli");
    private static final ResourceLocation EMPTY_SLOT_AMETHYST_SHARD = ResourceLocation.withDefaultNamespace("item/empty_slot_amethyst_shard");
    private final Component appliesTo;
    private final Component ingredients;
    private final Component upgradeDescription;
    private final Component baseSlotDescription;
    private final Component additionsSlotDescription;
    private final List<ResourceLocation> baseSlotEmptyIcons;
    private final List<ResourceLocation> additionalSlotEmptyIcons;

    public ModSmithingTemplateItem(
            Component appliesTo,
            Component ingredients,
            Component upgradeDescription,
            Component baseSlotDescription,
            Component additionsSlotDescription,
            List<ResourceLocation> baseSlotEmptyIcons,
            List<ResourceLocation> additionalSlotEmptyIcons,
            FeatureFlag... requiredFeatures
    ) {
        super(new Item.Properties().requiredFeatures(requiredFeatures));
        this.appliesTo = appliesTo;
        this.ingredients = ingredients;
        this.upgradeDescription = upgradeDescription;
        this.baseSlotDescription = baseSlotDescription;
        this.additionsSlotDescription = additionsSlotDescription;
        this.baseSlotEmptyIcons = baseSlotEmptyIcons;
        this.additionalSlotEmptyIcons = additionalSlotEmptyIcons;
    }

    public static ModSmithingTemplateItem createElytraUpgradeTemplate() {
        return new ModSmithingTemplateItem(
                ELYTRA_UPGRADE_APPLIES_TO,
                ELYTRA_UPGRADE_INGREDIENTS,
                ELYTRA_UPGRADE,
                ELYTRA_UPGRADE_BASE_SLOT_DESCRIPTION,
                ELYTRA_UPGRADE_ADDITIONS_SLOT_DESCRIPTION,
                createElytraUpgradeIconList(),
                createElytraUpgradeMaterialList()
        );
    }

    public static SmithingTemplateItem createRosariteUpgradeTemplate() {
        return new SmithingTemplateItem(
                ROSARITE_UPGRADE_APPLIES_TO,
                ROSARITE_UPGRADE_INGREDIENTS,
                ROSARITE_UPGRADE,
                ROSARITE_UPGRADE_BASE_SLOT_DESCRIPTION,
                ROSARITE_UPGRADE_ADDITIONS_SLOT_DESCRIPTION,
                createRosariteUpgradeIconList(),
                createRosariteUpgradeMaterialList()
        );
    }

    public static List<ResourceLocation> createRosariteUpgradeIconList() {
        return List.of(
                EMPTY_SLOT_HELMET,
                EMPTY_SLOT_SWORD,
                EMPTY_SLOT_CHESTPLATE,
                EMPTY_SLOT_PICKAXE,
                EMPTY_SLOT_LEGGINGS,
                EMPTY_SLOT_AXE,
                EMPTY_SLOT_BOOTS,
                EMPTY_SLOT_HOE,
                EMPTY_SLOT_SHOVEL
        );
    }

    public static List<ResourceLocation> createRosariteUpgradeMaterialList() {
        return List.of(EMPTY_SLOT_INGOT);
    }

    public static List<ResourceLocation> createElytraUpgradeIconList() {
        return Collections.singletonList(EMPTY_SLOT_ELYTRA);
    }

    public static List<ResourceLocation> createElytraUpgradeMaterialList() {
        return List.of(
                EMPTY_SLOT_INGOT,
                EMPTY_SLOT_DIAMOND,
                EMPTY_SLOT_EMERALD
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(this.upgradeDescription);
        tooltipComponents.add(CommonComponents.EMPTY);
        tooltipComponents.add(APPLIES_TO_TITLE);
        tooltipComponents.add(CommonComponents.space().append(this.appliesTo));
        tooltipComponents.add(INGREDIENTS_TITLE);
        tooltipComponents.add(CommonComponents.space().append(this.ingredients));
    }

    public Component getBaseSlotDescription() {
        return this.baseSlotDescription;
    }

    public Component getAdditionSlotDescription() {
        return this.additionsSlotDescription;
    }

    public List<ResourceLocation> getBaseSlotEmptyIcons() {
        return this.baseSlotEmptyIcons;
    }

    public List<ResourceLocation> getAdditionalSlotEmptyIcons() {
        return this.additionalSlotEmptyIcons;
    }
}
