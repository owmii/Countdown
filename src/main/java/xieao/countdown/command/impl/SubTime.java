package xieao.countdown.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import xieao.countdown.config.Config;
import xieao.countdown.world.TimeData;
import xieao.lib.util.Server;

public class SubTime {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("time")
                .requires(cs -> cs.hasPermissionLevel(2))
                .then(Commands.literal("add")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("seconds", IntegerArgumentType.integer(0, TimeData.MAX_TIME))
                                        .executes(context -> {
                                            ServerPlayerEntity player = (ServerPlayerEntity) context.getArgument("player", EntitySelector.class).selectOne(context.getSource());
                                            if (!Config.GENERAL.isGlobal.get()) {
                                                TimeData timeData = Server.getData(TimeData::new);
                                                int i = IntegerArgumentType.getInteger(context, "seconds");
                                                timeData.addPlayerTime(player.getUniqueID(), i, true);
                                            } else {
                                                player.sendMessage(new StringTextComponent(TextFormatting.RED + "You can't add time to a single player in globale mode!"));
                                                player.sendMessage(new StringTextComponent(TextFormatting.RED + "try: /countdown add all <seconds>"));
                                            }
                                            return 0;
                                        })
                                ))
                        .then(Commands.literal("all")
                                .then(Commands.argument("seconds", IntegerArgumentType.integer(0, TimeData.MAX_TIME))
                                        .executes(context -> {
                                            int i = IntegerArgumentType.getInteger(context, "seconds");
                                            TimeData timeData = Server.getData(TimeData::new);
                                            timeData.addGlobalTime(i, true);
                                            timeData.playersTime.forEach((uuid, aLong) -> {
                                                timeData.addPlayerTime(uuid, i, true);
                                            });
                                            return 0;
                                        })
                                ))
                )
                .then(Commands.literal("load")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("seconds", IntegerArgumentType.integer(0, TimeData.MAX_TIME))
                                        .executes(context -> {
                                            ServerPlayerEntity player = (ServerPlayerEntity) context.getArgument("player", EntitySelector.class).selectOne(context.getSource());
                                            if (!Config.GENERAL.isGlobal.get()) {
                                                TimeData timeData = Server.getData(TimeData::new);
                                                int i = IntegerArgumentType.getInteger(context, "seconds");
                                                timeData.setPlayerTime(player.getUniqueID(), i, true);
                                            } else {
                                                player.sendMessage(new StringTextComponent(TextFormatting.RED + "You can't load time to a single player in globale mode!"));
                                                player.sendMessage(new StringTextComponent(TextFormatting.RED + "try: /countdown load all <seconds>"));
                                            }
                                            return 0;
                                        })
                                ))
                        .then(Commands.literal("all")
                                .then(Commands.argument("seconds", IntegerArgumentType.integer(0, TimeData.MAX_TIME))
                                        .executes(context -> {
                                            int i = IntegerArgumentType.getInteger(context, "seconds");
                                            TimeData timeData = Server.getData(TimeData::new);
                                            timeData.setGlobalTime(i, true);
                                            timeData.playersTime.forEach((uuid, aLong) -> {
                                                timeData.setPlayerTime(uuid, i, true);
                                            });
                                            return 0;
                                        })
                                ))
                );


    }
}
