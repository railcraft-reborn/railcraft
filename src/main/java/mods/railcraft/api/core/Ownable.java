/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.core;

import java.util.Optional;
import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.Nameable;

/**
 * Implemented by objects that can be owned.
 * <p/>
 * Among other uses, when used on a Tile Entity, the Magnifying Glass can be used to inspect the
 * owner.
 *
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public interface Ownable extends Nameable {

  /**
   * Returns the GameProfile of the owner of the object.
   */
  Optional<GameProfile> getOwner();

  default GameProfile getOwnerOrThrow() {
    return this.getOwner()
        .orElseThrow(() -> new IllegalStateException("Expected owner to be present."));
  }

  void setOwner(@Nullable GameProfile owner);
}
