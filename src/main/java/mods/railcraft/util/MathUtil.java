package mods.railcraft.util;

import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

public final class MathUtil {

  public static float getDistanceBetweenAngles(float angle1, float angle2) {
    angle1 = normalizeAngle(angle1);
    angle2 = normalizeAngle(angle2);
    return normalizeAngle(angle1 - angle2);
  }

  public static float normalizeAngle(float angle) {
    while (angle < -180F)
      angle += 360F;
    while (angle > 180F)
      angle -= 360F;
    return angle;
  }

  public static boolean nearZero(double f) {
    return Math.abs(f) < 0.001;
  }

  public static BlockPos centroid(Collection<? extends Vec3i> points) {
    double x = 0;
    double y = 0;
    double z = 0;
    for (Vec3i pos : points) {
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
