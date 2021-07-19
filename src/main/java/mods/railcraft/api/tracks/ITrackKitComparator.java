/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

/**
 * Tracks that can interface with Comparators should implement this interface.
 *
 * For example a detector track.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface ITrackKitComparator extends ITrackKitInstance {

  int getComparatorInputOverride();
}
