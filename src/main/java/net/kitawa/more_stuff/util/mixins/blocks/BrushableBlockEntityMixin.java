package net.kitawa.more_stuff.util.mixins.blocks;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(BrushableBlockEntity.class)
public abstract class BrushableBlockEntityMixin extends BlockEntity {

    @Shadow private int brushCount;
    @Shadow private long brushCountResetsAtTick;
    @Shadow private long coolDownEndsAtTick;
    @Shadow @Nullable
    private Direction hitDirection;

    @Unique
    private int more_Stuff_NeoForge_1_21_1$getCompletionState(int requiredBrushesToBreak) {
        if (this.brushCount == 0) {
            return 0;
        }

        // Divide total brushes into 3 equal progression stages
        int stageSize = requiredBrushesToBreak / 3;
        if (this.brushCount < stageSize) {
            return 1;
        } else if (this.brushCount < stageSize * 2) {
            return 2;
        } else {
            return 3;
        }
    }

    @Shadow protected abstract void brushingCompleted(Player player);
    @Nullable
    @Shadow
    private ResourceKey<LootTable> lootTable;
    @Shadow
    private long lootTableSeed;
    @Shadow
    private ItemStack item = ItemStack.EMPTY;
    @Shadow
    private static final Logger LOGGER = LogUtils.getLogger();

    // You MUST call the correct BlockEntity constructor for mixins
    public BrushableBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    /**
     * Injects into the brush method to scale progress and cooldown by the brush's use duration.
     */
    @Inject(method = "brush", at = @At("HEAD"), cancellable = true)
    private void onBrush(long startTick, Player player, Direction direction, CallbackInfoReturnable<Boolean> cir) {

        ItemStack brushStack = player.getUseItem();
        int useDuration = brushStack.getItem().getUseDuration(brushStack, player); // ✅ correct for 1.21+
        float durationRatio = useDuration / 200.0F; // 1.0 = vanilla speed

        // Scale timings based on brush duration
        int brushCooldownTicks = Math.max(2, (int)(10 * durationRatio)); // default 10 ticks @ 200
        int requiredBrushesToBreak = Math.max(1, (int)(10 * durationRatio)); // default 10 @ 200

        this.brushCountResetsAtTick = startTick + 40L;

        if (startTick >= this.coolDownEndsAtTick && this.level instanceof ServerLevel) {
            this.coolDownEndsAtTick = startTick + brushCooldownTicks;
            this.more_Stuff_NeoForge_1_21_1$unpackLootTable(player);
            int prevState = this.more_Stuff_NeoForge_1_21_1$getCompletionState(requiredBrushesToBreak);

            if (++this.brushCount >= requiredBrushesToBreak) {
                this.brushingCompleted(player);
                cir.setReturnValue(true);
            } else {
                this.level.scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), 2);
                int newState  = this.more_Stuff_NeoForge_1_21_1$getCompletionState(requiredBrushesToBreak);
                if (prevState != newState) {
                    BlockState state = this.getBlockState();
                    this.level.setBlock(this.getBlockPos(), state.setValue(BlockStateProperties.DUSTED, newState), 3);
                }
                cir.setReturnValue(false);
            }
        } else {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private void more_Stuff_NeoForge_1_21_1$unpackLootTable(Player player) {
        if (this.lootTable != null && this.level != null && !this.level.isClientSide() && this.level.getServer() != null) {
            LootTable loottable = this.level.getServer().reloadableRegistries().getLootTable(this.lootTable);
            if (player instanceof ServerPlayer serverplayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger(serverplayer, this.lootTable);
            }

            LootParams lootparams = new LootParams.Builder((ServerLevel)this.level)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition))
                    .withLuck(player.getLuck())
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .create(LootContextParamSets.CHEST);
            ObjectArrayList<ItemStack> objectarraylist = loottable.getRandomItems(lootparams, this.lootTableSeed);

            this.item = switch (objectarraylist.size()) {
                case 0 -> ItemStack.EMPTY;
                case 1 -> (ItemStack)objectarraylist.get(0);
                default -> {
                    LOGGER.warn("Expected max 1 loot from loot table {}, but got {}", this.lootTable.location(), objectarraylist.size());
                    yield objectarraylist.get(0);
                }
            };
            this.lootTable = null;
            this.setChanged();
        }
    }
}
