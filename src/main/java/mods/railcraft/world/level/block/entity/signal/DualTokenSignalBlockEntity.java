package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalReceiver;
import mods.railcraft.api.signal.SignalReceiverProvider;
import mods.railcraft.api.signal.SingleSignalReceiver;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class DualTokenSignalBlockEntity extends TokenSignalBlockEntity
    implements SignalReceiverProvider, DualSignalBlockEntity {

  private final SingleSignalReceiver signalReceiver = new SingleSignalReceiver(this,
      this::syncToClient, __ -> this.level.getLightEngine().checkBlock(this.getBlockPos()));

  public DualTokenSignalBlockEntity() {
    super(RailcraftBlockEntityTypes.DUAL_TOKEN_SIGNAL.get());
  }

  @Override
  public void load() {
    if (!this.level.isClientSide()) {
      this.signalReceiver.refresh();
    }
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    this.signalReceiver.removed();
  }

  @Override
  public SignalAspect getSecondarySignalAspect() {
    return this.signalReceiver.getPrimarySignalAspect();
  }

  @Override
  public int getLightValue() {
    return Math.max(super.getLightValue(),
        this.getSecondarySignalAspect().getBlockLight());
  }

  @Override
  public SignalReceiver getSignalReceiver() {
    return this.signalReceiver;
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.put("signalReceiver", this.signalReceiver.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.signalReceiver.deserializeNBT(data.getCompound("signalReceiver"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    this.signalReceiver.writeSyncData(data);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.signalReceiver.readSyncData(data);
  }
}
