package net.kitawa.more_stuff.util.events;

import com.google.common.collect.Lists;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.items.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@EventBusSubscriber(modid = MoreStuff.MOD_ID)
public class ModVillagerTrades {

    private static Item getMetalArmor(Item copperItem, Item zincItem) {
        // If Create mod is loaded, 50/50 chance to use zinc instead of copper
        if (ModList.get().isLoaded("create")) {
            return Math.random() < 0.5 ? zincItem : copperItem;
        }
        return copperItem;
    }

    private static DyeItem getRandomDye(RandomSource random) {
        return DyeItem.byColor(DyeColor.byId(random.nextInt(16)));
    }

    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.LEATHERWORKER) {
            event.getTrades().get(5).add((trader, random) -> {
                ItemStack stack = new ItemStack(ModItems.LEATHER_GLIDER.get());

                if (stack.is(ItemTags.DYEABLE)) {
                    List<DyeItem> dyes = Lists.newArrayList();
                    dyes.add(getRandomDye(random));

                    if (random.nextFloat() > 0.7F) {
                        dyes.add(getRandomDye(random));
                    }

                    if (random.nextFloat() > 0.8F) {
                        dyes.add(getRandomDye(random));
                    }

                    stack = DyedItemColor.applyDyes(stack, dyes);
                }

                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 16), // set your emerald cost
                        stack,
                        6,   // max uses
                        30,  // villager XP
                        0.2F // price multiplier
                );
            });

            event.getTrades().get(4).add((trader, random) -> {
                ItemStack stack = new ItemStack(ModItems.LEATHER_WOLF_ARMOR.get());

                if (stack.is(ItemTags.DYEABLE)) {
                    List<DyeItem> dyes = Lists.newArrayList();
                    dyes.add(getRandomDye(random));

                    if (random.nextFloat() > 0.7F) {
                        dyes.add(getRandomDye(random));
                    }

                    if (random.nextFloat() > 0.8F) {
                        dyes.add(getRandomDye(random));
                    }

                    stack = DyedItemColor.applyDyes(stack, dyes);
                }

                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 6), // set your emerald cost
                        stack,
                        12,   // max uses
                        15,  // villager XP
                        0.2F // price multiplier
                );
            });
        }

        if (event.getType() != VillagerProfession.ARMORER) return;

        event.getTrades().get(1).add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 7),
                new ItemStack(getMetalArmor(ModItems.COPPER_LEGGINGS.get(), CreateCompatItems.ZINC_LEGGINGS.get())),
                12,
                1,
                0.2F
        ));
        event.getTrades().get(1).add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 4),
                new ItemStack(getMetalArmor(ModItems.COPPER_BOOTS.get(), CreateCompatItems.ZINC_BOOTS.get())),
                12,
                1,
                0.2F
        ));
        event.getTrades().get(1).add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 5),
                new ItemStack(getMetalArmor(ModItems.COPPER_HELMET.get(), CreateCompatItems.ZINC_HELMET.get())),
                12,
                1,
                0.2F
        ));
        event.getTrades().get(1).add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 9),
                new ItemStack(getMetalArmor(ModItems.COPPER_CHESTPLATE.get(), CreateCompatItems.ZINC_CHESTPLATE.get())),
                12,
                1,
                0.2F
        ));

        // Leggings trade
        event.getTrades().get(4).add((trader, random) -> {
            ItemStack stack = applyRandomEnchantments(new ItemStack(ModItems.EMERALD_LEGGINGS.get()), random, trader);

            return new MerchantOffer(
                    new ItemCost(Items.EMERALD, 14),
                    stack,
                    3,
                    30,
                    0.2F
            );
        });

// Boots trade
        event.getTrades().get(4).add((trader, random) -> {
            ItemStack stack = applyRandomEnchantments(new ItemStack(ModItems.EMERALD_BOOTS.get()), random, trader);

            return new MerchantOffer(
                    new ItemCost(Items.EMERALD, 8),
                    stack,
                    3,
                    30,
                    0.2F
            );
        });

// Helmet trade
        event.getTrades().get(5).add((trader, random) -> {
            ItemStack stack = applyRandomEnchantments(new ItemStack(ModItems.EMERALD_HELMET.get()), random, trader);

            return new MerchantOffer(
                    new ItemCost(Items.EMERALD, 8),
                    stack,
                    3,
                    30,
                    0.2F
            );
        });

// Chestplate trade
        event.getTrades().get(5).add((trader, random) -> {
            ItemStack stack = applyRandomEnchantments(new ItemStack(ModItems.EMERALD_CHESTPLATE.get()), random, trader);

            return new MerchantOffer(
                    new ItemCost(Items.EMERALD, 16),
                    stack,
                    3,
                    30,
                    0.2F
            );
        });
    }

    private static ItemStack applyRandomEnchantments(ItemStack stack, RandomSource random, Entity trader) {
        int enchantLevel = 5 + random.nextInt(15); // vanilla-style 5-19
        RegistryAccess registryAccess = trader.level().registryAccess();

        // Optional set of enchantments to restrict to (like vanilla's ON_TRADED_EQUIPMENT)
        Optional<HolderSet.Named<Enchantment>> optional = registryAccess.registryOrThrow(Registries.ENCHANTMENT)
                .getTag(EnchantmentTags.ON_TRADED_EQUIPMENT);

        // Apply enchantments using the vanilla-compatible helper
        return EnchantmentHelper.enchantItem(random, stack, enchantLevel, registryAccess, optional);
    }
}