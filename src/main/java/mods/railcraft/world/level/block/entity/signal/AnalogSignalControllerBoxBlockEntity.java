package mods.railcraft.world.level.block.entity.signal;

import java.util.BitSet;
import java.util.EnumMap;
import java.util.Map;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
  protected void blockRemoved() {
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
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putInt(CompoundTagKeys.INPUT_SIGNAL, this.inputSignal);

    var aspectsTag = output.childrenList(CompoundTagKeys.SIGNAL_ASPECT_TRIGGER_SIGNALS);
    for (var entry : this.signalAspectTriggerSignals.entrySet()) {
      var current = aspectsTag.addChild();
      current.store(CompoundTagKeys.NAME, SignalAspect.CODEC, entry.getKey());
      current.store(CompoundTagKeys.SIGNALS, ExtraCodecs.BIT_SET, entry.getValue());
    }

    output.putChild(CompoundTagKeys.SIGNAL_CONTROLLER, this.signalController);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    this.inputSignal = input.getIntOr(CompoundTagKeys.INPUT_SIGNAL, 0);

    var aspectsTag = input.childrenListOrEmpty(CompoundTagKeys.SIGNAL_ASPECT_TRIGGER_SIGNALS);
    aspectsTag.forEach(input1 -> {
      var name = input1.read(CompoundTagKeys.NAME, SignalAspect.CODEC).orElseThrow();
      var signals = input1.read(CompoundTagKeys.SIGNALS, ExtraCodecs.BIT_SET).orElseThrow();
      this.signalAspectTriggerSignals.put(name, signals);
    });
    input.readChild(CompoundTagKeys.SIGNAL_CONTROLLER, this.signalController);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf data) {
    super.writeToBuf(data);
    this.signalController.writeToBuf(data);
    data.writeMap(this.signalAspectTriggerSignals,
        FriendlyByteBuf::writeEnum, FriendlyByteBuf::writeBitSet);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
    super.readFromBuf(data);
    this.signalController.readFromBuf(data);
    this.signalAspectTriggerSignals.clear();
    this.signalAspectTriggerSignals.putAll(data.readMap(buf ->
            buf.readEnum(SignalAspect.class), FriendlyByteBuf::readBitSet));
  }
}
