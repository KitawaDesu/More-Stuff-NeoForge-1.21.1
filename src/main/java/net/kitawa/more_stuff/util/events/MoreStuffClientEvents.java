package net.kitawa.more_stuff.util.events;

import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.util.mob_armor.layers.IllagerArmorLayer;
import net.kitawa.more_stuff.util.mob_armor.layers.VexArmorLayer;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.VexRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(
        bus = EventBusSubscriber.Bus.MOD,
        value = {Dist.CLIENT}
)
public class MoreStuffClientEvents {
    @SubscribeEvent
    public static void addLayer(EntityRenderersEvent.AddLayers event) {
        VexRenderer renderer = (VexRenderer)event.getRenderer(EntityType.VEX);
        assert renderer != null;
        renderer.addLayer(new VexArmorLayer(renderer, event.getEntityModels(), event.getContext().getModelManager()));
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            List<Item> customShields = new ArrayList<>(List.of(
                    ModItems.STONE_SHIELD.get(),
                    ModItems.LAPIS_SHIELD.get(),
                    ModItems.QUARTZ_SHIELD.get(),
                    ModItems.COPPER_SHIELD.get(),
                    ModItems.GOLDEN_SHIELD.get(),
                    ModItems.ROSE_GOLDEN_SHIELD.get(),
                    ModItems.IRON_SHIELD.get(),
                    ModItems.DIAMOND_SHIELD.get(),
                    ModItems.EMERALD_SHIELD.get(),
                    ModItems.NETHERITE_SHIELD.get(),
                    ModItems.ROSARITE_SHIELD.get()
            ));

            if (ModList.get().isLoaded("create")) {
                customShields.add(CreateCompatItems.ZINC_SHIELD.get());
                customShields.add(CreateCompatItems.BRASS_SHIELD.get());
            }

            ResourceLocation blockingId = ResourceLocation.withDefaultNamespace("blocking");

            for (Item item : customShields) {
                ItemProperties.register(item, blockingId, (stack, level, entity, seed) -> {
                    return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
                });
            }
        });

        event.enqueueWork(() -> {
            List<Item> elytras = new ArrayList<>(List.of(
                    ModItems.LEATHER_GLIDER.get(),
                    ModItems.CHAINED_ELYTRA.get(),
                    ModItems.GOLDEN_ELYTRA.get(),
                    ModItems.IRON_ELYTRA.get(),
                    ModItems.COPPER_ELYTRA.get(),
                    ModItems.ROSE_GOLDEN_ELYTRA.get(),
                    ModItems.DIAMOND_ELYTRA.get(),
                    ModItems.EMERALD_ELYTRA.get(),
                    ModItems.NETHERITE_ELYTRA.get(),
                    ModItems.ROSARITE_ELYTRA.get()
            ));

            if (ModList.get().isLoaded("create")) {
                elytras.add(CreateCompatItems.ZINC_ELYTRA.get());
                elytras.add(CreateCompatItems.BRASS_ELYTRA.get());
            }

            ResourceLocation brokenId = ResourceLocation.withDefaultNamespace("broken");

            for (Item item : elytras) {
                ItemProperties.register(item, brokenId, (stack, level, entity, seed) -> {
                    return ElytraItem.isFlyEnabled(stack) ? 0.0F : 1.0F;
                });
            }
        });
    }
}
