package zeroneye.countdown.command.impl;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
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
                                player.sendMessage(new StringTextComponent(TextFormatting.RED + I18n.format("message.countdown.no.restart")));
                                player.sendMessage(new StringTextComponent(TextFormatting.RED + I18n.format("message.countdown.try", ": /countdown restart all")));
                            } else {
                                TimeData timeData = Server.getData(TimeData::new);
                                timeData.playersCountdown.put(player.getUniqueID(), Config.GENERAL.time.get());
                                player.sendMessage(new StringTextComponent(TextFormatting.DARK_AQUA + I18n.format("message.countdown.restarted.player")));
                            }
                            return 0;
                        }))
                .then(Commands.literal("all")
                        .executes(context -> {
                            TimeData timeData = Server.getData(TimeData::new);
                            if (Config.GENERAL.isGlobal.get()) {
                                timeData.setGlobalTime(Config.GENERAL.time.get(), true);
                                Server.chatToAll((player, texts) -> texts.add(new StringTextComponent(TextFormatting.DARK_AQUA + I18n.format("message.countdown.restarted.global"))));
                            } else {
                                timeData.playersCountdown.forEach((uuid, aLong) -> {
                                    timeData.playersCountdown.put(uuid, Config.GENERAL.time.get());
                                });
                                Server.chatToAll((player, texts) -> texts.add(new StringTextComponent(TextFormatting.DARK_AQUA + I18n.format("message.countdown.restarted.player"))));
                            }
                            return 0;
                        }));
    }
}
