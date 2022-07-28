/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal.entity;

import mods.railcraft.api.signal.SignalReceiver;

public interface SignalReceiverEntity extends SignalEntity {

  SignalReceiver getSignalReceiver();
}
