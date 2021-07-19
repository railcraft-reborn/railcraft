/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signals;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IBlockSignal extends ISignal {

  BlockSignal getBlockSignal();

  @Override
  default INetwork getNetwork() {
    return this.getBlockSignal();
  }

  @Override
  default TrackLocator getTrackLocator() {
    return getBlockSignal().getTrackLocator();
  }
}
