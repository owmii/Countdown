package zeroneye.countdown.command.impl;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import zeroneye.countdown.api.TimeData;
import zeroneye.countdown.config.Config;
import zeroneye.lib.util.Server;

public class SubRestart {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("restart")
                .requires(cs -> cs.hasPermissionLevel(2))
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            ServerPlayerEntity player = (ServerPlayerEntity) context.getArgument("player", EntitySelector.class).selectOne(context.getSource());
                            if (Config.GENERAL.isGlobal.get()) {
                                player.sendMessage(new TranslationTextComponent("message.countdown.no.restart").applyTextStyle(TextFormatting.RED));
                                player.sendMessage(new TranslationTextComponent("message.countdown.try", ": /countdown restart all").applyTextStyle(TextFormatting.RED));
                            } else {
                                TimeData timeData = Server.getData(TimeData::new);
                                timeData.playersCountdown.put(player.getUniqueID(), Config.GENERAL.time.get());
                                player.sendMessage(new TranslationTextComponent("message.countdown.restarted.player").applyTextStyle(TextFormatting.DARK_AQUA));
                            }
                            return 0;
                        }))
                .then(Commands.literal("all")
                        .executes(context -> {
                            TimeData timeData = Server.getData(TimeData::new);
                            if (Config.GENERAL.isGlobal.get()) {
                                timeData.setGlobalTime(Config.GENERAL.time.get(), true);
                                Server.chatToAll((player, texts) -> texts.add(new TranslationTextComponent("message.countdown.restarted.global").applyTextStyle(TextFormatting.DARK_AQUA)));
                            } else {
                                timeData.playersCountdown.forEach((uuid, aLong) -> {
                                    timeData.playersCountdown.put(uuid, Config.GENERAL.time.get());
                                });
                                Server.chatToAll((player, texts) -> texts.add(new TranslationTextComponent("message.countdown.restarted.player").applyTextStyle(TextFormatting.DARK_AQUA)));
                            }
                            return 0;
                        }));
    }
}
