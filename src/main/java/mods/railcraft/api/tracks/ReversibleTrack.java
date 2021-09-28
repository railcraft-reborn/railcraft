/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

import mods.railcraft.world.level.block.track.outfitted.OutfittedTrackBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementing this interface will allow your track to be direction specific.
 *
 * <p>
 * So long as you inherit from {@link OutfittedTrackBlock} it will automatically be reversible via
 * the Crowbar.
 * </p>
 *
 * <p>
 * When the track kit is facing {@link net.minecraft.util.Direction#SOUTH south} or
 * {@link net.minecraft.util.Direction#WEST west}, it is considered reversed.
 * </p>
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface ReversibleTrack {

  /**
   * Returns true if this track kit instance is currently reversed, or currently facing south or
   * west.
   *
   * @return True if the track kit is reversed
   */
  boolean isReversed(BlockState blockState, World level, BlockPos pos);

  /**
   * Set the track kit to be reversed, or be facing south or west (dependent on the rail direction).
   *
   * @param reversed Whether the track kit is reversed
   */
  void setReversed(BlockState blockState, World level, BlockPos pos, boolean reversed);
}
