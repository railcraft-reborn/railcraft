package mods.railcraft.api.signal;

import net.minecraft.util.INameable;

public interface Signal<T> extends INameable {

  TrackLocator getTrackLocator();

  SignalNetwork<T> getSignalNetwork();

  Class<T> getSignalType();
}
