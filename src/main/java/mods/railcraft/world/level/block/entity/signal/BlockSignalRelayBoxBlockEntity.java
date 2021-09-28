package mods.railcraft.world.level.block.entity.signal;

import javax.annotation.Nullable;
import mods.railcraft.api.signals.BlockSignal;
import mods.railcraft.api.signals.BlockSignalNetwork;
import mods.railcraft.api.signals.BlockSignalRelay;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SimpleSignalControllerNetwork;
import mods.railcraft.plugins.PowerPlugin;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class BlockSignalRelayBoxBlockEntity extends ActionSignalBoxBlockEntity
    implements BlockSignal, IAspectProvider, SignalEmitter {

  private final SimpleSignalControllerNetwork signalControllerNetwork =
      new SimpleSignalControllerNetwork(this, this::syncToClient);
  private final BlockSignalNetwork signalNetwork = new BlockSignalRelay(this, this::syncToClient);

  public BlockSignalRelayBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_RELAY_BOX.get());
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      this.signalControllerNetwork.tickClient();
      this.signalNetwork.tickClient();
      return;
    }
    this.signalControllerNetwork.tickServer();
    this.signalNetwork.tickServer();
    SignalAspect prevAspect = this.signalControllerNetwork.getAspect();
    if (this.signalControllerNetwork.isLinking())
      this.signalControllerNetwork.setAspect(SignalAspect.BLINK_YELLOW);
    else
      this.signalControllerNetwork.setAspect(signalNetwork.getSignalAspect());
    if (prevAspect != this.signalControllerNetwork.getAspect()) {
      this.updateNeighboringSignalBoxes();
      this.syncToClient();
    }
  }

  private void updateNeighboringSignalBoxes() {
    this.updateNeighbors();
    for (Direction side : Direction.Plane.HORIZONTAL) {
      TileEntity tile = this.adjacentCache.getTileOnSide(side);
      if (tile instanceof AbstractSignalBoxBlockEntity) {
        AbstractSignalBoxBlockEntity box = (AbstractSignalBoxBlockEntity) tile;
        box.neighboringSignalBoxChanged(this, side);
      }
    }
  }

  @Override
  public int getSignal(Direction side) {
    if (this.adjacentCache
        .getTileOnSide(side.getOpposite()) instanceof AbstractSignalBoxBlockEntity) {
      return PowerPlugin.NO_POWER;
    }
    return this.isEmittingRedstone(side) ? PowerPlugin.FULL_POWER : PowerPlugin.NO_POWER;
  }

  @Override
  public boolean isEmittingRedstone(Direction side) {
    return doesActionOnAspect(getBoxSignalAspect(side));
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.put("signalNetwork", this.signalNetwork.serializeNBT());
    data.put("signalControllerNetwork", this.signalControllerNetwork.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.signalNetwork.deserializeNBT(data.getCompound("signalNetwork"));
    this.signalControllerNetwork.deserializeNBT(data.getCompound("signalControllerNetwork"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    this.signalControllerNetwork.writeSyncData(data);
    this.signalNetwork.writeSyncData(data);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.signalControllerNetwork.readSyncData(data);
    this.signalNetwork.readSyncData(data);
  }

  @Override
  public BlockSignalNetwork getSignalNetwork() {
    return this.signalNetwork;
  }

  @Override
  public void doActionOnAspect(SignalAspect aspect, boolean trigger) {
    super.doActionOnAspect(aspect, trigger);
    updateNeighboringSignalBoxes();
  }

  @Override
  public SignalAspect getBoxSignalAspect(@Nullable Direction side) {
    return this.signalControllerNetwork.getAspect();
  }

  @Override
  public boolean canTransferAspect() {
    return true;
  }

  @Override
  public SignalAspect getTriggerAspect() {
    return this.getBoxSignalAspect(null);
  }
}
