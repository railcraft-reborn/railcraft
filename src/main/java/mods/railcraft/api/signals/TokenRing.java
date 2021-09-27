/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signals;

import java.util.Set;
import java.util.UUID;
import net.minecraft.util.math.BlockPos;

/**
 * Created by CovertJaguar on 7/26/2017 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface TokenRing extends INetwork {

  Set<UUID> getTrackedCarts();

  SignalAspect getAspect();

  UUID getId();

  BlockPos getCentroid();
}
