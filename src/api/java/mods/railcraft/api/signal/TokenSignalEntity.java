/*------------------------------------------------------------------------------
 Copyright (c) Railcraft, 2011-2023

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal;

import java.util.UUID;
import mods.railcraft.api.signal.entity.MonitoringSignalEntity;
import net.minecraft.core.BlockPos;

public interface TokenSignalEntity extends MonitoringSignalEntity<TokenSignalEntity> {

  BlockPos ringCentroidPos();

  UUID ringId();

  void setRingId(UUID ringId);

  @Override
  TokenRing signalNetwork();

  @Override
  default Class<TokenSignalEntity> type() {
    return TokenSignalEntity.class;
  }
}
