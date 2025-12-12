package mods.railcraft.world.level.block.entity;

import java.util.ArrayList;
import java.util.EnumSet;
import org.jspecify.annotations.Nullable;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalReceiver;
import mods.railcraft.api.signal.SingleSignalReceiver;
import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import mods.railcraft.api.track.SwitchActuator;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SwitchTrackMotorBlockEntity extends LockableSwitchTrackActuatorBlockEntity
    implements SignalReceiverEntity, SwitchActuator {

  private final SingleSignalReceiver signalReceiver =
      new SingleSignalReceiver(this, this::syncToClient, __ -> this.updateSwitched());

  private final EnumSet<SignalAspect> actionSignalAspects = EnumSet.of(SignalAspect.GREEN);

  private boolean redstoneTriggered;
  private boolean powered;

  public SwitchTrackMotorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SWITCH_TRACK_MOTOR.get(), blockPos, blockState);
  }

  public void neighborChanged() {
    boolean lastPowered = this.powered;
    this.powered = this.level.hasNeighborSignal(this.getBlockPos());
    if (this.redstoneTriggered && lastPowered != this.powered) {
      this.updateSwitched();
    }
  }

  private void updateSwitched() {
    boolean switched = this.powered
        || this.actionSignalAspects.contains(this.signalReceiver.getPrimarySignalAspect());
    SwitchTrackActuatorBlock.setSwitched(
        this.getBlockState(), this.level, this.getBlockPos(), switched);
  }

  @Override
  public boolean shouldSwitch(RollingStock cart) {
    return SwitchTrackActuatorBlock.isSwitched(this.getBlockState());
  }

  public EnumSet<SignalAspect> getActionSignalAspects() {
    return this.actionSignalAspects;
  }

  public boolean isRedstoneTriggered() {
    return this.redstoneTriggered;
  }

  public void setRedstoneTriggered(boolean redstoneTriggered) {
    this.redstoneTriggered = redstoneTriggered;
    this.updateSwitched();
  }

  @Override
  public void setCustomName(@Nullable Component name) {
    super.setCustomName(name);
  }

  @Override
  public SignalReceiver getSignalReceiver() {
    return this.signalReceiver;
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putChild(CompoundTagKeys.SIGNAL_RECEIVER, this.signalReceiver);
    output.store(CompoundTagKeys.ACTION_SIGNAL_ASPECTS, SignalAspect.CODEC.listOf(), new ArrayList<>(this.actionSignalAspects));
    output.putBoolean(CompoundTagKeys.REDSTONE_TRIGGERED, this.redstoneTriggered);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    input.readChild(CompoundTagKeys.SIGNAL_RECEIVER, this.signalReceiver);
    this.actionSignalAspects.clear();
    input.read(CompoundTagKeys.ACTION_SIGNAL_ASPECTS, SignalAspect.CODEC.listOf())
        .ifPresent(this.actionSignalAspects::addAll);
    this.redstoneTriggered = input.getBooleanOr(CompoundTagKeys.REDSTONE_TRIGGERED, false);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf data) {
    super.writeToBuf(data);
    this.signalReceiver.writeToBuf(data);
    data.writeEnumSet(this.actionSignalAspects, SignalAspect.class);
    data.writeBoolean(this.redstoneTriggered);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
    super.readFromBuf(data);
    this.signalReceiver.readFromBuf(data);
    this.actionSignalAspects.clear();
    this.actionSignalAspects.addAll(data.readEnumSet(SignalAspect.class));
    this.redstoneTriggered = data.readBoolean();
  }
}
