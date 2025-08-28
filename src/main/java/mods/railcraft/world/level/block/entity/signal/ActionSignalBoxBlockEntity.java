package mods.railcraft.world.level.block.entity.signal;

import java.util.ArrayList;
import java.util.EnumSet;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.SignalAspect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ActionSignalBoxBlockEntity extends LockableSignalBoxBlockEntity {

  private final EnumSet<SignalAspect> actionSignalAspects = EnumSet.of(SignalAspect.GREEN);

  public ActionSignalBoxBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState) {
    super(type, blockPos, blockState);
  }

  public final EnumSet<SignalAspect> getActionSignalAspects() {
    return this.actionSignalAspects;
  }

  protected final boolean isActionSignalAspect(SignalAspect signalAspect) {
    return this.actionSignalAspects.contains(signalAspect);
  }

  protected final void addActionSignalAspect(SignalAspect signalAspect) {
    this.actionSignalAspects.add(signalAspect);
    this.setChanged();
  }

  protected final void removeActionSignalAspect(SignalAspect signalAspect) {
    this.actionSignalAspects.remove(signalAspect);
    this.setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.store(CompoundTagKeys.ACTION_SIGNAL_ASPECTS, SignalAspect.CODEC.listOf(), new ArrayList<>(this.actionSignalAspects));
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.actionSignalAspects.clear();
    tag.read(CompoundTagKeys.ACTION_SIGNAL_ASPECTS, SignalAspect.CODEC.listOf())
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
