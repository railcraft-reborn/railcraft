/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

/**
 * Tracks that can emit a redstone signal should implement
 * this interface.
 *
 * For example a detector track.
 *
 * A track cannot implement both ITrackPowered and ITrackEmitter.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface ITrackKitEmitter extends ITrackKitInstance {

    /**
     * Return the redstone output of the track.
     */
    int getPowerOutput();
}
