package xieao.countdown;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import xieao.countdown.item.IItems;
import xieao.countdown.network.Packets;
import xieao.lib.client.renderer.item.IItemColorHolder;

import static xieao.lib.Lollipop.addModListener;

@Mod(Countdown.MOD_ID)
public class Countdown {
    public static final String MOD_ID = "countdown";

    public Countdown() {
        addModListener(this::commonSetup);
        addModListener(this::clientSetup);
    }

    void commonSetup(FMLCommonSetupEvent event) {
        Packets.register();
        //  IFeatures.register();
    }

    void clientSetup(FMLClientSetupEvent event) {
        IItemColorHolder.registerAll(IItems.ITEMS);
    }
}