package mods.railcraft.util;

import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class RayTraceUtil {

  private RayTraceUtil() {}

  /**
   * Same as {@link net.minecraft.block.Block#rayTrace(BlockPos, Vector3d, Vector3d, AxisAlignedBB)}
   */
  public static @Nullable HitResult rayTrace(BlockPos pos, Vec3 start, Vec3 end,
      VoxelShape shape) {
    Vec3 vec3d = start.subtract(pos.getX(), pos.getY(), pos.getZ());
    Vec3 vec3d1 = end.subtract(pos.getX(), pos.getY(), pos.getZ());
    BlockHitResult raytraceresult = shape.clip(vec3d, vec3d1, pos);
    return raytraceresult == null ? null
        : new BlockHitResult(
            raytraceresult.getLocation().add(pos.getX(), pos.getY(), pos.getZ()),
            raytraceresult.getDirection(),
            pos, raytraceresult.isInside());
  }

  public static @Nullable HitResult rayTracePlayerLook(Player player) {
    float reachAttribute = (float) player
        .getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();
    double reachDistance = player.isCreative() ? reachAttribute : reachAttribute - 0.5F;
    HitResult hitResult = player.pick(reachDistance, 1.0F, false);
    Vec3 eyePosition = player.getEyePosition(1.0F);
    boolean flag = !player.isCreative() && reachDistance > 3.0D;
    double distance = player.isCreative() ? 6.0D : reachDistance;
    if (player.isCreative()) {
      reachDistance = distance;
    }

    distance = distance * distance;
    if (hitResult != null) {
      distance = hitResult.getLocation().distanceToSqr(eyePosition);
    }

    Vec3 viewVector = player.getViewVector(1.0F);
    Vec3 reachPosition = eyePosition.add(
        viewVector.x * reachDistance, viewVector.y * reachDistance, viewVector.z * reachDistance);
    AABB boundingBox = player.getBoundingBox()
        .expandTowards(viewVector.scale(reachDistance))
        .inflate(1.0D, 1.0D, 1.0D);
    EntityHitResult entityRayTraceResult = ProjectileUtil.getEntityHitResult(player,
        eyePosition, reachPosition, boundingBox,
        entity -> !entity.isSpectator() && entity.isPickable(), distance);
    if (entityRayTraceResult != null) {
      Vec3 entityHitLocation = entityRayTraceResult.getLocation();
      double entityHitDistance = eyePosition.distanceToSqr(entityHitLocation);
      if (flag && entityHitDistance > 9.0D) {
        return BlockHitResult.miss(entityHitLocation,
            Direction.getNearest(viewVector.x, viewVector.y, viewVector.z),
            new BlockPos(entityHitLocation));
      } else if (entityHitDistance < distance || hitResult == null) {
        return entityRayTraceResult;
      }
    }
    return hitResult;
  }

  /**
   * Performs a ray trace to determine which side of the block is under the cursor.
   *
   * @param player PlayerEntity
   * @return a side value 0-5
   */
  public static @Nullable Direction getCurrentMousedOverSide(Player player) {
    var hitResult = rayTracePlayerLook(player);
    if (hitResult instanceof BlockHitResult blockHitResult) {
      return blockHitResult.getDirection();
    }
    return null;
  }
}
