package mods.railcraft.world.level.block.entity.signal;

import java.util.EnumSet;
import mods.railcraft.api.signal.SignalAspect;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
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
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    var actionAspectsTag = new ListTag();
    this.actionSignalAspects
        .forEach(aspect -> actionAspectsTag.add(StringTag.valueOf(aspect.getSerializedName())));
    tag.put("actionSignalAspects", actionAspectsTag);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    var actionAspectsTag = tag.getList("actionAspects", Tag.TAG_STRING);
    for (var aspectTag : actionAspectsTag) {
      SignalAspect.getByName(aspectTag.getAsString()).ifPresent(this.actionSignalAspects::add);
    }
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnumSet(this.actionSignalAspects, SignalAspect.class);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.actionSignalAspects.clear();
    this.actionSignalAspects.addAll(data.readEnumSet(SignalAspect.class));
  }
}
