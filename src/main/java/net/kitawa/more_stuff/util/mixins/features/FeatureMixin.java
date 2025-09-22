package net.kitawa.more_stuff.util.mixins.features;

import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Feature.class)
public abstract class FeatureMixin<FC extends FeatureConfiguration> {
    /**
     * @author
     * KitawaDesu
     * @reason
     * To make my custom blocktag work
     */
    @Overwrite
    public static boolean isDirt(BlockState state) {
        return state.is(ModBlockTags.CAN_BE_ALTERED_GROUND);
    }
}
