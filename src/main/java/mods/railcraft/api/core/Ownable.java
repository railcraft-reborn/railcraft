/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.core;

import com.mojang.authlib.GameProfile;
import net.minecraft.util.INameable;

/**
 * Implemented by objects that can be owned.
 * <p/>
 * Among other uses, when used on a Tile Entity, the Magnifying Glass can be used to inspect the
 * owner.
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public interface Ownable extends INameable {

  /**
   * Returns the GameProfile of the owner of the object.
   */
  GameProfile getOwner();
}
