package net.kitawa.more_stuff.util.mixins.blocks;

import net.kitawa.more_stuff.experimentals.blocks.util.FletchingTableMenu;
import net.kitawa.more_stuff.util.configs.ExperimentalUpdatesConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.FletchingTableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(FletchingTableBlock.class)
public abstract class FletchingTableBlockMixin extends CraftingTableBlock {

    protected FletchingTableBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit
    ) {
        // ---- CHECK CONFIG FIRST ----
        if (ExperimentalUpdatesConfig.IS_COMBAT_UPDATE_ALLOWED.get()) {
            if (level.isClientSide) {
            return InteractionResult.SUCCESS; // Client predicts success
        } else {
                player.openMenu(this.getMenuProvider(state, level, pos));
                player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE); // or your custom stat
                return InteractionResult.CONSUME;
            } // do nothing, acts like vanilla 1.21
        } else {
            return InteractionResult.PASS;
        }
    }

    @Nullable
    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (id, inventory, player) ->
                        new FletchingTableMenu(id, inventory, ContainerLevelAccess.create(level, pos)),
                Component.translatable("container.more_stuff.fletching_table")
        );
    }
}
