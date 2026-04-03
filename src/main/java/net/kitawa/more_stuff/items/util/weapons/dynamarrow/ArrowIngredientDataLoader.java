package net.kitawa.more_stuff.items.util.weapons.dynamarrow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import java.util.HashMap;
import java.util.Map;


public class ArrowIngredientDataLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<ResourceLocation, ArrowIngredientData> DATA = new HashMap<>();
    public static final ArrowIngredientDataLoader INSTANCE = new ArrowIngredientDataLoader();

    public ArrowIngredientDataLoader() {
        super(GSON, "arrow_ingredient_data");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager manager, ProfilerFiller filler) {
        DATA.clear();
        jsons.forEach((id, element) -> {
            try {
                JsonObject json = element.getAsJsonObject();
                String itemStr = json.has("item") ? json.get("item").getAsString() : id.toString();

                // Determine if this entry refers to a tag (#tag_name) or an item
                ItemOrTag itemOrTag;
                if (itemStr.startsWith("#")) {
                    ResourceLocation tagId = ResourceLocation.tryParse(itemStr.substring(1));
                    if (tagId == null) throw new IllegalArgumentException("Invalid tag ID: " + itemStr);
                    itemOrTag = new ItemOrTag(tagId, true);
                } else {
                    ResourceLocation itemId = ResourceLocation.tryParse(itemStr);
                    if (itemId == null) throw new IllegalArgumentException("Invalid item ID: " + itemStr);
                    itemOrTag = new ItemOrTag(itemId, false);
                }

                JsonObject components = json.has("components") ? json.getAsJsonObject("components") : new JsonObject();
                String type = json.has("type") ? json.get("type").getAsString() : "";

                // Optional XP requirement for modifiers
                int xp = 0;
                if ("more_stuff:modifier".equals(type) && json.has("xp")) {
                    xp = Mth.clamp(json.get("xp").getAsInt(), 0, Integer.MAX_VALUE);
                }

                DATA.put(itemOrTag.id(), new ArrowIngredientData(itemOrTag, components, type, xp));
                System.out.println("[ArrowIngredientDataLoader] Loaded data for " + itemOrTag.id() + " (type: " + type + ", xp: " + xp + ")");
            } catch (Exception e) {
                System.err.println("[ArrowIngredientDataLoader] Failed to load " + id + ": " + e);
            }
        });
        System.out.println("[ArrowIngredientDataLoader] Loaded " + DATA.size() + " entries total");
    }

    /**
     * Returns ArrowIngredientData for a given item, automatically checking tags if needed.
     */
    public static ArrowIngredientData get(Item item) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
        ArrowIngredientData data = DATA.get(itemId);
        if (data != null) return data;

        // Check all tag-based entries
        for (ArrowIngredientData entry : DATA.values()) {
            ItemOrTag iot = entry.item();
            if (iot.isTag()) {
                TagKey<Item> tag = TagKey.create(Registries.ITEM, iot.id());
                if (item.builtInRegistryHolder().is(tag)) {
                    return entry;
                }
            }
        }
        return null;
    }

    public static void onReload(AddReloadListenerEvent event) {
        System.out.println("[ArrowIngredientDataLoader] Reload listener registered!");
        event.addListener(INSTANCE);
    }

    // ─── Data record ───
    public record ArrowIngredientData(ItemOrTag item, JsonObject components, String type, int xp) {
        public JsonObject getComponentsAsJson() {
            return components;
        }

        public String getType() {
            return type;
        }

        public int getXpCost() {
            return xp;
        }
    }

    /** Represents either a single item or a tag */
    public record ItemOrTag(ResourceLocation id, boolean isTag) {}
}