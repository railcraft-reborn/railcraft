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

  private final Map<SignalAspect, BitSet> signalAspectTriggerSignals =
      new EnumMap<>(SignalAspect.class);

  public AnalogSignalControllerBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.ANALOG_SIGNAL_CONTROLLER_BOX.get());
    for (SignalAspect signalAspect : SignalAspect.values()) {
      this.signalAspectTriggerSignals.put(signalAspect, new BitSet());
    }
  }

  public Map<SignalAspect, BitSet> getSignalAspectTriggerSignals() {
    return this.signalAspectTriggerSignals;
  }

  public void setSignalAspectTriggerSignals(Map<SignalAspect, BitSet> signalAspectTriggerSignals) {
    this.signalAspectTriggerSignals.putAll(signalAspectTriggerSignals);
    this.updateSignalAspect();
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    this.signalController.removed();
  }

  @Override
  public void load() {
    if (!this.level.isClientSide()) {
      this.updateSignalAspect();
      this.signalController.refresh();
    }
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      this.signalController.spawnTuningAuraParticles();
      return;
    }
  }

  @Override
  public void neighborChanged() {
    int inputSignal = this.calculateInputSignal();
    if (inputSignal != this.inputSignal) {
      this.inputSignal = inputSignal;
      this.updateSignalAspect();
    }
  }

  private void updateSignalAspect() {
    this.calculatedSignalAspect = SignalAspect.OFF;
    for (Map.Entry<SignalAspect, BitSet> entry : this.signalAspectTriggerSignals.entrySet()) {
      SignalAspect current = entry.getKey();
      if (entry.getValue().get(this.inputSignal))
        this.calculatedSignalAspect = (this.calculatedSignalAspect == SignalAspect.OFF) ? current
            : SignalAspect.mostRestrictive(this.calculatedSignalAspect, current);
    }
    this.signalController.setSignalAspect(this.calculatedSignalAspect);
  }

  private int calculateInputSignal() {
    int signal = 0, tmp;
    for (Direction direction : Direction.values()) {
      if (direction == Direction.UP)
        continue;
      if (this.level.getBlockEntity(
          this.getBlockPos().relative(direction)) instanceof AbstractSignalBoxBlockEntity)
        continue;
      if ((tmp = this.level.getSignal(this.getBlockPos().relative(direction), direction)) > signal)
        signal = tmp;
      if ((tmp =
          this.level.getSignal(this.getBlockPos().relative(direction).below(), direction)) > signal)
        signal = tmp;
    }
    return signal;
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    return this.signalController.getSignalAspect();
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
    for (Map.Entry<SignalAspect, BitSet> entry : this.signalAspectTriggerSignals.entrySet()) {
      CompoundNBT nbt = new CompoundNBT();
      nbt.putString("name", entry.getKey().getSerializedName());
      nbt.putByteArray("signals", entry.getValue().toByteArray());
    }
    data.put("signalAspectTriggerSignals", aspectsTag);

    data.put("signalController", this.signalController.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.inputSignal = data.getInt("inputSignal");

    ListNBT aspectsTag = data.getList("signalAspectTriggerSignals", Constants.NBT.TAG_COMPOUND);
    for (INBT nbt : aspectsTag) {
      CompoundNBT compoundNbt = (CompoundNBT) nbt;
      this.signalAspectTriggerSignals.put(
          SignalAspect.getByName(compoundNbt.getString("name")).get(),
          BitSet.valueOf(compoundNbt.getByteArray("signals")));
    }

    this.signalController.deserializeNBT(data.getCompound("signalController"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    this.signalController.writeSyncData(data);
    data.writeVarInt(this.signalAspectTriggerSignals.size());
    for (Map.Entry<SignalAspect, BitSet> entry : this.signalAspectTriggerSignals.entrySet()) {
      data.writeEnum(entry.getKey());
      data.writeByteArray(entry.getValue().toByteArray());
    }
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.signalController.readSyncData(data);
    this.signalAspectTriggerSignals.clear();
    int size = data.readVarInt();
    for (int i = 0; i < size; i++) {
      this.signalAspectTriggerSignals.put(data.readEnum(SignalAspect.class),
          BitSet.valueOf(data.readByteArray(2048)));
    }
  }
}
