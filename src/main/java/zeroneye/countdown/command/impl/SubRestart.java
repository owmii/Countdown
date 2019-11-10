package zeroneye.countdown.command.impl;

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

public class SubRestart {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("restart")
                .requires(cs -> cs.hasPermissionLevel(2))
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            ServerPlayerEntity player = (ServerPlayerEntity) context.getArgument("player", EntitySelector.class).selectOne(context.getSource());
                            if (!Config.GENERAL.isGlobal.get()) {
                                TimeData timeData = Server.getData(TimeData::new);
                                timeData.playersCountdown.put(player.getUniqueID(), Config.GENERAL.time.get());
                            } else {
                                player.sendMessage(new StringTextComponent(TextFormatting.RED + "You can't restart a single player in globale mode!"));
                                player.sendMessage(new StringTextComponent(TextFormatting.RED + "try: /countdown restart all"));
                            }
                            return 0;
                        }))
                .then(Commands.literal("all")
                        .executes(context -> {
                            TimeData timeData = Server.getData(TimeData::new);
                            timeData.globalCountdown = Config.GENERAL.time.get();
                            timeData.playersCountdown.forEach((uuid, aLong) -> {
                                timeData.playersCountdown.put(uuid, Config.GENERAL.time.get());
                            });
                            return 0;
                        }));
    }
}
