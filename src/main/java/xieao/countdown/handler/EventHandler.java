package xieao.countdown.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import xieao.countdown.api.event.CountdownEvent;
import xieao.countdown.potion.IEffects;
import xieao.countdown.world.TimeData;
import xieao.lib.util.Server;

import java.util.UUID;

import static xieao.countdown.config.Config.GENERAL;

@Mod.EventBusSubscriber
public class EventHandler {
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
                        CountdownEvent.Player countdownEvent = new CountdownEvent.Player(player, time);
                        MinecraftForge.EVENT_BUS.post(countdownEvent);
                        time = countdownEvent.seconds;

                        int speed = 20;
                        if (time > 0 && player.isPotionActive(IEffects.PAUSE)) return;
                        if (player.isPotionActive(IEffects.SLOW_DOWN)) {
                            EffectInstance effectInstance = player.getActivePotionEffect(IEffects.SLOW_DOWN);
                            if (effectInstance != null) {
                                speed += (10 * effectInstance.getAmplifier() + 1);
                            }
                        }

                        if (time > 0 && player.world.getGameTime() % speed == 0) {
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
            CountdownEvent.Global countdownEvent = new CountdownEvent.Global(time);
            MinecraftForge.EVENT_BUS.post(countdownEvent);
            time = countdownEvent.seconds;
            if (time > 0 && Server.hasPlayers()) {
                int speed = 20;

                for (PlayerEntity player : Server.get().getPlayerList().getPlayers()) {
                    if (player.isPotionActive(IEffects.PAUSE)) return;
                    if (player.isPotionActive(IEffects.SLOW_DOWN)) {
                        EffectInstance effectInstance = player.getActivePotionEffect(IEffects.SLOW_DOWN);
                        if (effectInstance != null) {
                            speed += (10 * effectInstance.getAmplifier() + 1);
                        }
                    }
                }

                long finalTime = time;
                int finalSpeed = speed;
                Server.getWorld(0).ifPresent(world -> {
                    if (world.getGameTime() % finalSpeed == 0) {
                        timeData.setGlobalTime(finalTime - 1, true);
                    }
                });
            }
        }
    }
}
