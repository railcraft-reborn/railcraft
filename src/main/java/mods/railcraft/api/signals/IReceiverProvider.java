/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signals;

import net.minecraft.world.World;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IReceiverProvider {

  World getLevel();

  SignalReceiver getReceiver();

  void onControllerAspectChange(SignalController con, SignalAspect aspect);
}
