package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.entities.ModEntities;
import net.kitawa.more_stuff.entities.monster.ColoredSlime;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Slime.class)
public abstract class SlimeFinalizeSpawnMixin {

    @Inject(
            method = "finalizeSpawn",
            at = @At("HEAD"),
            cancellable = true
    )
    private void morestuff$replaceWithColoredSlime(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType spawnType,
            @Nullable SpawnGroupData data,
            CallbackInfoReturnable<SpawnGroupData> cir
    ) {
        Slime self = (Slime) (Object) this;

        // Only apply to vanilla Slimes
        if (self.getType() != EntityType.SLIME)
            return;

        RandomSource random = level.getRandom();

        if (spawnType == MobSpawnType.TRIAL_SPAWNER)
            return;

        // 18/19 chance to REPLACE the slime
        if (random.nextInt(19) == 0)
            return; // 1/19 chance to remain normal slime

        // Create colored slime
        ColoredSlime colored = ModEntities.COLORED_SLIME.get().create(level.getLevel());
        if (colored == null)
            return;

        // Copy size
        colored.setSize(self.getSize(), true);

        // Move to same position
        colored.moveTo(self.getX(), self.getY(), self.getZ(), self.getYRot(), self.getXRot());

        // Finalize spawn
        colored.finalizeSpawn(level, difficulty, spawnType, data);

        // Add to world
        level.getLevel().addFreshEntity(colored);

        // Remove original slime
        self.discard();

        // Stop vanilla processing
        cir.setReturnValue(null);
    }
}