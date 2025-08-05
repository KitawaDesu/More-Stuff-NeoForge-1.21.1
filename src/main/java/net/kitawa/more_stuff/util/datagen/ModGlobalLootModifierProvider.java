package net.kitawa.more_stuff.util.datagen;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.util.loot.modifiers.DyeRandomlyModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries) {
        super(output, registries, MoreStuff.MOD_ID);
    }

    @Override
    protected void start() {
        List<ResourceLocation> lootTables = List.of(
                ResourceLocation.withDefaultNamespace("chests/abandoned_mineshaft"),
                ResourceLocation.withDefaultNamespace("chests/ancient_city"),
                ResourceLocation.withDefaultNamespace("chests/bastion_bridge"),
                ResourceLocation.withDefaultNamespace("chests/bastion_hoglin_stable"),
                ResourceLocation.withDefaultNamespace("chests/bastion_other"),
                ResourceLocation.withDefaultNamespace("chests/bastion_treasure"),
                ResourceLocation.withDefaultNamespace("chests/buried_treasure"),
                ResourceLocation.withDefaultNamespace("chests/desert_pyramid"),
                ResourceLocation.withDefaultNamespace("chests/end_city_treasure"),
                ResourceLocation.withDefaultNamespace("chests/igloo_chest"),
                ResourceLocation.withDefaultNamespace("chests/jungle_temple"),
                ResourceLocation.withDefaultNamespace("chests/nether_bridge"),
                ResourceLocation.withDefaultNamespace("chests/pillager_outpost"),
                ResourceLocation.withDefaultNamespace("chests/ruined_portal"),
                ResourceLocation.withDefaultNamespace("chests/shipwreck_map"),
                ResourceLocation.withDefaultNamespace("chests/shipwreck_supply"),
                ResourceLocation.withDefaultNamespace("chests/shipwreck_treasure"),
                ResourceLocation.withDefaultNamespace("chests/simple_dungeon"),
                ResourceLocation.withDefaultNamespace("chests/spawn_bonus_chest"),
                ResourceLocation.withDefaultNamespace("chests/stronghold_corridor"),
                ResourceLocation.withDefaultNamespace("chests/stronghold_crossing"),
                ResourceLocation.withDefaultNamespace("chests/stronghold_library"),
                ResourceLocation.withDefaultNamespace("chests/underwater_ruin_big"),
                ResourceLocation.withDefaultNamespace("chests/underwater_ruin_small"),
                ResourceLocation.withDefaultNamespace("chests/woodland_mansion"),
                ResourceLocation.withDefaultNamespace("chests/village/village_tannery"),
                ResourceLocation.withDefaultNamespace("chests/village/village_weaponsmith")
        );

        for (ResourceLocation table : lootTables) {
            String name = "dye_randomly_" + table.getPath().replace('/', '-');
            LootItemCondition[] conditions = new LootItemCondition[] {
                    LootTableIdCondition.builder(table).build()
            };
            this.add(name, new DyeRandomlyModifier(conditions));
        }
    }
}


