package net.kitawa.more_stuff.blocks.util;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModdedBlockStateProperties {
    public static final BooleanProperty LAVALOGGED = BooleanProperty.create("lavalogged");
    public static final IntegerProperty COPPER_STABILITY_DISTANCE = IntegerProperty.create("copper_distance", 0, 10);
    public static final IntegerProperty IRON_STABILITY_DISTANCE = IntegerProperty.create("iron_distance", 0, 13);
    public static final IntegerProperty GOLDEN_STABILITY_DISTANCE = IntegerProperty.create("golden_distance", 0, 16);
    public static final IntegerProperty ANCIENT_STABILITY_DISTANCE = IntegerProperty.create("ancient_distance", 0, 19);
    public static final IntegerProperty PYROLIZED_STABILITY_DISTANCE = IntegerProperty.create("pyrolized_distance", 0, 7);
}
