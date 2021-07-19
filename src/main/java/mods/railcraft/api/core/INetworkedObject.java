/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

import net.minecraft.network.PacketBuffer;

public interface INetworkedObject extends IWorldSupplier {

  default void readPacketData(PacketBuffer data) {}

  default void writePacketData(PacketBuffer data) {}

  void sendUpdateToClient();
}
