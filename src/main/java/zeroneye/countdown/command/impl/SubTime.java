package zeroneye.countdown.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
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
                                                player.sendMessage(new StringTextComponent(TextFormatting.RED + "You can't add time to a single player in globale moder!"));
                                                player.sendMessage(new StringTextComponent(TextFormatting.RED + "try: /countdown time add all <seconds>"));
                                            } else {
                                                TimeData timeData = Server.getData(TimeData::new);
                                                int i = IntegerArgumentType.getInteger(context, "seconds");
                                                timeData.addPlayerTime(player.getUniqueID(), i, true);
                                                player.sendMessage(new StringTextComponent(TextFormatting.DARK_AQUA + "Added " + i + " seconds to your countdown timer!"));
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
                                                Server.chatToAll((player, texts) -> texts.add(new StringTextComponent(TextFormatting.DARK_AQUA + "Added " + i + " seconds to the global countdown timer!")));
                                            } else {
                                                timeData.playersCountdown.forEach((uuid, aLong) -> {
                                                    timeData.addPlayerTime(uuid, i, true);
                                                });
                                                Server.chatToAll((player, texts) -> texts.add(new StringTextComponent(TextFormatting.DARK_AQUA + "Added " + i + " seconds to your countdown timer!")));
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
                                                player.sendMessage(new StringTextComponent(TextFormatting.RED + "You can't remove time to a single player in globale mode!"));
                                                player.sendMessage(new StringTextComponent(TextFormatting.RED + "try: /countdown time remove all <seconds>"));
                                            } else {
                                                TimeData timeData = Server.getData(TimeData::new);
                                                int i = IntegerArgumentType.getInteger(context, "seconds");
                                                timeData.addPlayerTime(player.getUniqueID(), -i, true);
                                                player.sendMessage(new StringTextComponent(TextFormatting.RED + "Removed " + i + " seconds from your countdown timer!"));
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
                                                Server.chatToAll((player, texts) -> texts.add(new StringTextComponent(TextFormatting.RED + "Removed " + i + " seconds from the global countdown timer!")));
                                            } else {
                                                timeData.playersCountdown.forEach((uuid, aLong) -> {
                                                    timeData.addPlayerTime(uuid, -i, true);
                                                });
                                                Server.chatToAll((player, texts) -> texts.add(new StringTextComponent(TextFormatting.RED + "Removed " + i + " seconds from your countdown timer!")));
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
                                                player.sendMessage(new StringTextComponent(TextFormatting.RED + "You can't set time to a single player in globale mode!"));
                                                player.sendMessage(new StringTextComponent(TextFormatting.RED + "try: /countdown time set all <seconds>"));
                                            } else {
                                                TimeData timeData = Server.getData(TimeData::new);
                                                int i = IntegerArgumentType.getInteger(context, "seconds");
                                                timeData.setPlayerTime(player.getUniqueID(), i, true);
                                                player.sendMessage(new StringTextComponent(TextFormatting.DARK_AQUA + "Your countdown timer has been set to " + i + " seconds."));
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
                                                Server.chatToAll((player, texts) -> texts.add(new StringTextComponent(TextFormatting.DARK_AQUA + "The global timer has been set to " + i + " seconds.")));
                                            } else {
                                                timeData.playersCountdown.forEach((uuid, aLong) -> {
                                                    timeData.setPlayerTime(uuid, i, true);
                                                });
                                                Server.chatToAll((player, texts) -> texts.add(new StringTextComponent(TextFormatting.DARK_AQUA + "Your countdown countdown timer has been set to " + i + " seconds.")));
                                            }
                                            return 0;
                                        })
                                ))
                );


    }
}
