package xieao.countdown.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import xieao.lib.util.Time;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class HudHandler {
    public static int color = 0xffffff;
    public static int ticks;
    public static long time;

    @SubscribeEvent
    public static void clientTicks(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.side == LogicalSide.CLIENT) {
                if (time <= 10 && ticks % 10 == 0) {
                    if (time == 0 || color == 0xffffff) {
                        color = 0xff0000;
                    } else {
                        color = 0xffffff;
                    }
                }
                ticks++;
            }
        }
    }

    @SubscribeEvent
    public static void renderTime(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
            Minecraft mc = Minecraft.getInstance();
            FontRenderer fr = mc.fontRenderer;
            int width = event.getWindow().getScaledWidth();
            int height = event.getWindow().getScaledHeight();
            String s = Time.secToDHMS(time);
            fr.drawString(s, width - fr.getStringWidth(s) - 4, 4, color);
        }
    }
}
