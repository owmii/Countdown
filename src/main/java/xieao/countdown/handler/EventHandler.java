package xieao.countdown.handler;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import xieao.countdown.api.TimeData;
import xieao.countdown.api.event.CountdownEvent;
import xieao.countdown.network.packet.SetTime;
import xieao.countdown.potion.IEffects;
import xieao.lib.Lollipop;
import xieao.lib.util.Server;

import java.util.UUID;

import static xieao.countdown.config.Config.GENERAL;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void loggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            TimeData timeData = Server.getData(TimeData::new);
            if (!GENERAL.isGlobal.get()) {
                UUID id = event.getPlayer().getUniqueID();
                if (!timeData.playersCountdown.containsKey(id)) {
                    timeData.setPlayerTime(id, GENERAL.time.get(), true);
                } else {
                    timeData.playerSync.add(event.getPlayer().getUniqueID());
                }
            } else {
                timeData.globalSync = true;
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
                    if (timeData.playersCountdown.containsKey(id)) {
                        long time = timeData.playersCountdown.get(id);

                        if (timeData.playerSync.contains(id)) {
                            Lollipop.NET.toClient(new SetTime(time), player);
                            timeData.playerSync.remove(id);
                            if (time > 0 && player.isSpectator()) {
                                player.setGameType(GameType.SURVIVAL);
                            }
                        }

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
                        if (player.isPotionActive(IEffects.FAST_FORWARD)) {
                            EffectInstance effectInstance = player.getActivePotionEffect(IEffects.FAST_FORWARD);
                            if (effectInstance != null) {
                                speed -= (5 * (effectInstance.getAmplifier() + 1));
                            }
                        }

                        if (time > 0 && player.world.getGameTime() % Math.max(speed, 1) == 0) {
                            timeData.addPlayerTime(id, -1, true);
                        } else if (time <= 0) {
                            gameOver(player, time);
                        }
                    }
                } else if (timeData.globalCountdown <= 0) {
                    gameOver(player, timeData.globalCountdown);
                }
            }
        }
    }

    public static void gameOver(PlayerEntity player, long time) {
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity player1 = (ServerPlayerEntity) player;
            CountdownEvent.GameOver countdownEvent = new CountdownEvent.GameOver(player, time);
            if (MinecraftForge.EVENT_BUS.post(countdownEvent)) return;
            if (!player1.isSpectator() && player1.interactionManager.getGameType() == GameType.SURVIVAL) {
                player1.setGameType(GameType.SPECTATOR);
                player1.getServerWorld().getGameRules().get(GameRules.SPECTATORS_GENERATE_CHUNKS).set(false, player1.server);
                player1.sendMessage(new StringTextComponent(TextFormatting.RED + I18n.format("message.countdown.gameover")));
            }
        }
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (GENERAL.isGlobal.get() && event.phase == TickEvent.Phase.START) {
            TimeData timeData = Server.getData(TimeData::new);
            long time = timeData.globalCountdown;

            if (timeData.globalSync) {
                Lollipop.NET.toAll(new SetTime(time));
                for (PlayerEntity player : Server.get().getPlayerList().getPlayers()) {
                    if (time > 0 && player.isSpectator()) {
                        player.setGameType(GameType.SURVIVAL);
                    }
                }
            }

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
                            speed += (10 * (effectInstance.getAmplifier() + 1));
                        }
                    }
                    if (player.isPotionActive(IEffects.FAST_FORWARD)) {
                        EffectInstance effectInstance = player.getActivePotionEffect(IEffects.FAST_FORWARD);
                        if (effectInstance != null) {
                            speed -= (5 * (effectInstance.getAmplifier() + 1));
                        }
                    }
                }

                int finalSpeed = Math.max(speed, 1);
                Server.getWorld(0).ifPresent(world -> {
                    if (world.getGameTime() % finalSpeed == 0) {
                        timeData.addGlobalTime(-1, true);
                    }
                });
            }
        }
    }
}
