package mods.railcraft.world.signal;

import java.util.Optional;
import java.util.function.BiFunction;
import mods.railcraft.api.signals.BlockSignal;
import mods.railcraft.api.signals.SignalController;
import mods.railcraft.api.signals.ClientNetwork;
import mods.railcraft.api.signals.SignalReceiver;
import mods.railcraft.plugins.WorldPlugin;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum NetworkType {

  BLOCK_SIGNAL((level, pos) -> WorldPlugin.getTileEntity(level, pos, BlockSignal.class)
      .map(BlockSignal::getSignalNetwork)),
  SIGNAL_CONTROLLER((level, pos) -> WorldPlugin.getTileEntity(level, pos, SignalController.class)
      .map(SignalController::getSignalControllerNetwork)),
  SIGNAL_RECIEVER((level, pos) -> WorldPlugin.getTileEntity(level, pos, SignalReceiver.class)
      .map(SignalReceiver::getSignalReceiverNetwork));

  private final BiFunction<World, BlockPos, Optional<? extends ClientNetwork<?>>> getter;

  private NetworkType(BiFunction<World, BlockPos, Optional<? extends ClientNetwork<?>>> getter) {
    this.getter = getter;
  }

  public Optional<? extends ClientNetwork<?>> getNetwork(World level, BlockPos pos) {
    return this.getter.apply(level, pos);
  }
}
