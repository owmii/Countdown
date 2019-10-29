package xieao.countdown.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xieao.countdown.Countdown;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@OnlyIn(Dist.CLIENT)
public class HudSettings {
    private static final Path CONFIG_DIR = Paths.get(FMLPaths.CONFIGDIR.get().toAbsolutePath().toString(), Countdown.MOD_ID + "/hud.properties");
    private static final Logger LOG = LogManager.getLogger(Countdown.MOD_ID.toUpperCase());
    private static boolean hudVisiblity;
    private static double hudX;
    private static double hudY;

    public static boolean hudVisiblity() {
        return hudVisiblity;
    }

    public static double getHudX() {
        return hudX;
    }

    public static double getHudY() {
        return hudY;
    }

    public static void load() {
        try (InputStream inputstream = Files.newInputStream(CONFIG_DIR)) {
            Properties properties = new Properties();
            properties.load(inputstream);
            hudVisiblity = Boolean.parseBoolean(properties.getProperty("hudVisiblity", "true"));
            hudX = Double.parseDouble(properties.getProperty("hudX", "0"));
            hudY = Double.parseDouble(properties.getProperty("hudY", "0"));
        } catch (Exception e) {
            LOG.warn("Failed to load {}", CONFIG_DIR);
            set(true, 0, 0);
        }
    }

    public static void set(boolean v, double x, double y) {
        try (OutputStream outputstream = Files.newOutputStream(CONFIG_DIR)) {
            Properties properties = new Properties();
            properties.setProperty("hudVisiblity", "" + v);
            properties.setProperty("hudX", "" + x);
            properties.setProperty("hudY", "" + y);
            properties.store(outputstream, "Any bad change to this file will cause it to reset all values to default.");
        } catch (Exception e1) {
            LOG.warn("Failed to save {}", CONFIG_DIR, e1);
        }
        hudVisiblity = v;
        hudX = x;
        hudY = y;
    }
}
