package net.kitawa.more_stuff.worldgen.biomelayers;

import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class LayerDefinition {
    private final List<NoiseBlockRule> rules;
    private final BlockState fallback;

    public LayerDefinition(List<NoiseBlockRule> rules, BlockState fallback) {
        this.rules = rules;
        this.fallback = fallback;
    }

    public List<NoiseBlockRule> rules() { return rules; }
    public BlockState fallback() { return fallback; }
}