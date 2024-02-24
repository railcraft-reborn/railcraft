/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal;

import java.util.UUID;
import mods.railcraft.api.signal.entity.MonitoringSignalEntity;
import net.minecraft.world.phys.Vec3;

public interface TokenSignalEntity extends MonitoringSignalEntity<TokenSignalEntity> {

  Vec3 ringCentroidPos();

  UUID ringId();

  void setRingId(UUID ringId);

  @Override
  TokenRing signalNetwork();

  @Override
  default Class<TokenSignalEntity> type() {
    return TokenSignalEntity.class;
  }
}
