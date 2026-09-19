/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.core;

import java.util.Optional;
import org.jspecify.annotations.Nullable;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.Nameable;

/**
 * Implemented by objects that can be owned.
 * <p/>
 * Among other uses, when used on a BlockEntity, the Magnifying Glass can be used to inspect the
 * owner.
 */
public interface Ownable extends Nameable {

  /**
   * Returns the GameProfile of the owner of the object.
   */
  Optional<NameAndId> getOwner();

  default NameAndId getOwnerOrThrow() {
    return this.getOwner()
        .orElseThrow(() -> new IllegalStateException("Expected owner to be present."));
  }

  void setOwner(@Nullable NameAndId owner);
}
