package zeroneye.countdown.network;

import zeroneye.countdown.network.packet.SetTime;
import zeroneye.lib.Lollipop;

public class Packets {
    public static void register() {
        Lollipop.NET.register(SetTime.class, SetTime::encode, SetTime::decode, SetTime::handle);
    }
}
