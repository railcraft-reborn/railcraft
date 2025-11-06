package mods.railcraft.world.level.block.entity.signal;

import java.util.EnumSet;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.SignalAspect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
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
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    var actionAspectsTag = new ListTag();
    this.actionSignalAspects
        .forEach(aspect -> actionAspectsTag.add(StringTag.valueOf(aspect.getSerializedName())));
    tag.put(CompoundTagKeys.ACTION_SIGNAL_ASPECTS, actionAspectsTag);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    var actionAspectsTag = tag.getList(CompoundTagKeys.ACTION_SIGNAL_ASPECTS, Tag.TAG_STRING);
    this.actionSignalAspects.clear();
    for (var aspectTag : actionAspectsTag) {
      SignalAspect.fromName(aspectTag.getAsString()).ifPresent(this.actionSignalAspects::add);
    }
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
