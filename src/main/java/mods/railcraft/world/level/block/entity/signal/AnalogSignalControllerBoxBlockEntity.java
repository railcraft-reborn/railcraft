package mods.railcraft.world.level.block.entity.signal;

import java.util.BitSet;
import java.util.EnumMap;
import java.util.Map;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AnalogSignalControllerBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements SignalControllerEntity {

  private final SimpleSignalController signalController =
      new SimpleSignalController(1, this::syncToClient, this, true);

  private int inputSignal;
  private SignalAspect calculatedSignalAspect;

  private final Map<SignalAspect, BitSet> signalAspectTriggerSignals =
      new EnumMap<>(SignalAspect.class);

  public AnalogSignalControllerBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.ANALOG_SIGNAL_CONTROLLER_BOX.get(), blockPos, blockState);
    for (SignalAspect signalAspect : SignalAspect.values()) {
      this.signalAspectTriggerSignals.put(signalAspect, new BitSet());
    }
  }

  public Map<SignalAspect, BitSet> getSignalAspectTriggerSignals() {
    return this.signalAspectTriggerSignals;
  }

  public void setSignalAspectTriggerSignals(Map<SignalAspect, BitSet> signalAspectTriggerSignals) {
    this.signalAspectTriggerSignals.putAll(signalAspectTriggerSignals);
    this.setChanged();
    this.updateSignalAspect();
  }

  @Override
  public void blockRemoved() {
    super.blockRemoved();
    this.signalController.destroy();
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (!this.level.isClientSide()) {
      this.updateSignalAspect();
      this.signalController.refresh();
    }
  }

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState,
      AnalogSignalControllerBoxBlockEntity blockEntity) {
    blockEntity.signalController.spawnTuningAuraParticles();
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
    for (var entry : this.signalAspectTriggerSignals.entrySet()) {
      var current = entry.getKey();
      if (entry.getValue().get(this.inputSignal)) {
        this.calculatedSignalAspect = (this.calculatedSignalAspect == SignalAspect.OFF) ? current
            : SignalAspect.mostRestrictive(this.calculatedSignalAspect, current);
      }
    }
    this.signalController.setSignalAspect(this.calculatedSignalAspect);
  }

  private int calculateInputSignal() {
    int signal = 0, tmp;
    for (var direction : Direction.values()) {
      if (direction == Direction.UP) {
        continue;
      }
      if (this.level.getBlockEntity(
          this.getBlockPos().relative(direction)) instanceof AbstractSignalBoxBlockEntity) {
        continue;
      }
      if ((tmp =
          this.level.getSignal(this.getBlockPos().relative(direction), direction)) > signal) {
        signal = tmp;
      }
      if ((tmp =
          this.level.getSignal(this.getBlockPos().relative(direction).below(),
              direction)) > signal) {
        signal = tmp;
      }
    }
    return signal;
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    return this.signalController.aspect();
  }

  @Override
  public SimpleSignalController getSignalController() {
    return this.signalController;
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putInt("inputSignal", this.inputSignal);

    var aspectsTag = new ListTag();
    for (var entry : this.signalAspectTriggerSignals.entrySet()) {
      var nbt = new CompoundTag();
      nbt.putString("name", entry.getKey().getSerializedName());
      nbt.putByteArray("signals", entry.getValue().toByteArray());
    }
    tag.put("signalAspectTriggerSignals", aspectsTag);

    tag.put("signalController", this.signalController.serializeNBT());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.inputSignal = tag.getInt("inputSignal");

    var aspectsTag = tag.getList("signalAspectTriggerSignals", Tag.TAG_COMPOUND);
    for (var nbt : aspectsTag) {
      var compoundNbt = (CompoundTag) nbt;
      this.signalAspectTriggerSignals.put(
          SignalAspect.getByName(compoundNbt.getString("name")).get(),
          BitSet.valueOf(compoundNbt.getByteArray("signals")));
    }

    this.signalController.deserializeNBT(tag.getCompound("signalController"));
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    this.signalController.writeToBuf(data);
    data.writeMap(this.signalAspectTriggerSignals,
        FriendlyByteBuf::writeEnum, FriendlyByteBuf::writeBitSet);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.signalController.readFromBuf(data);
    this.signalAspectTriggerSignals.clear();
    this.signalAspectTriggerSignals.putAll(data.readMap(buf ->
            buf.readEnum(SignalAspect.class), FriendlyByteBuf::readBitSet));
  }
}
