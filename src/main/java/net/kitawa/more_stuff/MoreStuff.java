package net.kitawa.more_stuff;

import com.mojang.logging.LogUtils;
import net.kitawa.more_stuff.compat.create.blocks.CreateCompatBlocks;
import net.kitawa.more_stuff.compat.create.items.util.CreateCompatTeiredElytraLayer;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.compat.create.items.util.CreateCompatVexElytraLayer;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.items.life_tokens.LifeTokenItems;
import net.kitawa.more_stuff.items.util.TeiredElytraLayer;
import net.kitawa.more_stuff.items.util.VexElytraLayer;
import net.kitawa.more_stuff.util.configs.LifeTokensConfig;
import net.kitawa.more_stuff.util.configs.LifeBitDropsConfig;
import net.kitawa.more_stuff.util.configs.MoreStuffGeneralConfig;
import net.kitawa.more_stuff.util.events.MoreStuffClientEvents;
import net.kitawa.more_stuff.util.loot.ModLootModifiers;
import net.kitawa.more_stuff.util.mob_armor.ModLayerDefinitions;
import net.kitawa.more_stuff.util.mob_armor.ModModelLayers;
import net.kitawa.more_stuff.util.mob_armor.layers.HoglinArmorLayer;
import net.kitawa.more_stuff.util.mob_armor.layers.ZoglinArmorLayer;
import net.kitawa.more_stuff.worldgen.biome.ModTerrablenderAPI;
import net.kitawa.more_stuff.worldgen.biome.surface.ModSurfaceRules;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.slf4j.Logger;
import terrablender.api.SurfaceRuleManager;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MoreStuff.MOD_ID)
public class MoreStuff {

    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "more_stuff";
    private static final Logger LOGGER = LogUtils.getLogger();
    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public MoreStuff(IEventBus modEventBus, ModContainer modContainer) {

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.register(MoreStuffClientEvents.class);
        }
        // Register lifecycle listeners
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        ModLootModifiers.register(modEventBus);

        LifeTokensConfig.setup();
        LifeBitDropsConfig.setup();
        MoreStuffGeneralConfig.setup();

        // Register content
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);

        if (ModList.get().isLoaded("create")) {
            CreateCompatItems.register(modEventBus);
            CreateCompatBlocks.register(modEventBus);
        }

        if (LifeTokensConfig.CONFIG.addLifeTokens()) {
            LifeTokenItems.register(modEventBus);
        }
        // Register this class to listen to global events
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Move TerraBlender-related registration here to ensure it's initialized
            ModTerrablenderAPI.registerBiomes();
            // Surface rules
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MoreStuff.MOD_ID, ModSurfaceRules.overworld());
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.NETHER, MoreStuff.MOD_ID, ModSurfaceRules.nether());

        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.insertAfter(Items.KELP.getDefaultInstance(), ModItems.AQUANDA_KELP.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.FLOWERING_AZALEA.getDefaultInstance(), ModBlocks.AQUANDA.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AQUANDA.get().asItem().getDefaultInstance(), ModBlocks.GLOWING_AQUANDA.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.MOSS_CARPET.getDefaultInstance(), ModBlocks.AQUANDA_MOSS_BLOCK.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AQUANDA_MOSS_BLOCK.get().asItem().getDefaultInstance(), ModBlocks.AQUANDA_MOSS_CARPET.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.GLOW_BERRIES.getDefaultInstance(), ModItems.AQUANDA_BERRIES.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.FLOWERING_AZALEA_LEAVES.getDefaultInstance(), ModBlocks.AQUANDA_GEL.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AQUANDA_GEL.get().asItem().getDefaultInstance(), ModBlocks.GLOWING_AQUANDA_GEL.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.WARPED_NYLIUM.getDefaultInstance(), ModBlocks.PYROLIZED_NETHERRACK.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_NETHERRACK.get().asItem().getDefaultInstance(), ModBlocks.BLAZING_NYLIUM.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.BAMBOO.getDefaultInstance(), ModItems.PYROLIZED_BAMBOO.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.PYROLIZED_BAMBOO.get().getDefaultInstance(), ModItems.IRON_BAMBOO.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.IRON_BAMBOO.get().getDefaultInstance(), ModItems.COPPER_BAMBOO.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_BAMBOO.get().getDefaultInstance(), ModItems.GOLDEN_BAMBOO.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.GOLDEN_BAMBOO.get().getDefaultInstance(), ModItems.ANCIENT_BAMBOO.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.TWISTING_VINES.getDefaultInstance(), ModItems.BLAZING_VINES.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHER_SPROUTS.getDefaultInstance(), ModBlocks.BLAZING_ROOTS.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.BLAZING_ROOTS.get().asItem().getDefaultInstance(), ModBlocks.BLAZING_SPROUTS.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.DEEPSLATE_DIAMOND_ORE.getDefaultInstance(), ModBlocks.NETHER_COPPER_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.NETHER_COPPER_ORE.get().asItem().getDefaultInstance(), ModBlocks.PYROLIZED_COPPER_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_COPPER_ORE.get().asItem().getDefaultInstance(), ModBlocks.NETHER_IRON_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.NETHER_IRON_ORE.get().asItem().getDefaultInstance(), ModBlocks.PYROLIZED_IRON_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHER_GOLD_ORE.getDefaultInstance(), ModBlocks.PYROLIZED_GOLD_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHER_QUARTZ_ORE.getDefaultInstance(), ModBlocks.PYROLIZED_QUARTZ_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_QUARTZ_ORE.get().asItem().getDefaultInstance(), ModBlocks.NETHER_EXPERIENCE_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.NETHER_EXPERIENCE_ORE.get().asItem().getDefaultInstance(), ModBlocks.PYROLIZED_EXPERIENCE_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.WARPED_FUNGUS.getDefaultInstance(), ModBlocks.EBONY_FUNGUS.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.MAGMA_BLOCK.getDefaultInstance(), ModBlocks.BLAZING_MAGMA_BLOCK.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.SUGAR_CANE.getDefaultInstance(), ModBlocks.BLAZING_REEDS.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.BLAZING_NYLIUM.get().asItem().getDefaultInstance(), ModBlocks.FROZEN_NETHERRACK.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.FROZEN_NETHERRACK.get().asItem().getDefaultInstance(), ModBlocks.PACKED_FROZEN_NETHERRACK.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PACKED_FROZEN_NETHERRACK.get().asItem().getDefaultInstance(), ModBlocks.BLUE_FROZEN_NETHERRACK.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.BLAZING_MAGMA_BLOCK.get().asItem().getDefaultInstance(), ModBlocks.FREEZING_MAGMA_BLOCK.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.FREEZING_MAGMA_BLOCK.get().asItem().getDefaultInstance(), ModBlocks.PACKED_FREEZING_MAGMA_BLOCK.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PACKED_FREEZING_MAGMA_BLOCK.get().asItem().getDefaultInstance(), ModBlocks.BLUE_FREEZING_MAGMA_BLOCK.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.SOUL_SOIL.getDefaultInstance(), ModBlocks.POWDER_SOUL_SNOW.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.POWDER_SOUL_SNOW.get().asItem().getDefaultInstance(), ModBlocks.SOUL_SNOW_BLOCK.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_COPPER_ORE.get().asItem().getDefaultInstance(), ModBlocks.FROZEN_COPPER_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_IRON_ORE.get().asItem().getDefaultInstance(), ModBlocks.FROZEN_IRON_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_GOLD_ORE.get().asItem().getDefaultInstance(), ModBlocks.FROZEN_GOLD_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_QUARTZ_ORE.get().asItem().getDefaultInstance(), ModBlocks.FROZEN_QUARTZ_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_EXPERIENCE_ORE.get().asItem().getDefaultInstance(), ModBlocks.FROZEN_EXPERIENCE_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.WARPED_NYLIUM.getDefaultInstance(), ModBlocks.IRON_NYLIUM.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.IRON_NYLIUM.get().asItem().getDefaultInstance(), ModBlocks.COPPER_NYLIUM.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.COPPER_NYLIUM.get().asItem().getDefaultInstance(), ModBlocks.GOLDEN_NYLIUM.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.GOLDEN_NYLIUM.get().asItem().getDefaultInstance(), ModBlocks.ANCIENT_NYLIUM.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHER_WART.asItem().getDefaultInstance(), ModItems.WARPED_WART.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            if (ModList.get().isLoaded("create")) {
                event.insertAfter(ModBlocks.FROZEN_IRON_ORE.get().asItem().getDefaultInstance(), CreateCompatBlocks.NETHER_ZINC_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatBlocks.NETHER_ZINC_ORE.get().asItem().getDefaultInstance(), CreateCompatBlocks.PYROLIZED_ZINC_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatBlocks.PYROLIZED_ZINC_ORE.get().asItem().getDefaultInstance(), CreateCompatBlocks.FROZEN_ZINC_ORE.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.insertAfter(Items.IRON_NUGGET.getDefaultInstance(), ModItems.COPPER_NUGGET.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.GOLD_NUGGET.getDefaultInstance(), ModItems.ANCIENT_CHUNK.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.COPPER_INGOT.getDefaultInstance(), ModItems.ROSE_GOLD_INGOT.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHERITE_INGOT.getDefaultInstance(), ModItems.ROSARITE_INGOT.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_NUGGET.get().getDefaultInstance(), ModItems.ROSE_GOLD_NUGGET.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE.getDefaultInstance(), ModItems.ROSARITE_UPGRADE_SMITHING_TEMPLATE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSARITE_UPGRADE_SMITHING_TEMPLATE.get().getDefaultInstance(), ModItems.ELYTRA_UPGRADE_SMITHING_TEMPLATE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHER_BRICK.getDefaultInstance(), ModItems.RED_NETHER_BRICK.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.RED_NETHER_BRICK.get().getDefaultInstance(), ModItems.WARPED_NETHER_BRICK.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.WARPED_NETHER_BRICK.get().getDefaultInstance(), ModItems.PYROLIZED_NETHER_BRICK.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHER_WART.asItem().getDefaultInstance(), ModItems.WARPED_WART.get().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            if (ModList.get().isLoaded("create")) {
                event.insertAfter(Items.RAW_GOLD.getDefaultInstance(), BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "raw_zinc")).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(Items.RAW_IRON.getDefaultInstance(), CreateCompatItems.IRON_DUST.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(Items.RAW_GOLD.getDefaultInstance(), CreateCompatItems.GOLDEN_DUST.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(Items.RAW_COPPER.getDefaultInstance(), CreateCompatItems.COPPER_DUST.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "raw_zinc")).getDefaultInstance(), CreateCompatItems.ZINC_DUST.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.ZINC_DUST.get().getDefaultInstance(), CreateCompatItems.BRASS_DUST.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(Items.GOLD_INGOT.getDefaultInstance(), BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot")).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot")).getDefaultInstance(), BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "brass_ingot")).getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(Items.ANCIENT_DEBRIS.getDefaultInstance(), CreateCompatItems.CRUSHED_ANCIENT_DEBRIS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.CRUSHED_ANCIENT_DEBRIS.get().getDefaultInstance(), CreateCompatItems.ANCIENT_DUST.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
            if (LifeTokensConfig.CONFIG.addLifeTokens()) {
                event.insertAfter(Items.EXPERIENCE_BOTTLE.getDefaultInstance(), LifeTokenItems.LIFE_BIT.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(LifeTokenItems.LIFE_BIT.get().getDefaultInstance(), LifeTokenItems.LIFE_SHARD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(LifeTokenItems.LIFE_SHARD.get().getDefaultInstance(), LifeTokenItems.LIFE_TOKEN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.insertAfter(Items.WARPED_BUTTON.getDefaultInstance(), ModBlocks.EBONY_STEM.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.EBONY_STEM.asItem().getDefaultInstance(), ModBlocks.EBONY_HYPHAE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.EBONY_HYPHAE.asItem().getDefaultInstance(), ModBlocks.STRIPPED_EBONY_STEM.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.STRIPPED_EBONY_STEM.asItem().getDefaultInstance(), ModBlocks.STRIPPED_EBONY_HYPHAE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.STRIPPED_EBONY_HYPHAE.asItem().getDefaultInstance(), ModBlocks.EBONY_PLANKS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.EBONY_PLANKS.asItem().getDefaultInstance(), ModBlocks.EBONY_STAIRS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.EBONY_STAIRS.asItem().getDefaultInstance(), ModBlocks.EBONY_SLAB.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.EBONY_SLAB.asItem().getDefaultInstance(), ModBlocks.EBONY_FENCE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.EBONY_FENCE.asItem().getDefaultInstance(), ModBlocks.EBONY_FENCE_GATE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.EBONY_FENCE_GATE.asItem().getDefaultInstance(), ModBlocks.EBONY_DOOR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.EBONY_DOOR.asItem().getDefaultInstance(), ModBlocks.EBONY_TRAPDOOR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.EBONY_TRAPDOOR.asItem().getDefaultInstance(), ModBlocks.EBONY_PRESSURE_PLATE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.EBONY_PRESSURE_PLATE.asItem().getDefaultInstance(), ModBlocks.EBONY_BUTTON.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(Items.CHERRY_BUTTON.getDefaultInstance(), ModBlocks.AZALEA_LOG.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AZALEA_LOG.asItem().getDefaultInstance(), ModBlocks.AZALEA_WOOD.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AZALEA_WOOD.asItem().getDefaultInstance(), ModBlocks.STRIPPED_AZALEA_LOG.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.STRIPPED_AZALEA_LOG.asItem().getDefaultInstance(), ModBlocks.STRIPPED_AZALEA_WOOD.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.STRIPPED_AZALEA_WOOD.asItem().getDefaultInstance(), ModBlocks.AZALEA_PLANKS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AZALEA_PLANKS.asItem().getDefaultInstance(), ModBlocks.AZALEA_STAIRS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AZALEA_STAIRS.asItem().getDefaultInstance(), ModBlocks.AZALEA_SLAB.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AZALEA_SLAB.asItem().getDefaultInstance(), ModBlocks.AZALEA_FENCE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AZALEA_FENCE.asItem().getDefaultInstance(), ModBlocks.AZALEA_FENCE_GATE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AZALEA_FENCE_GATE.asItem().getDefaultInstance(), ModBlocks.AZALEA_DOOR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AZALEA_DOOR.asItem().getDefaultInstance(), ModBlocks.AZALEA_TRAPDOOR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AZALEA_TRAPDOOR.asItem().getDefaultInstance(), ModBlocks.AZALEA_PRESSURE_PLATE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AZALEA_PRESSURE_PLATE.asItem().getDefaultInstance(), ModBlocks.AZALEA_BUTTON.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(ModBlocks.AZALEA_BUTTON.asItem().getDefaultInstance(), ModBlocks.AQUANDA_STEM.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AQUANDA_STEM.asItem().getDefaultInstance(), ModBlocks.AQUANDA_HYPHAE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AQUANDA_HYPHAE.asItem().getDefaultInstance(), ModBlocks.STRIPPED_AQUANDA_STEM.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.STRIPPED_AQUANDA_STEM.asItem().getDefaultInstance(), ModBlocks.STRIPPED_AQUANDA_HYPHAE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.STRIPPED_AQUANDA_HYPHAE.asItem().getDefaultInstance(), ModBlocks.AQUANDA_PLANKS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AQUANDA_PLANKS.asItem().getDefaultInstance(), ModBlocks.AQUANDA_STAIRS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AQUANDA_STAIRS.asItem().getDefaultInstance(), ModBlocks.AQUANDA_SLAB.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AQUANDA_SLAB.asItem().getDefaultInstance(), ModBlocks.AQUANDA_FENCE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AQUANDA_FENCE.asItem().getDefaultInstance(), ModBlocks.AQUANDA_FENCE_GATE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AQUANDA_FENCE_GATE.asItem().getDefaultInstance(), ModBlocks.AQUANDA_DOOR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AQUANDA_DOOR.asItem().getDefaultInstance(), ModBlocks.AQUANDA_TRAPDOOR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AQUANDA_TRAPDOOR.asItem().getDefaultInstance(), ModBlocks.AQUANDA_PRESSURE_PLATE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.AQUANDA_PRESSURE_PLATE.asItem().getDefaultInstance(), ModBlocks.AQUANDA_BUTTON.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(Items.LIGHT_WEIGHTED_PRESSURE_PLATE.getDefaultInstance(), ModBlocks.ROSE_GOLD_BLOCK.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(Items.NETHERITE_BLOCK.getDefaultInstance(), ModBlocks.ROSARITE_BLOCK.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(Items.NETHER_BRICK_STAIRS.getDefaultInstance(), ModBlocks.CRACKED_NETHER_BRICK_STAIRS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.CRACKED_NETHER_BRICKS.getDefaultInstance(), ModBlocks.NETHER_BRICK_PILLAR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.NETHER_BRICK_PILLAR.asItem().getDefaultInstance(), ModBlocks.CRACKED_NETHER_BRICK_PILLAR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHER_BRICK_SLAB.getDefaultInstance(), ModBlocks.CRACKED_NETHER_BRICK_SLAB.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHER_BRICK_FENCE.getDefaultInstance(), ModBlocks.CRACKED_NETHER_BRICK_FENCE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHER_BRICK_WALL.getDefaultInstance(), ModBlocks.CRACKED_NETHER_BRICK_WALL.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(Items.RED_NETHER_BRICKS.getDefaultInstance(), ModBlocks.CRACKED_RED_NETHER_BRICKS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.CRACKED_RED_NETHER_BRICKS.asItem().getDefaultInstance(), ModBlocks.RED_NETHER_BRICK_PILLAR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.RED_NETHER_BRICK_PILLAR.asItem().getDefaultInstance(), ModBlocks.CRACKED_RED_NETHER_BRICK_PILLAR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.RED_NETHER_BRICK_STAIRS.getDefaultInstance(), ModBlocks.CRACKED_RED_NETHER_BRICK_STAIRS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.RED_NETHER_BRICK_SLAB.getDefaultInstance(), ModBlocks.CRACKED_RED_NETHER_BRICK_SLAB.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.RED_NETHER_BRICK_WALL.getDefaultInstance(), ModBlocks.CRACKED_RED_NETHER_BRICK_WALL.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.CRACKED_RED_NETHER_BRICK_WALL.asItem().getDefaultInstance(), ModBlocks.RED_NETHER_BRICK_FENCE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.RED_NETHER_BRICK_FENCE.asItem().getDefaultInstance(), ModBlocks.CRACKED_RED_NETHER_BRICK_FENCE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(ModBlocks.CRACKED_RED_NETHER_BRICK_FENCE.asItem().getDefaultInstance(), ModBlocks.WARPED_NETHER_BRICKS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.WARPED_NETHER_BRICKS.asItem().getDefaultInstance(), ModBlocks.CRACKED_WARPED_NETHER_BRICKS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.CRACKED_WARPED_NETHER_BRICKS.asItem().getDefaultInstance(), ModBlocks.WARPED_NETHER_BRICK_PILLAR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.WARPED_NETHER_BRICK_PILLAR.asItem().getDefaultInstance(), ModBlocks.CRACKED_WARPED_NETHER_BRICK_PILLAR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.CRACKED_WARPED_NETHER_BRICK_PILLAR.asItem().getDefaultInstance(), ModBlocks.WARPED_NETHER_BRICK_STAIRS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.WARPED_NETHER_BRICK_STAIRS.asItem().getDefaultInstance(), ModBlocks.CRACKED_WARPED_NETHER_BRICK_STAIRS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.CRACKED_WARPED_NETHER_BRICK_STAIRS.asItem().getDefaultInstance(), ModBlocks.WARPED_NETHER_BRICK_SLAB.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.WARPED_NETHER_BRICK_SLAB.asItem().getDefaultInstance(), ModBlocks.CRACKED_WARPED_NETHER_BRICK_SLAB.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.CRACKED_WARPED_NETHER_BRICK_SLAB.asItem().getDefaultInstance(), ModBlocks.WARPED_NETHER_BRICK_WALL.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.WARPED_NETHER_BRICK_WALL.asItem().getDefaultInstance(), ModBlocks.CRACKED_WARPED_NETHER_BRICK_WALL.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.CRACKED_WARPED_NETHER_BRICK_WALL.asItem().getDefaultInstance(), ModBlocks.WARPED_NETHER_BRICK_FENCE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.WARPED_NETHER_BRICK_FENCE.asItem().getDefaultInstance(), ModBlocks.CRACKED_WARPED_NETHER_BRICK_FENCE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(ModBlocks.CRACKED_WARPED_NETHER_BRICK_FENCE.asItem().getDefaultInstance(), ModBlocks.PYROLIZED_NETHER_BRICKS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_NETHER_BRICKS.asItem().getDefaultInstance(), ModBlocks.CRACKED_PYROLIZED_NETHER_BRICKS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICKS.asItem().getDefaultInstance(), ModBlocks.PYROLIZED_NETHER_BRICK_PILLAR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_NETHER_BRICK_PILLAR.asItem().getDefaultInstance(), ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_PILLAR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_PILLAR.asItem().getDefaultInstance(), ModBlocks.PYROLIZED_NETHER_BRICK_STAIRS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_NETHER_BRICK_STAIRS.asItem().getDefaultInstance(), ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_STAIRS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_STAIRS.asItem().getDefaultInstance(), ModBlocks.PYROLIZED_NETHER_BRICK_SLAB.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_NETHER_BRICK_SLAB.asItem().getDefaultInstance(), ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_SLAB.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_SLAB.asItem().getDefaultInstance(), ModBlocks.PYROLIZED_NETHER_BRICK_WALL.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_NETHER_BRICK_WALL.asItem().getDefaultInstance(), ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_WALL.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_WALL.asItem().getDefaultInstance(), ModBlocks.PYROLIZED_NETHER_BRICK_FENCE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_NETHER_BRICK_FENCE.asItem().getDefaultInstance(), ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_FENCE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(ModBlocks.CRACKED_PYROLIZED_NETHER_BRICK_FENCE.asItem().getDefaultInstance(), ModBlocks.BLAZE_BRICKS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.BLAZE_BRICKS.asItem().getDefaultInstance(), ModBlocks.BLAZE_BRICK_PILLAR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.BLAZE_BRICK_PILLAR.asItem().getDefaultInstance(), ModBlocks.BLAZE_BRICK_STAIRS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.BLAZE_BRICK_STAIRS.asItem().getDefaultInstance(), ModBlocks.BLAZE_BRICK_SLAB.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.BLAZE_BRICK_SLAB.asItem().getDefaultInstance(), ModBlocks.BLAZE_BRICK_WALL.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.BLAZE_BRICK_WALL.asItem().getDefaultInstance(), ModBlocks.BLAZE_BRICK_FENCE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(ModBlocks.BLAZE_BRICK_FENCE.asItem().getDefaultInstance(), ModBlocks.RED_BLAZE_BRICKS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.RED_BLAZE_BRICKS.asItem().getDefaultInstance(), ModBlocks.RED_BLAZE_BRICK_PILLAR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.RED_BLAZE_BRICK_PILLAR.asItem().getDefaultInstance(), ModBlocks.RED_BLAZE_BRICK_STAIRS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.RED_BLAZE_BRICK_STAIRS.asItem().getDefaultInstance(), ModBlocks.RED_BLAZE_BRICK_SLAB.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.RED_BLAZE_BRICK_SLAB.asItem().getDefaultInstance(), ModBlocks.RED_BLAZE_BRICK_WALL.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.RED_BLAZE_BRICK_WALL.asItem().getDefaultInstance(), ModBlocks.RED_BLAZE_BRICK_FENCE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(ModBlocks.RED_BLAZE_BRICK_FENCE.asItem().getDefaultInstance(), ModBlocks.WARPED_BLAZE_BRICKS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.WARPED_BLAZE_BRICKS.asItem().getDefaultInstance(), ModBlocks.WARPED_BLAZE_BRICK_PILLAR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.WARPED_BLAZE_BRICK_PILLAR.asItem().getDefaultInstance(), ModBlocks.WARPED_BLAZE_BRICK_STAIRS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.WARPED_BLAZE_BRICK_STAIRS.asItem().getDefaultInstance(), ModBlocks.WARPED_BLAZE_BRICK_SLAB.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.WARPED_BLAZE_BRICK_SLAB.asItem().getDefaultInstance(), ModBlocks.WARPED_BLAZE_BRICK_WALL.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.WARPED_BLAZE_BRICK_WALL.asItem().getDefaultInstance(), ModBlocks.WARPED_BLAZE_BRICK_FENCE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(ModBlocks.WARPED_BLAZE_BRICK_FENCE.asItem().getDefaultInstance(), ModBlocks.PYROLIZED_BLAZE_BRICKS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_BLAZE_BRICKS.asItem().getDefaultInstance(), ModBlocks.PYROLIZED_BLAZE_BRICK_PILLAR.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_BLAZE_BRICK_PILLAR.asItem().getDefaultInstance(), ModBlocks.PYROLIZED_BLAZE_BRICK_STAIRS.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_BLAZE_BRICK_STAIRS.asItem().getDefaultInstance(), ModBlocks.PYROLIZED_BLAZE_BRICK_SLAB.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_BLAZE_BRICK_SLAB.asItem().getDefaultInstance(), ModBlocks.PYROLIZED_BLAZE_BRICK_WALL.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModBlocks.PYROLIZED_BLAZE_BRICK_WALL.asItem().getDefaultInstance(), ModBlocks.PYROLIZED_BLAZE_BRICK_FENCE.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.insertAfter(Items.IRON_HOE.getDefaultInstance(), ModItems.COPPER_SHOVEL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_SHOVEL.get().getDefaultInstance(), ModItems.COPPER_PICKAXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_PICKAXE.get().getDefaultInstance(), ModItems.COPPER_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_AXE.get().getDefaultInstance(), ModItems.COPPER_HOE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_HOE.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_SHOVEL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSE_GOLDEN_SHOVEL.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_PICKAXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSE_GOLDEN_PICKAXE.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSE_GOLDEN_AXE.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_HOE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.DIAMOND_HOE.getDefaultInstance(), ModItems.EMERALD_SHOVEL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.EMERALD_SHOVEL.get().getDefaultInstance(), ModItems.EMERALD_PICKAXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.EMERALD_PICKAXE.get().getDefaultInstance(), ModItems.EMERALD_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.EMERALD_AXE.get().getDefaultInstance(), ModItems.EMERALD_HOE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHERITE_HOE.getDefaultInstance(), ModItems.ROSARITE_SHOVEL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSARITE_SHOVEL.get().getDefaultInstance(), ModItems.ROSARITE_PICKAXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSARITE_PICKAXE.get().getDefaultInstance(), ModItems.ROSARITE_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSARITE_AXE.get().getDefaultInstance(), ModItems.ROSARITE_HOE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.STONE_HOE.getDefaultInstance(), ModItems.LAPIS_SHOVEL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.LAPIS_SHOVEL.get().getDefaultInstance(), ModItems.LAPIS_PICKAXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.LAPIS_PICKAXE.get().getDefaultInstance(), ModItems.LAPIS_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.LAPIS_AXE.get().getDefaultInstance(), ModItems.LAPIS_HOE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.LAPIS_HOE.get().getDefaultInstance(), ModItems.QUARTZ_SHOVEL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.QUARTZ_SHOVEL.get().getDefaultInstance(), ModItems.QUARTZ_PICKAXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.QUARTZ_PICKAXE.get().getDefaultInstance(), ModItems.QUARTZ_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.QUARTZ_AXE.get().getDefaultInstance(), ModItems.QUARTZ_HOE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.SHEARS.getDefaultInstance(), ModItems.STONE_SHEARS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(ModItems.STONE_SHEARS.get().getDefaultInstance(), ModItems.WOODEN_SHEARS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.STONE_SHEARS.get().getDefaultInstance(), ModItems.LAPIS_SHEARS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.LAPIS_SHEARS.get().getDefaultInstance(), ModItems.QUARTZ_SHEARS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.SHEARS.getDefaultInstance(), ModItems.COPPER_SHEARS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_SHEARS.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_SHEARS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSE_GOLDEN_SHEARS.get().getDefaultInstance(), ModItems.GOLDEN_SHEARS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.GOLDEN_SHEARS.get().getDefaultInstance(), ModItems.DIAMOND_SHEARS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.DIAMOND_SHEARS.get().getDefaultInstance(), ModItems.EMERALD_SHEARS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.EMERALD_SHEARS.get().getDefaultInstance(), ModItems.NETHERITE_SHEARS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.NETHERITE_SHEARS.get().getDefaultInstance(), ModItems.ROSARITE_SHEARS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            if (ModList.get().isLoaded("create")) {
                event.insertAfter(Items.IRON_HOE.getDefaultInstance(), CreateCompatItems.ZINC_SHOVEL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.ZINC_SHOVEL.get().getDefaultInstance(), CreateCompatItems.ZINC_PICKAXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.ZINC_PICKAXE.get().getDefaultInstance(), CreateCompatItems.ZINC_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.ZINC_AXE.get().getDefaultInstance(), CreateCompatItems.ZINC_HOE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ModItems.COPPER_HOE.get().getDefaultInstance(), CreateCompatItems.BRASS_SHOVEL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.BRASS_SHOVEL.get().getDefaultInstance(), CreateCompatItems.BRASS_PICKAXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.BRASS_PICKAXE.get().getDefaultInstance(), CreateCompatItems.BRASS_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.BRASS_AXE.get().getDefaultInstance(), CreateCompatItems.BRASS_HOE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(Items.SHEARS.getDefaultInstance(), CreateCompatItems.ZINC_SHEARS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ModItems.COPPER_SHEARS.get().getDefaultInstance(), CreateCompatItems.BRASS_SHEARS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.insertAfter(Items.WOLF_ARMOR.getDefaultInstance(), ModItems.LEATHER_WOLF_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.LEATHER_WOLF_ARMOR.get().getDefaultInstance(), ModItems.CHAINMAIL_WOLF_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.CHAINMAIL_WOLF_ARMOR.get().getDefaultInstance(), ModItems.IRON_WOLF_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.IRON_WOLF_ARMOR.get().getDefaultInstance(), ModItems.COPPER_WOLF_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_WOLF_ARMOR.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_WOLF_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSE_GOLDEN_WOLF_ARMOR.get().getDefaultInstance(), ModItems.GOLDEN_WOLF_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.GOLDEN_WOLF_ARMOR.get().getDefaultInstance(), ModItems.DIAMOND_WOLF_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.DIAMOND_WOLF_ARMOR.get().getDefaultInstance(), ModItems.EMERALD_WOLF_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.EMERALD_WOLF_ARMOR.get().getDefaultInstance(), ModItems.NETHERITE_WOLF_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.NETHERITE_WOLF_ARMOR.get().getDefaultInstance(), ModItems.ROSARITE_WOLF_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.IRON_BOOTS.getDefaultInstance(), ModItems.COPPER_HELMET.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_HELMET.get().getDefaultInstance(), ModItems.COPPER_CHESTPLATE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_CHESTPLATE.get().getDefaultInstance(), ModItems.COPPER_ELYTRA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_ELYTRA.get().getDefaultInstance(), ModItems.COPPER_LEGGINGS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_LEGGINGS.get().getDefaultInstance(), ModItems.COPPER_BOOTS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_BOOTS.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_HELMET.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSE_GOLDEN_HELMET.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_CHESTPLATE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSE_GOLDEN_CHESTPLATE.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_ELYTRA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSE_GOLDEN_ELYTRA.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_LEGGINGS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSE_GOLDEN_LEGGINGS.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_BOOTS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.DIAMOND_BOOTS.getDefaultInstance(), ModItems.EMERALD_HELMET.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.EMERALD_HELMET.get().getDefaultInstance(), ModItems.EMERALD_CHESTPLATE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.EMERALD_CHESTPLATE.get().getDefaultInstance(), ModItems.EMERALD_ELYTRA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.EMERALD_ELYTRA.get().getDefaultInstance(), ModItems.EMERALD_LEGGINGS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.EMERALD_LEGGINGS.get().getDefaultInstance(), ModItems.EMERALD_BOOTS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHERITE_BOOTS.getDefaultInstance(), ModItems.ROSARITE_HELMET.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSARITE_HELMET.get().getDefaultInstance(), ModItems.ROSARITE_CHESTPLATE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSARITE_CHESTPLATE.get().getDefaultInstance(), ModItems.ROSARITE_ELYTRA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSARITE_ELYTRA.get().getDefaultInstance(), ModItems.ROSARITE_LEGGINGS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSARITE_LEGGINGS.get().getDefaultInstance(), ModItems.ROSARITE_BOOTS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.IRON_CHESTPLATE.getDefaultInstance(), ModItems.IRON_ELYTRA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.LEATHER_CHESTPLATE.getDefaultInstance(), ModItems.LEATHER_GLIDER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.GOLDEN_CHESTPLATE.getDefaultInstance(), ModItems.GOLDEN_ELYTRA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.DIAMOND_CHESTPLATE.getDefaultInstance(), ModItems.DIAMOND_ELYTRA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHERITE_CHESTPLATE.getDefaultInstance(), ModItems.NETHERITE_ELYTRA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.CHAINMAIL_CHESTPLATE.getDefaultInstance(), ModItems.CHAINED_ELYTRA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.IRON_SWORD.getDefaultInstance(), ModItems.COPPER_SWORD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_SWORD.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_SWORD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.DIAMOND_SWORD.getDefaultInstance(), ModItems.EMERALD_SWORD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHERITE_SWORD.getDefaultInstance(), ModItems.ROSARITE_SWORD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.IRON_AXE.getDefaultInstance(), ModItems.COPPER_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_AXE.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.DIAMOND_AXE.getDefaultInstance(), ModItems.EMERALD_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHERITE_AXE.getDefaultInstance(), ModItems.ROSARITE_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.LEATHER_HORSE_ARMOR.getDefaultInstance(), ModItems.CHAINMAIL_HORSE_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.IRON_HORSE_ARMOR.getDefaultInstance(), ModItems.COPPER_HORSE_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_HORSE_ARMOR.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_HORSE_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.DIAMOND_HORSE_ARMOR.getDefaultInstance(), ModItems.EMERALD_HORSE_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.EMERALD_HORSE_ARMOR.get().getDefaultInstance(), ModItems.NETHERITE_HORSE_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.NETHERITE_HORSE_ARMOR.get().getDefaultInstance(), ModItems.ROSARITE_HORSE_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(ModItems.LEATHER_WOLF_ARMOR.get().getDefaultInstance(), ModItems.TURTLE_SCUTE_WOLF_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            if (ModList.get().isLoaded("create")) {
                event.insertAfter(ModItems.IRON_WOLF_ARMOR.get().getDefaultInstance(), CreateCompatItems.ZINC_WOLF_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ModItems.COPPER_WOLF_ARMOR.get().getDefaultInstance(), CreateCompatItems.BRASS_WOLF_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(Items.IRON_BOOTS.getDefaultInstance(), CreateCompatItems.ZINC_HELMET.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.ZINC_HELMET.get().getDefaultInstance(), CreateCompatItems.ZINC_CHESTPLATE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.ZINC_CHESTPLATE.get().getDefaultInstance(), CreateCompatItems.ZINC_ELYTRA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.ZINC_ELYTRA.get().getDefaultInstance(), CreateCompatItems.ZINC_LEGGINGS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.ZINC_LEGGINGS.get().getDefaultInstance(), CreateCompatItems.ZINC_BOOTS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ModItems.COPPER_BOOTS.get().getDefaultInstance(), CreateCompatItems.BRASS_HELMET.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.BRASS_HELMET.get().getDefaultInstance(), CreateCompatItems.BRASS_CHESTPLATE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.BRASS_CHESTPLATE.get().getDefaultInstance(), CreateCompatItems.BRASS_ELYTRA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.BRASS_ELYTRA.get().getDefaultInstance(), CreateCompatItems.BRASS_LEGGINGS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.BRASS_LEGGINGS.get().getDefaultInstance(), CreateCompatItems.BRASS_BOOTS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(Items.IRON_AXE.getDefaultInstance(), CreateCompatItems.ZINC_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(Items.IRON_SWORD.getDefaultInstance(), CreateCompatItems.ZINC_SWORD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ModItems.COPPER_AXE.get().getDefaultInstance(), CreateCompatItems.BRASS_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ModItems.COPPER_SWORD.get().getDefaultInstance(), CreateCompatItems.BRASS_SWORD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(Items.IRON_HORSE_ARMOR.getDefaultInstance(), CreateCompatItems.ZINC_HORSE_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ModItems.COPPER_HORSE_ARMOR.get().getDefaultInstance(), CreateCompatItems.BRASS_HORSE_ARMOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.BRASS_SWORD.get().getDefaultInstance(), CreateCompatItems.BRASS_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CreateCompatItems.ZINC_SWORD.get().getDefaultInstance(), CreateCompatItems.ZINC_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
            event.insertAfter(Items.WOODEN_SWORD.getDefaultInstance(), ModItems.WOODEN_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.STONE_SWORD.getDefaultInstance(), ModItems.STONE_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.IRON_SWORD.getDefaultInstance(), ModItems.IRON_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.COPPER_SWORD.get().getDefaultInstance(), ModItems.COPPER_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSE_GOLDEN_SWORD.get().getDefaultInstance(), ModItems.ROSE_GOLDEN_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.GOLDEN_SWORD.getDefaultInstance(), ModItems.GOLDEN_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.DIAMOND_SWORD.getDefaultInstance(), ModItems.DIAMOND_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.EMERALD_SWORD.get().getDefaultInstance(), ModItems.EMERALD_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.NETHERITE_SWORD.getDefaultInstance(), ModItems.NETHERITE_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ROSARITE_SWORD.get().getDefaultInstance(), ModItems.ROSARITE_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.STONE_MACE.get().getDefaultInstance(), ModItems.LAPIS_SWORD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.LAPIS_SWORD.get().getDefaultInstance(), ModItems.LAPIS_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.LAPIS_MACE.get().getDefaultInstance(), ModItems.QUARTZ_SWORD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.QUARTZ_SWORD.get().getDefaultInstance(), ModItems.QUARTZ_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.STONE_AXE.getDefaultInstance(), ModItems.LAPIS_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.LAPIS_AXE.get().getDefaultInstance(), ModItems.QUARTZ_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
            EntityModelSet models = event.getEntityModels();for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
                EntityRenderer<?> renderer = event.getRenderer(type);
                if (!(renderer instanceof MobRenderer<?, ?> mobRenderer)) continue;

                // Apply only for Hoglin entity type
                if (type == EntityType.HOGLIN) {
                    try {
                        @SuppressWarnings("unchecked")
                        MobRenderer<Hoglin, HoglinModel<Hoglin>> hoglinRenderer =
                                (MobRenderer<Hoglin, HoglinModel<Hoglin>>) mobRenderer;

                        hoglinRenderer.addLayer(new HoglinArmorLayer(hoglinRenderer, models));
                    } catch (ClassCastException ignored) {
                        // Renderer doesn't match expected type, ignore
                    }
                }

                // Repeat for Zoglin if needed:
                if (type == EntityType.ZOGLIN) {
                    try {
                        @SuppressWarnings("unchecked")
                        MobRenderer<Zoglin, HoglinModel<Zoglin>> zoglinRenderer =
                                (MobRenderer<Zoglin, HoglinModel<Zoglin>>) mobRenderer;

                        zoglinRenderer.addLayer(new ZoglinArmorLayer(zoglinRenderer, models));
                    } catch (ClassCastException ignored) {
                        // Ignore
                    }
                }
            }
            for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
                EntityRenderer<?> renderer = event.getRenderer(type);
                if (!(renderer instanceof LivingEntityRenderer<?, ?> livingRenderer)) continue;

                // Check if the model is a HumanoidModel
                if (!(livingRenderer.getModel() instanceof HumanoidModel<?>)) continue;

                try {
                    // Unsafe but necessary cast to the correct types
                    @SuppressWarnings("unchecked")
                    LivingEntityRenderer<LivingEntity, HumanoidModel<LivingEntity>> castedRenderer =
                            (LivingEntityRenderer<LivingEntity, HumanoidModel<LivingEntity>>) livingRenderer;

                    castedRenderer.addLayer(new TeiredElytraLayer<>(castedRenderer, models));

                    if (ModList.get().isLoaded("create")) {
                        castedRenderer.addLayer(new CreateCompatTeiredElytraLayer<>(castedRenderer, models));
                    }
                } catch (ClassCastException ignored) {

                }
            }
            for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
                EntityRenderer<?> renderer = event.getRenderer(type);
                if (!(renderer instanceof LivingEntityRenderer<?, ?> livingRenderer)) continue;

                // Check if the model is a HumanoidModel
                if (!(livingRenderer.getModel() instanceof IllagerModel<?>)) continue;

                try {
                    // Unsafe but necessary cast to the correct types
                    @SuppressWarnings("unchecked")
                    LivingEntityRenderer<LivingEntity, HumanoidModel<LivingEntity>> castedRenderer =
                            (LivingEntityRenderer<LivingEntity, HumanoidModel<LivingEntity>>) livingRenderer;

                    castedRenderer.addLayer(new TeiredElytraLayer<>(castedRenderer, models));

                    if (ModList.get().isLoaded("create")) {
                        castedRenderer.addLayer(new CreateCompatTeiredElytraLayer<>(castedRenderer, models));
                    }
                } catch (ClassCastException ignored) {
                    // Skip if generic mismatch
                }
            }
            for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
                EntityRenderer<?> renderer = event.getRenderer(type);
                if (!(renderer instanceof LivingEntityRenderer<?, ?> livingRenderer)) continue;

                // Check if the model is a HumanoidModel
                if (!(livingRenderer.getModel() instanceof WitchModel<?>)) continue;

                try {
                    // Unsafe but necessary cast to the correct types
                    @SuppressWarnings("unchecked")
                    LivingEntityRenderer<LivingEntity, HumanoidModel<LivingEntity>> castedRenderer =
                            (LivingEntityRenderer<LivingEntity, HumanoidModel<LivingEntity>>) livingRenderer;

                    castedRenderer.addLayer(new TeiredElytraLayer<>(castedRenderer, models));

                    if (ModList.get().isLoaded("create")) {
                        castedRenderer.addLayer(new CreateCompatTeiredElytraLayer<>(castedRenderer, models));
                    }
                } catch (ClassCastException ignored) {
                    // Skip if generic mismatch
                }
            }
            for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
                EntityRenderer<?> renderer = event.getRenderer(type);
                if (!(renderer instanceof LivingEntityRenderer<?, ?> livingRenderer)) continue;

                // Check if the model is a HumanoidModel
                if (!(livingRenderer.getModel() instanceof VexModel)) continue;

                try {
                    // Unsafe but necessary cast to the correct types
                    @SuppressWarnings("unchecked")
                    LivingEntityRenderer<LivingEntity, HumanoidModel<LivingEntity>> castedRenderer =
                            (LivingEntityRenderer<LivingEntity, HumanoidModel<LivingEntity>>) livingRenderer;

                    castedRenderer.addLayer(new VexElytraLayer<>(castedRenderer, models));

                    if (ModList.get().isLoaded("create")) {
                        castedRenderer.addLayer(new CreateCompatVexElytraLayer<>(castedRenderer, models));
                    }
                } catch (ClassCastException ignored) {
                    // Skip if generic mismatch
                }
            }
            for (PlayerSkin.Model skin : event.getSkins()) {
                EntityRenderer<? extends LivingEntity> renderer = event.getSkin(skin);

                // Confirm it's a PlayerRenderer
                if (renderer instanceof PlayerRenderer playerRenderer) {
                    // ElytraLayer expects <AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>
                    playerRenderer.addLayer(new TeiredElytraLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>(
                            playerRenderer,
                            event.getEntityModels()
                    ));
                    if (ModList.get().isLoaded("create")) {
                        playerRenderer.addLayer(new CreateCompatTeiredElytraLayer<>(
                                playerRenderer,
                                models
                        ));
                    }
                }
            }
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(ModModelLayers.HOGLIN_ARMOR, ModLayerDefinitions::HoglinArmor);
            event.registerLayerDefinition(ModModelLayers.ZOGLIN_ARMOR, ModLayerDefinitions::HoglinArmor);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                        CauldronInteraction.WATER.map().put(ModItems.LEATHER_GLIDER.get(), CauldronInteraction.DYED_ITEM);
                        CauldronInteraction.WATER.map().put(ModItems.NETHERITE_WOLF_ARMOR.get(), CauldronInteraction.DYED_ITEM);
                        CauldronInteraction.WATER.map().put(ModItems.DIAMOND_WOLF_ARMOR.get(), CauldronInteraction.DYED_ITEM);
                        CauldronInteraction.WATER.map().put(ModItems.CHAINMAIL_WOLF_ARMOR.get(), CauldronInteraction.DYED_ITEM);
                        CauldronInteraction.WATER.map().put(ModItems.IRON_WOLF_ARMOR.get(), CauldronInteraction.DYED_ITEM);
                        CauldronInteraction.WATER.map().put(ModItems.GOLDEN_WOLF_ARMOR.get(), CauldronInteraction.DYED_ITEM);
                        CauldronInteraction.WATER.map().put(ModItems.LEATHER_WOLF_ARMOR.get(), CauldronInteraction.DYED_ITEM);
                        CauldronInteraction.WATER.map().put(ModItems.COPPER_WOLF_ARMOR.get(), CauldronInteraction.DYED_ITEM);
                        CauldronInteraction.WATER.map().put(ModItems.ROSE_GOLDEN_ELYTRA.get(), CauldronInteraction.DYED_ITEM);
                        CauldronInteraction.WATER.map().put(ModItems.ROSARITE_WOLF_ARMOR.get(), CauldronInteraction.DYED_ITEM);
                        CauldronInteraction.WATER.map().put(ModItems.EMERALD_WOLF_ARMOR.get(), CauldronInteraction.DYED_ITEM);
                        CauldronInteraction.WATER.map().put(ModItems.TURTLE_SCUTE_WOLF_ARMOR.get(), CauldronInteraction.DYED_ITEM);
                    }
            );
            if (ModList.get().isLoaded("create")) {
                event.enqueueWork(() -> {
                            CauldronInteraction.WATER.map().put(CreateCompatItems.BRASS_WOLF_ARMOR.get(), CauldronInteraction.DYED_ITEM);
                            CauldronInteraction.WATER.map().put(CreateCompatItems.ZINC_WOLF_ARMOR.get(), CauldronInteraction.DYED_ITEM);
                        }
                );
            }
            Minecraft.getInstance().getItemColors().register(
                    (p_329705_, p_329706_) -> p_329706_ > 0 ? -1 : DyedItemColor.getOrDefault(p_329705_, -6265536),
                    ModItems.LEATHER_WOLF_ARMOR
            );
            Minecraft.getInstance().getItemColors().register((p_329699_, p_329700_) -> p_329700_ != 1 ? -1 : DyedItemColor.getOrDefault(p_329699_, 0),
                    ModItems.IRON_WOLF_ARMOR,
                    ModItems.COPPER_WOLF_ARMOR,
                    ModItems.ROSE_GOLDEN_WOLF_ARMOR,
                    ModItems.EMERALD_WOLF_ARMOR,
                    ModItems.ROSARITE_WOLF_ARMOR,
                    ModItems.EMERALD_WOLF_ARMOR,
                    ModItems.NETHERITE_WOLF_ARMOR,
                    ModItems.GOLDEN_WOLF_ARMOR,
                    ModItems.CHAINMAIL_WOLF_ARMOR,
                    ModItems.TURTLE_SCUTE_WOLF_ARMOR,
                    ModItems.DIAMOND_WOLF_ARMOR
            );
            if (ModList.get().isLoaded("create")) {
                Minecraft.getInstance().getItemColors().register((p_329699_, p_329700_) -> p_329700_ != 1 ? -1 : DyedItemColor.getOrDefault(p_329699_, 0),
                        CreateCompatItems.BRASS_WOLF_ARMOR,
                        CreateCompatItems.ZINC_WOLF_ARMOR
                );
            }
            Minecraft.getInstance().getItemColors().register((p_329699_, p_329700_) -> p_329700_ != 1 ? -1 : DyedItemColor.getOrDefault(p_329699_, -6265536),
                    ModItems.LEATHER_GLIDER
            );
            Minecraft.getInstance().getBlockColors().register(
                    (p_276237_, p_276238_, p_276239_, p_276240_) -> p_276238_ != null && p_276239_ != null
                            ? BiomeColors.getAverageGrassColor(p_276238_, p_276239_)
                            : GrassColor.getDefaultColor(),
                    ModBlocks.AZALEA_PLANKS.get(),
                    ModBlocks.AZALEA_SLAB.get(),
                    ModBlocks.AZALEA_LOG.get(),
                    ModBlocks.AZALEA_STAIRS.get(),
                    ModBlocks.STRIPPED_AZALEA_LOG.get(),
                    ModBlocks.STRIPPED_AZALEA_WOOD.get(),
                    ModBlocks.AZALEA_BUTTON.get(),
                    ModBlocks.AZALEA_FENCE.get(),
                    ModBlocks.AZALEA_FENCE_GATE.get(),
                    ModBlocks.AZALEA_TRAPDOOR.get(),
                    ModBlocks.AZALEA_PRESSURE_PLATE.get(),
                    Blocks.AZALEA,
                    Blocks.FLOWERING_AZALEA,
                    Blocks.KELP,
                    Blocks.KELP_PLANT,
                    Blocks.SEAGRASS,
                    Blocks.TALL_SEAGRASS,
                    Blocks.MOSS_BLOCK,
                    Blocks.MOSS_CARPET,
                    Blocks.MOSSY_COBBLESTONE,
                    Blocks.MOSSY_COBBLESTONE_SLAB,
                    Blocks.MOSSY_COBBLESTONE_STAIRS,
                    Blocks.MOSSY_COBBLESTONE_WALL,
                    Blocks.MOSSY_STONE_BRICKS,
                    Blocks.MOSSY_STONE_BRICK_SLAB,
                    Blocks.MOSSY_STONE_BRICK_STAIRS,
                    Blocks.MOSSY_STONE_BRICK_WALL,
                    Blocks.INFESTED_MOSSY_STONE_BRICKS,
                    Blocks.BIG_DRIPLEAF,
                    Blocks.BIG_DRIPLEAF_STEM,
                    Blocks.CAVE_VINES,
                    Blocks.CAVE_VINES_PLANT
            );

            if (ModList.get().isLoaded("everycomp")) {
                Minecraft.getInstance().getBlockColors().register(
                        (p_276237_, p_276238_, p_276239_, p_276240_) -> p_276238_ != null && p_276239_ != null
                                ? BiomeColors.getAverageGrassColor(p_276238_, p_276239_)
                                : GrassColor.getDefaultColor(),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/azalea_full_drawers_1")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/azalea_full_drawers_2")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/azalea_full_drawers_4")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/azalea_half_drawers_1")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/azalea_half_drawers_2")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/azalea_half_drawers_4")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/azalea_trim"))
                );
            }

            Minecraft.getInstance().getBlockColors().register(
                    (p_276237_, p_276238_, p_276239_, p_276240_) -> p_276238_ != null && p_276239_ != null
                            ? BiomeColors.getAverageFoliageColor(p_276238_, p_276239_)
                            : GrassColor.getDefaultColor(),
                    Blocks.AZALEA_LEAVES,
                    Blocks.FLOWERING_AZALEA_LEAVES
            );
            Minecraft.getInstance().getBlockColors().register(
                    (p_276233_, p_276234_, p_276235_, p_276236_) -> p_276234_ != null && p_276235_ != null
                            ? BiomeColors.getAverageGrassColor(
                            p_276234_, p_276233_.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER ? p_276235_.below() : p_276235_
                    )
                            : GrassColor.getDefaultColor(),
                    ModBlocks.AZALEA_DOOR.get(),
                    Blocks.SMALL_DRIPLEAF
            );
            Minecraft.getInstance().getBlockColors().register(
                    (p_276237_, p_276238_, p_276239_, p_276240_) -> p_276238_ != null && p_276239_ != null
                            ? BiomeColors.getAverageWaterColor(p_276238_, p_276239_)
                            : GrassColor.getDefaultColor(),
                    ModBlocks.AQUANDA_PLANKS.get(),
                    ModBlocks.AQUANDA_SLAB.get(),
                    ModBlocks.AQUANDA_STEM.get(),
                    ModBlocks.AQUANDA_HYPHAE.get(),
                    ModBlocks.AQUANDA_STAIRS.get(),
                    ModBlocks.STRIPPED_AQUANDA_STEM.get(),
                    ModBlocks.STRIPPED_AQUANDA_HYPHAE.get(),
                    ModBlocks.AQUANDA_BUTTON.get(),
                    ModBlocks.AQUANDA_FENCE.get(),
                    ModBlocks.AQUANDA_FENCE_GATE.get(),
                    ModBlocks.AQUANDA_TRAPDOOR.get(),
                    ModBlocks.AQUANDA_PRESSURE_PLATE.get(),
                    ModBlocks.AQUANDA.get(),
                    ModBlocks.AQUANDA_GEL.get(),
                    ModBlocks.GLOWING_AQUANDA.get(),
                    ModBlocks.GLOWING_AQUANDA_GEL.get(),
                    ModBlocks.AQUANDA_VINES.get(),
                    ModBlocks.AQUANDA_VINES_PLANT.get(),
                    ModBlocks.AQUANDA_MOSS_BLOCK.get(),
                    ModBlocks.AQUANDA_MOSS_CARPET.get(),
                    ModBlocks.AQUANDA_KELP.get(),
                    ModBlocks.AQUANDA_KELP_PLANT.get(),
                    ModBlocks.MOSSY_AQUANDA_COBBLESTONE.get(),
                    ModBlocks.MOSSY_AQUANDA_COBBLESTONE_SLAB.get(),
                    ModBlocks.MOSSY_AQUANDA_COBBLESTONE_STAIRS.get(),
                    ModBlocks.MOSSY_AQUANDA_COBBLESTONE_WALL.get(),
                    ModBlocks.MOSSY_AQUANDA_STONE_BRICKS.get(),
                    ModBlocks.MOSSY_AQUANDA_STONE_BRICK_SLAB.get(),
                    ModBlocks.MOSSY_AQUANDA_STONE_BRICK_STAIRS.get(),
                    ModBlocks.MOSSY_AQUANDA_STONE_BRICK_WALL.get()
            );
            if (ModList.get().isLoaded("everycomp")) {
                Minecraft.getInstance().getBlockColors().register(
                        (p_276237_, p_276238_, p_276239_, p_276240_) -> p_276238_ != null && p_276239_ != null
                                ? BiomeColors.getAverageWaterColor(p_276238_, p_276239_)
                                : GrassColor.getDefaultColor(),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/aquanda_full_drawers_1")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/aquanda_full_drawers_2")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/aquanda_full_drawers_4")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/aquanda_half_drawers_1")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/aquanda_half_drawers_2")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/aquanda_half_drawers_4")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/aquanda_trim"))
                );
            }

            Minecraft.getInstance().getBlockColors().register(
                    (p_276233_, p_276234_, p_276235_, p_276236_) -> p_276234_ != null && p_276235_ != null
                            ? BiomeColors.getAverageWaterColor(
                            p_276234_, p_276233_.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER ? p_276235_.below() : p_276235_
                    )
                            : GrassColor.getDefaultColor(),
                    ModBlocks.AQUANDA_DOOR.get()
            );

            if (ModList.get().isLoaded("everycomp")) {
                Minecraft.getInstance().getItemColors().register(
                        (stack, tintIndex) -> GrassColor.get(0.5, 1.0),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/azalea_full_drawers_1")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/azalea_full_drawers_2")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/azalea_full_drawers_4")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/azalea_half_drawers_1")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/azalea_half_drawers_2")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/azalea_half_drawers_4")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/azalea_trim"))
                );
            }

            Minecraft.getInstance().getItemColors().register(
                    (stack, tintIndex) -> GrassColor.get(0.5, 1.0),
                    ModBlocks.AZALEA_PLANKS.get(),
                    ModBlocks.AZALEA_SLAB.get(),
                    ModBlocks.AZALEA_LOG.get(),
                    ModBlocks.AZALEA_STAIRS.get(),
                    ModBlocks.STRIPPED_AZALEA_LOG.get(),
                    ModBlocks.STRIPPED_AZALEA_WOOD.get(),
                    ModBlocks.AZALEA_BUTTON.get(),
                    ModBlocks.AZALEA_FENCE.get(),
                    ModBlocks.AZALEA_FENCE_GATE.get(),
                    ModBlocks.AZALEA_TRAPDOOR.get(),
                    ModBlocks.AZALEA_PRESSURE_PLATE.get(),
                    ModBlocks.AZALEA_DOOR.get(),
                    Items.AZALEA,
                    Items.FLOWERING_AZALEA,
                    Items.KELP,
                    Items.SEAGRASS,
                    Items.MOSS_BLOCK,
                    Items.MOSS_CARPET,
                    Items.MOSSY_COBBLESTONE,
                    Items.MOSSY_COBBLESTONE_SLAB,
                    Items.MOSSY_COBBLESTONE_STAIRS,
                    Items.MOSSY_COBBLESTONE_WALL,
                    Items.MOSSY_STONE_BRICKS,
                    Items.MOSSY_STONE_BRICK_SLAB,
                    Items.MOSSY_STONE_BRICK_STAIRS,
                    Items.MOSSY_STONE_BRICK_WALL,
                    Items.INFESTED_MOSSY_STONE_BRICKS,
                    Items.AZALEA_LEAVES,
                    Items.FLOWERING_AZALEA_LEAVES,
                    Items.BIG_DRIPLEAF,
                    Items.SMALL_DRIPLEAF,
                    Items.KELP
            );

            if (ModList.get().isLoaded("everycomp")) {
                Minecraft.getInstance().getItemColors().register(
                        (stack, tintIndex) -> 0xFF3F76E4,
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/aquanda_full_drawers_1")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/aquanda_full_drawers_2")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/aquanda_full_drawers_4")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/aquanda_half_drawers_1")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/aquanda_half_drawers_2")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/aquanda_half_drawers_4")),
                        BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("everycomp", "sd/more_stuff/aquanda_trim"))
                );
            }

            Minecraft.getInstance().getItemColors().register(
                    (stack, tintIndex) -> 0xFF3F76E4, // Tint all layers with water-blue
                    ModBlocks.AQUANDA.get(), // Tint all layers with water-blue
                    ModBlocks.AQUANDA_GEL.get(), // Tint all layers with water-blue
                    ModBlocks.GLOWING_AQUANDA.get(), // Tint all layers with water-blue
                    ModBlocks.GLOWING_AQUANDA_GEL.get(),
                    ModBlocks.AQUANDA_PLANKS.get(),
                    ModBlocks.AQUANDA_SLAB.get(),
                    ModBlocks.AQUANDA_STEM.get(),
                    ModBlocks.AQUANDA_HYPHAE.get(),
                    ModBlocks.AQUANDA_STAIRS.get(),
                    ModBlocks.STRIPPED_AQUANDA_STEM.get(),
                    ModBlocks.STRIPPED_AQUANDA_HYPHAE.get(),
                    ModBlocks.AQUANDA_BUTTON.get(),
                    ModBlocks.AQUANDA_FENCE.get(),
                    ModBlocks.AQUANDA_FENCE_GATE.get(),
                    ModBlocks.AQUANDA_TRAPDOOR.get(),
                    ModBlocks.AQUANDA_PRESSURE_PLATE.get(),
                    ModBlocks.AQUANDA_DOOR.get(),
                    ModBlocks.AQUANDA_MOSS_BLOCK.get(),
                    ModBlocks.AQUANDA_MOSS_CARPET.get(),
                    ModItems.AQUANDA_KELP.get(),
                    ModBlocks.MOSSY_AQUANDA_COBBLESTONE.get(),
                    ModBlocks.MOSSY_AQUANDA_COBBLESTONE_SLAB.get(),
                    ModBlocks.MOSSY_AQUANDA_COBBLESTONE_STAIRS.get(),
                    ModBlocks.MOSSY_AQUANDA_COBBLESTONE_WALL.get(),
                    ModBlocks.MOSSY_AQUANDA_STONE_BRICKS.get(),
                    ModBlocks.MOSSY_AQUANDA_STONE_BRICK_SLAB.get(),
                    ModBlocks.MOSSY_AQUANDA_STONE_BRICK_STAIRS.get(),
                    ModBlocks.MOSSY_AQUANDA_STONE_BRICK_WALL.get() // Replace with your actual item registry object
            );

            event.enqueueWork(() -> {
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AZALEA_TRAPDOOR.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AZALEA_DOOR.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AQUANDA_TRAPDOOR.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AQUANDA_DOOR.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AQUANDA_VINES.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AQUANDA_VINES_PLANT.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AQUANDA_KELP.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AQUANDA_KELP_PLANT.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLAZING_VINES.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLAZING_VINES_PLANT.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.COPPER_BAMBOO_SAPLING.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.COPPER_BAMBOO_STALK.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ANCIENT_BAMBOO_SAPLING.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ANCIENT_BAMBOO_STALK.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GOLDEN_BAMBOO_SAPLING.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GOLDEN_BAMBOO_STALK.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.IRON_BAMBOO_SAPLING.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.IRON_BAMBOO_STALK.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PYROLIZED_BAMBOO_SAPLING.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PYROLIZED_BAMBOO_STALK.get(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLAZING_REEDS.get(), RenderType.cutout());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.NETHERRACK_STONES.get(), RenderType.cutout());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.STONES.get(), RenderType.cutout());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DEEPSLATE_STONES.get(), RenderType.cutout());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PYROLIZED_STONES.get(), RenderType.cutout());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BASALT_STONES.get(), RenderType.cutout());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AQUANDA_GEL.get(), RenderType.translucent());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GLOWING_AQUANDA_GEL.get(), RenderType.translucent());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AQUANDA.get(), RenderType.translucent());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GLOWING_AQUANDA.get(), RenderType.translucent());
                        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WARPED_WART.get(), RenderType.cutoutMipped());
                    }
            );
        }
    }
}
