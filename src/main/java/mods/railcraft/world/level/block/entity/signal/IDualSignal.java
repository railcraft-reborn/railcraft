package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signals.DualLamp;
import mods.railcraft.api.signals.SignalAspect;

public interface IDualSignal {

  SignalAspect getSignalAspect(DualLamp lamp);
}
