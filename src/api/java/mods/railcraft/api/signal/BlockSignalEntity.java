/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal;

import mods.railcraft.api.core.Ownable;
import mods.railcraft.api.signal.entity.MonitoringSignalEntity;

public interface BlockSignalEntity
    extends MonitoringSignalEntity<BlockSignalEntity>, Ownable {

  @Override
  default TrackLocator trackLocator() {
    return this.signalNetwork().trackLocator();
  }

  @Override
  BlockSignal signalNetwork();

  @Override
  default Class<BlockSignalEntity> type() {
    return BlockSignalEntity.class;
  }
}
