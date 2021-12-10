/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

import net.minecraft.network.FriendlyByteBuf;

public interface Syncable {

  default void writeSyncData(FriendlyByteBuf data) {}

  default void readSyncData(FriendlyByteBuf data) {}

  void syncToClient();
}
