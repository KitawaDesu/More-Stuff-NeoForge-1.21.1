package net.kitawa.more_stuff.util.events;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.enchantments.ModEnchantments;
import net.kitawa.more_stuff.entities.monster.ColoredSlime;
import net.kitawa.more_stuff.entities.util.ModAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobSplitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import java.util.*;

@EventBusSubscriber(modid = MoreStuff.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class MoreStuffGameEvents {

    @SubscribeEvent
    public static void onSlimeSplit(MobSplitEvent event) {
        Mob parent = event.getParent();
        Collection<MobEffectInstance> effects = parent.getActiveEffects();

        for (Mob child : event.getChildren()) {
            // Apply potion effects to all slimes
            if (!effects.isEmpty()) {
                for (MobEffectInstance effect : effects) {
                    child.addEffect(new MobEffectInstance(
                            effect.getEffect(),
                            effect.getDuration(),
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible(),
                            effect.showIcon()
                    ));
                }
            }

            // If parent is a ColoredSlime, copy the color info
            if (parent instanceof ColoredSlime && child instanceof ColoredSlime) {
                ((ColoredSlime) child).copyFrom((ColoredSlime) parent);
            }
            // If you want other custom data for non-colored slimes, handle it here
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();

        Optional<BlockPos> posOpt = event.getPosition();
        if (posOpt.isEmpty()) return;
        BlockPos pos = posOpt.get();

        float hardness = event.getState().getDestroySpeed(player.level(), pos);
        if (hardness < 50f) return;

        double level = player.getAttributeValue(ModAttributes.OBSIDIAN_BREAK_SPEED);
        if (level <= 0) return;

        float toughBreakerBonus = (float)(level * level + 1);

        // Efficiency attribute
        double efficiencyLevel = player.getAttributeValue(Attributes.MINING_EFFICIENCY);
        float efficiencyBonus = efficiencyLevel > 0 ? (float)(efficiencyLevel) : 0f;

        // Haste effect (each level adds 20% speed)
        float hasteMultiplier = 1.0f;
        if (player.hasEffect(MobEffects.DIG_SPEED)) {
            int hasteLevel = player.getEffect(MobEffects.DIG_SPEED).getAmplifier() + 1;
            hasteMultiplier = 1.0f + (0.2f * hasteLevel);
        }

        // Set speed = (original tool speed + both bonuses) * haste
        event.setNewSpeed((event.getOriginalSpeed() + toughBreakerBonus + efficiencyBonus) * hasteMultiplier);
    }

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        Player player = (Player) event.getBreaker();
        if (player == null) return;

        ItemStack tool = player.getMainHandItem();
        var registry = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        boolean hasSmeltingTouch = tool.getEnchantmentLevel(
                registry.getOrThrow(ModEnchantments.SMELTING_TOUCH)
        ) > 0;

        if (!hasSmeltingTouch) return;

        int fortuneLevel = tool.getEnchantmentLevel(registry.getOrThrow(Enchantments.FORTUNE));

        BlockState state = event.getState();
        Level level = player.level();
        BlockPos pos = event.getPos();

        // ── Ancient debris special case ───────────────────────────────────────
        if (state.is(Blocks.ANCIENT_DEBRIS)) {
            event.getDrops().clear();

            int count = 1;
            if (fortuneLevel > 0) {
                RandomSource random = player.getRandom();
                for (int i = 0; i < fortuneLevel; i++) {
                    if (random.nextFloat() < 0.33f) count++;
                }
            }

            event.getDrops().add(new ItemEntity(
                    level,
                    pos.getX(), pos.getY(), pos.getZ(),
                    new ItemStack(Items.NETHERITE_SCRAP, count)
            ));

            spawnSmeltingXp(level, pos, 1.0f * count);
            return;
        }

        // ── Normal smelting touch ─────────────────────────────────────────────
        List<ItemEntity> originalDrops = new ArrayList<>(event.getDrops());
        event.getDrops().clear();

        float totalXp = 0f;

        for (ItemEntity itemEntity : originalDrops) {
            ItemStack stack = itemEntity.getItem();
            SmeltingResult result = getSmeltedResult(level, stack);

            if (!result.isEmpty()) {
                ItemStack smeltedStack = result.stack().copy();
                smeltedStack.setCount(stack.getCount());
                event.getDrops().add(new ItemEntity(
                        level,
                        itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
                        smeltedStack
                ));

                float xp = result.xpPerItem() * stack.getCount();

                // Only multiply XP if both smelting touch AND fortune are active
                if (fortuneLevel > 0) {
                    xp *= 1.0f + (fortuneLevel * 0.75f);
                }

                totalXp += xp;
            } else {
                event.getDrops().add(itemEntity);
            }
        }

        if (totalXp > 0f) {
            spawnSmeltingXp(level, pos, totalXp);
        }
    }

    /**
     * Spawns XP orbs matching the smelting recipe's experience value.
     * Fractional XP (e.g. 0.1 per iron ore) is handled the same way
     * vanilla furnaces handle it — accumulated and rolled probabilistically.
     */
    private static void spawnSmeltingXp(Level level, BlockPos pos, float xp) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        int fullOrbs = (int) xp;
        float remainder = xp - fullOrbs;

        // Roll for the fractional part — e.g. 0.7 = 70% chance of 1 extra orb
        if (remainder > 0f && level.random.nextFloat() < remainder) {
            fullOrbs++;
        }

        if (fullOrbs > 0) {
            ExperienceOrb.award(serverLevel, Vec3.atCenterOf(pos), fullOrbs);
        }
    }

    private record SmeltingResult(ItemStack stack, float xpPerItem) {
        static final SmeltingResult EMPTY = new SmeltingResult(ItemStack.EMPTY, 0f);
        boolean isEmpty() { return stack.isEmpty(); }
    }

    private static SmeltingResult getSmeltedResult(Level level, ItemStack input) {
        if (!(level instanceof ServerLevel serverLevel)) return SmeltingResult.EMPTY;

        return serverLevel.getRecipeManager()
                .getRecipeFor(
                        RecipeType.SMELTING,
                        new SingleRecipeInput(input),
                        serverLevel
                )
                .map(r -> new SmeltingResult(
                        r.value().getResultItem(serverLevel.registryAccess()).copy(),
                        r.value().getExperience()
                ))
                .orElse(SmeltingResult.EMPTY);
    }
}
