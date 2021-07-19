package mods.railcraft.world.level.block;

import java.util.Random;
import mods.railcraft.client.ClientEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class MagicOreBlock extends Block {

  public MagicOreBlock(Properties properties) {
    super(properties);
  }

  @Override
  public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
    return 15;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    super.animateTick(stateIn, worldIn, pos, rand);
    if (RailcraftBlocks.FIRESTONE.get() == stateIn.getBlock()) {
      BlockPos start = new BlockPos(pos.getX() - 10 + rand.nextInt(20), pos.getY(),
          pos.getZ() - 10 + rand.nextInt(20));
      Vector3d startPosition = new Vector3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 0.8, 0.5);
      Vector3d endPosition =
          new Vector3d(start.getX(), start.getY(), start.getZ()).add(0.5, 0.5, 0.5);
      ClientEffects.INSTANCE.fireSparkEffect(worldIn, startPosition, endPosition);
      spawnBurningFaceParticles(worldIn, pos);
    }
  }

  private void spawnBurningFaceParticles(World worldIn, BlockPos pos) {
    Random random = worldIn.getRandom();
    double pixel = 0.0625D;

    BlockState state = worldIn.getBlockState(pos);

    for (Direction facing : Direction.values()) {
      if (!Block.shouldRenderFace(state, worldIn, pos, facing))
        continue;

      double px = pos.getX();
      double py = pos.getY();
      double pz = pos.getZ();

      if (facing.getAxis() == Direction.Axis.X)
        px += pixel * facing.getStepX()
            + (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0 : 0.0);
      else
        px += random.nextFloat();

      if (facing.getAxis() == Direction.Axis.Y)
        py += pixel * facing.getStepY()
            + (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0 : 0.0);
      else
        py += random.nextFloat();

      if (facing.getAxis() == Direction.Axis.Z)
        pz += pixel * facing.getStepZ()
            + (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0 : 0.0);
      else
        pz += random.nextFloat();

      // worldIn.spawnParticle(EnumParticleTypes.FLAME, px, py, pz, 0.0D, 0.0D, 0.0D);
      // worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, px, py, pz, 0.0D, 0.0D, 0.0D);
    }
  }
}
