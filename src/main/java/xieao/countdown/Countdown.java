package xieao.countdown;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import xieao.countdown.command.MainCommand;
import xieao.countdown.config.Config;
import xieao.countdown.network.Packets;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static xieao.lib.Lollipop.addEventListener;
import static xieao.lib.Lollipop.addModListener;

@Mod(Countdown.MOD_ID)
public class Countdown {
    public static final String MOD_ID = "countdown";

    public Countdown() {
        Path dir = FMLPaths.CONFIGDIR.get();
        Path configDir = Paths.get(dir.toAbsolutePath().toString(), MOD_ID);

        try {
            Files.createDirectory(configDir);
        } catch (Exception ignored) {
        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.CONFIG_SPEC, MOD_ID + "/common.toml");
        addModListener(this::commonSetup);
        addEventListener(this::serverStarting);
    }

    void commonSetup(FMLCommonSetupEvent event) {
        Packets.register();
    }

    void serverStarting(FMLServerStartingEvent evt) {
        MainCommand.register(evt.getCommandDispatcher());
    }
}