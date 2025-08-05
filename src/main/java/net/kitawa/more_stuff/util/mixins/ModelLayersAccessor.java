package net.kitawa.more_stuff.util.mixins;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ModelLayers.class)
@OnlyIn(Dist.CLIENT)
public interface ModelLayersAccessor {

    @Accessor("ALL_MODELS")
    static Set<ModelLayerLocation> getAllModels() {
        throw new AssertionError("Mixin failed to apply.");
    }
}
