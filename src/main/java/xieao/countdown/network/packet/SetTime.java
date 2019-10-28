package xieao.countdown.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import xieao.countdown.client.HudHandler;

import java.util.function.Supplier;

public class SetTime {
    private long time;

    public SetTime(long time) {
        this.time = time;
    }

    public static void encode(SetTime msg, PacketBuffer buffer) {
        buffer.writeLong(msg.time);
    }

    public static SetTime decode(PacketBuffer buffer) {
        return new SetTime(buffer.readLong());
    }

    public static void handle(SetTime msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            HudHandler.time = msg.time;
            if (msg.time > 10) {
                HudHandler.color = 0xffffff;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
