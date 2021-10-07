package mods.railcraft.world.level.block.entity.signal;

import java.util.EnumSet;
import java.util.Set;
import mods.railcraft.api.signal.SignalAspect;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public abstract class ActionSignalBoxBlockEntity extends SecureSignalBoxBlockEntity {

  private final Set<SignalAspect> actionSignalAspects = EnumSet.of(SignalAspect.GREEN);

  public ActionSignalBoxBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  public final Set<SignalAspect> getActionSignalAspects() {
    return this.actionSignalAspects;
  }

  protected final boolean isActionSignalAspect(SignalAspect signalAspect) {
    return this.actionSignalAspects.contains(signalAspect);
  }

  protected final void addActionSignalAspect(SignalAspect signalAspect) {
    this.actionSignalAspects.add(signalAspect);
  }

  protected final void removeActionSignalAspect(SignalAspect signalAspect) {
    this.actionSignalAspects.remove(signalAspect);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    ListNBT actionAspectsTag = new ListNBT();
    this.actionSignalAspects
        .forEach(aspect -> actionAspectsTag.add(StringNBT.valueOf(aspect.getSerializedName())));
    data.put("actionSignalAspects", actionAspectsTag);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    ListNBT actionAspectsTag = data.getList("actionAspects", Constants.NBT.TAG_STRING);
    for (INBT aspectTag : actionAspectsTag) {
      SignalAspect.getByName(aspectTag.getAsString()).ifPresent(this.actionSignalAspects::add);
    }
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeVarInt(this.actionSignalAspects.size());
    this.actionSignalAspects.forEach(data::writeEnum);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.actionSignalAspects.clear();
    int size = data.readVarInt();
    for (int i = 0; i < size; i++) {
      this.actionSignalAspects.add(data.readEnum(SignalAspect.class));
    }
  }
}
