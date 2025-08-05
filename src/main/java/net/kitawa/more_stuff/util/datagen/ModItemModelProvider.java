package net.kitawa.more_stuff.util.datagen;

import net.kitawa.more_stuff.compat.create.blocks.CreateCompatBlocks;
import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MoreStuff.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        trimmedArmorItem(ModItems.COPPER_HELMET);
        trimmedArmorItem(ModItems.COPPER_CHESTPLATE);
        trimmedArmorItem(ModItems.COPPER_LEGGINGS);
        trimmedArmorItem(ModItems.COPPER_BOOTS);
        trimmedArmorItem(ModItems.ROSE_GOLDEN_HELMET);
        trimmedArmorItem(ModItems.ROSE_GOLDEN_CHESTPLATE);
        trimmedArmorItem(ModItems.ROSE_GOLDEN_LEGGINGS);
        trimmedArmorItem(ModItems.ROSE_GOLDEN_BOOTS);
        trimmedArmorItem(ModItems.EMERALD_HELMET);
        trimmedArmorItem(ModItems.EMERALD_CHESTPLATE);
        trimmedArmorItem(ModItems.EMERALD_LEGGINGS);
        trimmedArmorItem(ModItems.EMERALD_BOOTS);
        trimmedArmorItem(ModItems.ROSARITE_HELMET);
        trimmedArmorItem(ModItems.ROSARITE_CHESTPLATE);
        trimmedArmorItem(ModItems.ROSARITE_LEGGINGS);
        trimmedArmorItem(ModItems.ROSARITE_BOOTS);
        if (ModList.get().isLoaded("create")) {
            trimmedArmorItem(CreateCompatItems.BRASS_HELMET);
            trimmedArmorItem(CreateCompatItems.BRASS_CHESTPLATE);
            trimmedArmorItem(CreateCompatItems.BRASS_LEGGINGS);
            trimmedArmorItem(CreateCompatItems.BRASS_BOOTS);
            trimmedArmorItem(CreateCompatItems.ZINC_HELMET);
            trimmedArmorItem(CreateCompatItems.ZINC_CHESTPLATE);
            trimmedArmorItem(CreateCompatItems.ZINC_LEGGINGS);
            trimmedArmorItem(CreateCompatItems.ZINC_BOOTS);

            handheldItem(CreateCompatItems.ZINC_PICKAXE);
            handheldItem(CreateCompatItems.ZINC_AXE);
            handheldItem(CreateCompatItems.ZINC_SHOVEL);
            handheldItem(CreateCompatItems.ZINC_SWORD);
            handheldItem(CreateCompatItems.ZINC_HOE);

            handheldItem(CreateCompatItems.BRASS_PICKAXE);
            handheldItem(CreateCompatItems.BRASS_AXE);
            handheldItem(CreateCompatItems.BRASS_SHOVEL);
            handheldItem(CreateCompatItems.BRASS_SWORD);
            handheldItem(CreateCompatItems.BRASS_HOE);

            basicItem(CreateCompatItems.CRUSHED_ANCIENT_DEBRIS.get());
            basicItem(CreateCompatItems.ANCIENT_DUST.get());
            basicItem(CreateCompatItems.COPPER_DUST.get());
            basicItem(CreateCompatItems.BRASS_DUST.get());
            basicItem(CreateCompatItems.ZINC_DUST.get());
            basicItem(CreateCompatItems.IRON_DUST.get());
            basicItem(CreateCompatItems.GOLDEN_DUST.get());
            handheldItem(CreateCompatItems.ZINC_HORSE_ARMOR);
            handheldItem(CreateCompatItems.BRASS_HORSE_ARMOR);
            simpleBlockItem(CreateCompatBlocks.FROZEN_ZINC_ORE.get());
            simpleBlockItem(CreateCompatBlocks.NETHER_ZINC_ORE.get());
            simpleBlockItem(CreateCompatBlocks.PYROLIZED_ZINC_ORE.get());

            handheldItem(CreateCompatItems.BRASS_MACE);
            handheldItem(CreateCompatItems.ZINC_MACE);

            basicItem(CreateCompatItems.BRASS_SHEARS.get());
            basicItem(CreateCompatItems.ZINC_SHEARS.get());
        }

        handheldItem(ModItems.WOODEN_MACE);
        handheldItem(ModItems.STONE_MACE);
        handheldItem(ModItems.LAPIS_MACE);
        handheldItem(ModItems.QUARTZ_MACE);
        handheldItem(ModItems.IRON_MACE);
        handheldItem(ModItems.COPPER_MACE);
        handheldItem(ModItems.ROSE_GOLDEN_MACE);
        handheldItem(ModItems.GOLDEN_MACE);
        handheldItem(ModItems.DIAMOND_MACE);
        handheldItem(ModItems.EMERALD_MACE);
        handheldItem(ModItems.NETHERITE_MACE);
        handheldItem(ModItems.ROSARITE_MACE);

        basicItem(ModItems.WOODEN_SHEARS.get());
        basicItem(ModItems.STONE_SHEARS.get());
        basicItem(ModItems.LAPIS_SHEARS.get());
        basicItem(ModItems.QUARTZ_SHEARS.get());
        basicItem(ModItems.COPPER_SHEARS.get());
        basicItem(ModItems.ROSE_GOLDEN_SHEARS.get());
        basicItem(ModItems.GOLDEN_SHEARS.get());
        basicItem(ModItems.DIAMOND_SHEARS.get());
        basicItem(ModItems.EMERALD_SHEARS.get());
        basicItem(ModItems.NETHERITE_SHEARS.get());
        basicItem(ModItems.ROSARITE_SHEARS.get());
        basicItem(ModItems.WARPED_WART.get());
        basicItem(ModItems.PYROLIZED_NETHER_BRICK.get());
        basicItem(ModItems.RED_NETHER_BRICK.get());
        basicItem(ModItems.WARPED_NETHER_BRICK.get());

        basicItem(ModItems.ROSE_GOLD_INGOT.get());
        basicItem(ModItems.ROSARITE_INGOT.get());
        basicItem(ModItems.COPPER_NUGGET.get());
        basicItem(ModItems.ANCIENT_CHUNK.get());

        handheldItem(ModItems.LAPIS_PICKAXE);
        handheldItem(ModItems.LAPIS_AXE);
        handheldItem(ModItems.LAPIS_SHOVEL);
        handheldItem(ModItems.LAPIS_SWORD);
        handheldItem(ModItems.LAPIS_HOE);

        handheldItem(ModItems.QUARTZ_PICKAXE);
        handheldItem(ModItems.QUARTZ_AXE);
        handheldItem(ModItems.QUARTZ_SHOVEL);
        handheldItem(ModItems.QUARTZ_SWORD);
        handheldItem(ModItems.QUARTZ_HOE);

        handheldItem(ModItems.ROSE_GOLDEN_PICKAXE);
        handheldItem(ModItems.ROSE_GOLDEN_AXE);
        handheldItem(ModItems.ROSE_GOLDEN_SHOVEL);
        handheldItem(ModItems.ROSE_GOLDEN_SWORD);
        handheldItem(ModItems.ROSE_GOLDEN_HOE);

        handheldItem(ModItems.COPPER_PICKAXE);
        handheldItem(ModItems.COPPER_AXE);
        handheldItem(ModItems.COPPER_SHOVEL);
        handheldItem(ModItems.COPPER_SWORD);
        handheldItem(ModItems.COPPER_HOE);

        handheldItem(ModItems.EMERALD_PICKAXE);
        handheldItem(ModItems.EMERALD_AXE);
        handheldItem(ModItems.EMERALD_SHOVEL);
        handheldItem(ModItems.EMERALD_SWORD);
        handheldItem(ModItems.EMERALD_HOE);

        handheldItem(ModItems.ROSARITE_PICKAXE);
        handheldItem(ModItems.ROSARITE_AXE);
        handheldItem(ModItems.ROSARITE_SHOVEL);
        handheldItem(ModItems.ROSARITE_SWORD);
        handheldItem(ModItems.ROSARITE_HOE);

        handheldItem(ModItems.CHAINMAIL_HORSE_ARMOR);
        handheldItem(ModItems.COPPER_HORSE_ARMOR);
        handheldItem(ModItems.ROSE_GOLDEN_HORSE_ARMOR);
        handheldItem(ModItems.EMERALD_HORSE_ARMOR);
        handheldItem(ModItems.NETHERITE_HORSE_ARMOR);
        handheldItem(ModItems.ROSARITE_HORSE_ARMOR);

        buttonItem(ModBlocks.EBONY_BUTTON, ModBlocks.EBONY_PLANKS);
        fenceItem(ModBlocks.EBONY_FENCE, ModBlocks.EBONY_PLANKS);

        basicItem(ModBlocks.EBONY_DOOR.asItem());
        basicItem(ModItems.STONES.asItem());
        basicItem(ModItems.DEEPSLATE_STONES.asItem());
        basicItem(ModItems.NETHERRACK_STONES.asItem());
        basicItem(ModItems.PYROLIZED_STONES.asItem());
        basicItem(ModItems.BASALT_STONES.asItem());
        simpleBlockItem(ModBlocks.SOUL_SNOW_BLOCK.get());
        simpleBlockItem(ModBlocks.POWDER_SOUL_SNOW.get());
        simpleBlockItem(ModBlocks.FREEZING_MAGMA_BLOCK.get());
        simpleBlockItem(ModBlocks.PACKED_FREEZING_MAGMA_BLOCK.get());
        simpleBlockItem(ModBlocks.BLUE_FREEZING_MAGMA_BLOCK.get());
        simpleBlockItem(ModBlocks.FROZEN_NETHERRACK.get());
        simpleBlockItem(ModBlocks.PACKED_FROZEN_NETHERRACK.get());
        simpleBlockItem(ModBlocks.BLUE_FROZEN_NETHERRACK.get());
        simpleBlockItem(ModBlocks.FROZEN_QUARTZ_ORE.get());
        simpleBlockItem(ModBlocks.FROZEN_GOLD_ORE.get());
        simpleBlockItem(ModBlocks.FROZEN_IRON_ORE.get());
        simpleBlockItem(ModBlocks.FROZEN_COPPER_ORE.get());
        simpleBlockItem(ModBlocks.FROZEN_EXPERIENCE_ORE.get());
        simpleBlockItem(ModBlocks.PYROLIZED_NETHER_BRICK_PILLAR.get());
        simpleBlockItem(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_PILLAR.get());
        simpleBlockItem(ModBlocks.RED_NETHER_BRICK_PILLAR.get());
        simpleBlockItem(ModBlocks.CRACKED_RED_NETHER_BRICK_PILLAR.get());
        simpleBlockItem(ModBlocks.NETHER_BRICK_PILLAR.get());
        simpleBlockItem(ModBlocks.CRACKED_NETHER_BRICK_PILLAR.get());
        simpleBlockItem(ModBlocks.BLAZE_BRICK_PILLAR.get());
        simpleBlockItem(ModBlocks.BLAZE_BRICKS.get());
        simpleBlockItem(ModBlocks.RED_BLAZE_BRICK_PILLAR.get());
        simpleBlockItem(ModBlocks.RED_BLAZE_BRICKS.get());
        simpleBlockItem(ModBlocks.PYROLIZED_BLAZE_BRICK_PILLAR.get());
        simpleBlockItem(ModBlocks.PYROLIZED_BLAZE_BRICKS.get());
        simpleBlockItem(ModBlocks.PYROLIZED_NETHER_BRICKS.get());
        simpleBlockItem(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICKS.get());
        simpleBlockItem(ModBlocks.WARPED_BLAZE_BRICK_PILLAR.get());
        simpleBlockItem(ModBlocks.WARPED_BLAZE_BRICKS.get());
        simpleBlockItem(ModBlocks.WARPED_NETHER_BRICKS.get());
        simpleBlockItem(ModBlocks.WARPED_NETHER_BRICK_PILLAR.get());
        simpleBlockItem(ModBlocks.CRACKED_WARPED_NETHER_BRICKS.get());
        simpleBlockItem(ModBlocks.CRACKED_WARPED_NETHER_BRICK_PILLAR.get());
    }

    // Shoutout to El_Redstoniano for making this
    private void trimmedArmorItem(DeferredItem<Item> itemDeferredItem) {
        final String MOD_ID = MoreStuff.MOD_ID; // Change this to your mod id

        if(itemDeferredItem.get() instanceof ArmorItem armorItem) {
            trimMaterials.forEach((trimMaterial, value) -> {
                float trimValue = value;

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = armorItem.toString();
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = ResourceLocation.parse(armorItemPath);
                ResourceLocation trimResLoc = ResourceLocation.parse(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = ResourceLocation.parse(currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc.getNamespace() + ":item/" + armorItemResLoc.getPath())
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemDeferredItem.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc.getNamespace()  + ":item/" + trimNameResLoc.getPath()))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                ResourceLocation.fromNamespaceAndPath(MOD_ID,
                                        "item/" + itemDeferredItem.getId().getPath()));
            });
        }
    }

    public void buttonItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/button_inventory"))
                .texture("texture",  ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

    public void fenceItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

    public void wallItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(DeferredItem<?> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID,"item/" + item.getId().getPath()));
    }
}