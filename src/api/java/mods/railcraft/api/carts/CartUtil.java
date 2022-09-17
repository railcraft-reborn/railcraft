/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.carts;

import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class CartUtil {

  private static LinkageManager linkageManager;
  private static TrainTransferHelper transferHelper;

  public static TrainTransferHelper transferHelper() {
    Objects.requireNonNull(transferHelper);
    return transferHelper;
  }

  @ApiStatus.Internal
  public static void _setTransferHelper(TrainTransferHelper transferHelper) {
    if (CartUtil.transferHelper != null) {
      throw new IllegalStateException("transferHelper already set.");
    }
    CartUtil.transferHelper = transferHelper;
  }

  public static LinkageManager linkageManager() {
    Objects.requireNonNull(linkageManager);
    return linkageManager;
  }

  @ApiStatus.Internal
  public static void _setLinkageManager(LinkageManager linkageManager) {
    if (CartUtil.linkageManager != null) {
      throw new IllegalStateException("linkageManager already set.");
    }
    CartUtil.linkageManager = linkageManager;
  }

  public static List<AbstractMinecart> getMinecartsIn(Level level, BlockPos p1, BlockPos p2) {
    return getMinecartsIn(level, p1, p2, AbstractMinecart.class);
  }

  public static <T extends AbstractMinecart> List<T> getMinecartsIn(Level level, BlockPos p1,
      BlockPos p2, Class<T> type) {
    return level.getEntitiesOfClass(type,
        new AABB(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ()),
        EntitySelector.ENTITY_STILL_ALIVE);
  }

  /**
   * Returns the cart's "speed". It is not capped by the carts max speed, it instead returns the
   * cart's "potential" speed. Used by collision and linkage logic. Do not use this to determine how
   * fast a cart is currently moving.
   *
   * @return speed
   */
  public static double getCartSpeedUncapped(Vec3 deltaMovement) {
    return Math.sqrt(getCartSpeedUncappedSquared(deltaMovement));
  }

  public static double getCartSpeedUncappedSquared(Vec3 deltaMovement) {
    return deltaMovement.x() * deltaMovement.x() + deltaMovement.z() * deltaMovement.z();
  }

  public static boolean cartVelocityIsLessThan(Vec3 deltaMovement, float velocity) {
    return Math.abs(deltaMovement.x()) < velocity
        && Math.abs(deltaMovement.z()) < velocity;
  }

  private CartUtil() {}
}
