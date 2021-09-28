/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signals;

public interface SignalReceiver extends BlockEntityLike {

  SignalReceiverNetwork getSignalReceiverNetwork();

  void onControllerAspectChange(SignalControllerNetwork con, SignalAspect aspect);
}
