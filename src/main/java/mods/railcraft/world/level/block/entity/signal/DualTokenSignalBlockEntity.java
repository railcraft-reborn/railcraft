package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.SignalControllerNetwork;
import mods.railcraft.api.signals.SignalReceiver;
import mods.railcraft.api.signals.SimpleSignalReceiverNetwork;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

public class DualTokenSignalBlockEntity extends TokenSignalBlockEntity
    implements SignalReceiver, DualSignal {

  private final SimpleSignalReceiverNetwork signalReceiverNetwork =
      new SimpleSignalReceiverNetwork(this, this::syncToClient);

  public DualTokenSignalBlockEntity() {
    super(RailcraftBlockEntityTypes.DUAL_TOKEN_SIGNAL.get());
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
  public SimpleSignalReceiverNetwork getSignalReceiverNetwork() {
    return this.signalReceiverNetwork;
  }

  @Override
  public SignalAspect getTopAspect() {
    return this.getSignalAspect();
  }

  @Override
  public SignalAspect getBottomAspect() {
    return this.signalReceiverNetwork.getAspect();
  }

  @Override
  public ITextComponent getPrimaryNetworkName() {
    return this.signalReceiverNetwork.getName();
  }
}
