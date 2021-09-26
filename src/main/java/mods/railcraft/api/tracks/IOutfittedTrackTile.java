/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

import com.mojang.authlib.GameProfile;

/**
 * Don't use this, its an interface that allows other API code access to internal functions of the
 * code.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IOutfittedTrackTile {

  TrackType getTrackType();

  ITrackKitInstance getTrackKitInstance();

  void sendUpdateToClient();

  GameProfile getOwner();
}
