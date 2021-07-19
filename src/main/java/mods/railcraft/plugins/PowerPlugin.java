package mods.railcraft.plugins;

import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class PowerPlugin {

  public static final int NO_POWER = 0;
  public static final int FULL_POWER = 15;

  public static boolean isBlockBeingPowered(World world, BlockPos pos) {
    return world.hasNeighborSignal(pos);
  }

  public static boolean hasSignal(World world, BlockPos pos, Direction from) {
    return world.hasSignal(pos.relative(from), from);
  }

  public static int getSignal(World world, BlockPos pos, Direction from) {
    return world.getSignal(pos.relative(from), from);
  }

  public static boolean isBlockBeingPoweredByRepeater(World world, BlockPos pos, Direction from) {
    Block block = world.getBlockState(pos.relative(from)).getBlock();
    return block == Blocks.REPEATER && hasSignal(world, pos, from);
  }

  public static boolean isBlockBeingPoweredByRepeater(World world, BlockPos pos) {
    return Direction.Plane.HORIZONTAL.stream()
        .anyMatch(side -> isBlockBeingPoweredByRepeater(world, pos, side));
  }

  public static boolean isRedstonePowered(World world, BlockPos pos) {
    return Arrays.stream(Direction.values())
        .anyMatch(side -> hasSignal(world, pos, 0, side)
            || hasSignal(world, pos, -1, side));
  }

  private static boolean hasSignal(World world, BlockPos pos, int yOffset,
      Direction side) {
    BlockPos wirePos = pos.above(yOffset).relative(side);
    BlockState state = world.getBlockState(wirePos);
    return state.getSignal(world, wirePos, side) > 0;
  }
}
