package mods.railcraft.world.level.block.entity;

import java.util.EnumSet;
import java.util.Set;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalReceiver;
import mods.railcraft.api.signal.SignalReceiverProvider;
import mods.railcraft.api.signal.SingleSignalReceiver;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

public class SwitchTrackMotorBlockEntity extends LockableSwitchTrackActuatorBlockEntity
    implements SignalReceiverProvider {

  private final SingleSignalReceiver signalReceiver =
      new SingleSignalReceiver(this, this::syncToClient, __ -> this.updateSwitched());

  private final Set<SignalAspect> actionSignalAspects = EnumSet.of(SignalAspect.GREEN);

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

  public Set<SignalAspect> getActionSignalAspects() {
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
  public SignalReceiver getSignalReceiver() {
    return this.signalReceiver;
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("signalReceiver", this.signalReceiver.serializeNBT());
    ListTag actionAspectsTag = new ListTag();
    this.actionSignalAspects
        .forEach(aspect -> actionAspectsTag.add(StringTag.valueOf(aspect.getSerializedName())));
    tag.put("actionSignalAspects", actionAspectsTag);
    tag.putBoolean("redstoneTriggered", this.redstoneTriggered);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.signalReceiver.deserializeNBT(tag.getCompound("signalReceiver"));
    ListTag actionAspectsTag = tag.getList("actionAspects", Tag.TAG_STRING);
    for (Tag aspectTag : actionAspectsTag) {
      SignalAspect.getByName(aspectTag.getAsString()).ifPresent(this.actionSignalAspects::add);
    }
    this.redstoneTriggered = tag.getBoolean("redstoneTriggered");
  }

  @Override
  public void writeSyncData(FriendlyByteBuf data) {
    super.writeSyncData(data);
    this.signalReceiver.writeSyncData(data);
    data.writeVarInt(this.actionSignalAspects.size());
    this.actionSignalAspects.forEach(data::writeEnum);
    data.writeBoolean(this.redstoneTriggered);
  }

  @Override
  public void readSyncData(FriendlyByteBuf data) {
    super.readSyncData(data);
    this.signalReceiver.readSyncData(data);
    this.actionSignalAspects.clear();
    int size = data.readVarInt();
    for (int i = 0; i < size; i++) {
      this.actionSignalAspects.add(data.readEnum(SignalAspect.class));
    }
    this.redstoneTriggered = data.readBoolean();
  }
}
