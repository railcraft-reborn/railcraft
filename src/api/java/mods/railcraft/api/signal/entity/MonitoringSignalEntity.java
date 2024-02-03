package mods.railcraft.api.signal.entity;

import mods.railcraft.api.signal.SignalNetwork;
import mods.railcraft.api.signal.TrackLocator;

/**
 * A signal which monitors a section of track.
 *
 * @author Sm0keySa1m0n
 *
 * @param <T>
 */
public interface MonitoringSignalEntity<T> extends SignalEntity {

  TrackLocator trackLocator();

  SignalNetwork<T> signalNetwork();

  Class<T> type();
}
