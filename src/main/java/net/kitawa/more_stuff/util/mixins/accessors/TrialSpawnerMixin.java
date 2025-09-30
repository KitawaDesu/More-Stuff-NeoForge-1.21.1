package net.kitawa.more_stuff.util.mixins.accessors;

import net.kitawa.more_stuff.util.ducks.TrialSpawnerDuck;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TrialSpawner.class)
public abstract class TrialSpawnerMixin implements TrialSpawnerDuck {
    @Mutable
    @Shadow @Final
    private TrialSpawnerConfig ominousConfig;
    @Mutable
    @Shadow @Final
    private TrialSpawnerConfig normalConfig;

    @Override
    public void more_stuff$setOminousConfig(TrialSpawnerConfig value) {
        this.ominousConfig = value;
    }

    @Override
    public void more_stuff$setNormalConfig(TrialSpawnerConfig value) {
        this.normalConfig = value;
    }
}