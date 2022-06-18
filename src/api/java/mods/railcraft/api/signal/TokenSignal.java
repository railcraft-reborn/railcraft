/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal;

import java.util.UUID;
import mods.railcraft.api.core.BlockEntityLike;
import net.minecraft.core.BlockPos;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface TokenSignal extends BlockEntityLike, SurveyableSignal<TokenSignal> {

  BlockPos getTokenRingCentroid();

  UUID getTokenRingId();

  void setTokenRingId(UUID tokenRingId);

  @Override
  TokenRing getSignalNetwork();

  @Override
  default Class<TokenSignal> getSignalType() {
    return TokenSignal.class;
  }
}
