/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signals;

import java.util.Collection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * This interface represent an object that can be paired with another object.
 *
 * Generally this applies to AbstractPair, but it is also used for creating TokenRings.
 *
 * Created by CovertJaguar on 7/26/2017 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface INetwork {

  void startLinking();

  void endLinking();

  boolean add(TileEntity other);

  Collection<BlockPos> getPeers();
}
