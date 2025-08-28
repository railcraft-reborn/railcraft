package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalReceiver;
import mods.railcraft.api.signal.SingleSignalReceiver;
import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;

public class SignalReceiverBoxBlockEntity extends ActionSignalBoxBlockEntity
    implements SignalReceiverEntity {

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
  protected void blockRemoved() {
    super.blockRemoved();
    this.signalReceiver.destroy();
  }

  @Override
  public int getRedstoneSignal(Direction direction) {
    return this.isActionSignalAspect(this.signalReceiver.getPrimarySignalAspect())
        ? Redstone.SIGNAL_MAX
        : Redstone.SIGNAL_NONE;
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
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.put(CompoundTagKeys.SIGNAL_RECEIVER, this.signalReceiver.serializeNBT(provider));
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    tag.getCompound(CompoundTagKeys.SIGNAL_RECEIVER).ifPresent(compoundTag -> {
      this.signalReceiver.deserializeNBT(provider, compoundTag);
    });
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf data) {
    super.writeToBuf(data);
    this.signalReceiver.writeToBuf(data);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
    super.readFromBuf(data);
    this.signalReceiver.readFromBuf(data);
  }
}
