package net.kitawa.more_stuff.blocks;

import net.kitawa.more_stuff.blocks.custom.general.entities.ModdedBrushableBlockRenderer;
import net.kitawa.more_stuff.blocks.custom.general.entities.ModdedSpawnerBlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModBlockEntityRenderers {

    public static void register() {
        BlockEntityRenderers.register(
                ModBlockEntities.BRUSHABLE_BLOCK_NO_FALL.get(),
                ModdedBrushableBlockRenderer::new
        );
        BlockEntityRenderers.register(ModBlockEntities.MOD_SPAWNER.get(), ModdedSpawnerBlockEntityRenderer::new);
    }
}