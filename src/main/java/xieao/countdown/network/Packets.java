package xieao.countdown.network;

import xieao.countdown.network.packet.SetTime;
import xieao.lib.Lollipop;

public class Packets {
    public static void register() {
        Lollipop.NET.register(SetTime.class, SetTime::encode, SetTime::decode, SetTime::handle);
    }
}
