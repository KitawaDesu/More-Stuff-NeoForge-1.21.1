package net.kitawa.more_stuff.util.tags;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class ModEntityTags {

    // Example: this tag can be used for your "priority" mobs
    public static final TagKey<EntityType<?>> PRIORITY_TARGET = create("priority_target");

    private ModEntityTags() {}

    private static TagKey<EntityType<?>> create(String name) {
        return TagKey.create(BuiltInRegistries.ENTITY_TYPE.key(), ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name));
    }
}
