package net.kitawa.more_stuff.util.events;

import net.kitawa.more_stuff.util.mob_armor.layers.IllagerArmorLayer;
import net.kitawa.more_stuff.util.mob_armor.layers.VexArmorLayer;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.VexRenderer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

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
}
