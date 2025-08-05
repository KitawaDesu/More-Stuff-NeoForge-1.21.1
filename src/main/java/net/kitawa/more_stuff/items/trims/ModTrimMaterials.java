package net.kitawa.more_stuff.items.trims;


import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.items.util.ModArmorMaterials;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.util.Map;

public class ModTrimMaterials {
    public static final ResourceKey<TrimMaterial> ROSARITE =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "rosarite"));
    public static final ResourceKey<TrimMaterial> ROSE_GOLD =
            ResourceKey.create(Registries.TRIM_MATERIAL, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "rose_gold"));

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, ROSARITE, ModItems.ROSARITE_INGOT.get(), Style.EMPTY.withColor(TextColor.parseColor("#f06e94").getOrThrow()), 0.75F,Map.of(ModArmorMaterials.ROSARITE, "rosarite_darker"));
        register(context, ROSE_GOLD, ModItems.ROSE_GOLD_INGOT.get(), Style.EMPTY.withColor(TextColor.parseColor("#f06e94").getOrThrow()), 0.85F,Map.of(ModArmorMaterials.ROSE_GOLDEN, "rose_gold_darker"));
    }

    private static <V, K> void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> trimKey, Item item, Style style, float itemModelIndex, Map<Holder<ArmorMaterial>, String>  overrideArmorMaterials) {
        TrimMaterial trimmaterial = TrimMaterial.create(
                trimKey.location().getPath(),
                item,
                itemModelIndex,
                Component.translatable(Util.makeDescriptionId("trim_material", trimKey.location())).withStyle(style),
                overrideArmorMaterials
        );
        context.register(trimKey, trimmaterial);
    }
}
