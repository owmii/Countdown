package zeroneye.countdown.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zeroneye.countdown.api.TimeData;
import zeroneye.countdown.config.Config;
import zeroneye.countdown.potion.IEffects;
import zeroneye.lib.block.BlockBase;
import zeroneye.lib.client.particle.Effect;
import zeroneye.lib.client.particle.Effects;
import zeroneye.lib.util.Server;
import zeroneye.lib.util.Text;
import zeroneye.lib.util.math.V3d;

import java.util.Random;

public class TimeBlock extends BlockBase {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private final int color;

    public TimeBlock(Properties properties, int color) {
        super(properties);
        this.color = color;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof PlayerEntity) {
            TimeData timeData = Server.getData(TimeData::new);
            Random rand = worldIn.rand;
            if (this == IBlocks.TIME_PLUS) {
                int i = rand.nextInt(Config.GENERAL.timePlusMin.get() + (Config.GENERAL.timePlusMax.get() - Config.GENERAL.timePlusMin.get()));
                if (Config.GENERAL.isGlobal.get()) {
                    timeData.addGlobalTime(i, true);
                    Server.chatToAll((player, texts) -> texts.add(Text.format("message.countdown.added.global", i)));
                } else {
                    timeData.addPlayerTime(entityIn.getUniqueID(), i, true);
                    entityIn.sendMessage(Text.format("message.countdown.added.player", i));
                }
            } else if (this == IBlocks.PAUSE) {
                ((PlayerEntity) entityIn).addPotionEffect(new EffectInstance(IEffects.PAUSE, 200 + rand.nextInt(9800), 0, false, false));
            } else if (this == IBlocks.SLOW_DOWN) {
                ((PlayerEntity) entityIn).addPotionEffect(new EffectInstance(IEffects.SLOW_DOWN, 200 + rand.nextInt(9800), rand.nextInt(4), false, false));
            }
            worldIn.removeBlock(pos, false);
        }
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getPackedLightmapCoords(BlockState state, IEnviromentBlockReader worldIn, BlockPos pos) {
        return 15728880;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        Effects.create(Effect.GLOW_DENS, worldIn, new V3d(pos)
                .east(0.5D + Math.random() * 0.3D - Math.random() * 0.3D)
                .south(0.5D + Math.random() * 0.3D - Math.random() * 0.3D)
                .up(0.15D)).maxAge(50).color(this.color)
                .blend().alpha(0.7F, 1).gravity(-0.03F).scale(2, 1, 0).spawn();
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return func_220055_a(worldIn, pos.down(), Direction.UP);
    }
}
