package net.kitawa.more_stuff.util.mob_armor;

import com.google.common.collect.Sets;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.util.mixins.ModelLayersAccessor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class ModModelLayers {
    public static final Map<ModelLayerLocation, LayerDefinition> LAYERS = new HashMap();
    public static final ModelLayerLocation HOGLIN_ARMOR = register("hoglin_armor", ModLayerDefinitions.HoglinArmor());
    public static final ModelLayerLocation ZOGLIN_ARMOR = register("zoglin_armor", ModLayerDefinitions.HoglinArmor());

    public ModModelLayers() {
    }

    private static ModelLayerLocation register(String name, LayerDefinition layerDefinition) {
        ModelLayerLocation layerLocation = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name), "main");
        ModelLayersAccessor.getAllModels().add(layerLocation);
        LAYERS.put(layerLocation, layerDefinition);
        return layerLocation;
    }
}
