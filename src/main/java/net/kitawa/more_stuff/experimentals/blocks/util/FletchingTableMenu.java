package net.kitawa.more_stuff.experimentals.blocks.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.kitawa.more_stuff.experimentals.items.ExperimentalCombatItems;
import net.kitawa.more_stuff.items.util.weapons.dynamarrow.ArrowDataComponents;
import net.kitawa.more_stuff.items.util.weapons.dynamarrow.ArrowIngredientDataLoader;
import net.kitawa.more_stuff.items.util.weapons.dynamarrow.DynamicArrowItem;
import net.kitawa.more_stuff.util.recipes.fletching_table.FletchingInput;
import net.kitawa.more_stuff.util.recipes.fletching_table.FletchingRecipe;
import net.kitawa.more_stuff.util.recipes.fletching_table.ModRecipeTypes;
import net.kitawa.more_stuff.util.screen.ModMenus;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// ─── FletchingTableMenu ───
public class FletchingTableMenu extends AbstractContainerMenu {

    // ─── Slot indices ───
    public static final int TIP_SLOT = 0;
    public static final int SHAFT_SLOT = 1;
    public static final int FLETCHING_SLOT = 2;
    public static final int MODIFIER_SLOT = 3;
    public static final int ARROW_SLOT = 4;
    public static final int RESULT_SLOT = 5; // output slot in GUI (after inputs)

    private static final int SPECTRAL_DURATION = 200; // ticks

    private final ContainerLevelAccess access;
    private final Player player;

    // ─── Input container (5 inputs now) ───
    public final Container container = new SimpleContainer(5) {
        @Override
        public void setChanged() {
            FletchingTableMenu.this.slotsChanged(this);
            super.setChanged();
        }
    };

    // ─── Result container ───
    public final ResultContainer resultContainer = new ResultContainer();

    public FletchingTableMenu(int id, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenus.FLETCHING_TABLE.get(), id);
        this.access = access;
        this.player = playerInventory.player;

        // ─── Input slots (0-4) ───
        this.addSlot(new TypeRestrictedSlot(this, container, TIP_SLOT, 87, 57, "more_stuff:tip"));
        this.addSlot(new TypeRestrictedSlot(this, container, SHAFT_SLOT, 65, 57, "more_stuff:shaft"));
        this.addSlot(new TypeRestrictedSlot(this, container, FLETCHING_SLOT, 43, 57, "more_stuff:fletching"));
        this.addSlot(new TypeRestrictedSlot(this, container, MODIFIER_SLOT, 145, 17, "more_stuff:modifier"));

        // Arrow slot (now read-only, not placeable)
        this.addSlot(new ReadOnlySlot(container, ARROW_SLOT, 65, 17));

        // ─── Output slot ───
        this.addSlot(new OutputSlot(container, resultContainer, 0, 145, 57));

        // ─── Player inventory ───
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 9; c++)
                this.addSlot(new Slot(playerInventory, c + r * 9 + 9, 8 + c * 18, 84 + r * 18));
        for (int c = 0; c < 9; c++)
            this.addSlot(new Slot(playerInventory, c, 8 + c * 18, 142));
    }

    @Override
    public void slotsChanged(Container inventory) {
        this.access.execute((level, pos) -> {
            if (!level.isClientSide) updateResult(level);
        });
    }

    private void updateResult(Level level) {
        ItemStack tip = container.getItem(TIP_SLOT);
        ItemStack shaft = container.getItem(SHAFT_SLOT);
        ItemStack fletching = container.getItem(FLETCHING_SLOT);
        ItemStack modifier = container.getItem(MODIFIER_SLOT);
        ItemStack arrowInSlot = container.getItem(ARROW_SLOT);

        ItemStack result = ItemStack.EMPTY;

        // --- Special case: arrow + modifier upgrade ---
        if (!arrowInSlot.isEmpty() && !modifier.isEmpty()) {
            result = upgradeArrowWithModifier(arrowInSlot, modifier);
            if (!result.isEmpty()) {
                resultContainer.setItem(0, result);
                broadcastChanges();
                return;
            }
        }

        // --- Default fletching path ---
        if (!tip.isEmpty() && !shaft.isEmpty() && !fletching.isEmpty()) {
            FletchingInput input = new FletchingInput(container);
            Optional<FletchingRecipe> recipeOpt = level.getRecipeManager()
                    .getRecipeFor(ModRecipeTypes.FLETCHING.get(), input, level)
                    .map(RecipeHolder::value);

            if (recipeOpt.isPresent()) {
                result = recipeOpt.get().assemble(input, level.registryAccess());
            } else {
                result = assembleDefaultArrow(tip, shaft, fletching, modifier, arrowInSlot);
            }
        }

        resultContainer.setItem(0, result);
        broadcastChanges();
    }

    // --- Helper: upgrade existing arrow with a modifier ---
    private ItemStack upgradeArrowWithModifier(ItemStack arrow, ItemStack modifier) {
        ArrowIngredientDataLoader.ArrowIngredientData modData = ArrowIngredientDataLoader.get(modifier.getItem());
        boolean isJsonModifier = modData != null && "more_stuff:modifier".equals(modData.getType());
        boolean isPotion = isPotionItem(modifier);

        if (!isJsonModifier && !isPotion) return ItemStack.EMPTY;

        int xpCost = modData != null ? modData.getXpCost() : 0;
        if (xpCost > 0 && player.experienceLevel < xpCost) return ItemStack.EMPTY;

        ItemStack upgraded = new ItemStack(ExperimentalCombatItems.DYNAMIC_ARROW.get());
        applyDefaultComponentsToResult(upgraded);
        copyArrowBase(arrow, upgraded);

        // === Apply JSON modifier data ===
        if (isJsonModifier && modData != null) {
            JsonObject components = modData.getComponentsAsJson();

            // Merge and apply regular components
            JsonObject merged = new JsonObject();
            mergeComponentData(merged, modifier);
            applyMergedComponents(upgraded, merged);

            // 💧 Handle potion contents inside JSON modifiers
            if (components.has("minecraft:potion_contents") && upgraded.getItem() instanceof DynamicArrowItem) {
                JsonObject potionObj = components.getAsJsonObject("minecraft:potion_contents");
                PotionContents potionContents = parsePotionContents(potionObj);
                upgraded.set(DataComponents.POTION_CONTENTS, potionContents);
                upgraded.set(ArrowDataComponents.HAS_POTION.get(), true);
            }
        }

        // === Handle direct potion items (regular potions used as modifiers) ===
        if (isPotion) {
            PotionContents contents = modifier.get(DataComponents.POTION_CONTENTS);
            if (contents != null) {
                upgraded.set(DataComponents.POTION_CONTENTS, contents);
                upgraded.set(ArrowDataComponents.HAS_POTION.get(), true);
            }
        }

        // === Handle XP cost ===
        if (xpCost > 0) {
            int currentXp = Optional.ofNullable(upgraded.get(ArrowDataComponents.XP_LEVEL_COST.get())).orElse(0);
            upgraded.set(ArrowDataComponents.XP_LEVEL_COST.get(), currentXp + xpCost);
        }

        return upgraded;
    }

    private ItemStack assembleDefaultArrow(ItemStack tip, ItemStack shaft, ItemStack fletching, ItemStack modifier, ItemStack baseArrow) {
        ItemStack result = new ItemStack(ExperimentalCombatItems.DYNAMIC_ARROW.get(), 8);
        applyDefaultComponentsToResult(result);
        copyArrowBase(baseArrow, result);

        JsonObject merged = new JsonObject();
        mergeComponentData(merged, tip);
        mergeComponentData(merged, shaft);
        mergeComponentData(merged, fletching);
        mergeComponentData(merged, modifier);
        applyMergedComponents(result, merged);

        if (modifier != null && !modifier.isEmpty()) {
            ArrowIngredientDataLoader.ArrowIngredientData modData = ArrowIngredientDataLoader.get(modifier.getItem());
            boolean isPotion = isPotionItem(modifier);

            if (isPotion) {
                PotionContents contents = modifier.get(DataComponents.POTION_CONTENTS);
                if (contents != null && result.getItem() instanceof DynamicArrowItem) {
                    result.set(DataComponents.POTION_CONTENTS, contents);
                    result.set(ArrowDataComponents.HAS_POTION.get(), true);
                }
            }

            if (modData != null) {
                JsonObject components = modData.getComponentsAsJson();
                if (components.has("minecraft:potion_contents") && result.getItem() instanceof DynamicArrowItem) {
                    JsonObject potionObj = components.getAsJsonObject("minecraft:potion_contents");
                    PotionContents potionContents = parsePotionContents(potionObj);
                    result.set(DataComponents.POTION_CONTENTS, potionContents);
                    result.set(ArrowDataComponents.HAS_POTION.get(), true);
                }
            }
        }

        return result;
    }

    private void copyArrowBase(ItemStack from, ItemStack to) {
        if (from.getItem() instanceof DynamicArrowItem) {
            copyDynamicArrowData(from, to);
        } else if (from.is(Items.TIPPED_ARROW)) {
            to.set(ArrowDataComponents.HAS_POTION.get(), true);
            to.set(DataComponents.POTION_CONTENTS, from.get(DataComponents.POTION_CONTENTS));
        } else if (from.is(Items.SPECTRAL_ARROW)) {
            to.set(ArrowDataComponents.HAS_POTION.get(), true);
            to.set(DataComponents.POTION_CONTENTS, createGlowingPotionContents());
        }
    }

    private void applyMergedComponents(ItemStack arrow, JsonObject merged) {
        for (var entry : merged.entrySet()) {
            String key = entry.getKey();
            JsonElement val = entry.getValue();
            String namespacedKey = key.contains(":") ? key : "more_stuff:" + key;

            // ─── Special handling for potion_contents ───
            if (namespacedKey.equals("minecraft:potion_contents") && val.isJsonObject()) {
                JsonObject potionObj = val.getAsJsonObject();
                PotionContents contents = parsePotionContents(potionObj);
                arrow.set(DataComponents.POTION_CONTENTS, contents);
                arrow.set(ArrowDataComponents.HAS_POTION.get(), true);
                continue;
            }

            if (!val.isJsonPrimitive()) continue;
            JsonPrimitive prim = val.getAsJsonPrimitive();

            switch (namespacedKey) {
                case "more_stuff:arrow_gravity" -> arrow.set(ArrowDataComponents.GRAVITY.get(), prim.getAsDouble());
                case "more_stuff:arrow_base_damage" -> arrow.set(ArrowDataComponents.BASE_DAMAGE.get(), prim.getAsDouble());
                case "more_stuff:arrow_damage_multiplier" -> arrow.set(ArrowDataComponents.DAMAGE_MULTIPLIER.get(), prim.getAsDouble());
                case "more_stuff:arrow_water_inertia" -> arrow.set(ArrowDataComponents.WATER_INERTIA.get(), prim.getAsFloat());
                case "more_stuff:arrow_crit_chance" -> arrow.set(ArrowDataComponents.CRIT.get(), prim.getAsFloat());
                case "more_stuff:arrow_no_physics" -> arrow.set(ArrowDataComponents.NO_PHYSICS.get(), prim.getAsBoolean());
                case "more_stuff:arrow_on_fire" -> arrow.set(ArrowDataComponents.ON_FIRE.get(), prim.getAsBoolean());
                case "more_stuff:arrow_is_explosive" -> arrow.set(ArrowDataComponents.EXPLOSIVE.get(), prim.getAsBoolean());
                case "more_stuff:arrow_pierce_level" -> arrow.set(ArrowDataComponents.PIERCE_LEVEL.get(), prim.getAsInt());
                case "more_stuff:arrow_sound_event" -> arrow.set(ArrowDataComponents.SOUND_EVENT.get(), ResourceLocation.tryParse(prim.getAsString()));
                case "more_stuff:arrow_tip" -> arrow.set(ArrowDataComponents.TIP.get(), prim.getAsString());
                case "more_stuff:arrow_shaft" -> arrow.set(ArrowDataComponents.SHAFT.get(), prim.getAsString());
                case "more_stuff:arrow_fletching" -> arrow.set(ArrowDataComponents.FLETCHING.get(), prim.getAsString());
                case "more_stuff:arrow_xp_level_cost" -> arrow.set(ArrowDataComponents.XP_LEVEL_COST.get(), prim.getAsInt());
                case "more_stuff:arrow_modifier" -> arrow.set(ArrowDataComponents.MODIFIER.get(), ResourceLocation.tryParse(prim.getAsString()));
            }

            Float crit = arrow.get(ArrowDataComponents.CRIT.get());

// If no crit value is defined, default to 0.25f
            if (crit == null) {
                crit = 0.25f;
            } else {
                // Clamp to [0.0f, 1.0f]
                crit = Math.min(Math.max(crit, 0.0f), 1.0f);
            }

            arrow.set(ArrowDataComponents.CRIT.get(), crit);
        }
    }

    private static PotionContents parsePotionContents(JsonObject potionObj) {
        Optional<Holder<Potion>> potionHolder = Optional.empty();
        Optional<Integer> customColor = Optional.empty();
        List<MobEffectInstance> effects = new ArrayList<>();

        // Resolve potion type
        if (potionObj.has("potion")) {
            ResourceLocation potionId = ResourceLocation.tryParse(potionObj.get("potion").getAsString());
            if (potionId != null) {
                potionHolder = BuiltInRegistries.POTION.getHolder(potionId).map(h -> (Holder<Potion>) h);
            }
        }

        // Custom color
        if (potionObj.has("custom_color")) {
            customColor = Optional.of(potionObj.get("custom_color").getAsInt());
        }

        // Custom effects
        if (potionObj.has("custom_effects")) {
            for (JsonElement e : potionObj.getAsJsonArray("custom_effects")) {
                if (!e.isJsonObject()) continue;
                JsonObject o = e.getAsJsonObject();

                ResourceLocation id = ResourceLocation.tryParse(o.get("id").getAsString());
                int duration = o.has("duration") ? o.get("duration").getAsInt() : 200;
                int amp = o.has("amplifier") ? o.get("amplifier").getAsInt() : 0;
                boolean ambient = o.has("ambient") && o.get("ambient").getAsBoolean();
                boolean showParticles = !o.has("show_particles") || o.get("show_particles").getAsBoolean();
                boolean showIcon = !o.has("show_icon") || o.get("show_icon").getAsBoolean();

                // Find effect
                Holder<MobEffect> effectHolder = BuiltInRegistries.MOB_EFFECT.getHolder(id).orElse(null);
                if (effectHolder != null) {
                    effects.add(new MobEffectInstance(effectHolder, duration, amp, ambient, showParticles, showIcon));
                }
            }
        }

        // ✅ Fixed constructor call
        return new PotionContents(potionHolder, customColor, effects);
    }



    private void mergeComponentData(JsonObject merged, ItemStack stack) {
        if (stack.isEmpty()) return;
        ArrowIngredientDataLoader.ArrowIngredientData data = ArrowIngredientDataLoader.get(stack.getItem());
        if (data == null) return;

        JsonObject obj = data.getComponentsAsJson();
        for (var entry : obj.entrySet()) {
            String key = entry.getKey();
            JsonElement val = entry.getValue();
            if (!val.isJsonPrimitive()) continue;
            JsonPrimitive prim = val.getAsJsonPrimitive();

            if (prim.isNumber()) {
                double current = merged.has(key) ? merged.get(key).getAsDouble() : 0;
                double added = prim.getAsDouble();
                merged.addProperty(key, current + added);
            } else if (prim.isBoolean()) {
                boolean current = merged.has(key) && merged.get(key).getAsBoolean();
                merged.addProperty(key, current || prim.getAsBoolean());
            } else if (prim.isString() && !merged.has(key)) {
                merged.addProperty(key, prim.getAsString());
            }
        }

        if (data.getXpCost() > 0) {
            double current = merged.has("more_stuff:xp_level_cost") ? merged.get("more_stuff:xp_level_cost").getAsDouble() : 0;
            merged.addProperty("more_stuff:xp_level_cost", current + data.getXpCost());
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    private void applyDefaultComponentsToResult(ItemStack result) {
        result.set(ArrowDataComponents.GRAVITY.get(), 0.05);
        result.set(ArrowDataComponents.DAMAGE_MULTIPLIER.get(), 1.0);
        result.set(ArrowDataComponents.BASE_DAMAGE.get(), 2.0);
        result.set(ArrowDataComponents.CRIT.get(), 0.25f);
        result.set(ArrowDataComponents.NO_PHYSICS.get(), false);
        result.set(ArrowDataComponents.WATER_INERTIA.get(), 0.6f);
        result.set(ArrowDataComponents.PIERCE_LEVEL.get(), 0);
        result.set(ArrowDataComponents.HAS_POTION.get(), false);
        result.set(ArrowDataComponents.ON_FIRE.get(), false);
        result.set(ArrowDataComponents.EXPLOSIVE.get(), false);
        result.set(ArrowDataComponents.TIP.get(), "");
        result.set(ArrowDataComponents.SHAFT.get(), "");
        result.set(ArrowDataComponents.FLETCHING.get(), "");
    }

    private void copyDynamicArrowData(ItemStack from, ItemStack to) {
        Boolean hasPotion = from.get(ArrowDataComponents.HAS_POTION.get());
        to.set(ArrowDataComponents.HAS_POTION.get(), hasPotion != null && hasPotion);
        if (hasPotion != null && hasPotion) {
            var potionContents = from.get(DataComponents.POTION_CONTENTS);
            to.set(DataComponents.POTION_CONTENTS, potionContents);
        }

        Double gm = from.get(ArrowDataComponents.GRAVITY.get());
        if (gm != null) to.set(ArrowDataComponents.GRAVITY.get(), gm);

        Double dm = from.get(ArrowDataComponents.DAMAGE_MULTIPLIER.get());
        if (dm != null) to.set(ArrowDataComponents.DAMAGE_MULTIPLIER.get(), dm);

        Double bd = from.get(ArrowDataComponents.BASE_DAMAGE.get());
        if (bd != null) to.set(ArrowDataComponents.BASE_DAMAGE.get(), bd);

        Float wi = from.get(ArrowDataComponents.WATER_INERTIA.get());
        if (wi != null) to.set(ArrowDataComponents.WATER_INERTIA.get(), wi);

        Float crit = from.get(ArrowDataComponents.CRIT.get());
        if (crit != null) to.set(ArrowDataComponents.CRIT.get(), 0.25f);

        Boolean np = from.get(ArrowDataComponents.NO_PHYSICS.get());
        if (np != null && np) to.set(ArrowDataComponents.NO_PHYSICS.get(), true);

        Integer pierce = from.get(ArrowDataComponents.PIERCE_LEVEL.get());
        if (pierce != null) to.set(ArrowDataComponents.PIERCE_LEVEL.get(), pierce);

        Boolean explosive = from.get(ArrowDataComponents.EXPLOSIVE.get());
        if (explosive != null && explosive) to.set(ArrowDataComponents.EXPLOSIVE.get(), true);

        Boolean onFire = from.get(ArrowDataComponents.ON_FIRE.get());
        if (onFire != null) to.set(ArrowDataComponents.ON_FIRE.get(), onFire);

        String tip = from.get(ArrowDataComponents.TIP.get());
        if (tip != null) to.set(ArrowDataComponents.TIP.get(), tip);
        String shaft = from.get(ArrowDataComponents.SHAFT.get());
        if (shaft != null) to.set(ArrowDataComponents.SHAFT.get(), shaft);
        String fletching = from.get(ArrowDataComponents.FLETCHING.get());
        if (fletching != null) to.set(ArrowDataComponents.FLETCHING.get(), fletching);

        ResourceLocation sound = from.get(ArrowDataComponents.SOUND_EVENT.get());
        if (sound != null) to.set(ArrowDataComponents.SOUND_EVENT.get(), sound);
    }

    private PotionContents createGlowingPotionContents() {
        MobEffectInstance glow = new MobEffectInstance(MobEffects.GLOWING, SPECTRAL_DURATION, 0);
        return new PotionContents(Optional.empty(), Optional.empty(), List.of(glow));
    }

    // ─── Read-only slot ───
    public static class ReadOnlySlot extends Slot {
        public ReadOnlySlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }
    }

    // ─── Slot classes ───
    public static class TypeRestrictedSlot extends Slot {
        public final String requiredType;
        private final FletchingTableMenu menu;

        public TypeRestrictedSlot(FletchingTableMenu menu, Container container, int index, int x, int y, String requiredType) {
            super(container, index, x, y);
            this.menu = menu;
            this.requiredType = requiredType;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            if (stack.isEmpty()) return false;

            // Check arrow slot presence (lock other crafting slots if arrow present)
            ItemStack arrowSlotStack = menu.container.getItem(FletchingTableMenu.ARROW_SLOT);
            boolean arrowPresent = !arrowSlotStack.isEmpty();

            if (arrowPresent) {
                // If an arrow exists, lock tip/shaft/fletching (but allow modifier and arrow slot itself)
                if (this.getSlotIndex() != FletchingTableMenu.MODIFIER_SLOT
                        && this.getSlotIndex() != FletchingTableMenu.ARROW_SLOT
                        && this.getSlotIndex() != FletchingTableMenu.RESULT_SLOT) {
                    return false;
                }
            }

            // ─── Modifier slot ───
            if (this.getSlotIndex() == FletchingTableMenu.MODIFIER_SLOT) {
                // Allow any potion
                if (stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION)) {
                    return true;
                }

                // Allow JSON-defined modifiers (tag-aware)
                ArrowIngredientDataLoader.ArrowIngredientData data = ArrowIngredientDataLoader.get(stack.getItem());
                return data != null && "more_stuff:modifier".equals(data.getType());
            }

            // ─── Arrow slot ───
            if ("arrow".equals(requiredType)) {
                return stack.is(Items.ARROW)
                        || stack.is(Items.TIPPED_ARROW)
                        || stack.is(Items.SPECTRAL_ARROW)
                        || stack.getItem() instanceof DynamicArrowItem;
            }

            // ─── Tip / Shaft / Fletching slots ───
            ArrowIngredientDataLoader.ArrowIngredientData data = ArrowIngredientDataLoader.get(stack.getItem());
            return data != null && requiredType.equals(data.getType());
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        newStack = stack.copy();

        // Determine if an arrow is present in arrow slot to lock behavior
        ItemStack arrowSlot = container.getItem(ARROW_SLOT);
        boolean arrowPresent = !arrowSlot.isEmpty();

        // --- Handle result slot (shift-click crafting) ---
        if (index == RESULT_SLOT) {
            int playerStart = RESULT_SLOT + 1;
            if (!this.moveItemStackTo(stack, playerStart, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }

            // Consume inputs via OutputSlot’s logic
            slot.onTake(player, stack);
            slot.onQuickCraft(stack, newStack);
            return newStack;
        }

        // --- Handle player inventory shift-clicks (move into inputs) ---
        int playerStart = RESULT_SLOT + 1;
        if (index >= playerStart) {
            boolean moved = false;

            if (arrowPresent) {
                // If arrow present, only allow shift into modifier slot
                Slot modSlot = this.slots.get(MODIFIER_SLOT);
                if (modSlot.mayPlace(stack) && !modSlot.hasItem()) {
                    moved = this.moveItemStackTo(stack, MODIFIER_SLOT, MODIFIER_SLOT + 1, false);
                }
            } else {
                // Normal behavior: try to place item into one of the input slots (0..ARROW_SLOT)
                for (int i = 0; i <= ARROW_SLOT; i++) {
                    Slot inputSlot = this.slots.get(i);
                    if (inputSlot.mayPlace(stack) && !inputSlot.hasItem()) {
                        moved = this.moveItemStackTo(stack, i, i + 1, false);
                        if (moved) break;
                    }
                }
            }

            if (!moved) return ItemStack.EMPTY;
        }
        // --- Handle input slots shift-clicked out into player inventory ---
        else {
            if (!this.moveItemStackTo(stack, playerStart, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        }

        // --- Update slot state ---
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return newStack;
    }

    private static class OutputSlot extends Slot {
        private final Container inputContainer;

        public OutputSlot(Container inputContainer, Container resultContainer, int index, int x, int y) {
            super(resultContainer, index, x, y);
            this.inputContainer = inputContainer;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            ItemStack arrowStack = inputContainer.getItem(ARROW_SLOT);
            ItemStack modifierStack = inputContainer.getItem(MODIFIER_SLOT);

            boolean hasEnoughXP = true;

            // --- Handle XP deduction when arrow + modifier are both present ---
            if (!arrowStack.isEmpty() && !modifierStack.isEmpty()) {
                ArrowIngredientDataLoader.ArrowIngredientData modData = ArrowIngredientDataLoader.get(modifierStack.getItem());
                int xpCost = modData != null ? modData.getXpCost() : 0;
                if (xpCost > 0) {
                    if (player.experienceLevel >= xpCost) {
                        player.giveExperienceLevels(-xpCost);
                    } else {
                        hasEnoughXP = false; // player can't pay XP
                    }
                }
            }

            // --- Consume 1 item from each non-empty input slot only ---
            for (int i = 0; i < inputContainer.getContainerSize(); i++) {
                if (i == RESULT_SLOT) continue;

                ItemStack input = inputContainer.getItem(i);
                if (!input.isEmpty()) {
                    // Only shrink modifier if player had enough XP
                    if (i == MODIFIER_SLOT && !hasEnoughXP) continue;

                    input.shrink(1);
                    if (input.getCount() <= 0) {
                        inputContainer.setItem(i, ItemStack.EMPTY);
                    }
                }
            }

            inputContainer.setChanged();
            super.onTake(player, stack);
        }
    }

    private static boolean isPotionItem(ItemStack stack) {
        return stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, pos) -> {
            // Return all 5 input slots to player inventory
            for (int i = 0; i < 5; i++) {
                player.getInventory().placeItemBackInInventory(container.removeItemNoUpdate(i));
            }
        });
        resultContainer.removeItemNoUpdate(0);
    }
}