/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signals;

import java.util.UUID;
import net.minecraft.util.math.BlockPos;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface TokenSignal extends ISignal {

  TokenRing getTokenRing();

  BlockPos getTokenRingCentroid();

  UUID getTokenRingId();

  @Override
  default INetwork getNetwork() {
    return this.getTokenRing();
  }
}
