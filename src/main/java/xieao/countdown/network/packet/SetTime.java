package xieao.countdown.network.packet;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import xieao.countdown.client.HudHandler;
import xieao.countdown.potion.IEffects;
import xieao.lib.util.Server;

import java.util.function.Supplier;

public class SetTime {
    private long time;

    public SetTime(long time) {
        this.time = time;
    }

    public static void encode(SetTime msg, PacketBuffer buffer) {
        final boolean[] flags = new boolean[2];
        for (ServerPlayerEntity player : Server.get().getPlayerList().getPlayers()) {
            if (player.isPotionActive(IEffects.SLOW_DOWN)) {
                flags[0] = true;
            }
            if (player.isPotionActive(IEffects.PAUSE)) {
                flags[1] = true;
                if (flags[0]) {
                    break;
                }
            }
        }
        buffer.writeBoolean(flags[0]);
        buffer.writeBoolean(flags[1]);
        buffer.writeLong(msg.time);
    }

    public static SetTime decode(PacketBuffer buffer) {
        HudHandler.potionFlags[0] = buffer.readBoolean();
        HudHandler.potionFlags[1] = buffer.readBoolean();
        return new SetTime(buffer.readLong());
    }

    public static void handle(SetTime msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            HudHandler.time = msg.time;
            if (msg.time > 10) {
                HudHandler.color = 0xffffff;
            }
            System.out.println(msg.time);
        });
        ctx.get().setPacketHandled(true);
    }
}
