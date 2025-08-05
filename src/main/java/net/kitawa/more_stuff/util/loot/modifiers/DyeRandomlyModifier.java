package net.kitawa.more_stuff.util.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kitawa.more_stuff.util.configs.MoreStuffGeneralConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class DyeRandomlyModifier extends LootModifier {

    public static final MapCodec<DyeRandomlyModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst).apply(inst, DyeRandomlyModifier::new));

    public DyeRandomlyModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext) {RandomSource random = lootContext.getRandom();
        float threshold = (float) (random.nextFloat() * MoreStuffGeneralConfig.CONFIG.applyLootDyeingMultiplier());
        float chance = random.nextFloat();

        if (chance > threshold) {
            return generatedLoot; // Exit early if chance check fails
        }

        for (ItemStack stack : generatedLoot) {
            if (!stack.is(ItemTags.DYEABLE)) {
                continue;
            }

            int r = random.nextInt(MoreStuffGeneralConfig.CONFIG.R());
            int g = random.nextInt(MoreStuffGeneralConfig.CONFIG.G());
            int b = random.nextInt(MoreStuffGeneralConfig.CONFIG.B());
            int color = (r << 16) + (g << 8) + b;

            DyedItemColor dyedColor = new DyedItemColor(color, true);
            stack.set(DataComponents.DYED_COLOR, dyedColor);
        }

        return generatedLoot;
    }


    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}


