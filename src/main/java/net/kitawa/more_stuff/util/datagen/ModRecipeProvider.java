package net.kitawa.more_stuff.util.datagen;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.items.ModItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.registries.DeferredBlock;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        // Generate terracotta brick recipes (color → brick)
        generateTerracottaBrickRecipes(recipeOutput);

        // Generate sandstone brick recipes
        generateBrickRecipes(
                recipeOutput,
                Map.ofEntries(
                        Map.entry(Blocks.CUT_SANDSTONE, ModBlocks.SANDSTONE_BRICKS),
                        Map.entry(Blocks.CUT_RED_SANDSTONE, ModBlocks.RED_SANDSTONE_BRICKS),
                        Map.entry(Blocks.POLISHED_ANDESITE, ModBlocks.CUT_ANDESITE),
                        Map.entry(ModBlocks.CUT_ANDESITE.get(), ModBlocks.ANDESITE_BRICKS),
                        Map.entry(Blocks.POLISHED_DIORITE, ModBlocks.CUT_DIORITE),
                        Map.entry(ModBlocks.CUT_DIORITE.get(), ModBlocks.DIORITE_BRICKS),
                        Map.entry(Blocks.POLISHED_GRANITE, ModBlocks.CUT_GRANITE),
                        Map.entry(ModBlocks.CUT_GRANITE.get(), ModBlocks.GRANITE_BRICKS),
                        Map.entry(Blocks.GOLD_BLOCK, ModBlocks.CUT_GOLD_BLOCK),
                        Map.entry(ModBlocks.CUT_GOLD_BLOCK.get(), ModBlocks.CUT_GOLDEN_BRICKS),
                        Map.entry(Blocks.POLISHED_TUFF, ModBlocks.CUT_TUFF),
                        Map.entry(Blocks.POLISHED_BASALT, ModBlocks.CUT_BASALT),
                        Map.entry(ModBlocks.CUT_BASALT.get(), ModBlocks.BASALT_BRICKS),
                        Map.entry(ModBlocks.BASALT_BRICKS.get(), ModBlocks.BASALT_TILES),
                        Map.entry(Blocks.POLISHED_DEEPSLATE, ModBlocks.CUT_DEEPSLATE),
                        Map.entry(Blocks.DRIPSTONE_BLOCK, ModBlocks.POLISHED_DRIPSTONE),
                        Map.entry(ModBlocks.POLISHED_DRIPSTONE.get(), ModBlocks.CUT_DRIPSTONE),
                        Map.entry(ModBlocks.CUT_DRIPSTONE.get(), ModBlocks.DRIPSTONE_BRICKS),
                        Map.entry(Blocks.CALCITE, ModBlocks.POLISHED_CALCITE),
                        Map.entry(ModBlocks.POLISHED_CALCITE.get(), ModBlocks.CUT_CALCITE),
                        Map.entry(ModBlocks.CUT_CALCITE.get(), ModBlocks.CALCITE_BRICKS),
                        Map.entry(Blocks.QUARTZ_BLOCK, ModBlocks.CUT_QUARTZ_BLOCK),
                        Map.entry(Blocks.QUARTZ_BRICKS, ModBlocks.QUARTZ_TILES),
                        Map.entry(Blocks.TUFF_BRICKS, ModBlocks.TUFF_TILES),
                        Map.entry(ModBlocks.CALCITE_BRICKS.get(), ModBlocks.CALCITE_TILES),
                        Map.entry(ModBlocks.DRIPSTONE_BRICKS.get(), ModBlocks.DRIPSTONE_TILES),
                        Map.entry(ModBlocks.ANDESITE_BRICKS.get(), ModBlocks.ANDESITE_TILES),
                        Map.entry(ModBlocks.GRANITE_BRICKS.get(), ModBlocks.GRANITE_TILES),
                        Map.entry(ModBlocks.DIORITE_BRICKS.get(), ModBlocks.DIORITE_TILES),
                        Map.entry(Blocks.POLISHED_BLACKSTONE_BRICKS, ModBlocks.POLISHED_BLACKSTONE_TILES),
                        Map.entry(Blocks.PRISMARINE, ModBlocks.POLISHED_PRISMARINE),
                        Map.entry(ModBlocks.POLISHED_PRISMARINE.get(), ModBlocks.CUT_PRISMARINE),
                        Map.entry(Blocks.PRISMARINE_BRICKS, ModBlocks.PRISMARINE_TILES),
                        Map.entry(Blocks.STONE_BRICKS, ModBlocks.STONE_BRICK_TILES),
                        Map.entry(Blocks.POLISHED_BLACKSTONE, ModBlocks.CUT_POLISHED_BLACKSTONE),
                        Map.entry(ModBlocks.SANDSTONE_BRICKS.get(), ModBlocks.SANDSTONE_TILES),
                        Map.entry(ModBlocks.RED_SANDSTONE_BRICKS.get(), ModBlocks.RED_SANDSTONE_TILES)
                )
        );
        generatePillarRecipes(
                recipeOutput,
                Map.ofEntries(
                        Map.entry(Blocks.PRISMARINE_BRICKS, ModBlocks.PRISMARINE_BRICK_PILLAR),
                        Map.entry(Blocks.POLISHED_BLACKSTONE_BRICKS, ModBlocks.POLISHED_BLACKSTONE_BRICK_PILLAR),
                        Map.entry(ModBlocks.BASALT_BRICKS.get(), ModBlocks.BASALT_BRICK_PILLAR),
                        Map.entry(Blocks.TUFF_BRICKS, ModBlocks.TUFF_BRICK_PILLAR),
                        Map.entry(ModBlocks.ANDESITE_BRICKS.get(), ModBlocks.ANDESITE_BRICK_PILLAR),
                        Map.entry(ModBlocks.DIORITE_BRICKS.get(), ModBlocks.DIORITE_BRICK_PILLAR),
                        Map.entry(ModBlocks.GRANITE_BRICKS.get(), ModBlocks.GRANITE_BRICK_PILLAR),
                        Map.entry(ModBlocks.CALCITE_BRICKS.get(), ModBlocks.CALCITE_BRICK_PILLAR),
                        Map.entry(Blocks.STONE_BRICKS, ModBlocks.STONE_BRICK_PILLAR),
                        Map.entry(ModBlocks.SANDSTONE_BRICKS.get(), ModBlocks.SANDSTONE_BRICK_PILLAR),
                        Map.entry(ModBlocks.RED_SANDSTONE_BRICKS.get(), ModBlocks.RED_SANDSTONE_BRICK_PILLAR)
                )
        );

        generateChainRecipes(
                recipeOutput,
                Map.ofEntries(
                        Map.entry(
                                ModBlocks.GOLDEN_CHAIN.get(),
                                new ChainIngredients(Items.GOLD_NUGGET, Items.GOLD_INGOT)
                        ),
                        Map.entry(
                                ModBlocks.ROSE_GOLDEN_CHAIN.get(),
                                new ChainIngredients(ModItems.ROSE_GOLD_NUGGET.get(), ModItems.ROSE_GOLD_INGOT.get())
                        )
                )
        );

        generateBrushRecipes(
                recipeOutput,
                Map.ofEntries(
                        Map.entry(
                                ModItems.WOODEN_BRUSH.get(),
                                new BrushIngredients(Ingredient.of(ItemTags.PLANKS))
                        ),
                        Map.entry(
                                ModItems.STONE_BRUSH.get(),
                                new BrushIngredients(Ingredient.of(ItemTags.STONE_TOOL_MATERIALS))
                        ),
                        Map.entry(
                                ModItems.IRON_BRUSH.get(),
                                new BrushIngredients(Ingredient.of(Items.IRON_INGOT))
                        ),
                        Map.entry(
                                ModItems.GOLDEN_BRUSH.get(),
                                new BrushIngredients(Ingredient.of(Items.GOLD_INGOT))
                        ),
                        Map.entry(
                                ModItems.ROSE_GOLDEN_BRUSH.get(),
                                new BrushIngredients(Ingredient.of(ModItems.ROSE_GOLD_INGOT.get()))
                        ),
                        Map.entry(
                                ModItems.DIAMOND_BRUSH.get(),
                                new BrushIngredients(Ingredient.of(Items.DIAMOND))
                        ),
                        Map.entry(
                                ModItems.EMERALD_BRUSH.get(),
                                new BrushIngredients(Ingredient.of(Items.EMERALD))
                        )
                )
        );

        generateShapelessFlexible(
                recipeOutput,
                ResourceLocation.fromNamespaceAndPath("more_stuff", "copper_chains_from_existing_chains"),
                ModBlocks.ROSE_GOLDEN_CHAIN.get().asItem(),
                8,
                List.of(
                        Ingredient.of(ModBlocks.GOLDEN_CHAIN.get()),
                        Ingredient.of(ModBlocks.GOLDEN_CHAIN.get()),
                        Ingredient.of(ModBlocks.GOLDEN_CHAIN.get()),
                        Ingredient.of(ModBlocks.GOLDEN_CHAIN.get()),
                        Ingredient.of(ModBlocks.COPPER_CHAIN.get()),
                        Ingredient.of(ModBlocks.COPPER_CHAIN.get()),
                        Ingredient.of(ModBlocks.COPPER_CHAIN.get()),
                        Ingredient.of(ModBlocks.COPPER_CHAIN.get())
                ),
                "",       // optional group
                RecipeCategory.MISC
        );

        // Example: flexible shapeless recipe
        generateShapelessFlexible(
                recipeOutput,
                ResourceLocation.fromNamespaceAndPath("more_stuff", "gold_ingots_from_various_sources"),
                Items.GOLD_INGOT,
                9,
                List.of(
                        Ingredient.of(ModBlocks.CUT_GOLD_BLOCK.get(), ModBlocks.CUT_GOLDEN_BRICKS.get())
                ),
                "golden_ingots",       // optional group
                RecipeCategory.MISC
        );

        generateAllWaxedCopperRecipes(recipeOutput);
        // Mossy recipes
        generateMossyRecipes(recipeOutput);
        generateCobbledTerracottaSmelting(recipeOutput);
        generateCobbledSandstoneSmelting(recipeOutput);
        generateAllLanternRecipes(recipeOutput);
        generateColoredSlimeBallRecipes(recipeOutput);
    }

    private void generateAllWaxedCopperRecipes(RecipeOutput recipeOutput) {

        Map<Block, Block> waxingMap = Map.of(
                ModBlocks.COPPER_CHAIN.get(), ModBlocks.WAXED_COPPER_CHAIN.get(),
                ModBlocks.EXPOSED_COPPER_CHAIN.get(), ModBlocks.WAXED_EXPOSED_COPPER_CHAIN.get(),
                ModBlocks.WEATHERED_COPPER_CHAIN.get(), ModBlocks.WAXED_WEATHERED_COPPER_CHAIN.get(),
                ModBlocks.OXIDIZED_COPPER_CHAIN.get(), ModBlocks.WAXED_OXIDIZED_COPPER_CHAIN.get()
        );

        waxingMap.forEach((unwaxed, waxed) -> {

            generateShapelessFlexible(
                    recipeOutput,
                    ResourceLocation.fromNamespaceAndPath(
                            "more_stuff",
                            BuiltInRegistries.BLOCK.getKey(waxed).getPath() + "_waxing"
                    ),
                    waxed,                          // Output block
                    1,
                    List.of(
                            Ingredient.of(unwaxed), // Ingredient 1: unwaxed chain state
                            Ingredient.of(Items.HONEYCOMB) // Ingredient 2: honeycomb
                    ),
                    "copper_chains",
                    RecipeCategory.MISC
            );
        });
    }

    private void generateTerracottaBrickRecipes(RecipeOutput recipeOutput) {
        ModBlocks.TERRACOTTA_BRICKS.forEach((color, brickDeferred) -> {
            Block inputBlock = switch (color) {
                case "white" -> Blocks.WHITE_TERRACOTTA;
                case "orange" -> Blocks.ORANGE_TERRACOTTA;
                case "magenta" -> Blocks.MAGENTA_TERRACOTTA;
                case "light_blue" -> Blocks.LIGHT_BLUE_TERRACOTTA;
                case "yellow" -> Blocks.YELLOW_TERRACOTTA;
                case "lime" -> Blocks.LIME_TERRACOTTA;
                case "pink" -> Blocks.PINK_TERRACOTTA;
                case "gray" -> Blocks.GRAY_TERRACOTTA;
                case "light_gray" -> Blocks.LIGHT_GRAY_TERRACOTTA;
                case "cyan" -> Blocks.CYAN_TERRACOTTA;
                case "purple" -> Blocks.PURPLE_TERRACOTTA;
                case "blue" -> Blocks.BLUE_TERRACOTTA;
                case "brown" -> Blocks.BROWN_TERRACOTTA;
                case "green" -> Blocks.GREEN_TERRACOTTA;
                case "red" -> Blocks.RED_TERRACOTTA;
                case "black" -> Blocks.BLACK_TERRACOTTA;
                default -> Blocks.TERRACOTTA;
            };
            Block outputBlock = brickDeferred.get();

            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputBlock, 4)
                    .define('X', inputBlock)
                    .pattern("XX")
                    .pattern("XX")
                    .unlockedBy(getHasName(inputBlock), has(inputBlock))
                    .save(recipeOutput);
        });
    }

    private void generateBrickRecipes(RecipeOutput recipeOutput, Map<Block, DeferredBlock<Block>> brickMap) {
        brickMap.forEach((inputBlock, brickDeferred) -> {
            Block outputBlock = brickDeferred.get();

            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputBlock, 4)
                    .define('X', inputBlock)
                    .pattern("XX")
                    .pattern("XX")
                    .unlockedBy(getHasName(inputBlock), has(inputBlock))
                    .save(recipeOutput);
        });
    }

    private void generatePillarRecipes(RecipeOutput recipeOutput, Map<Block, DeferredBlock<RotatedPillarBlock>> pillarMap) {
        pillarMap.forEach((inputBlock, pillarDeferred) -> {
            Block outputBlock = pillarDeferred.get();

            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputBlock, 2)
                    .define('X', inputBlock)
                    .pattern("X")
                    .pattern("X")
                    .unlockedBy(getHasName(inputBlock), has(inputBlock))
                    .save(recipeOutput);
        });
    }

    public record ChainIngredients(Item nugget, Item ingot) {}

    private void generateChainRecipes(RecipeOutput recipeOutput,
                                      Map<Block, ChainIngredients> chainMap) {

        chainMap.forEach((outputBlock, ingredients) -> {
            Item nugget = ingredients.nugget();
            Item ingot  = ingredients.ingot();

            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, outputBlock, 1)
                    .define('N', nugget)
                    .define('I', ingot)
                    .pattern("N")
                    .pattern("I")
                    .pattern("N")
                    .unlockedBy(getHasName(nugget), has(nugget))
                    .unlockedBy(getHasName(ingot), has(ingot))
                    .save(recipeOutput);
        });
    }

    public record BrushIngredients(Ingredient material) {}

    private void generateBrushRecipes(
            RecipeOutput recipeOutput,
            Map<Item, BrushIngredients> chainMap
    ) {
        chainMap.forEach((outputItem, ingredients) -> {
            Ingredient material = ingredients.material();

            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, outputItem, 1)
                    .define('F', Items.FEATHER)
                    .define('M', material) // ✅ works with items AND tags
                    .define('S', Items.STICK)
                    .pattern("F")
                    .pattern("M")
                    .pattern("S")
                    .unlockedBy("has_material", has(material)) // ✅ tag-aware unlock
                    .save(recipeOutput);
        });
    }

    private Criterion<?> has(Ingredient material) {
        Ingredient.Value[] values = material.getValues();

        // Single item ingredient
        if (values.length == 1 && values[0] instanceof Ingredient.ItemValue itemValue) {
            Item item = itemValue.item().getItem(); // ✅ ItemStack → Item
            return has(item);
        }

        // Single tag ingredient
        if (values.length == 1 && values[0] instanceof Ingredient.TagValue tagValue) {
            return has(tagValue.tag());
        }

        throw new IllegalStateException(
                "Unsupported ingredient for unlock criteria: " + material
        );
    }

    private void generateLanternRecipes(RecipeOutput recipeOutput,
                                      Map<Block, ChainIngredients> chainMap) {

        chainMap.forEach((outputBlock, ingredients) -> {
            Item nugget = ingredients.nugget();
            Item ingot  = ingredients.ingot();

            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, outputBlock, 1)
                    .define('N', nugget)
                    .define('T', ingot)
                    .pattern("NNN")
                    .pattern("NTN")
                    .pattern("NNN")
                    .unlockedBy(getHasName(nugget), has(nugget))
                    .unlockedBy(getHasName(ingot), has(ingot))
                    .save(recipeOutput);
        });
    }

    private void generateShapelessFlexible(
            RecipeOutput output,
            ResourceLocation id,
            ItemLike result,
            int count,
            List<Ingredient> ingredients,
            @Nullable String group,
            RecipeCategory category
    ) {
        if (ingredients.isEmpty() || ingredients.size() > 9) {
            throw new IllegalArgumentException("Shapeless recipe must have 1–9 ingredients");
        }

        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(category, result, count);

        if (group != null) {
            builder.group(group);
        }

        ingredients.forEach(builder::requires);

        builder.unlockedBy("has_" + result.asItem(), has(result.asItem()));
        builder.save(output, id);
    }

    private void generateMossyRecipes(RecipeOutput recipeOutput) {
        record MossyPair(Block base, Block mossy) {}
        List<MossyPair> pairs = List.of(
                new MossyPair(Blocks.DEEPSLATE, ModBlocks.MOSSY_DEEPSLATE.get()),
                new MossyPair(Blocks.COBBLED_DEEPSLATE, ModBlocks.MOSSY_COBBLED_DEEPSLATE.get()),
                new MossyPair(Blocks.DEEPSLATE_BRICKS, ModBlocks.MOSSY_DEEPSLATE_BRICKS.get()),
                new MossyPair(Blocks.CHISELED_DEEPSLATE, ModBlocks.MOSSY_CHISELED_DEEPSLATE.get()),
                new MossyPair(Blocks.DEEPSLATE_TILES, ModBlocks.MOSSY_DEEPSLATE_TILES.get()),
                new MossyPair(Blocks.STONE, ModBlocks.MOSSY_STONE.get()),
                new MossyPair(Blocks.CHISELED_STONE_BRICKS, ModBlocks.MOSSY_CHISELED_STONE_BRICKS.get())
        );

        Ingredient mossOrVines = Ingredient.of(Items.MOSS_BLOCK, Items.VINE);

        for (MossyPair p : pairs) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, p.mossy())
                    .requires(p.base())
                    .requires(mossOrVines)
                    .unlockedBy(getHasName(p.base()), has(p.base()))
                    .save(recipeOutput);
        }
    }

    private void generateCobbledTerracottaSmelting(RecipeOutput output) {
        ModBlocks.TERRACOTTA_COBBLED.forEach((color, cobbledDeferred) -> {
            Block cobbled = cobbledDeferred.get();
            Block result = switch (color) {
                case "white" -> Blocks.WHITE_TERRACOTTA;
                case "orange" -> Blocks.ORANGE_TERRACOTTA;
                case "magenta" -> Blocks.MAGENTA_TERRACOTTA;
                case "light_blue" -> Blocks.LIGHT_BLUE_TERRACOTTA;
                case "yellow" -> Blocks.YELLOW_TERRACOTTA;
                case "lime" -> Blocks.LIME_TERRACOTTA;
                case "pink" -> Blocks.PINK_TERRACOTTA;
                case "gray" -> Blocks.GRAY_TERRACOTTA;
                case "light_gray" -> Blocks.LIGHT_GRAY_TERRACOTTA;
                case "cyan" -> Blocks.CYAN_TERRACOTTA;
                case "purple" -> Blocks.PURPLE_TERRACOTTA;
                case "blue" -> Blocks.BLUE_TERRACOTTA;
                case "brown" -> Blocks.BROWN_TERRACOTTA;
                case "green" -> Blocks.GREEN_TERRACOTTA;
                case "red" -> Blocks.RED_TERRACOTTA;
                case "black" -> Blocks.BLACK_TERRACOTTA;
                default -> Blocks.TERRACOTTA;
            };

            SimpleCookingRecipeBuilder.generic(
                            Ingredient.of(cobbled),
                            RecipeCategory.BUILDING_BLOCKS,
                            result,
                            0.1f,
                            200,
                            RecipeSerializer.SMELTING_RECIPE,
                            SmeltingRecipe::new
                    )
                    .unlockedBy(getHasName(cobbled), has(cobbled))
                    .save(output, "more_stuff:" + getItemName(result) + "_from_smelting_" + getItemName(cobbled));
        });
    }

    private void generateCobbledSandstoneSmelting(RecipeOutput output) {
        Map<String, Supplier<? extends Block>> COBBLED_SANDSTONES = Map.of(
                "sandstone", ModBlocks.COBBLED_SANDSTONE,
                "red_sandstone", ModBlocks.COBBLED_RED_SANDSTONE
        );

        COBBLED_SANDSTONES.forEach((type, cobbledDeferred) -> {
            Block cobbled = cobbledDeferred.get();
            Block result = switch (type) {
                case "sandstone" -> Blocks.SANDSTONE;
                case "red_sandstone" -> Blocks.RED_SANDSTONE;
                default -> Blocks.SANDSTONE;
            };

            SimpleCookingRecipeBuilder.generic(
                            Ingredient.of(cobbled),
                            RecipeCategory.BUILDING_BLOCKS,
                            result,
                            0.1f,
                            200,
                            RecipeSerializer.SMELTING_RECIPE,
                            SmeltingRecipe::new
                    )
                    .unlockedBy(getHasName(cobbled), has(cobbled))
                    .save(output,
                            "more_stuff:" + getItemName(result) + "_from_smelting_" + getItemName(cobbled));
        });
    }

    private Item getNuggetForColor(String color) {
        return switch (color) {
            case "rose_gold" -> ModItems.ROSE_GOLD_NUGGET.get();
            default ->  Items.GOLD_NUGGET;
        };
    }

    private Item getTorchForLanternType(boolean isSoul) {
        return isSoul ? Items.SOUL_TORCH : Items.TORCH;
    }

    private void generateAllLanternRecipes(RecipeOutput output) {

        Map<Block, ChainIngredients> lanternMap = new HashMap<>();

        // ---------------------------------------------------------
        // NORMAL LANTERNS
        // ---------------------------------------------------------
        ModBlocks.LANTERNS.forEach((color, deferredBlock) -> {
            Block block = deferredBlock.get();

            Item nugget = getNuggetForColor(color);
            Item torch  = getTorchForLanternType(false); // normal torch

            lanternMap.put(block, new ChainIngredients(nugget, torch));
        });

        // ---------------------------------------------------------
        // SOUL LANTERNS
        // ---------------------------------------------------------
        ModBlocks.SOUL_LANTERNS.forEach((color, deferredBlock) -> {
            Block block = deferredBlock.get();

            Item nugget = getNuggetForColor(color);
            Item torch  = getTorchForLanternType(true); // soul torch

            lanternMap.put(block, new ChainIngredients(nugget, torch));
        });

        // ---------------------------------------------------------
        // BUILD RECIPES WITH YOUR EXISTING SYSTEM
        // ---------------------------------------------------------
        generateLanternRecipes(output, lanternMap);
    }

    private static Item getDyeForColor(String color) {
        return switch (color) {
            case "white"      -> Items.WHITE_DYE;
            case "orange"     -> Items.ORANGE_DYE;
            case "magenta"    -> Items.MAGENTA_DYE;
            case "light_blue" -> Items.LIGHT_BLUE_DYE;
            case "yellow"     -> Items.YELLOW_DYE;
            case "lime"       -> Items.LIME_DYE;
            case "pink"       -> Items.PINK_DYE;
            case "gray"       -> Items.GRAY_DYE;
            case "light_gray" -> Items.LIGHT_GRAY_DYE;
            case "cyan"       -> Items.CYAN_DYE;
            case "purple"     -> Items.PURPLE_DYE;
            case "blue"       -> Items.BLUE_DYE;
            case "brown"      -> Items.BROWN_DYE;
            case "green"      -> Items.GREEN_DYE;
            case "red"        -> Items.RED_DYE;
            case "black"      -> Items.BLACK_DYE;
            default -> throw new IllegalArgumentException("No dye for color: " + color);
        };
    }

    private void generateColoredSlimeBallRecipes(RecipeOutput output) {

        for (String color : ModItems.SLIME_COLORS) {

            // Skip special cases
            if (color.equals("clear") || color.equals("tinted")) {
                continue;
            }

            Item dye = getDyeForColor(color);
            ItemLike result = ModItems.SLIME_BALLS.get(color).get();

            generateShapelessFlexible(
                    output,
                    ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, color + "_slime_ball_from_dyeing"),
                    result,
                    1,
                    List.of(
                            Ingredient.of(Items.SLIME_BALL),
                            Ingredient.of(dye)
                    ),
                    "colored_slime_balls",
                    RecipeCategory.MISC
            );
        }
    }
}
