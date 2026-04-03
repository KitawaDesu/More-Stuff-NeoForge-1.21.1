package net.kitawa.more_stuff.worldgen.biomelayers;

import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record MiddleLayer(List<MiddleSection> sections, BlockState fallback) {}