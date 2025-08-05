package net.kitawa.more_stuff.compat.create.blocks;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.items.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CreateCompatBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MoreStuff.MOD_ID);


    public static final DeferredBlock<DropExperienceBlock> NETHER_ZINC_ORE = registerBlock("nether_zinc_ore",
            () -> new DropExperienceBlock(UniformInt.of(0, 1), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NETHER)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_ORE)
            )
    );

    public static final DeferredBlock<DropExperienceBlock> PYROLIZED_ZINC_ORE = registerBlock("pyrolized_zinc_ore",
            () -> new DropExperienceBlock(UniformInt.of(0, 1), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_ORE)
            )
    );


    public static final DeferredBlock<DropExperienceBlock> FROZEN_ZINC_ORE = registerBlock("frozen_zinc_ore",
            () -> new DropExperienceBlock(UniformInt.of(0, 1), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.ICE)
                    .friction(0.98F)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.NETHER_ORE)
            )
    );

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }


    private static <T extends Block> DeferredBlock<T> registerBlockNoItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
