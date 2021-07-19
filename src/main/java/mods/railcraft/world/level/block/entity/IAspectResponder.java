package mods.railcraft.world.level.block.entity;

import mods.railcraft.api.signals.SignalAspect;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IAspectResponder {

  boolean doesActionOnAspect(SignalAspect aspect);

  void doActionOnAspect(SignalAspect aspect, boolean trigger);
}
