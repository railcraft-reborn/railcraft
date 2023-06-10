/*------------------------------------------------------------------------------
 Copyright (c) Railcraft, 2011-2023

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface TuningAuraHandler {

  boolean isTuningAuraActive();

  void spawnTuningAura(BlockEntity start, BlockEntity dest);
}
