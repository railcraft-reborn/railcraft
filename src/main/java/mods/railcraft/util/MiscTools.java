package mods.railcraft.util;

import java.util.Arrays;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public final class MiscTools {

  public static final Random RANDOM = new Random();

  public static String cleanTag(String tag) {
    return tag.replaceAll("[Rr]ailcraft\\p{Punct}", "").replaceFirst("^tile\\.", "")
        .replaceFirst("^item\\.", "");
  }

  /**
   * Same as {@link net.minecraft.block.Block#rayTrace(BlockPos, Vector3d, Vector3d, AxisAlignedBB)}
   */
  public static @Nullable RayTraceResult rayTrace(BlockPos pos, Vector3d start, Vector3d end,
      VoxelShape shape) {
    Vector3d vec3d = start.subtract(pos.getX(), pos.getY(), pos.getZ());
    Vector3d vec3d1 = end.subtract(pos.getX(), pos.getY(), pos.getZ());
    BlockRayTraceResult raytraceresult = shape.clip(vec3d, vec3d1, pos);
    return raytraceresult == null ? null
        : new BlockRayTraceResult(
            raytraceresult.getLocation().add(pos.getX(), pos.getY(), pos.getZ()),
            raytraceresult.getDirection(),
            pos, raytraceresult.isInside());
  }

  public static @Nullable RayTraceResult rayTracePlayerLook(PlayerEntity player) {
    float reachAttribute = (float) player
        .getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();
    double reachDistance = player.isCreative() ? reachAttribute : reachAttribute - 0.5F;
    RayTraceResult hitResult = player.pick(reachDistance, 1.0F, false);
    Vector3d eyePosition = player.getEyePosition(1.0F);
    boolean flag = !player.isCreative() && reachDistance > 3.0D;
    double distance = player.isCreative() ? 6.0D : reachDistance;
    if (player.isCreative()) {
      reachDistance = distance;
    }

    distance = distance * distance;
    if (hitResult != null) {
      distance = hitResult.getLocation().distanceToSqr(eyePosition);
    }

    Vector3d viewVector = player.getViewVector(1.0F);
    Vector3d reachPosition = eyePosition.add(
        viewVector.x * reachDistance, viewVector.y * reachDistance, viewVector.z * reachDistance);
    AxisAlignedBB boundingBox = player.getBoundingBox()
        .expandTowards(viewVector.scale(reachDistance))
        .inflate(1.0D, 1.0D, 1.0D);
    EntityRayTraceResult entityRayTraceResult = ProjectileHelper.getEntityHitResult(player,
        eyePosition, reachPosition, boundingBox,
        entity -> !entity.isSpectator() && entity.isPickable(), distance);
    if (entityRayTraceResult != null) {
      Vector3d entityHitLocation = entityRayTraceResult.getLocation();
      double entityHitDistance = eyePosition.distanceToSqr(entityHitLocation);
      if (flag && entityHitDistance > 9.0D) {
        return BlockRayTraceResult.miss(entityHitLocation,
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
  public static @Nullable Direction getCurrentMousedOverSide(PlayerEntity player) {
    RayTraceResult mouseOver = rayTracePlayerLook(player);
    if (mouseOver instanceof BlockRayTraceResult)
      return ((BlockRayTraceResult) mouseOver).getDirection();
    return null;
  }

  /**
   * Returns the side closest to the player. Used in placement logic for blocks.
   *
   * @return a side
   */
  public static Direction getSideFacingPlayer(LivingEntity entity) {
    return Direction.orderedByNearest(entity)[0];
  }

  public static @Nullable Direction getSideFacingTrack(World world, BlockPos pos) {
    return Arrays.stream(Direction.values())
        .filter(dir -> AbstractRailBlock.isRail(world, pos.relative(dir)))
        .findFirst()
        .orElse(null);
  }

  public static boolean areCoordinatesOnSide(BlockPos start, BlockPos end, Direction side) {
    return start.relative(side).equals(end);
  }

  private MiscTools() {}

}
