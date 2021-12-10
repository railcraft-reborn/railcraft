package mods.railcraft.util;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

/**
 * Created by CovertJaguar on 10/30/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public final class MathTools {

  public static final UUID NIL_UUID = new UUID(0, 0);
  private static final ThreadLocalRandom random = ThreadLocalRandom.current();

  public static boolean isNil(UUID uuid) {
    return NIL_UUID.equals(uuid);
  }

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
    return new BlockPos(x, y, z);
  }

  public static double gaussian() {
    return random.nextGaussian();
  }

  public static float nextFloat() {
    return random.nextFloat();
  }

  public static float signedFloat() {
    return signedFloat(random);
  }

  public static float signedFloat(Random random) {
    return unit(random) * random.nextFloat();
  }

  public static int unit() {
    return unit(random);
  }

  /**
   * Returns {@code 1} or {@code -1} by random.
   *
   * @param random the seed
   */
  public static int unit(Random random) {
    return random.nextInt(2) * 2 - 1;
  }
}
