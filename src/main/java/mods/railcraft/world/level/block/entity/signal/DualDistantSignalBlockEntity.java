package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.DualSignalReceiver;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalReceiver;
import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class DualDistantSignalBlockEntity extends AbstractSignalBlockEntity
    implements SignalReceiverEntity, DualSignalBlockEntity {

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
  public void preRemoveSideEffects(BlockPos pos, BlockState state) {
    super.preRemoveSideEffects(pos, state);
    this.blockRemoved();
  }

  private void blockRemoved() {
    this.signalReceiver.destroy();
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
  public SignalReceiver getSignalReceiver() {
    return this.signalReceiver;
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putChild(CompoundTagKeys.SIGNAL_RECEIVER, this.signalReceiver);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    input.readChild(CompoundTagKeys.SIGNAL_RECEIVER, this.signalReceiver);
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
