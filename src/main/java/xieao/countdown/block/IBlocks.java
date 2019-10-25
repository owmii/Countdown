package xieao.countdown.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xieao.countdown.item.ItemGroups;
import xieao.lib.block.IBlockBase;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IBlocks {
    public static final List<BlockItem> BLOCK_ITEMS = new ArrayList<>();
    public static final List<Block> BLOCKS = new ArrayList<>();
    //  public static final Block GLIOPHIN;

    static {
        // GLIOPHIN = register("gliophin", new LiquidBlock(Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F)));
    }

    static <T extends Block & IBlockBase> T register(String name, T block) {
        BlockItem itemBlock = block.getBlockItem(new Item.Properties(), ItemGroups.MAIN);
        itemBlock.setRegistryName(name);
        block.setRegistryName(name);
        BLOCK_ITEMS.add(itemBlock);
        BLOCKS.add(block);
        return block;
    }

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Block> event) {
        BLOCKS.forEach(block -> event.getRegistry().register(block));
    }
}
