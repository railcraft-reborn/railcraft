package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signals.DualSignalReceiver;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalControllerNetwork;
import mods.railcraft.api.signals.SignalReceiver;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

public class DualDistantSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalReceiver, DualSignal {

  private final DualSignalReceiver signalReceiverNetwork =
      new DualSignalReceiver(this, this::syncToClient);

  public DualDistantSignalBlockEntity() {
    super(RailcraftBlockEntityTypes.DUAL_DISTANT_SIGNAL.get());
  }

  @Override
  public int getLightValue() {
    return Math.max(this.getTopAspect().getBlockLight(),
        this.getBottomAspect().getBlockLight());
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      this.signalReceiverNetwork.tickClient();
      return;
    }

    this.signalReceiverNetwork.tickServer();
    int numPairs = this.signalReceiverNetwork.getPeerCount();
    boolean changed = false;
    switch (numPairs) {
      case 0:
        changed = this.signalReceiverNetwork.setTopAspect(SignalAspect.BLINK_RED);
      case 1:
        changed |= this.signalReceiverNetwork.setBottomAspect(SignalAspect.BLINK_RED);
    }
    if (changed) {
      syncToClient();
    }
  }

  @Override
  public void onControllerAspectChange(SignalControllerNetwork con, SignalAspect aspect) {
    syncToClient();
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.put("signalReceiverNetwork", this.signalReceiverNetwork.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.signalReceiverNetwork.deserializeNBT(data.getCompound("signalReceiverNetwork"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    this.signalReceiverNetwork.writeSyncData(data);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.signalReceiverNetwork.readSyncData(data);
  }

  @Override
  public DualSignalReceiver getSignalReceiverNetwork() {
    return this.signalReceiverNetwork;
  }

  @Override
  public SignalAspect getTopAspect() {
    return this.signalReceiverNetwork.getTopAspect();
  }

  @Override
  public SignalAspect getBottomAspect() {
    return this.signalReceiverNetwork.getBottomAspect();
  }

  @Override
  public SignalAspect getSignalAspect() {
    return this.signalReceiverNetwork.getTopAspect();
  }

  @Override
  public ITextComponent getPrimaryNetworkName() {
    return this.signalReceiverNetwork.getName();
  }
}
