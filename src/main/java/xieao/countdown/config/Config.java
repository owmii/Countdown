package xieao.countdown.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import xieao.countdown.world.TimeData;

public class Config {
    public static final Config GENERAL;
    public static final ForgeConfigSpec CONFIG_SPEC;

    public final ForgeConfigSpec.BooleanValue isGlobal;
    public final ForgeConfigSpec.LongValue time;

    public Config(ForgeConfigSpec.Builder builder) {
        this.isGlobal = builder.comment(
                "If true all players will have the same countdown.",
                "If false each player will have its own countdown.").define("isGlobal", false);
        this.time = builder.comment(
                " ", "How many seconds.",
                "Default: 1800 (30 min) max: " + TimeData.MAX_TIME + " (1 year).").defineInRange("seconds", 1800L, 1L, TimeData.MAX_TIME);
    }

    static {
        final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
        CONFIG_SPEC = specPair.getRight();
        GENERAL = specPair.getLeft();
    }
}
