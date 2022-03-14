/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.track;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Implementing this interface will allow your track to be powered via Redstone.
 * <p/>
 * And so long as you inherit from {@link PoweredOutfittedTrackBlock} all the code for updating the
 * power state is already in place (including propagation).
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface PoweredTrack {

  /**
   * Getter for a simple boolean variable for caching the power state.
   */
  boolean isPowered(BlockState blockState, Level level, BlockPos blockPos);

  /**
   * Setter for a simple boolean variable for caching the power state.
   */
  void setPowered(BlockState blockState, Level level, BlockPos blockPos, boolean powered);

  /**
   * The distance that a redstone signal will be passed along from track to track.
   */
  int getPowerPropagation(BlockState blockState, Level level, BlockPos blockPos);

  /**
   * Allows finer control of whether tracks can pass power.
   */
  default boolean canPropagatePowerTo(BlockState testState) {
    return true;
  }
}
