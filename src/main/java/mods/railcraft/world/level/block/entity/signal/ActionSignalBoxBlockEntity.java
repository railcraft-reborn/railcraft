package mods.railcraft.world.level.block.entity.signal;

import java.util.ArrayList;
import java.util.EnumSet;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.SignalAspect;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class ActionSignalBoxBlockEntity extends LockableSignalBoxBlockEntity {

  private final EnumSet<SignalAspect> actionSignalAspects = EnumSet.of(SignalAspect.GREEN);

  public ActionSignalBoxBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState) {
    super(type, blockPos, blockState);
  }

  public final EnumSet<SignalAspect> getActionSignalAspects() {
    return this.actionSignalAspects;
  }

  public final boolean isActionSignalAspect(SignalAspect signalAspect) {
    return this.actionSignalAspects.contains(signalAspect);
  }

  public final void addActionSignalAspect(SignalAspect signalAspect) {
    this.actionSignalAspects.add(signalAspect);
    this.setChanged();
  }

  public final boolean removeActionSignalAspect(SignalAspect signalAspect) {
    boolean value = this.actionSignalAspects.remove(signalAspect);
    if (value) {
      this.setChanged();
    }
    return value;
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.store(CompoundTagKeys.ACTION_SIGNAL_ASPECTS, SignalAspect.CODEC.listOf(), new ArrayList<>(this.actionSignalAspects));
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    this.actionSignalAspects.clear();
    input.read(CompoundTagKeys.ACTION_SIGNAL_ASPECTS, SignalAspect.CODEC.listOf())
        .ifPresent(this.actionSignalAspects::addAll);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnumSet(this.actionSignalAspects, SignalAspect.class);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
    super.readFromBuf(data);
    this.actionSignalAspects.clear();
    this.actionSignalAspects.addAll(data.readEnumSet(SignalAspect.class));
  }
}
