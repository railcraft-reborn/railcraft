package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signal.DualSignalReceiver;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalReceiverProvider;
import mods.railcraft.api.signal.SingleSignalReceiver;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class DualDistantSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalReceiverProvider, DualSignalBlockEntity {

  private final DualSignalReceiver signalReceiver = new DualSignalReceiver(this,
      this::syncToClient, __ -> this.refreshLight(), __ -> this.refreshLight());

  private void refreshLight() {
    this.level.getLightEngine().checkBlock(this.getBlockPos());
  }

  public DualDistantSignalBlockEntity() {
    super(RailcraftBlockEntityTypes.DUAL_DISTANT_SIGNAL.get());
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
  public int getLightValue() {
    return Math.max(super.getLightValue(),
        this.getSecondarySignalAspect().getBlockLight());
  }

  @Override
  public SignalAspect getPrimarySignalAspect() {
    return this.signalReceiver.getPrimarySignalAspect();
  }

  @Override
  public SignalAspect getSecondarySignalAspect() {
    return this.signalReceiver.getSecondarySignalAspect();
  }

  @Override
  public SingleSignalReceiver getSignalReceiver() {
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
