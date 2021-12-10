package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signal.DualSignalReceiver;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalReceiverProvider;
import mods.railcraft.api.signal.SingleSignalReceiver;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

public class DualDistantSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalReceiverProvider, DualSignalBlockEntity {

  private final DualSignalReceiver signalReceiver = new DualSignalReceiver(this,
      this::syncToClient, __ -> this.refreshLight(), __ -> this.refreshLight());

  private void refreshLight() {
    this.level.getLightEngine().checkBlock(this.getBlockPos());
  }

  public DualDistantSignalBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.DUAL_DISTANT_SIGNAL.get(), blockPos, blockState);
  }

  @Override
  public void onLoad() {
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
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("signalReceiver", this.signalReceiver.serializeNBT());
  }

  @Override
  public void load( CompoundTag tag) {
    super.load(tag);
    this.signalReceiver.deserializeNBT(tag.getCompound("signalReceiver"));
  }

  @Override
  public void writeSyncData(FriendlyByteBuf data) {
    super.writeSyncData(data);
    this.signalReceiver.writeSyncData(data);
  }

  @Override
  public void readSyncData(FriendlyByteBuf data) {
    super.readSyncData(data);
    this.signalReceiver.readSyncData(data);
  }
}
