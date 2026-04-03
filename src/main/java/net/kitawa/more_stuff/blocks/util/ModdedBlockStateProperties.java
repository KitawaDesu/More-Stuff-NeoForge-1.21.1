package net.kitawa.more_stuff.blocks.util;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModdedBlockStateProperties {
    public static final BooleanProperty LAVALOGGED = BooleanProperty.create("lavalogged");
    public static final IntegerProperty PALLADIUM_STABILITY_DISTANCE = IntegerProperty.create("palladium_distance", 0, 9);
    public static final IntegerProperty COPPER_STABILITY_DISTANCE = IntegerProperty.create("copper_distance", 0, 10);
    public static final IntegerProperty BRASS_STABILITY_DISTANCE = IntegerProperty.create("brass_distance", 0, 12);
    public static final IntegerProperty BRONZE_STABILITY_DISTANCE = IntegerProperty.create("bronze_distance", 0, 12);
    public static final IntegerProperty CINCINNASITE_STABILITY_DISTANCE = IntegerProperty.create("cincinnasite_distance", 0, 12);
    public static final IntegerProperty IRON_STABILITY_DISTANCE = IntegerProperty.create("iron_distance", 0, 13);
    public static final IntegerProperty TIN_STABILITY_DISTANCE = IntegerProperty.create("tin_distance", 0, 13);
    public static final IntegerProperty ZINC_STABILITY_DISTANCE = IntegerProperty.create("zinc_distance", 0, 13);
    public static final IntegerProperty STEEL_STABILITY_DISTANCE = IntegerProperty.create("steel_distance", 0, 15);
    public static final IntegerProperty GOLDEN_STABILITY_DISTANCE = IntegerProperty.create("golden_distance", 0, 16);
    public static final IntegerProperty ANCIENT_STABILITY_DISTANCE = IntegerProperty.create("ancient_distance", 0, 19);
    public static final IntegerProperty PYROLIZED_STABILITY_DISTANCE = IntegerProperty.create("pyrolized_distance", 0, 7);

    public static final IntegerProperty GEL_DISTANCE = IntegerProperty.create("gel_distance", 0, 16);
}
