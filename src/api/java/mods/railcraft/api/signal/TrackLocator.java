/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signal;

import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;

/**
 * Created by CovertJaguar on 7/9/2017 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
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
    if (!this.level.get().isLoaded(this.trackPos)) {
      return Status.UNKNOWN;
    }
    if (!BaseRailBlock.isRail(this.level.get(), this.trackPos)) {
      this.trackPos = null;
      return this.locateTrack();
    }
    return Status.VALID;
  }

  private Status locateTrack() {
    return BlockPos
        .betweenClosedStream(this.originPos.offset(-2, 0, -2), this.originPos.offset(2, 0, 2))
        .map(this::testForTrack)
        .filter(Predicate.not(Status::invalid))
        .findFirst()
        .orElse(Status.INVALID);
  }

  private Status testForTrack(BlockPos blockPos) {
    for (int i = -2; i < 4; i++) {
      if (!this.level.get().isLoaded(blockPos)) {
        return Status.UNKNOWN;
      }
      if (BaseRailBlock.isRail(this.level.get(), blockPos)) {
        this.trackPos = blockPos;
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
