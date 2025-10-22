package mods.railcraft.util;

import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
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

  public static BlockPos blockPosContaining(double x, double y, double z) {
    return new BlockPos(Mth.floor(x), Mth.floor(y), Mth.floor(z));
  }

  public static BlockPos blockPosContaining(Position pos) {
    return blockPosContaining(pos.x(), pos.y(), pos.z());
  }

  public static Vec3 atLowerCornerWithOffset(Vec3i vec, double x, double y, double z) {
    return new Vec3((double) vec.getX() + x, (double) vec.getY() + y, (double) vec.getZ() + z);
  }
}
