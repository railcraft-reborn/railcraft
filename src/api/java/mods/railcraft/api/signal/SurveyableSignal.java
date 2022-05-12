package mods.railcraft.api.signal;

public interface SurveyableSignal<T> extends Signal {

  TrackLocator getTrackLocator();

  SignalNetwork<T> getSignalNetwork();

  Class<T> getSignalType();
}
