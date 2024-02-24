package mods.railcraft.util;

import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

public final class MathUtil {

  public static boolean nearZero(double f) {
    return Math.abs(f) < 0.001;
  }

  public static BlockPos centroid(Collection<? extends Vec3i> points) {
    if (points.isEmpty()) {
      return BlockPos.ZERO;
    }
    double x = 0;
    double y = 0;
    double z = 0;
    for (var pos : points) {
      x += pos.getX();
      y += pos.getY();
      z += pos.getZ();
    }
    int size = points.size();
    x /= size;
    y /= size;
    z /= size;
    return BlockPos.containing(x, y, z);
  }
}
