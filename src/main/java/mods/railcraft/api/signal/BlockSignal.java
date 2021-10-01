/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal;

import mods.railcraft.api.core.BlockEntityLike;
import mods.railcraft.api.core.Ownable;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface BlockSignal
    extends Signal<BlockSignal>, BlockEntityLike, Ownable {

  @Override
  default TrackLocator getTrackLocator() {
    return this.getSignalNetwork().getTrackLocator();
  }

  @Override
  BlockSignalNetwork getSignalNetwork();

  @Override
  default Class<BlockSignal> getSignalType() {
    return BlockSignal.class;
  }
}
