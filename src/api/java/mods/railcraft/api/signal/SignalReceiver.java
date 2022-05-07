package mods.railcraft.api.signal;

public interface SignalReceiver {

  void linked(SignalController signalController);

  void unlinked(SignalController signalController);

  void receiveSignalAspect(SignalController signalController, SignalAspect signalAspect);

  void refresh();

  void destroy();
}
