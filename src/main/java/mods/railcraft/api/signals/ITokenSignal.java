/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signals;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface ITokenSignal extends ISignal {

  ITokenRing getTokenRing();

  @Override
  default INetwork getNetwork() {
    return this.getTokenRing();
  }
}
