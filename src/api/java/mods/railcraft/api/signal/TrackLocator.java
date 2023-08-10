/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signal;

import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;

public class TrackLocator {

  private final Supplier<Level> level;
  private final BlockPos originPos;
  @Nullable
  private BlockPos trackPos;

  public TrackLocator(Supplier<Level> level, BlockPos originPos) {
    this.level = level;
    this.originPos = originPos;
  }

  @Nullable
  public BlockPos trackPos() {
    if (this.trackPos == null) {
      this.locateTrack();
    }
    return this.trackPos;
  }

  public Status trackStatus() {
    if (this.trackPos == null) {
      return locateTrack();
    }
    var level = this.level.get();
    if (!level.isLoaded(this.trackPos)) {
      return Status.UNKNOWN;
    }
    if (!BaseRailBlock.isRail(level, this.trackPos)) {
      this.trackPos = null;
      return this.locateTrack();
    }
    return Status.VALID;
  }

  private Status locateTrack() {
    int x = this.originPos.getX();
    int y = this.originPos.getY();
    int z = this.originPos.getZ();
    var status = this.testForTrack(x, y, z);
    if (status != Status.INVALID)
      return status;
    status = testForTrack(x - 1, y, z);
    if (status != Status.INVALID)
      return status;
    status = testForTrack(x + 1, y, z);
    if (status != Status.INVALID)
      return status;
    status = testForTrack(x, y, z - 1);
    if (status != Status.INVALID)
      return status;
    status = testForTrack(x, y, z + 1);
    if (status != Status.INVALID)
      return status;
    status = testForTrack(x - 2, y, z);
    if (status != Status.INVALID)
      return status;
    status = testForTrack(x + 2, y, z);
    if (status != Status.INVALID)
      return status;
    status = testForTrack(x, y, z - 2);
    if (status != Status.INVALID)
      return status;
    return testForTrack(x, y, z + 2);
  }

  private Status testForTrack(int x, int y, int z) {
    var level = this.level.get();
    for (int i = -2; i < 4; i++) {
      var pos = new BlockPos(x, y - i, z);
      if (!level.isLoaded(pos)) {
        return Status.UNKNOWN;
      }
      if (BaseRailBlock.isRail(level, pos)) {
        this.trackPos = pos;
        return Status.VALID;
      }
    }
    return Status.INVALID;
  }

  public enum Status {

    VALID, INVALID, UNKNOWN;

    public boolean invalid() {
      return this == INVALID;
    }
  }
}
