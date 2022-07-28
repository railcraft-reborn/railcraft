package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SingleSignalReceiver;
import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

public class DistantSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalReceiverEntity {

  private final SingleSignalReceiver signalReceiver = new SingleSignalReceiver(this,
      this::syncToClient, __ -> this.level.getLightEngine().checkBlock(this.getBlockPos()));

  public DistantSignalBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.DISTANT_SIGNAL.get(), blockPos, blockState);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (!this.level.isClientSide()) {
      this.signalReceiver.refresh();
    }
  }

  public void blockRemoved() {
    this.signalReceiver.destroy();
  }

  @Override
  public SingleSignalReceiver getSignalReceiver() {
    return this.signalReceiver;
  }

  @Override
  public SignalAspect getPrimarySignalAspect() {
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
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    this.signalReceiver.writeToBuf(data);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.signalReceiver.readFromBuf(data);
  }
}
