package mods.railcraft.world.level.block.entity.signal;

import java.util.BitSet;
import java.util.EnumMap;
import java.util.Map;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalControllerProvider;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;

public class AnalogSignalControllerBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements SignalControllerProvider, ITickableTileEntity {

  private final SimpleSignalController signalController =
      new SimpleSignalController(1, this::syncToClient, this, true);

  private int inputSignal;
  private SignalAspect calculatedSignalAspect;

  public Map<SignalAspect, BitSet> aspectTriggerSignals = new EnumMap<>(SignalAspect.class);

  private boolean loaded;

  public AnalogSignalControllerBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.ANALOG_SIGNAL_CONTROLLER_BOX.get());
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    this.signalController.removed();
  }

  @Override
  public void tick() {
    if (this.level.isClientSide()) {
      this.signalController.spawnTuningAuraParticles();
      return;
    }
    if (!this.loaded) {
      this.loaded = true;
      this.signalController.refresh();
    }
  }

  @Override
  public void neighborChanged() {
    int inputSignal = this.calculateInputSignal();
    if (inputSignal != this.inputSignal) {
      this.inputSignal = inputSignal;
      this.calculatedSignalAspect = SignalAspect.OFF;
      for (Map.Entry<SignalAspect, BitSet> entry : this.aspectTriggerSignals.entrySet()) {
        SignalAspect current = entry.getKey();
        if (entry.getValue().get(this.inputSignal))
          this.calculatedSignalAspect = (this.calculatedSignalAspect == SignalAspect.OFF) ? current
              : SignalAspect.mostRestrictive(this.calculatedSignalAspect, current);
      }
      this.signalController.setSignalAspect(this.calculatedSignalAspect);
    }
  }

  private int calculateInputSignal() {
    int signal = 0, tmp;
    for (Direction direction : Direction.values()) {
      if (direction == Direction.UP)
        continue;
      if (this.level.getBlockEntity(
          this.getBlockPos().relative(direction)) instanceof AbstractSignalBoxBlockEntity)
        continue;
      if ((tmp = this.level.getSignal(this.getBlockPos(), direction)) > signal)
        signal = tmp;
      if ((tmp = this.level.getSignal(this.getBlockPos().below(), direction)) > signal)
        signal = tmp;
    }
    return signal;
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    if (this.signalController.isLinking()) {
      return SignalAspect.BLINK_YELLOW;
    } else if (!this.signalController.hasPeers()) {
      return SignalAspect.BLINK_RED;
    } else {
      return this.calculatedSignalAspect;
    }
  }

  @Override
  public SimpleSignalController getSignalController() {
    return this.signalController;
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putInt("inputSignal", this.inputSignal);

    ListNBT aspectsTag = new ListNBT();
    for (Map.Entry<SignalAspect, BitSet> entry : this.aspectTriggerSignals.entrySet()) {
      CompoundNBT nbt = new CompoundNBT();
      nbt.putString("name", entry.getKey().getSerializedName());
      nbt.putByteArray("signals", entry.getValue().toByteArray());
    }
    data.put("aspects", aspectsTag);

    data.put("signalController", this.signalController.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.inputSignal = data.getInt("inputSignal");

    this.aspectTriggerSignals.clear();
    ListNBT aspectsNbt = data.getList("aspects", Constants.NBT.TAG_COMPOUND);
    for (INBT nbt : aspectsNbt) {
      CompoundNBT compoundNbt = (CompoundNBT) nbt;
      this.aspectTriggerSignals.put(SignalAspect.getByName(compoundNbt.getString("name")).get(),
          BitSet.valueOf(compoundNbt.getByteArray("signals")));
    }

    this.signalController.deserializeNBT(data.getCompound("signalController"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    this.signalController.writeSyncData(data);
    data.writeVarInt(this.aspectTriggerSignals.size());
    for (Map.Entry<SignalAspect, BitSet> entry : this.aspectTriggerSignals.entrySet()) {
      data.writeEnum(entry.getKey());
      byte[] signals = entry.getValue().toByteArray();
      data.writeVarInt(signals.length);
      data.writeBytes(signals);
    }
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.signalController.readSyncData(data);
    this.aspectTriggerSignals.clear();
    int size = data.readVarInt();
    for (int i = 0; i < size; i++) {
      this.aspectTriggerSignals.put(data.readEnum(SignalAspect.class),
          BitSet.valueOf(data.readByteArray(data.readVarInt())));
    }
  }
}
