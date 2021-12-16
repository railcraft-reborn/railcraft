/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

import net.minecraft.network.FriendlyByteBuf;

public interface NetworkSerializable {

  void writeToBuf(FriendlyByteBuf out);

  void readFromBuf(FriendlyByteBuf in);
}
