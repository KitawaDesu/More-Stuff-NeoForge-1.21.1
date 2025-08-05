package net.kitawa.more_stuff.util.datagen;

import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, MoreStuff.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModItemTags.GLIDERS)
                .add(Items.ELYTRA)
                .add(ModItems.LEATHER_GLIDER.get())
                .add(ModItems.CHAINED_ELYTRA.get())
                .add(ModItems.GOLDEN_ELYTRA.get())
                .add(ModItems.COPPER_ELYTRA.get())
                .add(ModItems.IRON_ELYTRA.get())
                .add(ModItems.ROSE_GOLDEN_ELYTRA.get())
                .add(ModItems.EMERALD_ELYTRA.get())
                .add(ModItems.DIAMOND_ELYTRA.get())
                .add(ModItems.NETHERITE_ELYTRA.get())
                .add(ModItems.ROSARITE_ELYTRA.get());

        tag(ModItemTags.TEIRED_GLIDERS)
                .add(ModItems.LEATHER_GLIDER.get())
                .add(ModItems.CHAINED_ELYTRA.get())
                .add(ModItems.GOLDEN_ELYTRA.get())
                .add(ModItems.COPPER_ELYTRA.get())
                .add(ModItems.IRON_ELYTRA.get())
                .add(ModItems.ROSE_GOLDEN_ELYTRA.get())
                .add(ModItems.EMERALD_ELYTRA.get())
                .add(ModItems.DIAMOND_ELYTRA.get())
                .add(ModItems.NETHERITE_ELYTRA.get())
                .add(ModItems.ROSARITE_ELYTRA.get());

        tag(ModItemTags.UPGRADES_TO_DIAMOND_LEVEL_ELYTRA)
                .add(ModItems.COPPER_ELYTRA.get())
                .add(ModItems.IRON_ELYTRA.get())
                .add(ModItems.ROSE_GOLDEN_ELYTRA.get());

        tag(ModItemTags.WOLF_ARMOR)
                .add(Items.WOLF_ARMOR)
                .add(ModItems.LEATHER_WOLF_ARMOR.get())
                .add(ModItems.CHAINMAIL_WOLF_ARMOR.get())
                .add(ModItems.GOLDEN_WOLF_ARMOR.get())
                .add(ModItems.ROSE_GOLDEN_WOLF_ARMOR.get())
                .add(ModItems.COPPER_WOLF_ARMOR.get())
                .add(ModItems.IRON_WOLF_ARMOR.get())
                .add(ModItems.DIAMOND_WOLF_ARMOR.get())
                .add(ModItems.TURTLE_SCUTE_WOLF_ARMOR.get())
                .add(ModItems.EMERALD_WOLF_ARMOR.get())
                .add(ModItems.NETHERITE_WOLF_ARMOR.get())
                .add(ModItems.ROSARITE_WOLF_ARMOR.get());

        tag(ItemTags.MACE_ENCHANTABLE)
                .add(Items.MACE)
                .add(ModItems.WOODEN_MACE.get())
                .add(ModItems.STONE_MACE.get())
                .add(ModItems.IRON_MACE.get())
                .add(ModItems.COPPER_MACE.get())
                .add(ModItems.ROSE_GOLDEN_MACE.get())
                .add(ModItems.GOLDEN_MACE.get())
                .add(ModItems.DIAMOND_MACE.get())
                .add(ModItems.EMERALD_MACE.get())
                .add(ModItems.NETHERITE_MACE.get())
                .add(ModItems.ROSARITE_MACE.get())
                .add(ModItems.LAPIS_MACE.get())
                .add(ModItems.QUARTZ_MACE.get());

        if (ModList.get().isLoaded("create")) {
            tag(ModItemTags.HORSE_ARMOR)
                    .add(CreateCompatItems.BRASS_HORSE_ARMOR.get())
                    .add(CreateCompatItems.ZINC_HORSE_ARMOR.get());

            tag(ItemTags.MACE_ENCHANTABLE)
                    .add(CreateCompatItems.ZINC_MACE.get())
                    .add(CreateCompatItems.BRASS_MACE.get());

            tag(ModItemTags.GLIDERS)
                    .add(CreateCompatItems.ZINC_ELYTRA.get())
                    .add(CreateCompatItems.BRASS_ELYTRA.get());

            tag(ModItemTags.TEIRED_GLIDERS)
                    .add(CreateCompatItems.ZINC_ELYTRA.get())
                    .add(CreateCompatItems.BRASS_ELYTRA.get());

            tag(ModItemTags.UPGRADES_TO_DIAMOND_LEVEL_ELYTRA)
                    .add(CreateCompatItems.ZINC_ELYTRA.get())
                    .add(CreateCompatItems.BRASS_ELYTRA.get());

            tag(ModItemTags.WOLF_ARMOR)
                    .add(CreateCompatItems.ZINC_WOLF_ARMOR.get())
                    .add(CreateCompatItems.BRASS_WOLF_ARMOR.get());

            tag(ModItemTags.HOGLIN_ARMOR)
                    .add(CreateCompatItems.ZINC_HOGLIN_ARMOR.get())
                    .add(CreateCompatItems.BRASS_HOGLIN_ARMOR.get());

            this.tag(ItemTags.TRIMMABLE_ARMOR)
                    .add(CreateCompatItems.ZINC_HELMET.get())
                    .add(CreateCompatItems.ZINC_CHESTPLATE.get())
                    .add(CreateCompatItems.ZINC_LEGGINGS.get())
                    .add(CreateCompatItems.ZINC_BOOTS.get())
                    .add(CreateCompatItems.BRASS_HELMET.get())
                    .add(CreateCompatItems.BRASS_CHESTPLATE.get())
                    .add(CreateCompatItems.BRASS_LEGGINGS.get())
                    .add(CreateCompatItems.BRASS_BOOTS.get());

            tag(ItemTags.SWORDS)
                    .add(CreateCompatItems.ZINC_SWORD.get())
                    .add(CreateCompatItems.BRASS_SWORD.get());
            tag(ItemTags.PICKAXES)
                    .add(CreateCompatItems.ZINC_PICKAXE.get())
                    .add(CreateCompatItems.BRASS_PICKAXE.get());
            tag(ItemTags.SHOVELS)
                    .add(CreateCompatItems.ZINC_SHOVEL.get())
                    .add(CreateCompatItems.BRASS_SHOVEL.get());
            tag(ItemTags.AXES)
                    .add(CreateCompatItems.ZINC_AXE.get())
                    .add(CreateCompatItems.BRASS_AXE.get());
            tag(ItemTags.HOES)
                    .add(CreateCompatItems.ZINC_HOE.get())
                    .add(CreateCompatItems.BRASS_HOE.get());

            tag(ItemTags.HEAD_ARMOR)
                    .add(CreateCompatItems.ZINC_HELMET.get())
                    .add(CreateCompatItems.BRASS_HELMET.get());
            tag(ItemTags.CHEST_ARMOR)
                    .add(CreateCompatItems.ZINC_CHESTPLATE.get())
                    .add(CreateCompatItems.BRASS_CHESTPLATE.get());
            tag(ItemTags.LEG_ARMOR)
                    .add(CreateCompatItems.ZINC_LEGGINGS.get())
                    .add(CreateCompatItems.BRASS_LEGGINGS.get());
            tag(ItemTags.FOOT_ARMOR)
                    .add(CreateCompatItems.ZINC_BOOTS.get())
                    .add(CreateCompatItems.BRASS_BOOTS.get());

            tag(ModItemTags.BRASS_TOOL_MATERIALS)
                    .add(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "brass_ingot")));

            tag(ModItemTags.ZINC_TOOL_MATERIALS)
                    .add(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot")));

            tag(ModItemTags.SHEARS)
                    .add(CreateCompatItems.ZINC_SHEARS.get())
                    .add(CreateCompatItems.BRASS_SHEARS.get());
        }

        tag(ModItemTags.ABSORBS_DAMAGE)
                .addTag(ModItemTags.WOLF_ARMOR)
                .addTag(ModItemTags.HOGLIN_ARMOR);

        tag(ModItemTags.SHEARS)
                .add(Items.SHEARS)
                .add(ModItems.WOODEN_SHEARS.get())
                .add(ModItems.STONE_SHEARS.get())
                .add(ModItems.COPPER_SHEARS.get())
                .add(ModItems.ROSE_GOLDEN_SHEARS.get())
                .add(ModItems.GOLDEN_SHEARS.get())
                .add(ModItems.DIAMOND_SHEARS.get())
                .add(ModItems.EMERALD_SHEARS.get())
                .add(ModItems.NETHERITE_SHEARS.get())
                .add(ModItems.ROSARITE_SHEARS.get())
                .add(ModItems.LAPIS_SHEARS.get())
                .add(ModItems.QUARTZ_SHEARS.get());

        tag(ModItemTags.HOGLIN_ARMOR)
                .add(ModItems.HOGLIN_ARMOR.get())
                .add(ModItems.LEATHER_HOGLIN_ARMOR.get())
                .add(ModItems.CHAINMAIL_HOGLIN_ARMOR.get())
                .add(ModItems.GOLDEN_HOGLIN_ARMOR.get())
                .add(ModItems.ROSE_GOLDEN_HOGLIN_ARMOR.get())
                .add(ModItems.COPPER_HOGLIN_ARMOR.get())
                .add(ModItems.IRON_HOGLIN_ARMOR.get())
                .add(ModItems.DIAMOND_HOGLIN_ARMOR.get())
                .add(ModItems.TURTLE_SCUTE_HOGLIN_ARMOR.get())
                .add(ModItems.EMERALD_HOGLIN_ARMOR.get())
                .add(ModItems.NETHERITE_HOGLIN_ARMOR.get())
                .add(ModItems.ROSARITE_HOGLIN_ARMOR.get());

        tag(ModItemTags.HORSE_ARMOR)
                .add(Items.LEATHER_HORSE_ARMOR)
                .add(ModItems.CHAINMAIL_HORSE_ARMOR.get())
                .add(Items.IRON_HORSE_ARMOR)
                .add(ModItems.COPPER_HORSE_ARMOR.get())
                .add(ModItems.ROSE_GOLDEN_HORSE_ARMOR.get())
                .add(ModItems.EMERALD_HORSE_ARMOR.get())
                .add(ModItems.ROSARITE_HORSE_ARMOR.get())
                .add(ModItems.NETHERITE_HORSE_ARMOR.get())
                .add(Items.GOLDEN_HORSE_ARMOR)
                .add(Items.DIAMOND_HORSE_ARMOR);

        tag(ItemTags.HEAD_ARMOR)
                .add(ModItems.EMERALD_HELMET.get())
                .add(ModItems.ROSE_GOLDEN_HELMET.get())
                .add(ModItems.COPPER_HELMET.get())
                .add(ModItems.ROSARITE_HELMET.get());
        tag(ItemTags.CHEST_ARMOR)
                .add(ModItems.EMERALD_CHESTPLATE.get())
                .add(ModItems.ROSE_GOLDEN_CHESTPLATE.get())
                .add(ModItems.COPPER_CHESTPLATE.get())
                .add(ModItems.ROSARITE_CHESTPLATE.get());
        tag(ItemTags.LEG_ARMOR)
                .add(ModItems.EMERALD_LEGGINGS.get())
                .add(ModItems.ROSE_GOLDEN_LEGGINGS.get())
                .add(ModItems.COPPER_LEGGINGS.get())
                .add(ModItems.ROSARITE_LEGGINGS.get());
        tag(ItemTags.FOOT_ARMOR)
                .add(ModItems.EMERALD_BOOTS.get())
                .add(ModItems.ROSE_GOLDEN_BOOTS.get())
                .add(ModItems.COPPER_BOOTS.get())
                .add(ModItems.ROSARITE_BOOTS.get());

        tag(ItemTags.SWORDS)
                .add(ModItems.EMERALD_SWORD.get())
                .add(ModItems.ROSE_GOLDEN_SWORD.get())
                .add(ModItems.COPPER_SWORD.get())
                .add(ModItems.ROSARITE_SWORD.get())
                .add(ModItems.LAPIS_SWORD.get())
                .add(ModItems.QUARTZ_SWORD.get());
        tag(ItemTags.PICKAXES)
                .add(ModItems.EMERALD_PICKAXE.get())
                .add(ModItems.ROSE_GOLDEN_PICKAXE.get())
                .add(ModItems.COPPER_PICKAXE.get())
                .add(ModItems.ROSARITE_PICKAXE.get())
                .add(ModItems.LAPIS_PICKAXE.get())
                .add(ModItems.QUARTZ_PICKAXE.get());
        tag(ItemTags.SHOVELS)
                .add(ModItems.EMERALD_SHOVEL.get())
                .add(ModItems.ROSE_GOLDEN_SHOVEL.get())
                .add(ModItems.COPPER_SHOVEL.get())
                .add(ModItems.ROSARITE_SHOVEL.get())
                .add(ModItems.LAPIS_SHOVEL.get())
                .add(ModItems.QUARTZ_SHOVEL.get());
        tag(ItemTags.AXES)
                .add(ModItems.EMERALD_AXE.get())
                .add(ModItems.ROSE_GOLDEN_AXE.get())
                .add(ModItems.COPPER_AXE.get())
                .add(ModItems.ROSARITE_AXE.get())
                .add(ModItems.LAPIS_AXE.get())
                .add(ModItems.QUARTZ_AXE.get());
        tag(ItemTags.HOES)
                .add(ModItems.EMERALD_HOE.get())
                .add(ModItems.ROSE_GOLDEN_HOE.get())
                .add(ModItems.COPPER_HOE.get())
                .add(ModItems.ROSARITE_HOE.get())
                .add(ModItems.LAPIS_HOE.get())
                .add(ModItems.QUARTZ_HOE.get());

        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.COPPER_HELMET.get())
                .add(ModItems.COPPER_CHESTPLATE.get())
                .add(ModItems.COPPER_LEGGINGS.get())
                .add(ModItems.COPPER_BOOTS.get())
                .add(ModItems.ROSE_GOLDEN_HELMET.get())
                .add(ModItems.ROSE_GOLDEN_CHESTPLATE.get())
                .add(ModItems.ROSE_GOLDEN_LEGGINGS.get())
                .add(ModItems.ROSE_GOLDEN_BOOTS.get())
                .add(ModItems.EMERALD_HELMET.get())
                .add(ModItems.EMERALD_CHESTPLATE.get())
                .add(ModItems.EMERALD_LEGGINGS.get())
                .add(ModItems.EMERALD_BOOTS.get())
                .add(ModItems.ROSARITE_HELMET.get())
                .add(ModItems.ROSARITE_CHESTPLATE.get())
                .add(ModItems.ROSARITE_LEGGINGS.get())
                .add(ModItems.ROSARITE_BOOTS.get());

        this.tag(ItemTags.TRIM_MATERIALS)
                .add(ModItems.ROSARITE_INGOT.get())
                .add(ModItems.ROSE_GOLD_INGOT.get());

        tag(ItemTags.CHEST_ARMOR_ENCHANTABLE)
                .addTag(ModItemTags.TEIRED_GLIDERS);

        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .addTag(ModItemTags.SHEARS)
                .addTag(ModItemTags.WOLF_ARMOR)
                .addTag(ModItemTags.HOGLIN_ARMOR)
                .addTag(ModItemTags.HORSE_ARMOR)
                .addTag(ModItemTags.GLIDERS)
                .add(ModItems.WOODEN_MACE.get())
                .add(ModItems.STONE_MACE.get())
                .add(ModItems.IRON_MACE.get())
                .add(ModItems.COPPER_MACE.get())
                .add(ModItems.ROSE_GOLDEN_MACE.get())
                .add(ModItems.GOLDEN_MACE.get())
                .add(ModItems.DIAMOND_MACE.get())
                .add(ModItems.EMERALD_MACE.get())
                .add(ModItems.NETHERITE_MACE.get())
                .add(ModItems.ROSARITE_MACE.get())
                .add(ModItems.LAPIS_MACE.get())
                .add(ModItems.QUARTZ_MACE.get());

        tag(ItemTags.MINING_ENCHANTABLE)
                .addTag(ModItemTags.SHEARS);

        tag(ItemTags.ARMOR_ENCHANTABLE)
                .addTag(ModItemTags.HORSE_ARMOR);

        tag(ItemTags.WEAPON_ENCHANTABLE)
                .add(ModItems.WOODEN_MACE.get())
                .add(ModItems.STONE_MACE.get())
                .add(ModItems.IRON_MACE.get())
                .add(ModItems.COPPER_MACE.get())
                .add(ModItems.ROSE_GOLDEN_MACE.get())
                .add(ModItems.GOLDEN_MACE.get())
                .add(ModItems.DIAMOND_MACE.get())
                .add(ModItems.EMERALD_MACE.get())
                .add(ModItems.NETHERITE_MACE.get())
                .add(ModItems.ROSARITE_MACE.get())
                .add(ModItems.LAPIS_MACE.get())
                .add(ModItems.QUARTZ_MACE.get());

        tag(ItemTags.FIRE_ASPECT_ENCHANTABLE)
                .add(ModItems.WOODEN_MACE.get())
                .add(ModItems.STONE_MACE.get())
                .add(ModItems.IRON_MACE.get())
                .add(ModItems.COPPER_MACE.get())
                .add(ModItems.ROSE_GOLDEN_MACE.get())
                .add(ModItems.GOLDEN_MACE.get())
                .add(ModItems.DIAMOND_MACE.get())
                .add(ModItems.EMERALD_MACE.get())
                .add(ModItems.NETHERITE_MACE.get())
                .add(ModItems.ROSARITE_MACE.get())
                .add(ModItems.LAPIS_MACE.get())
                .add(ModItems.QUARTZ_MACE.get());

        tag(ItemTags.SWORD_ENCHANTABLE)
                .add(Items.MACE)
                .add(ModItems.WOODEN_MACE.get())
                .add(ModItems.STONE_MACE.get())
                .add(ModItems.IRON_MACE.get())
                .add(ModItems.COPPER_MACE.get())
                .add(ModItems.ROSE_GOLDEN_MACE.get())
                .add(ModItems.GOLDEN_MACE.get())
                .add(ModItems.DIAMOND_MACE.get())
                .add(ModItems.EMERALD_MACE.get())
                .add(ModItems.NETHERITE_MACE.get())
                .add(ModItems.ROSARITE_MACE.get())
                .add(ModItems.LAPIS_MACE.get())
                .add(ModItems.QUARTZ_MACE.get());

        tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .add(Items.MACE)
                .add(ModItems.WOODEN_MACE.get())
                .add(ModItems.STONE_MACE.get())
                .add(ModItems.IRON_MACE.get())
                .add(ModItems.COPPER_MACE.get())
                .add(ModItems.ROSE_GOLDEN_MACE.get())
                .add(ModItems.GOLDEN_MACE.get())
                .add(ModItems.DIAMOND_MACE.get())
                .add(ModItems.EMERALD_MACE.get())
                .add(ModItems.NETHERITE_MACE.get())
                .add(ModItems.ROSARITE_MACE.get())
                .add(ModItems.LAPIS_MACE.get())
                .add(ModItems.QUARTZ_MACE.get());

        tag(ItemTags.VANISHING_ENCHANTABLE)
                .addTag(ModItemTags.SHEARS)
                .add(ModItems.WOODEN_MACE.get())
                .add(ModItems.STONE_MACE.get())
                .add(ModItems.IRON_MACE.get())
                .add(ModItems.COPPER_MACE.get())
                .add(ModItems.ROSE_GOLDEN_MACE.get())
                .add(ModItems.GOLDEN_MACE.get())
                .add(ModItems.DIAMOND_MACE.get())
                .add(ModItems.EMERALD_MACE.get())
                .add(ModItems.NETHERITE_MACE.get())
                .add(ModItems.ROSARITE_MACE.get())
                .add(ModItems.LAPIS_MACE.get())
                .add(ModItems.QUARTZ_MACE.get());

        tag(ItemTags.EQUIPPABLE_ENCHANTABLE)
                .addTag(ModItemTags.WOLF_ARMOR)
                .addTag(ModItemTags.HOGLIN_ARMOR)
                .addTag(ModItemTags.TEIRED_GLIDERS)
                .addTag(ModItemTags.HORSE_ARMOR);

        tag(ItemTags.FREEZE_IMMUNE_WEARABLES)
                .add(ModItems.LEATHER_GLIDER.get())
                .add(ModItems.LEATHER_WOLF_ARMOR.get())
                .add(ModItems.LEATHER_HOGLIN_ARMOR.get());

        tag(ItemTags.PIGLIN_LOVED)
                .add(ModItems.GOLDEN_ELYTRA.get())
                .add(ModItems.GOLDEN_BAMBOO.get())
                .add(ModItems.GOLDEN_WOLF_ARMOR.get())
                .add(ModItems.ROSE_GOLD_INGOT.get())
                .add(ModItems.ROSE_GOLD_NUGGET.get())
                .add(ModBlocks.ROSE_GOLD_BLOCK.asItem())
                .add(ModItems.ROSE_GOLDEN_SHOVEL.get())
                .add(ModItems.ROSE_GOLDEN_PICKAXE.get())
                .add(ModItems.ROSE_GOLDEN_HOE.get())
                .add(ModItems.ROSE_GOLDEN_AXE.get())
                .add(ModItems.ROSE_GOLDEN_SWORD.get())
                .add(ModItems.ROSE_GOLDEN_BOOTS.get())
                .add(ModItems.ROSE_GOLDEN_LEGGINGS.get())
                .add(ModItems.ROSE_GOLDEN_CHESTPLATE.get())
                .add(ModItems.ROSE_GOLDEN_ELYTRA.get())
                .add(ModItems.ROSE_GOLDEN_HELMET.get())
                .add(ModItems.ROSE_GOLDEN_HORSE_ARMOR.get())
                .add(ModItems.ROSE_GOLDEN_WOLF_ARMOR.get());

    }
}
