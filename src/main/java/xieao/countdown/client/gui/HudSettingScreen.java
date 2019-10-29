package xieao.countdown.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xieao.countdown.client.HudHandler;
import xieao.countdown.config.HudSettings;
import xieao.lib.util.Time;

@OnlyIn(Dist.CLIENT)
public class HudSettingScreen extends Screen {
    public boolean visiblity;
    public double cdX;
    public double cdY;

    public HudSettingScreen() {
        super(new StringTextComponent("Hud settings"));
    }

    @Override
    protected void init() {
        this.visiblity = HudSettings.hudVisiblity();
        this.cdX = HudSettings.getHudX();
        this.cdY = HudSettings.getHudY();
    }

    @Override
    public void render(int x, int y, float pt) {
        renderBackground();
        if (this.visiblity) {
            Minecraft mc = Minecraft.getInstance();
            FontRenderer fr = mc.fontRenderer;
            String s = Time.secToDHMS(HudHandler.time);
            fr.drawString(s, (float) (this.width - fr.getStringWidth(s) - 4 + this.cdX), (float) (4 + this.cdY), HudHandler.color);
        }
        super.render(x, y, pt);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int i, double xd, double yd) {
        this.cdX += xd;
        this.cdY += yd;
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int i) {
        HudSettings.set(this.visiblity, this.cdX, this.cdY);
        return false;
    }

    @Override
    public void onClose() {
        HudSettings.set(this.visiblity, this.cdX, this.cdY);
        super.onClose();
    }
}
