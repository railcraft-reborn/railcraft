package mods.railcraft.world.level.block.entity;

import java.util.EnumSet;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalReceiver;
import mods.railcraft.api.signal.SingleSignalReceiver;
import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import mods.railcraft.api.track.SwitchActuator;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

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
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put(CompoundTagKeys.SIGNAL_RECEIVER, this.signalReceiver.serializeNBT());
    var actionAspectsTag = new ListTag();
    this.actionSignalAspects
        .forEach(aspect -> actionAspectsTag.add(StringTag.valueOf(aspect.getSerializedName())));
    tag.put(CompoundTagKeys.ACTION_SIGNAL_ASPECTS, actionAspectsTag);
    tag.putBoolean(CompoundTagKeys.REDSTONE_TRIGGERED, this.redstoneTriggered);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.signalReceiver.deserializeNBT(tag.getCompound(CompoundTagKeys.SIGNAL_RECEIVER));
    var actionAspectsTag = tag.getList(CompoundTagKeys.ACTION_SIGNAL_ASPECTS, Tag.TAG_STRING);
    this.actionSignalAspects.clear();
    for (var aspectTag : actionAspectsTag) {
      SignalAspect.fromName(aspectTag.getAsString()).ifPresent(this.actionSignalAspects::add);
    }
    this.redstoneTriggered = tag.getBoolean(CompoundTagKeys.REDSTONE_TRIGGERED);
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    this.signalReceiver.writeToBuf(data);
    data.writeEnumSet(this.actionSignalAspects, SignalAspect.class);
    data.writeBoolean(this.redstoneTriggered);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.signalReceiver.readFromBuf(data);
    this.actionSignalAspects.clear();
    this.actionSignalAspects.addAll(data.readEnumSet(SignalAspect.class));
    this.redstoneTriggered = data.readBoolean();
  }
}
