//package xieao.countdown.world.gen;
//
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.gen.GenerationStage;
//import net.minecraft.world.gen.feature.ConfiguredFeature;
//import net.minecraft.world.gen.feature.Feature;
//import net.minecraft.world.gen.feature.IFeatureConfig;
//import net.minecraft.world.gen.feature.NoFeatureConfig;
//import net.minecraft.world.gen.placement.FrequencyConfig;
//import net.minecraft.world.gen.placement.Placement;
//import net.minecraftforge.common.BiomeDictionary;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
//public class IFeatures {
//    public static final List<Feature<?>> FEATURES = new ArrayList<>();
//    public static final Feature<NoFeatureConfig> HOR_LOG;
//
//    static {
//        HOR_LOG = register("hor_log", new WoodFeature(NoFeatureConfig::deserialize));
//    }
//
//    public static void register() {
//        Biome.BIOMES.forEach(biome -> {
//            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST)
//                    || biome.getCategory() == Biome.Category.FOREST
//                    || BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)
//                    || biome.getCategory() == Biome.Category.SWAMP
//                    || BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)
//                    || biome.getCategory() == Biome.Category.SAVANNA) {
//                ConfiguredFeature<?> configuredFeature = Biome.createDecoratedFeature(HOR_LOG, IFeatureConfig.NO_FEATURE_CONFIG,
//                        Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(800));
//                biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, configuredFeature);
//            }
//        });
//    }
//
//    static Feature<NoFeatureConfig> register(String id, Feature<NoFeatureConfig> feature) {
//        feature.setRegistryName(id);
//        FEATURES.add(feature);
//        return feature;
//    }
//
//    @SubscribeEvent
//    public static void onRegistry(RegistryEvent.Register<Feature<?>> event) {
//        FEATURES.forEach(feature -> event.getRegistry().register(feature));
//    }
//}