package zeroneye.countdown.world.gen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.IPlantable;
import zeroneye.countdown.block.IBlocks;

import java.util.Random;
import java.util.function.Function;

public class TimeFeature extends Feature<NoFeatureConfig> {
    private static final Block[] BLOCKS = {IBlocks.PAUSE, IBlocks.SLOW_DOWN, IBlocks.TIME_PLUS};

    public TimeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        BlockState state = world.getBlockState(pos);
        Block block = BLOCKS[rand.nextInt(BLOCKS.length)];
        if ((world.isAirBlock(pos) || state.getBlock() instanceof IPlantable) && block.isValidPosition(state, world, pos)) {
            world.setBlockState(pos, block.getDefaultState(), 2);
        }
        return false;
    }
}
