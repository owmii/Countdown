package xieao.countdown.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import xieao.countdown.Countdown;

public class MainCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(Countdown.MOD_ID)
                        .then(SubRestart.register())
                        .then(SubEditeTime.register())
        );
    }
}
