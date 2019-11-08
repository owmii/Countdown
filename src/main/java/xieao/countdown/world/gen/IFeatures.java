package xieao.countdown.world.gen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xieao.countdown.config.Config;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IFeatures {
    public static final List<Feature<?>> FEATURES = new ArrayList<>();
    public static final Feature<NoFeatureConfig> TIME;

    static {
        TIME = register("time", new TimeFeature(NoFeatureConfig::deserialize));
    }

    public static void register() {
        if (Config.GENERAL.worldGen.get()) {
            Biome.BIOMES.forEach(biome -> {
                ConfiguredFeature<?> configuredFeature = Biome.createDecoratedFeature(TIME, IFeatureConfig.NO_FEATURE_CONFIG,
                        Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(Config.GENERAL.genChance.get()));
                biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, configuredFeature);
            });
        }
    }

    static Feature<NoFeatureConfig> register(String id, Feature<NoFeatureConfig> feature) {
        feature.setRegistryName(id);
        FEATURES.add(feature);
        return feature;
    }

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Feature<?>> event) {
        FEATURES.forEach(feature -> event.getRegistry().register(feature));
    }
}