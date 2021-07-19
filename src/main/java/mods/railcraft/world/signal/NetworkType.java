package mods.railcraft.world.signal;

import java.util.Optional;
import java.util.function.BiFunction;
import mods.railcraft.api.signals.IBlockSignal;
import mods.railcraft.api.signals.IControllerProvider;
import mods.railcraft.api.signals.IMutableNetwork;
import mods.railcraft.api.signals.IReceiverProvider;
import mods.railcraft.plugins.WorldPlugin;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum NetworkType {

  BLOCK_SIGNAL((level, pos) -> WorldPlugin.getTileEntity(level, pos, IBlockSignal.class)
      .map(IBlockSignal::getBlockSignal)),
  CONTROLLER((level, pos) -> WorldPlugin.getTileEntity(level, pos, IControllerProvider.class)
      .map(IControllerProvider::getController)),
  RECIEVER((level, pos) -> WorldPlugin.getTileEntity(level, pos, IReceiverProvider.class)
      .map(IReceiverProvider::getReceiver));

  private final BiFunction<World, BlockPos, Optional<? extends IMutableNetwork>> getter;

  private NetworkType(BiFunction<World, BlockPos, Optional<? extends IMutableNetwork>> getter) {
    this.getter = getter;
  }

  public Optional<? extends IMutableNetwork> getNetwork(World level, BlockPos pos) {
    return this.getter.apply(level, pos);
  }
}
