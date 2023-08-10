/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.track;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;

public final class TrackScanUtil {

  private TrackScanUtil() {}

  /**
   * Verifies that two rails are connected to each other along a straight line with no gaps or
   * wanderings.
   *
   * @param level The World object
   * @return true if they are connected
   */
  public static boolean areTracksConnectedAlongAxis(Level level, BlockPos start, BlockPos end) {
    return scanStraightTrackSection(level, start, end).status() == Status.VALID;
  }

  /**
   * Verifies that two rails are connected to each other along a straight line with no gaps or
   * wanderings.
   * <p/>
   * Also records the min and max y values along the way.
   *
   * @param level The World object
   * @return ScanResult object with results
   */
  public static Result scanStraightTrackSection(Level level, BlockPos start, BlockPos end) {
    int x1 = start.getX();
    int y1 = start.getY();
    int z1 = start.getZ();

    int x2 = end.getX();
    int y2 = end.getY();
    int z2 = end.getZ();

    int minY = Math.min(y1, y2);
    int maxY = Math.max(y1, y2);
    if (x1 != x2 && z1 != z2)
      return new Result(Status.NOT_ALIGNED, minY, maxY);
    if (x1 != x2) {
      int min;
      int max;
      int yy;
      if (x1 < x2) {
        min = x1;
        max = x2;
        yy = y1;
      } else {
        min = x2;
        max = x1;
        yy = y2;
      }
      for (int xx = min; xx <= max; xx++) {
        // if (world.blockExists(xx, yy, z1))
        BlockPos p = new BlockPos(xx, yy, z1);
        if (BaseRailBlock.isRail(level, p)) {
          // NOOP
        } else if (BaseRailBlock.isRail(level, p.below())) {
          yy--;
          if (yy < minY)
            minY = yy;
        } else if (BaseRailBlock.isRail(level, p.above())) {
          yy++;
          if (yy > maxY)
            maxY = yy;
        } else if (!level.isLoaded(p)) {
          return new Result(Status.UNKNOWN, minY, maxY);
        } else
          return new Result(Status.PATH_NOT_FOUND, minY, maxY);
      }
    } else if (z1 != z2) {
      int min;
      int max;
      int yy;
      if (z1 < z2) {
        min = z1;
        max = z2;
        yy = y1;
      } else {
        min = z2;
        max = z1;
        yy = y2;
      }
      for (int zz = min; zz <= max; zz++) {
        // if (world.blockExists(x1, yy, zz))
        BlockPos p = new BlockPos(x1, yy, zz);
        if (BaseRailBlock.isRail(level, p)) {
          // NOOP
        } else if (BaseRailBlock.isRail(level, p.below())) {
          yy--;
          if (yy < minY)
            minY = yy;
        } else if (BaseRailBlock.isRail(level, p.above())) {
          yy++;
          if (yy > maxY)
            maxY = yy;
        } else if (!level.isLoaded(p)) {
          return new Result(Status.UNKNOWN, minY, maxY);
        } else
          return new Result(Status.PATH_NOT_FOUND, minY, maxY);
      }
    }
    return new Result(Status.VALID, minY, maxY);
  }

  public record Result(Status status, int minY, int maxY) {}

  public enum Status {

    VALID,
    UNKNOWN,
    NOT_ALIGNED,
    PATH_NOT_FOUND;

    public boolean valid() {
      return this == VALID;
    }

    public boolean unknown() {
      return this == UNKNOWN;
    }
  }
}
