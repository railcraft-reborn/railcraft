package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SingleSignalReceiver;
import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
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

  @Override
  public void preRemoveSideEffects(BlockPos pos, BlockState state) {
    super.preRemoveSideEffects(pos, state);
    this.blockRemoved();
  }

  private void blockRemoved() {
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
