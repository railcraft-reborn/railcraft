package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalReceiver;
import mods.railcraft.api.signal.SignalReceiverProvider;
import mods.railcraft.api.signal.SingleSignalReceiver;
import mods.railcraft.util.PowerUtil;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

public class SignalReceiverBoxBlockEntity extends ActionSignalBoxBlockEntity
    implements SignalReceiverProvider {

  private final SingleSignalReceiver signalReceiver = new SingleSignalReceiver(this,
      this::syncToClient, __ -> this.setChanged());

  public SignalReceiverBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SIGNAL_RECEIVER_BOX.get(), blockPos, blockState);
  }

  @Override
  public void onLoad() {
    super.onLoad();
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
  public int getRedstoneSignal(Direction direction) {
    return this.isActionSignalAspect(this.signalReceiver.getPrimarySignalAspect())
        ? PowerUtil.FULL_POWER
        : PowerUtil.NO_POWER;
  }

  @Override
  public SignalReceiver getSignalReceiver() {
    return this.signalReceiver;
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    return this.signalReceiver.getPrimarySignalAspect();
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("signalReceiver", this.signalReceiver.serializeNBT());
  }

  @Override
  public void load(CompoundTag tag) {
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
