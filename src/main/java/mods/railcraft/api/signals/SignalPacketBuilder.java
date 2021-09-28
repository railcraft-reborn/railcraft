/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signals;

import java.util.Collection;
import mods.railcraft.world.signal.NetworkType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface SignalPacketBuilder {

  void syncNetworkPeers(NetworkType type, BlockPos pos, Collection<BlockPos> peers,
      RegistryKey<World> dimension);

  void requestNetworkPeersSync(NetworkType type, BlockPos pos);
}
