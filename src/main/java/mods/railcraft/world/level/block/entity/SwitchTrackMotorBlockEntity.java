package mods.railcraft.world.level.block.entity;

import java.util.EnumSet;
import java.util.Set;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalReceiver;
import mods.railcraft.api.signal.SignalReceiverProvider;
import mods.railcraft.api.signal.SingleSignalReceiver;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;

public class SwitchTrackMotorBlockEntity extends LockableSwitchTrackActuatorBlockEntity
    implements SignalReceiverProvider {

  private final SingleSignalReceiver signalReceiver =
      new SingleSignalReceiver(this, this::syncToClient, __ -> this.updateSwitched());

  private final Set<SignalAspect> actionSignalAspects = EnumSet.of(SignalAspect.GREEN);

  private boolean redstoneTriggered;
  private boolean powered;

  public SwitchTrackMotorBlockEntity() {
    super(RailcraftBlockEntityTypes.SWITCH_TRACK_MOTOR.get());
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
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.put("signalReceiver", this.signalReceiver.serializeNBT());
    ListNBT actionAspectsTag = new ListNBT();
    this.actionSignalAspects
        .forEach(aspect -> actionAspectsTag.add(StringNBT.valueOf(aspect.getSerializedName())));
    data.put("actionSignalAspects", actionAspectsTag);
    data.putBoolean("redstoneTriggered", this.redstoneTriggered);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.signalReceiver.deserializeNBT(data.getCompound("signalReceiver"));
    ListNBT actionAspectsTag = data.getList("actionAspects", Constants.NBT.TAG_STRING);
    for (INBT aspectTag : actionAspectsTag) {
      SignalAspect.getByName(aspectTag.getAsString()).ifPresent(this.actionSignalAspects::add);
    }
    this.redstoneTriggered = data.getBoolean("redstoneTriggered");
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    this.signalReceiver.writeSyncData(data);
    data.writeVarInt(this.actionSignalAspects.size());
    this.actionSignalAspects.forEach(data::writeEnum);
    data.writeBoolean(this.redstoneTriggered);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
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
