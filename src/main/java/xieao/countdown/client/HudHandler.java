package xieao.countdown.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Mod;
import xieao.countdown.Countdown;
import xieao.countdown.client.gui.HudSettingScreen;
import xieao.countdown.config.HudSettings;
import xieao.countdown.potion.IEffects;
import xieao.lib.util.Time;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class HudHandler {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Countdown.MOD_ID, "textures/gui/widgets.png");
    public static final KeyBinding KEY = new KeyBinding("keybind.hud.settings", 67, "category.countdown");
    public static int color = 0xb2b3b2;
    public static int ticks;
    public static long time;
    public static boolean[] potionFlags = new boolean[2];

    @SubscribeEvent
    public static void clientTicks(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.side == LogicalSide.CLIENT) {
                if (time <= 10 && ticks % 10 == 0) {
                    if (time == 0 || color == 0xffffff) {
                        color = 0xff0000;
                    } else {
                        color = 0xb2b3b2;
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
        if (mc.currentScreen instanceof HudSettingScreen) return;
        if (event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
            int width = event.getWindow().getScaledWidth();
            renderClock((float) HudSettings.getHudX() + width, (float) HudSettings.getHudY());
        }
    }

    public static void renderClock(float x, float y) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(BACKGROUND);
        GlStateManager.pushMatrix();
        String s = Time.secToDHMS(time).replace(":", TextFormatting.DARK_GRAY + " : " + TextFormatting.RESET);
        GlStateManager.translatef(x - mc.fontRenderer.getStringWidth(s) - 45, y + 3, 0.0F);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GuiUtils.drawTexturedModalRect(0, 0, 0, 0, 200, 20, 0);

        if (potionFlags[0] || mc.player.isPotionActive(IEffects.SLOW_DOWN)) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(4.0F, 4F, 0.0F);
            GuiUtils.drawTexturedModalRect(0, 0, 18, 20, 9, 12, 0);
            GlStateManager.popMatrix();
        }
        if (potionFlags[1] || mc.player.isPotionActive(IEffects.PAUSE)) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(18.0F, 4F, 0.0F);
            GuiUtils.drawTexturedModalRect(0, 0, 27, 20, 37, 32, 0);
            GlStateManager.popMatrix();
        }

        if (mc.currentScreen instanceof HudSettingScreen) {
            GlStateManager.enableBlend();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 0.3F);
            GuiUtils.drawTexturedModalRect(55, 22, 0, 20, 18, 18, 0);
            GlStateManager.disableBlend();
        }

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translatef(35.0F, 6.5F, 0.0F);
        mc.fontRenderer.drawString(s, 0, 0, color);
        GlStateManager.popMatrix();
    }
}
