/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal.entity;

import mods.railcraft.api.signal.SignalController;

public interface SignalControllerEntity extends SignalEntity {

  SignalController getSignalController();
}
