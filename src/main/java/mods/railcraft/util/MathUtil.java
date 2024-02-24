package mods.railcraft.util;

import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public final class MathUtil {

  public static boolean nearZero(double f) {
    return Math.abs(f) < 0.001;
  }

  public static Vec3 centroid(Collection<? extends Vec3i> points) {
    if (points.isEmpty()) {
      return Vec3.ZERO;
    }
    double x = 0;
    double y = 0;
    double z = 0;
    for (var pos : points) {
      x += pos.getX() + 0.5;
      y += pos.getY() + 0.5;
      z += pos.getZ() + 0.5;
    }
    int size = points.size();
    x /= size;
    y /= size;
    z /= size;
    return new Vec3(x, y, z);
  }
}
