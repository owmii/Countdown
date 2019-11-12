package zeroneye.countdown.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.player.ServerPlayerEntity;
import zeroneye.countdown.api.TimeData;
import zeroneye.countdown.config.Config;
import zeroneye.lib.util.Server;
import zeroneye.lib.util.Text;

public class SubTime {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("time")
                .requires(cs -> cs.hasPermissionLevel(2))
                .then(Commands.literal("add")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("seconds", IntegerArgumentType.integer(0, TimeData.MAX_TIME))
                                        .executes(context -> {
                                            ServerPlayerEntity player = (ServerPlayerEntity) context.getArgument("player", EntitySelector.class).selectOne(context.getSource());
                                            if (Config.GENERAL.isGlobal.get()) {
                                                player.sendMessage(Text.format("message.countdown.no.add"));
                                                player.sendMessage(Text.format("message.countdown.try", ": /countdown time add all <seconds>"));
                                            } else {
                                                TimeData timeData = Server.getData(TimeData::new);
                                                int i = IntegerArgumentType.getInteger(context, "seconds");
                                                timeData.addPlayerTime(player.getUniqueID(), i, true);
                                                player.sendMessage(Text.format("message.countdown.added.player", i));
                                            }
                                            return 0;
                                        })
                                ))
                        .then(Commands.literal("all")
                                .then(Commands.argument("seconds", IntegerArgumentType.integer(0, TimeData.MAX_TIME))
                                        .executes(context -> {
                                            int i = IntegerArgumentType.getInteger(context, "seconds");
                                            TimeData timeData = Server.getData(TimeData::new);
                                            if (Config.GENERAL.isGlobal.get()) {
                                                timeData.addGlobalTime(i, true);
                                                Server.chatToAll((player, texts) -> texts.add(Text.format("message.countdown.added.global", i)));
                                            } else {
                                                timeData.playersCountdown.forEach((uuid, aLong) -> {
                                                    timeData.addPlayerTime(uuid, i, true);
                                                });
                                                Server.chatToAll((player, texts) -> texts.add(Text.format("message.countdown.added.player", i)));
                                            }
                                            return 0;
                                        })
                                ))
                )
                .then(Commands.literal("remove")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("seconds", IntegerArgumentType.integer(0, TimeData.MAX_TIME))
                                        .executes(context -> {
                                            ServerPlayerEntity player = (ServerPlayerEntity) context.getArgument("player", EntitySelector.class).selectOne(context.getSource());
                                            if (Config.GENERAL.isGlobal.get()) {
                                                player.sendMessage(Text.format("message.countdown.no.remove"));
                                                player.sendMessage(Text.format("message.countdown.try", ": /countdown time remove all <seconds>"));
                                            } else {
                                                TimeData timeData = Server.getData(TimeData::new);
                                                int i = IntegerArgumentType.getInteger(context, "seconds");
                                                timeData.addPlayerTime(player.getUniqueID(), -i, true);
                                                player.sendMessage(Text.format("message.countdown.removed.player", i));
                                            }
                                            return 0;
                                        })
                                ))
                        .then(Commands.literal("all")
                                .then(Commands.argument("seconds", IntegerArgumentType.integer(0, TimeData.MAX_TIME))
                                        .executes(context -> {
                                            int i = IntegerArgumentType.getInteger(context, "seconds");
                                            TimeData timeData = Server.getData(TimeData::new);
                                            if (Config.GENERAL.isGlobal.get()) {
                                                timeData.addGlobalTime(-i, true);
                                                Server.chatToAll((player, texts) -> texts.add(Text.format("message.countdown.removed.global", i)));
                                            } else {
                                                timeData.playersCountdown.forEach((uuid, aLong) -> {
                                                    timeData.addPlayerTime(uuid, -i, true);
                                                });
                                                Server.chatToAll((player, texts) -> texts.add(Text.format("message.countdown.removed.player", i)));
                                            }
                                            return 0;
                                        })
                                ))
                )
                .then(Commands.literal("set")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("seconds", IntegerArgumentType.integer(0, TimeData.MAX_TIME))
                                        .executes(context -> {
                                            ServerPlayerEntity player = (ServerPlayerEntity) context.getArgument("player", EntitySelector.class).selectOne(context.getSource());
                                            if (Config.GENERAL.isGlobal.get()) {
                                                player.sendMessage(Text.format("message.countdown.no.set"));
                                                player.sendMessage(Text.format("message.countdown.try", ": /countdown time set all <seconds>"));
                                            } else {
                                                TimeData timeData = Server.getData(TimeData::new);
                                                int i = IntegerArgumentType.getInteger(context, "seconds");
                                                timeData.setPlayerTime(player.getUniqueID(), i, true);
                                                player.sendMessage(Text.format("message.countdown.set.player"));
                                            }
                                            return 0;
                                        })
                                ))
                        .then(Commands.literal("all")
                                .then(Commands.argument("seconds", IntegerArgumentType.integer(0, TimeData.MAX_TIME))
                                        .executes(context -> {
                                            int i = IntegerArgumentType.getInteger(context, "seconds");
                                            TimeData timeData = Server.getData(TimeData::new);
                                            if (Config.GENERAL.isGlobal.get()) {
                                                timeData.setGlobalTime(i, true);
                                                Server.chatToAll((player, texts) -> texts.add(Text.format("message.countdown.set.global")));
                                            } else {
                                                timeData.playersCountdown.forEach((uuid, aLong) -> {
                                                    timeData.setPlayerTime(uuid, i, true);
                                                });
                                                Server.chatToAll((player, texts) -> texts.add(Text.format("message.countdown.set.player")));
                                            }
                                            return 0;
                                        })
                                ))
                );


    }
}
