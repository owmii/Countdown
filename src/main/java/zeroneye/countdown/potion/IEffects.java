package zeroneye.countdown.potion;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import zeroneye.lib.potion.EffectBase;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IEffects {
    public static final List<Effect> EFFECTS = new ArrayList<>();
    public static final Effect PAUSE = register("pause", new EffectBase(EffectType.BENEFICIAL, 13458603));
    public static final Effect SLOW_DOWN = register("slow_down", new EffectBase(EffectType.BENEFICIAL, 4521796));
    public static final Effect FAST_FORWARD = register("fast_forward", new EffectBase(EffectType.HARMFUL, 16711680));

    static <T extends Effect> T register(String name, T effect) {
        effect.setRegistryName(name);
        EFFECTS.add(effect);
        return effect;
    }

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Effect> event) {
        EFFECTS.forEach(item -> event.getRegistry().register(item));
    }
}
