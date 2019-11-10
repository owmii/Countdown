package zeroneye.countdown.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import zeroneye.countdown.api.TimeData;

public class Config {
    public static final Config GENERAL;
    public static final ForgeConfigSpec CONFIG_SPEC;

    public final ForgeConfigSpec.BooleanValue isGlobal;
    public final ForgeConfigSpec.LongValue time;

    public final ForgeConfigSpec.IntValue timePlusMin;
    public final ForgeConfigSpec.IntValue timePlusMax;
    public final ForgeConfigSpec.BooleanValue worldGen;
    public final ForgeConfigSpec.IntValue genChance;

    public Config(ForgeConfigSpec.Builder builder) {
        this.isGlobal = builder.comment(
                "If true all players will have the same countdown.",
                "If false each player will have its own countdown.").define("isGlobal", false);
        this.time = builder.comment(
                "", "How many seconds.",
                "Default: 7200 (2 Hours) max: " + TimeData.MAX_TIME + " (1 year).").defineInRange("seconds", 7200L, 1L, TimeData.MAX_TIME);
        this.timePlusMin = builder.comment(
                "", "Minimum and maximum of random time gotten by Time Plus blocks.")
                .defineInRange("timePlusMin", 60, 0, TimeData.MAX_TIME);
        this.timePlusMax = builder.defineInRange("timePlusMax", 1200, 1, TimeData.MAX_TIME);
        this.worldGen = builder.comment("", "Enable World gen.")
                .define("worldGen", true);
        this.genChance = builder.comment("World gen chance, big number = more chance.")
                .defineInRange("genChance", 10, 1, 500);
    }

    static {
        final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
        CONFIG_SPEC = specPair.getRight();
        GENERAL = specPair.getLeft();
    }
}
