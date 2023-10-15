package mods.railcraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public final class RedstoneUtil {

  public static boolean hasRepeaterSignal(Level level, BlockPos pos, Direction from) {
    var state = level.getBlockState(pos.relative(from));
    return state.is(Blocks.REPEATER) && level.hasSignal(pos.relative(from), from);
  }

  public static boolean hasRepeaterSignal(Level level, BlockPos pos) {
    return Direction.Plane.HORIZONTAL.stream()
        .anyMatch(side -> hasRepeaterSignal(level, pos, side));
  }
}
