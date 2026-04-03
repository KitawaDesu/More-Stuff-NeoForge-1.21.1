package net.kitawa.more_stuff.entities.monster;

import net.kitawa.more_stuff.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class VeilWraithEntity extends EnderMan {

    private float targetVisibility = 0.05f;

    private static final EntityDataAccessor<Float> DATA_VISIBILITY =
            SynchedEntityData.defineId(VeilWraithEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> DATA_PASTE_APPLIED =
            SynchedEntityData.defineId(VeilWraithEntity.class, EntityDataSerializers.BOOLEAN);

    public VeilWraithEntity(EntityType<? extends EnderMan> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return EnderMan.createAttributes();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VISIBILITY, 0.05f);
        builder.define(DATA_PASTE_APPLIED, false);
    }

    public float getVisibility() {
        return this.entityData.get(DATA_VISIBILITY);
    }

    private void setVisibility(float v) {
        this.entityData.set(DATA_VISIBILITY, v);
    }

    public boolean isPasteApplied() {
        return this.entityData.get(DATA_PASTE_APPLIED);
    }

    public void setPasteApplied(boolean applied) {
        this.entityData.set(DATA_PASTE_APPLIED, applied);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if (item.is(ModItems.VEIL_PASTE.get())) {
            if (!level().isClientSide()) {
                setVisibility(1.0f);
                targetVisibility = 1.0f;
                setPasteApplied(true);
                item.shrink(1);
            }
            return InteractionResult.sidedSuccess(level().isClientSide());
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            updateVisibility();
        }
    }

    private void updateVisibility() {
        if (isPasteApplied()) {
            setVisibility(1.0f);
            return;
        }

        float current = getVisibility();
        BlockPos pos = this.blockPosition();
        int lightLevel = level().getBrightness(LightLayer.BLOCK, pos);

        targetVisibility = lightLevel / 15.0f;

        if (this.getTarget() == null) {
            targetVisibility = Math.min(targetVisibility, 0.3f);
        }

        float newVisibility = current + (targetVisibility - current) * 0.15f;
        setVisibility(newVisibility);
    }
}