package net.kitawa.more_stuff.util.recipes.fletching_table;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class FletchingRecipe implements Recipe<FletchingInput> {

    private final String group;
    private final ItemStack result;
    private final Ingredient tip;
    private final Ingredient shaft;
    private final Ingredient fletching;
    private final Ingredient modifier;

    public FletchingRecipe(String group, Ingredient tip, Ingredient shaft, Ingredient fletching, Ingredient modifier, ItemStack result) {
        this.group = group;
        this.tip = tip;
        this.shaft = shaft;
        this.fletching = fletching;
        this.modifier = modifier == null ? Ingredient.EMPTY : modifier;
        this.result = result;
    }

    @Override
    public boolean matches(FletchingInput input, Level level) {
        boolean tipMatch = tip.test(input.getItem(0));
        boolean shaftMatch = shaft.test(input.getItem(1));
        boolean fletchingMatch = fletching.test(input.getItem(2));

        ItemStack modifierStack = input.getItem(3);

        boolean modifierMatch;
        if (modifier == Ingredient.EMPTY) {
            // Only match if the slot is actually empty
            modifierMatch = modifierStack.isEmpty();
        } else {
            // Modifier is required: must match the input
            modifierMatch = modifier.test(modifierStack);
        }

        return tipMatch && shaftMatch && fletchingMatch && modifierMatch;
    }

    @Override
    public ItemStack assemble(FletchingInput input, HolderLookup.Provider provider) {
        ItemStack baseResult = result.copy();

        // If the modifier is a potion, copy over its potion data
        ItemStack modifierStack = input.getItem(3); // modifier slot
        if (!modifierStack.isEmpty() &&
                (modifierStack.is(Items.POTION) || modifierStack.is(Items.SPLASH_POTION) || modifierStack.is(Items.LINGERING_POTION))) {

            // Only apply if the output is a tipped arrow
            if (baseResult.is(Items.TIPPED_ARROW)) {
                baseResult.set(DataComponents.POTION_CONTENTS, modifierStack.get(DataComponents.POTION_CONTENTS));
            }
        }

        return baseResult;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(tip);
        list.add(shaft);
        list.add(fletching);
        if (!modifier.isEmpty()) list.add(modifier);
        return list;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.FLETCHING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.FLETCHING.get();
    }

    public static class Serializer implements RecipeSerializer<FletchingRecipe> {

        public static final MapCodec<FletchingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(r -> r.group),
                        Ingredient.CODEC_NONEMPTY.fieldOf("tip").forGetter(r -> r.tip),
                        Ingredient.CODEC_NONEMPTY.fieldOf("shaft").forGetter(r -> r.shaft),
                        Ingredient.CODEC_NONEMPTY.fieldOf("fletching").forGetter(r -> r.fletching),
                        Ingredient.CODEC.optionalFieldOf("modifier", Ingredient.EMPTY).forGetter(r -> r.modifier),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result)
                ).apply(builder, FletchingRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, FletchingRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<FletchingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FletchingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static FletchingRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            String group = buf.readUtf();
            Ingredient tip = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            Ingredient shaft = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            Ingredient fletching = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            Ingredient modifier = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
            return new FletchingRecipe(group, tip, shaft, fletching, modifier, result);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, FletchingRecipe recipe) {
            buf.writeUtf(recipe.group);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.tip);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.shaft);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.fletching);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.modifier);
            ItemStack.STREAM_CODEC.encode(buf, recipe.result);
        }
    }
}
