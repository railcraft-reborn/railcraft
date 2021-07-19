/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

public interface ITrackKitSwitch extends ITrackKitInstance {

    /**
     * Returns whether the switch track should "appear" switched.
     * Has no influence on where a cart actually ends up.
     * This is mostly due to issues with the visual state updating slower than carts can approach the track.
     *
     * @return true if the track appears switched
     */
    boolean isVisuallySwitched();

    /**
     * Switch Tracks have an additional orientation state beyond normal tracks.
     * This function returns that value.
     *
     * @return true if the track is mirrored
     */
    boolean isMirrored();
}
