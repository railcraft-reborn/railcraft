package mods.railcraft.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class PowerUtil {

  public static final int NO_POWER = 0;
  public static final int FULL_POWER = 15;

  public static boolean hasRepeaterSignal(World world, BlockPos pos, Direction from) {
    Block block = world.getBlockState(pos.relative(from)).getBlock();
    return block == Blocks.REPEATER && world.hasSignal(pos, from);
  }

  public static boolean hasRepeaterSignal(World world, BlockPos pos) {
    return Direction.Plane.HORIZONTAL.stream()
        .anyMatch(side -> hasRepeaterSignal(world, pos, side));
  }
}
