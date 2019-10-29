package xieao.countdown.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import xieao.countdown.client.gui.HudSettingScreen;
import xieao.countdown.config.HudSettings;
import xieao.lib.util.Time;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class HudHandler {
    public static final KeyBinding KEY = new KeyBinding("keybind.hud.settings", 67, "Countdown");
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
            if (KEY.isPressed()) {
                Minecraft.getInstance().displayGuiScreen(new HudSettingScreen());
            }
        }
    }

    @SubscribeEvent
    public static void renderTime(RenderGameOverlayEvent.Pre event) {
        if (!HudSettings.hudVisiblity()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.currentScreen != null) return;
        if (event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
            FontRenderer fr = mc.fontRenderer;
            int width = event.getWindow().getScaledWidth();
            int height = event.getWindow().getScaledHeight();
            String s = Time.secToDHMS(time);
            fr.drawString(s, (float) (width - fr.getStringWidth(s) - 4 + HudSettings.getHudX()), (float) (4 + HudSettings.getHudY()), color);
        }
    }
}
