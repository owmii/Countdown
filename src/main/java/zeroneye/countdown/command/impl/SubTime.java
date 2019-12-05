package zeroneye.countdown.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
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
                                                player.sendMessage(new TranslationTextComponent("message.countdown.no.add").applyTextStyle(TextFormatting.RED));
                                                player.sendMessage(new TranslationTextComponent("message.countdown.try", ": /countdown time add all <seconds>").applyTextStyle(TextFormatting.RED));
                                            } else {
                                                TimeData timeData = Server.getData(TimeData::new);
                                                int i = IntegerArgumentType.getInteger(context, "seconds");
                                                timeData.addPlayerTime(player.getUniqueID(), i, true);
                                                player.sendMessage(new TranslationTextComponent("message.countdown.added.player", i).applyTextStyle(TextFormatting.DARK_AQUA));
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
                                                Server.chatToAll((player, texts) -> texts.add(new TranslationTextComponent("message.countdown.added.global", i).applyTextStyle(TextFormatting.DARK_AQUA)));
                                            } else {
                                                timeData.playersCountdown.forEach((uuid, aLong) -> {
                                                    timeData.addPlayerTime(uuid, i, true);
                                                });
                                                Server.chatToAll((player, texts) -> texts.add(new TranslationTextComponent("message.countdown.added.player", i).applyTextStyle(TextFormatting.DARK_AQUA)));
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
                                                player.sendMessage(new TranslationTextComponent("message.countdown.no.remove").applyTextStyle(TextFormatting.RED));
                                                player.sendMessage(new TranslationTextComponent("message.countdown.try", ": /countdown time remove all <seconds>").applyTextStyle(TextFormatting.RED));
                                            } else {
                                                TimeData timeData = Server.getData(TimeData::new);
                                                int i = IntegerArgumentType.getInteger(context, "seconds");
                                                timeData.addPlayerTime(player.getUniqueID(), -i, true);
                                                player.sendMessage(new TranslationTextComponent("message.countdown.removed.player", i).applyTextStyle(TextFormatting.RED));
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
                                                Server.chatToAll((player, texts) -> texts.add(new TranslationTextComponent("message.countdown.removed.global", i).applyTextStyle(TextFormatting.RED)));
                                            } else {
                                                timeData.playersCountdown.forEach((uuid, aLong) -> {
                                                    timeData.addPlayerTime(uuid, -i, true);
                                                });
                                                Server.chatToAll((player, texts) -> texts.add(new TranslationTextComponent("message.countdown.removed.player", i).applyTextStyle(TextFormatting.RED)));
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
                                                player.sendMessage(new TranslationTextComponent("message.countdown.no.set").applyTextStyle(TextFormatting.RED));
                                                player.sendMessage(new TranslationTextComponent("message.countdown.try", ": /countdown time set all <seconds>").applyTextStyle(TextFormatting.RED));
                                            } else {
                                                TimeData timeData = Server.getData(TimeData::new);
                                                int i = IntegerArgumentType.getInteger(context, "seconds");
                                                timeData.setPlayerTime(player.getUniqueID(), i, true);
                                                player.sendMessage(new TranslationTextComponent("message.countdown.set.player").applyTextStyle(TextFormatting.DARK_AQUA));
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
                                                Server.chatToAll((player, texts) -> texts.add(new TranslationTextComponent("message.countdown.set.global").applyTextStyle(TextFormatting.DARK_AQUA)));
                                            } else {
                                                timeData.playersCountdown.forEach((uuid, aLong) -> {
                                                    timeData.setPlayerTime(uuid, i, true);
                                                });
                                                Server.chatToAll((player, texts) -> texts.add(new TranslationTextComponent("message.countdown.set.player").applyTextStyle(TextFormatting.DARK_AQUA)));
                                            }
                                            return 0;
                                        })
                                ))
                );


    }
}
