/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.carts;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
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

  // Most of these functions have been replaced internally by the EntitySearcher, but they remain
  // here in the API
  // for historic purposes and the fact that the EntitySearcher isn't part of the API.

  public static boolean isMinecartOnRailAt(Level world, BlockPos pos, float sensitivity) {
    return isMinecartOnRailAt(world, pos, sensitivity, AbstractMinecart.class);
  }

  public static boolean isMinecartOnRailAt(Level world, BlockPos pos, float sensitivity,
      Class<? extends AbstractMinecart> type) {
    return BaseRailBlock.isRail(world, pos) && isMinecartAt(world, pos, sensitivity, type);
  }

  public static boolean isMinecartOnAnySide(Level world, BlockPos pos, float sensitivity) {
    return isMinecartOnAnySide(world, pos, sensitivity, AbstractMinecart.class);
  }

  public static boolean isMinecartOnAnySide(Level world, BlockPos pos, float sensitivity,
      Class<? extends AbstractMinecart> type) {
    return Arrays.stream(Direction.values())
        .anyMatch(side -> !getMinecartsOnSide(world, pos, sensitivity, side, type).isEmpty());
  }

  public static boolean isMinecartAt(Level world, BlockPos pos, float sensitivity) {
    return isMinecartAt(world, pos, sensitivity, AbstractMinecart.class);
  }

  public static boolean isMinecartAt(Level world, BlockPos pos, float sensitivity,
      Class<? extends AbstractMinecart> type) {
    return !getMinecartsAt(world, pos, sensitivity, type).isEmpty();
  }

  public static List<AbstractMinecart> getMinecartsOnAllSides(Level world, BlockPos pos,
      float sensitivity) {
    return getMinecartsOnAllSides(world, pos, sensitivity, AbstractMinecart.class);
  }

  public static <T extends AbstractMinecart> List<T> getMinecartsOnAllSides(Level world,
      BlockPos pos, float sensitivity, Class<T> type) {
    return Arrays.stream(Direction.values())
        .flatMap(side -> getMinecartsOnSide(world, pos, sensitivity, side, type).stream())
        .collect(Collectors.toList());
  }

  public static boolean isMinecartOnSide(Level world, BlockPos pos, float sensitivity,
      Direction side) {
    return getMinecartOnSide(world, pos, sensitivity, side) != null;
  }

  public static boolean isMinecartOnSide(Level world, BlockPos pos, float sensitivity,
      Direction side, Class<? extends AbstractMinecart> type) {
    return getMinecartOnSide(world, pos, sensitivity, side, type) != null;
  }

  public static List<AbstractMinecart> getMinecartsOnSide(Level world, BlockPos pos,
      float sensitivity, Direction side) {
    return getMinecartsOnSide(world, pos, sensitivity, side, AbstractMinecart.class);
  }

  public static <T extends AbstractMinecart> List<T> getMinecartsOnSide(Level world,
      BlockPos pos, float sensitivity, Direction side, Class<T> type) {
    return getMinecartsAt(world, pos.relative(side), sensitivity, type);
  }

  public static @Nullable AbstractMinecart getMinecartOnSide(Level world, BlockPos pos,
      float sensitivity, Direction side) {
    return getMinecartOnSide(world, pos, sensitivity, side, AbstractMinecart.class);
  }

  public static @Nullable <T extends AbstractMinecart> T getMinecartOnSide(Level world,
      BlockPos pos, float sensitivity, Direction side, Class<T> type) {
    return getMinecartsOnSide(world, pos, sensitivity, side, type).stream().findAny().orElse(null);
  }

  /**
   * @param sensitivity Controls the size of the search box, ranges from (-inf, 0.49].
   */
  public static List<AbstractMinecart> getMinecartsAt(Level world, BlockPos pos,
      float sensitivity) {
    return getMinecartsAt(world, pos, sensitivity, AbstractMinecart.class);
  }

  public static <T extends AbstractMinecart> List<T> getMinecartsAt(Level world, BlockPos pos,
      float sensitivity, Class<T> type) {
    sensitivity = Math.min(sensitivity, 0.49f);
    return world.getEntitiesOfClass(type,
        new AABB(pos.getX() + sensitivity, pos.getY(), pos.getZ() + sensitivity,
            pos.getX() + 1 - sensitivity, pos.getY() + 1 - sensitivity,
            pos.getZ() + 1 - sensitivity),
        EntitySelector.ENTITY_STILL_ALIVE);
  }

  public static List<AbstractMinecart> getMinecartsIn(Level world, BlockPos p1, BlockPos p2) {
    return getMinecartsIn(world, p1, p2, AbstractMinecart.class);
  }

  public static <T extends AbstractMinecart> List<T> getMinecartsIn(Level world, BlockPos p1,
      BlockPos p2, Class<T> type) {
    return world.getEntitiesOfClass(type,
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
