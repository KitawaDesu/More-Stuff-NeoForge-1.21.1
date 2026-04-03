package net.kitawa.more_stuff.util.datagen;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Map;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MoreStuff.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        // Terracotta families
        registerTerracottaFamily(ModBlocks.TERRACOTTA_COBBLED);
        registerTerracottaFamily(ModBlocks.TERRACOTTA_BRICKS);

        // Special sandstone blocks
        registerSandstone(ModBlocks.COBBLED_SANDSTONE, false);
        registerSandstone(ModBlocks.COBBLED_RED_SANDSTONE, true);
        registerGenericCubeAll(ModBlocks.SANDSTONE_BRICKS);
        registerGenericCubeAll(ModBlocks.RED_SANDSTONE_BRICKS);
        registerGenericCubeAll(ModBlocks.BASALT_BRICKS);
        registerGenericCubeAll(ModBlocks.ANDESITE_BRICKS);
        registerGenericCubeAll(ModBlocks.GRANITE_BRICKS);
        registerGenericCubeAll(ModBlocks.DIORITE_BRICKS);
        registerGenericCubeAll(ModBlocks.CUT_GOLD_BLOCK);
        registerGenericCubeAll(ModBlocks.CUT_GOLDEN_BRICKS);
        registerGenericCubeAll(ModBlocks.POLISHED_DRIPSTONE);
        registerGenericCubeAll(ModBlocks.POLISHED_CALCITE);
        registerGenericCubeAll(ModBlocks.DRIPSTONE_BRICKS);
        registerGenericCubeAll(ModBlocks.CALCITE_BRICKS);
        registerGenericCubeAll(ModBlocks.BASALT_TILES);
        registerGenericCubeAll(ModBlocks.TUFF_TILES);
        registerGenericCubeAll(ModBlocks.QUARTZ_TILES);
        registerGenericCubeAll(ModBlocks.CALCITE_TILES);
        registerGenericCubeAll(ModBlocks.DRIPSTONE_TILES);
        registerGenericCubeAll(ModBlocks.ANDESITE_TILES);
        registerGenericCubeAll(ModBlocks.DIORITE_TILES);
        registerGenericCubeAll(ModBlocks.GRANITE_TILES);
        registerGenericCubeAll(ModBlocks.POLISHED_BLACKSTONE_TILES);
        registerGenericCubeAll(ModBlocks.POLISHED_PRISMARINE);
        registerGenericCubeAll(ModBlocks.PRISMARINE_TILES);
        registerGenericCubeAll(ModBlocks.STONE_BRICK_TILES);
        registerGenericCubeAll(ModBlocks.SANDSTONE_TILES);
        registerGenericCubeAll(ModBlocks.RED_SANDSTONE_TILES);

        // Chiseled basalt
        registerGenericTopSideBottom(ModBlocks.CHISELED_BASALT);
        registerGenericTopSideBottom(ModBlocks.CUT_SMOOTH_STONE);
        registerGenericTopSideBottom(ModBlocks.CUT_BASALT);
        registerGenericTopSideBottom(ModBlocks.CUT_QUARTZ_BLOCK);

        registerGenericPillar(ModBlocks.PRISMARINE_BRICK_PILLAR);
        registerGenericPillar(ModBlocks.POLISHED_BLACKSTONE_BRICK_PILLAR);
        registerGenericPillar(ModBlocks.BASALT_BRICK_PILLAR);
        registerGenericPillar(ModBlocks.TUFF_BRICK_PILLAR);
        registerGenericPillar(ModBlocks.ANDESITE_BRICK_PILLAR);
        registerGenericPillar(ModBlocks.GRANITE_BRICK_PILLAR);
        registerGenericPillar(ModBlocks.DIORITE_BRICK_PILLAR);
        registerGenericPillar(ModBlocks.CALCITE_BRICK_PILLAR);
        registerGenericPillar(ModBlocks.DRIPSTONE_BRICK_PILLAR);
        registerGenericPillar(ModBlocks.STONE_BRICK_PILLAR);
        registerGenericPillar(ModBlocks.SANDSTONE_BRICK_PILLAR);
        registerGenericPillar(ModBlocks.RED_SANDSTONE_BRICK_PILLAR);

        // Cut stones
        registerCutStone(ModBlocks.CUT_GRANITE, "granite");
        registerCutStone(ModBlocks.CUT_ANDESITE, "andesite");
        registerCutStone(ModBlocks.CUT_DIORITE, "diorite");
        registerCutStone(ModBlocks.CUT_TUFF, "tuff");
        registerCutStone(ModBlocks.CUT_DEEPSLATE, "deepslate");
        registerModdedCutStone(ModBlocks.CUT_CALCITE, "calcite");
        registerModdedCutStone(ModBlocks.CUT_DRIPSTONE, "dripstone");
        registerModdedCutStone(ModBlocks.CUT_PRISMARINE, "prismarine");
        registerCutStone(ModBlocks.CUT_POLISHED_BLACKSTONE, "blackstone");

        registerChainBlock(ModBlocks.COPPER_CHAIN);
        registerChainBlock(ModBlocks.EXPOSED_COPPER_CHAIN);
        registerChainBlock(ModBlocks.WEATHERED_COPPER_CHAIN);
        registerChainBlock(ModBlocks.OXIDIZED_COPPER_CHAIN);

// Waxed chains — use same texture as unwaxed
        registerChainBlock(ModBlocks.WAXED_COPPER_CHAIN);
        registerChainBlock(ModBlocks.WAXED_EXPOSED_COPPER_CHAIN);
        registerChainBlock(ModBlocks.WAXED_WEATHERED_COPPER_CHAIN);
        registerChainBlock(ModBlocks.WAXED_OXIDIZED_COPPER_CHAIN);

        registerChainBlock(ModBlocks.GOLDEN_CHAIN);
        registerChainBlock(ModBlocks.ROSE_GOLDEN_CHAIN);
        registerGenericSpawner(ModBlocks.GOLDEN_SPAWNER);

        registerLanternFamily(ModBlocks.LANTERNS);
        registerLanternFamily(ModBlocks.SOUL_LANTERNS);
    }

    // =============================
    // TERRACOTTA FAMILY
    // =============================
    private void registerTerracottaFamily(Map<String, DeferredBlock<Block>> map) {
        map.forEach((color, block) -> registerSimple(block));
    }

    private void registerSimple(DeferredBlock<? extends Block> block) {
        String id = block.getId().getPath();
        simpleBlockWithItem(block.get(), uncheckedCubeAll(id));
    }

    private void registerLanternFamily(Map<String, DeferredBlock<LanternBlock>> map) {
        map.forEach((color, block) -> registerLantern(block));
    }

    // =============================
    // SANDSTONE / GENERIC BLOCKS
    // =============================
    private void registerSandstone(DeferredBlock<? extends Block> block, boolean isRed) {
        String id = block.getId().getPath();
        String texture = isRed ? "minecraft:block/red_sandstone_bottom" : "minecraft:block/sandstone_bottom";

        ModelFile model = models().getBuilder(id)
                .parent(new ModelFile.UncheckedModelFile("minecraft:block/cube_all"))
                .texture("all", texture)
                .texture("particle", texture);

        simpleBlockWithItem(block.get(), model);
    }

    private void registerGenericCubeAll(DeferredBlock<? extends Block> block) {
        String id = block.getId().getPath();
        String textureName = id;

        ModelFile model = models().getBuilder(id)
                .parent(new ModelFile.UncheckedModelFile("minecraft:block/cube_all"))
                .texture("all", MoreStuff.MOD_ID + ":block/" + textureName)
                .texture("particle", MoreStuff.MOD_ID + ":block/" + textureName);

        simpleBlockWithItem(block.get(), model);
    }

    private void registerGenericSpawner(DeferredBlock<? extends Block> block) {
        String id = block.getId().getPath();
        String textureName = id;

        ModelFile model = models().getBuilder(id)
                .parent(new ModelFile.UncheckedModelFile("minecraft:block/cube_all_inner_faces"))
                .texture("all", MoreStuff.MOD_ID + ":block/" + textureName);

        simpleBlockWithItem(block.get(), model);
    }

    // =============================
    // CHISELED BASALT
    // top/bottom = _bottom
    // sides = registry name
    // =============================
    private void registerGenericTopSideBottom(DeferredBlock<? extends Block> block) {
        String id = block.getId().getPath();

        // side = registry name, end = registry_name_bottom
        ResourceLocation side = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "block/" + id);
        ResourceLocation end = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "block/" + id + "_top");

        models().cubeColumn(id, side, end);
        simpleBlockWithItem(block.get(), models().getExistingFile(ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "block/" + id)));
    }

    private void registerCutStone(DeferredBlock<? extends Block> block, String baseName) {
        String id = block.getId().getPath();

        // side = registry name, end = polished vanilla block
        ResourceLocation side = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "block/" + id);
        ResourceLocation end = ResourceLocation.fromNamespaceAndPath("minecraft", "block/polished_" + baseName);

        models().cubeColumn(id, side, end);
        simpleBlockWithItem(block.get(), models().getExistingFile(ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "block/" + id)));
    }

    private void registerModdedCutStone(DeferredBlock<? extends Block> block, String baseName) {
        String id = block.getId().getPath();

        // side = registry name, end = polished vanilla block
        ResourceLocation side = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "block/" + id);
        ResourceLocation end = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "block/polished_" + baseName);

        models().cubeColumn(id, side, end);
        simpleBlockWithItem(block.get(), models().getExistingFile(ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "block/" + id)));
    }

    private void registerGenericPillar(DeferredBlock<? extends Block> block) {
        String id = block.getId().getPath();

        ResourceLocation side = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "block/" + id);
        ResourceLocation top  = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "block/" + id + "_top");

        // vanilla-matching models
        ModelFile vertical   = models().cubeColumn(id, side, top);
        ModelFile horizontal = models().cubeColumnHorizontal(id + "_horizontal", side, top);

        // ---- BLOCKSTATE (matches your target JSON) ----
        getVariantBuilder(block.get())
                // axis = x
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState()
                .modelFile(horizontal)
                .rotationX(90)
                .rotationY(90)
                .addModel()

                // axis = y
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState()
                .modelFile(vertical)
                .addModel()

                // axis = z
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState()
                .modelFile(horizontal)
                .rotationX(90)
                .addModel();

        // Item uses the vertical pillar
        simpleBlockItem(block.get(), vertical);
    }

    private void registerLantern(DeferredBlock<? extends Block> block) {
        String id = block.getId().getPath();

        // --- models/block/<id>.json ---
        ModelFile lanternModel = models()
                .withExistingParent(id, mcLoc("block/template_lantern"))
                .texture("lantern", MoreStuff.MOD_ID + ":block/" + id);

        // --- models/block/<id>_hanging.json ---
        ModelFile hangingModel = models()
                .withExistingParent(id + "_hanging", mcLoc("block/template_hanging_lantern"))
                .texture("lantern", MoreStuff.MOD_ID + ":block/" + id);

        // --- blockstates/<id>.json ---
        getVariantBuilder(block.get())
                .partialState().with(LanternBlock.HANGING, false)
                .modelForState().modelFile(lanternModel).addModel()

                .partialState().with(LanternBlock.HANGING, true)
                .modelForState().modelFile(hangingModel).addModel();
    }

    private void registerChainBlock(DeferredBlock<? extends Block> block) {
        String id = block.getId().getPath();

        // Remove "waxed_" so waxed chains use the unwaxed texture
        String textureName = id.replace("waxed_", "");

        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                MoreStuff.MOD_ID,
                "block/" + textureName
        );

        // ---- BLOCK MODEL ----
        ModelFile chainModel = models().withExistingParent(id, mcLoc("block/block"))
                .texture("particle", texture.toString())
                .texture("all", texture.toString())

                // ===== Element 1 =====
                .element()
                .from(6.5f, 0f, 8f)
                .to(9.5f, 16f, 8f)
                .rotation().origin(8f, 8f, 8f).axis(Direction.Axis.Y).angle(45).end()
                .shade(false)
                .face(Direction.NORTH).uvs(3, 0, 0, 16).texture("#all").end()
                .face(Direction.SOUTH).uvs(0, 0, 3, 16).texture("#all").end()
                .end()

                // ===== Element 2 =====
                .element()
                .from(8f, 0f, 6.5f)
                .to(8f, 16f, 9.5f)
                .rotation().origin(8f, 8f, 8f).axis(Direction.Axis.Y).angle(45).end()
                .shade(false)
                .face(Direction.WEST).uvs(6, 0, 3, 16).texture("#all").end()
                .face(Direction.EAST).uvs(3, 0, 6, 16).texture("#all").end()
                .end();

        // ---- BLOCKSTATES ----
        getVariantBuilder(block.get())

                // axis = x
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState()
                .modelFile(chainModel)
                .rotationX(90).rotationY(90)
                .addModel()

                // axis = y
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState()
                .modelFile(chainModel)
                .addModel()

                // axis = z
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState()
                .modelFile(chainModel)
                .rotationX(90)
                .addModel();
    }

    // =============================
    // MODEL HELPERS
    // =============================
    private ModelFile uncheckedCubeAll(String id) {
        return models().getBuilder(id)
                .parent(new ModelFile.UncheckedModelFile("minecraft:block/cube_all"))
                .texture("all", MoreStuff.MOD_ID + ":block/" + id)
                .texture("particle", MoreStuff.MOD_ID + ":block/" + id);
    }
}
