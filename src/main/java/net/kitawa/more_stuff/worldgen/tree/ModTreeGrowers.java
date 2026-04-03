package net.kitawa.more_stuff.worldgen.tree;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.worldgen.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower AQUANDA_BONEMEAL = new TreeGrower(MoreStuff.MOD_ID,
            Optional.empty(), Optional.of(ModConfiguredFeatures.AQUANDA_BONEMEAL), Optional.empty());
    public static final TreeGrower HYBERNATUS = new TreeGrower(MoreStuff.MOD_ID,
            Optional.empty(), Optional.of(ModConfiguredFeatures.HYBERNATUS), Optional.empty());

}
