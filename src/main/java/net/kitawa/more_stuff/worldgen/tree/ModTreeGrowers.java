package net.kitawa.more_stuff.worldgen.tree;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.worldgen.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower AQUANDA = new TreeGrower(MoreStuff.MOD_ID + ":bloodwood",
            Optional.empty(), Optional.of(ModConfiguredFeatures.AQUANDA), Optional.empty());

}
