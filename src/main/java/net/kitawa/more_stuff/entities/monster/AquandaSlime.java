package net.kitawa.more_stuff.entities.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

import static net.minecraft.world.entity.Mob.checkMobSpawnRules;

public class AquandaSlime extends Slime {

    // ⭐ NEW — timer to detect “time since last jump”
    public int timeSinceJump = 0;

    public AquandaSlime(EntityType<? extends Slime> type, Level level) {
        super(type, level);
        this.moveControl = new AquandaMoveControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.setAirSupply(this.getMaxAirSupply());

        // ⭐ NEW — increment jump timer every tick
        this.timeSinceJump++;
    }

    @Override
    public int getMaxAirSupply() {
        return 300;
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new AquandaSlimeAttackGoal(this));
        this.goalSelector.addGoal(3, new AquandaRandomDirectionGoal(this));
        this.goalSelector.addGoal(5, new AquandaKeepOnJumpingGoal(this));

        this.targetSelector.addGoal(
                1,
                new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                        p -> Math.abs(p.getY() - this.getY()) <= 4.0)
        );

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }


    /* ---------------------------
       Replacements for vanilla Slime goals
     --------------------------- */

    static class AquandaSlimeAttackGoal extends Goal {
        private final AquandaSlime slime;
        private int growTiredTimer;

        public AquandaSlimeAttackGoal(AquandaSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity == null) return false;
            return this.slime.canAttack(livingentity)
                    && this.slime.getMoveControl() instanceof AquandaSlime.AquandaMoveControl;
        }

        @Override
        public void start() {
            this.growTiredTimer = reducedTickDelay(300);
            super.start();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity == null) return false;
            return this.slime.canAttack(livingentity) && --this.growTiredTimer > 0;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity != null) {
                this.slime.lookAt(livingentity, 10.0F, 10.0F);
            }

            if (this.slime.getMoveControl() instanceof AquandaSlime.AquandaMoveControl slimeMoveControl) {
                slimeMoveControl.setDirection(this.slime.getYRot(), this.slime.isDealsDamage());
            }
        }
    }


    static class AquandaRandomDirectionGoal extends Goal {
        private final AquandaSlime slime;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public AquandaRandomDirectionGoal(AquandaSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.slime.getTarget() == null
                    && (this.slime.onGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION))
                    && this.slime.getMoveControl() instanceof AquandaSlime.AquandaMoveControl;
        }

        @Override
        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = this.adjustedTickDelay(40 + this.slime.getRandom().nextInt(60));
                this.chosenDegrees = this.slime.getRandom().nextInt(360);
            }

            if (this.slime.getMoveControl() instanceof AquandaSlime.AquandaMoveControl slimeMoveControl) {
                slimeMoveControl.setDirection(this.chosenDegrees, false);
            }
        }
    }


    static class AquandaKeepOnJumpingGoal extends Goal {
        private final AquandaSlime slime;

        public AquandaKeepOnJumpingGoal(AquandaSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !this.slime.isPassenger();
        }

        @Override
        public void tick() {
            if (this.slime.getMoveControl() instanceof AquandaSlime.AquandaMoveControl slimeMoveControl) {
                slimeMoveControl.setWantedMovement(1.0);
            }
        }
    }

    private int waterDashTimer;
    // ---------------------------------------------------------------
    //   CUSTOM MOVE CONTROL — now emits sink trigger after jumps
    // ---------------------------------------------------------------
    static class AquandaMoveControl extends MoveControl {

        private final AquandaSlime slime;
        private float yRot;
        private int jumpDelay;
        private boolean aggressive;

        public AquandaMoveControl(AquandaSlime slime) {
            super(slime);
            this.slime = slime;
            this.yRot = slime.getYRot();
        }

        public void setDirection(float yRot, boolean aggressive) {
            this.yRot = yRot;
            this.aggressive = aggressive;
        }

        public void setWantedMovement(double speed) {
            this.speedModifier = speed;
            this.operation = Operation.MOVE_TO;
        }

        @Override
        public void tick() {
            this.mob.setYRot(rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
            this.mob.yHeadRot = this.mob.getYRot();
            this.mob.yBodyRot = this.mob.getYRot();

            if (this.operation != Operation.MOVE_TO) {
                this.mob.setZza(0F);
                return;
            }

            this.operation = Operation.WAIT;

            boolean onGround = this.mob.onGround();
            boolean inWater = this.mob.isInWater();

            // ------------------------------------------
            // 🔵 FIRST: medium water dash (after 20 ticks)
            // ------------------------------------------
            if (inWater && !onGround) {

                slime.waterDashTimer++;

                if (slime.waterDashTimer >= 20) { // 1 second
                    Vec3 look = this.mob.getLookAngle().normalize();

                    double forwardBoost = 0.50D;
                    double upwardBoost  = 0.075D;

                    this.mob.setDeltaMovement(
                            this.mob.getDeltaMovement().add(
                                    look.x * forwardBoost,
                                    upwardBoost,
                                    look.z * forwardBoost
                            )
                    );

                    // ✔ Reset AFTER a dash happens
                    slime.waterDashTimer = 0;
                }

                return; // do NOT run ground logic
            }

            // ------------------------------------------
            // ✔ Reset ONLY when on ground
            // ------------------------------------------
            if (onGround) {
                slime.waterDashTimer = 0;
            }

            // ==========================================
            // 🟢 Existing ground jump code
            // ==========================================
            if (onGround) {
                this.mob.setSpeed((float)(this.speedModifier *
                        this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));

                if (jumpDelay-- <= 0) {

                    jumpDelay = this.slime.getJumpDelay();
                    if (aggressive) jumpDelay /= 3;

                    slime.getJumpControl().jump();

                    // ⭐ does NOT reset waterDashTimer anymore
                    // (only resets when onGround at top of block)

                    Vec3 look = this.mob.getLookAngle().normalize();
                    double forwardBoost = 1.00D;
                    double upwardBoost = 0.10D;

                    this.mob.setDeltaMovement(
                            this.mob.getDeltaMovement().add(
                                    look.x * forwardBoost,
                                    upwardBoost,
                                    look.z * forwardBoost
                            )
                    );

                    if (slime.doPlayJumpSound()) {
                        slime.playSound(
                                slime.getJumpSound(),
                                slime.getSoundVolume(),
                                slime.getVoicePitch()
                        );
                    }
                } else {
                    slime.xxa = 0;
                    slime.zza = 0;
                    mob.setSpeed(0F);
                }

                return;
            }

            // ---------------------------------------
            // fallback slow movement
            // ---------------------------------------
            this.mob.setSpeed((float)(this.speedModifier *
                    this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.3));
        }
    }




    public static boolean checkAquandaSlimeSpawnRules(
            EntityType<AquandaSlime> slime, LevelAccessor level, MobSpawnType spawnType,
            BlockPos pos, RandomSource random
    ) {
        if (MobSpawnType.isSpawner(spawnType)) {
            return checkMobSpawnRules(slime, level, spawnType, pos, random);
        } else {
            if (level.getDifficulty() != Difficulty.PEACEFUL) {

                // --- NEW: Allow slime to spawn in water ---
                // If the block contains water, allow spawn checks to continue
                if (level.getFluidState(pos).is(FluidTags.WATER)) {
                    return checkMobSpawnRules(slime, level, spawnType, pos, random);
                }
                // ------------------------------------------

                if (spawnType == MobSpawnType.SPAWNER) {
                    return checkMobSpawnRules(slime, level, spawnType, pos, random);
                }

                if (level.getBiome(pos).is(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS)
                        && pos.getY() > 50
                        && pos.getY() < 70
                        && random.nextFloat() < 0.5F
                        && random.nextFloat() < level.getMoonBrightness()
                        && level.getMaxLocalRawBrightness(pos) <= random.nextInt(8)) {
                    return checkMobSpawnRules(slime, level, spawnType, pos, random);
                }

                if (!(level instanceof WorldGenLevel)) {
                    return false;
                }

                ChunkPos chunkpos = new ChunkPos(pos);
                boolean flag = WorldgenRandom.seedSlimeChunk(chunkpos.x, chunkpos.z,
                                ((WorldGenLevel) level).getSeed(), 987234911L)
                        .nextInt(10) == 0;

                if (random.nextInt(10) == 0 && flag && pos.getY() < 40) {
                    return checkMobSpawnRules(slime, level, spawnType, pos, random);
                }
            }

            return false;
        }
    }
}
