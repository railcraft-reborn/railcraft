package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signals.SignalAspect;

public interface DualSignal {

  SignalAspect getTopAspect();

  SignalAspect getBottomAspect();
}
