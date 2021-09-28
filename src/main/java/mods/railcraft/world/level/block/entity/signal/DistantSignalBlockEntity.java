package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalControllerNetwork;
import mods.railcraft.api.signals.SignalReceiver;
import mods.railcraft.api.signals.SignalReceiverNetwork;
import mods.railcraft.api.signals.SimpleSignalReceiverNetwork;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

public class DistantSignalBlockEntity extends AbstractSignalBlockEntity implements SignalReceiver {

  private final SimpleSignalReceiverNetwork signalReceiverNetwork =
      new SimpleSignalReceiverNetwork(this, this::syncToClient);

  public DistantSignalBlockEntity() {
    super(RailcraftBlockEntityTypes.DISTANT_SIGNAL.get());
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      this.signalReceiverNetwork.tickClient();
      return;
    }
    this.signalReceiverNetwork.tickServer();
  }

  @Override
  public void onControllerAspectChange(SignalControllerNetwork con, SignalAspect aspect) {
    this.syncToClient();
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.put("receiver", this.signalReceiverNetwork.serializeNBT());
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
  public SignalReceiverNetwork getSignalReceiverNetwork() {
    return this.signalReceiverNetwork;
  }

  @Override
  public SignalAspect getSignalAspect() {
    return this.signalReceiverNetwork.getAspect();
  }

  @Override
  public ITextComponent getPrimaryNetworkName() {
    return this.signalReceiverNetwork.getName();
  }
}
