/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.carts;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public final class CartUtil {

  private static LinkageManager linkageManager = new LinkageManager() {};
  private static ITrainTransferHelper transferHelper = new ITrainTransferHelper() {};

  public static ITrainTransferHelper transferHelper() {
    return transferHelper;
  }

  /**
   * Returns an instance of ILinkageManager.
   * <p/>
   * Will return null if Railcraft is not installed.
   *
   * @return an instance of ILinkageManager
   */
  public static LinkageManager linkageManager() {
    return linkageManager;
  }

  // Most of these functions have been replaced internally by the EntitySearcher, but they remain
  // here in the API
  // for historic purposes and the fact that the EntitySearcher isn't part of the API.

  public static boolean isMinecartOnRailAt(World world, BlockPos pos, float sensitivity) {
    return isMinecartOnRailAt(world, pos, sensitivity, AbstractMinecartEntity.class);
  }

  public static boolean isMinecartOnRailAt(World world, BlockPos pos, float sensitivity,
      Class<? extends AbstractMinecartEntity> type) {
    return AbstractRailBlock.isRail(world, pos) && isMinecartAt(world, pos, sensitivity, type);
  }

  public static boolean isMinecartOnAnySide(World world, BlockPos pos, float sensitivity) {
    return isMinecartOnAnySide(world, pos, sensitivity, AbstractMinecartEntity.class);
  }

  public static boolean isMinecartOnAnySide(World world, BlockPos pos, float sensitivity,
      Class<? extends AbstractMinecartEntity> type) {
    return Arrays.stream(Direction.values())
        .anyMatch(side -> !getMinecartsOnSide(world, pos, sensitivity, side, type).isEmpty());
  }

  public static boolean isMinecartAt(World world, BlockPos pos, float sensitivity) {
    return isMinecartAt(world, pos, sensitivity, AbstractMinecartEntity.class);
  }

  public static boolean isMinecartAt(World world, BlockPos pos, float sensitivity,
      Class<? extends AbstractMinecartEntity> type) {
    return !getMinecartsAt(world, pos, sensitivity, type).isEmpty();
  }

  public static List<AbstractMinecartEntity> getMinecartsOnAllSides(World world, BlockPos pos,
      float sensitivity) {
    return getMinecartsOnAllSides(world, pos, sensitivity, AbstractMinecartEntity.class);
  }

  public static <T extends AbstractMinecartEntity> List<T> getMinecartsOnAllSides(World world,
      BlockPos pos, float sensitivity, Class<T> type) {
    return Arrays.stream(Direction.values())
        .flatMap(side -> getMinecartsOnSide(world, pos, sensitivity, side, type).stream())
        .collect(Collectors.toList());
  }

  public static boolean isMinecartOnSide(World world, BlockPos pos, float sensitivity,
      Direction side) {
    return getMinecartOnSide(world, pos, sensitivity, side) != null;
  }

  public static boolean isMinecartOnSide(World world, BlockPos pos, float sensitivity,
      Direction side, Class<? extends AbstractMinecartEntity> type) {
    return getMinecartOnSide(world, pos, sensitivity, side, type) != null;
  }

  public static List<AbstractMinecartEntity> getMinecartsOnSide(World world, BlockPos pos,
      float sensitivity, Direction side) {
    return getMinecartsOnSide(world, pos, sensitivity, side, AbstractMinecartEntity.class);
  }

  public static <T extends AbstractMinecartEntity> List<T> getMinecartsOnSide(World world,
      BlockPos pos, float sensitivity, Direction side, Class<T> type) {
    return getMinecartsAt(world, pos.relative(side), sensitivity, type);
  }

  public static @Nullable AbstractMinecartEntity getMinecartOnSide(World world, BlockPos pos,
      float sensitivity, Direction side) {
    return getMinecartOnSide(world, pos, sensitivity, side, AbstractMinecartEntity.class);
  }

  public static @Nullable <T extends AbstractMinecartEntity> T getMinecartOnSide(World world,
      BlockPos pos, float sensitivity, Direction side, Class<T> type) {
    return getMinecartsOnSide(world, pos, sensitivity, side, type).stream().findAny().orElse(null);
  }

  /**
   * @param sensitivity Controls the size of the search box, ranges from (-inf, 0.49].
   */
  public static List<AbstractMinecartEntity> getMinecartsAt(World world, BlockPos pos,
      float sensitivity) {
    return getMinecartsAt(world, pos, sensitivity, AbstractMinecartEntity.class);
  }

  public static <T extends AbstractMinecartEntity> List<T> getMinecartsAt(World world, BlockPos pos,
      float sensitivity, Class<T> type) {
    sensitivity = Math.min(sensitivity, 0.49f);
    return world.getEntitiesOfClass(type,
        new AxisAlignedBB(pos.getX() + sensitivity, pos.getY(), pos.getZ() + sensitivity,
            pos.getX() + 1 - sensitivity, pos.getY() + 1 - sensitivity,
            pos.getZ() + 1 - sensitivity),
        EntityPredicates.ENTITY_STILL_ALIVE);
  }

  public static List<AbstractMinecartEntity> getMinecartsIn(World world, BlockPos p1, BlockPos p2) {
    return getMinecartsIn(world, p1, p2, AbstractMinecartEntity.class);
  }

  public static <T extends AbstractMinecartEntity> List<T> getMinecartsIn(World world, BlockPos p1,
      BlockPos p2, Class<T> type) {
    return world.getEntitiesOfClass(type,
        new AxisAlignedBB(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ()),
        EntityPredicates.ENTITY_STILL_ALIVE);
  }

  /**
   * Returns the cart's "speed". It is not capped by the carts max speed, it instead returns the
   * cart's "potential" speed. Used by collision and linkage logic. Do not use this to determine how
   * fast a cart is currently moving.
   *
   * @return speed
   */
  public static double getCartSpeedUncapped(Vector3d deltaMovement) {
    return Math.sqrt(getCartSpeedUncappedSquared(deltaMovement));
  }

  public static double getCartSpeedUncappedSquared(Vector3d deltaMovement) {
    return deltaMovement.x() * deltaMovement.x() + deltaMovement.z() * deltaMovement.z();
  }

  public static boolean cartVelocityIsLessThan(Vector3d deltaMovement, float velocity) {
    return Math.abs(deltaMovement.x()) < velocity
        && Math.abs(deltaMovement.z()) < velocity;
  }

  private CartUtil() {}
}
