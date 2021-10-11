/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.track;

import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
  boolean isPowered(BlockState blockState, World level, BlockPos pos);

  /**
   * Setter for a simple boolean variable for caching the power state.
   *
   * <p>
   * Note: It is suggested to send update to client if the new powered state is different.
   * </p>
   */
  void setPowered(BlockState blockState, World level, BlockPos pos, boolean powered);

  /**
   * The distance that a redstone signal will be passed along from track to track.
   */
  int getPowerPropagation(BlockState blockState, World level, BlockPos pos);

  /**
   * Allows finer control of whether tracks can pass power.
   */
  default boolean canPropagatePowerTo(BlockState testState) {
    return true;
  }
}
