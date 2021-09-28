/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signals;

import javax.annotation.Nullable;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CovertJaguar on 7/9/2017 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TrackLocator {

  private final World level;
  private final BlockPos blockPos;
  @Nullable
  private BlockPos trackLocation;

  public TrackLocator(World level, BlockPos blockPos) {
    this.level = level;
    this.blockPos = blockPos;
  }

  @Nullable
  public BlockPos getTrackLocation() {
    if (this.trackLocation == null)
      this.locateTrack();
    return this.trackLocation;
  }

  public Status getTrackStatus() {
    if (this.trackLocation == null)
      return locateTrack();
    if (!this.level.isLoaded(this.trackLocation))
      return Status.UNKNOWN;
    if (!AbstractRailBlock.isRail(this.level, this.trackLocation)) {
      this.trackLocation = null;
      return this.locateTrack();
    }
    return Status.VALID;
  }

  private Status locateTrack() {
    int x = this.blockPos.getX();
    int y = this.blockPos.getY();
    int z = this.blockPos.getZ();
    Status status = this.testForTrack(x, y, z);
    if (status != Status.INVALID)
      return status;
    status = this.testForTrack(x - 1, y, z);
    if (status != Status.INVALID)
      return status;
    status = this.testForTrack(x + 1, y, z);
    if (status != Status.INVALID)
      return status;
    status = this.testForTrack(x, y, z - 1);
    if (status != Status.INVALID)
      return status;
    status = this.testForTrack(x, y, z + 1);
    if (status != Status.INVALID)
      return status;
    status = this.testForTrack(x - 2, y, z);
    if (status != Status.INVALID)
      return status;
    status = this.testForTrack(x + 2, y, z);
    if (status != Status.INVALID)
      return status;
    status = this.testForTrack(x, y, z - 2);
    if (status != Status.INVALID)
      return status;
    status = this.testForTrack(x, y, z + 2);
    if (status != Status.INVALID)
      return status;
    return Status.INVALID;
  }

  private Status testForTrack(int x, int y, int z) {
    for (int i = -2; i < 4; i++) {
      BlockPos pos = new BlockPos(x, y - i, z);
      if (!this.level.isLoaded(pos))
        return Status.UNKNOWN;
      if (AbstractRailBlock.isRail(this.level, pos)) {
        this.trackLocation = pos;
        return Status.VALID;
      }
    }
    return Status.INVALID;
  }

  public enum Status {
    VALID, INVALID, UNKNOWN
  }
}
