/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal;

import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;

public interface TokenRing extends SignalNetwork<TokenSignalEntity> {

  Set<UUID> getTrackedCarts();

  UUID getId();

  BlockPos getCentroid();
}
