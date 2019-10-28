package xieao.countdown.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import xieao.countdown.world.TimeData;
import xieao.lib.util.Server;

import java.util.UUID;

import static xieao.countdown.config.Config.GENERAL;

@Mod.EventBusSubscriber
public class EventHandler {
    //TODO slowdown
    //TODO pause
    //TODO time +
    //TODO curses

    @SubscribeEvent
    public static void loggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            TimeData timeData = Server.getData(TimeData::new);
            if (!GENERAL.isGlobal.get()) {
                UUID id = event.getPlayer().getUniqueID();
                if (!timeData.playersTime.containsKey(id)) {
                    timeData.setPlayerTime(id, GENERAL.time.get(), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (event.phase == TickEvent.Phase.END) {
            if (event.side == LogicalSide.SERVER) {
                TimeData timeData = Server.getData(TimeData::new);
                if (!GENERAL.isGlobal.get()) {
                    UUID id = player.getUniqueID();
                    if (timeData.playersTime.containsKey(id)) {
                        long time = timeData.playersTime.get(id);
                        if (time > 0 && player.world.getGameTime() % 20 == 0) {
                            timeData.setPlayerTime(id, time - 1, true);
                        } else if (time <= 0) {
                            gameOver(player);
                        }
                    }
                } else if (timeData.globalTime <= 0) {
                    gameOver(player);
                }
            }
        }
    }

    public static void gameOver(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity player1 = (ServerPlayerEntity) player;
            if (!player1.isSpectator() && player1.interactionManager.getGameType() == GameType.SURVIVAL) {
                player1.setGameType(GameType.SPECTATOR);
                player1.getServerWorld().getGameRules().get(GameRules.SPECTATORS_GENERATE_CHUNKS).set(false, player1.server);
            }
        }
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        TimeData timeData = Server.getData(TimeData::new);
        if (GENERAL.isGlobal.get()) {
            long time = timeData.globalTime;
            if (time > 0 && Server.hasPlayers()) {
                Server.getWorld(0).ifPresent(world -> {
                    if (world.getGameTime() % 20 == 0) {
                        timeData.setGlobalTime(time - 1, true);
                    }
                });
            }
        }
    }
}
