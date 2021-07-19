/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.fuel;

/**
 * Any cart that implements this class will respond to the Routing NeedsFuel condition.
 *
 * Additionally any Tile Entity that implements it will respond to the NeedsFuel BC trigger.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface INeedsFuel {
    boolean needsFuel();
}
