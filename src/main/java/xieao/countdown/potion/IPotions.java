package xieao.countdown.potion;

import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xieao.lib.potion.Brew;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IPotions {
    public static final List<Potion> POTIONS = new ArrayList<>();
    public static final Potion PAUSE = register("pause", new Potion(new EffectInstance(IEffects.PAUSE, 12000)));
    public static final Potion LONG_PAUSE = register("long_pause", new Potion("pause", new EffectInstance(IEffects.PAUSE, 22000)));
    public static final Potion SLOW_DOWN = register("slow_down", new Potion(new EffectInstance(IEffects.SLOW_DOWN, 12000)));
    public static final Potion LONG_SLOW_DOWN = register("long_slow_down", new Potion("slow_down", new EffectInstance(IEffects.SLOW_DOWN, 22000)));

    public static void initBrew() {
        Brew.addMix(Potions.AWKWARD, Items.CLOCK, PAUSE);
        Brew.addMix(PAUSE, Items.REDSTONE, LONG_PAUSE);
        Brew.addMix(Potions.AWKWARD, Items.COBWEB, SLOW_DOWN);
        Brew.addMix(SLOW_DOWN, Items.REDSTONE, LONG_SLOW_DOWN);
    }

    static <T extends Potion> T register(String name, T potion) {
        potion.setRegistryName(name);
        POTIONS.add(potion);
        return potion;
    }

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Potion> event) {
        POTIONS.forEach(item -> event.getRegistry().register(item));
    }
}
