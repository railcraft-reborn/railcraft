/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal;

import mods.railcraft.api.core.BlockEntityLike;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface SignalControllerProvider extends BlockEntityLike {

  SignalController getSignalController();
}
