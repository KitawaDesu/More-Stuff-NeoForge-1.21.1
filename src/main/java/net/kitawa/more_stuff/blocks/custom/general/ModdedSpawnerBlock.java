package net.kitawa.more_stuff.blocks.custom.general;

import net.kitawa.more_stuff.blocks.ModBlockEntities;
import net.kitawa.more_stuff.blocks.custom.general.entities.ModdedSpawnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ModdedSpawnerBlock extends SpawnerBlock {
    public ModdedSpawnerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ModdedSpawnerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> blockEntityType
    ) {
        return createTickerHelper(
                blockEntityType,
                ModBlockEntities.MOD_SPAWNER.get(),
                level.isClientSide
                        ? ModdedSpawnerBlockEntity::clientTick
                        : ModdedSpawnerBlockEntity::serverTick
        );
    }
}
