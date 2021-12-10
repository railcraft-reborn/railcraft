package mods.railcraft.api.signal;

import net.minecraft.world.Nameable;

public interface Signal<T> extends Nameable {

  TrackLocator getTrackLocator();

  SignalNetwork<T> getSignalNetwork();

  Class<T> getSignalType();
}
