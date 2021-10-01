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
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public abstract class ActionSignalBoxBlockEntity extends SecuredSignalBoxBlockEntity {

  private final Set<SignalAspect> actionAspects = EnumSet.of(SignalAspect.GREEN);

  public ActionSignalBoxBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  protected final boolean isActionAspect(SignalAspect aspect) {
    return this.actionAspects.contains(aspect);
  }

  protected final void addActionAspect(SignalAspect aspect) {
    this.actionAspects.add(aspect);
  }

  protected final void removeActionAspect(SignalAspect aspect) {
    this.actionAspects.remove(aspect);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    ListNBT actionAspectsTag = new ListNBT();
    this.actionAspects
        .forEach(aspect -> actionAspectsTag.add(StringNBT.valueOf(aspect.getSerializedName())));
    data.put("actionAspects", actionAspectsTag);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    ListNBT actionAspectsTag = data.getList("actionAspects", Constants.NBT.TAG_STRING);
    for (INBT aspectTag : actionAspectsTag) {
      SignalAspect.getByName(aspectTag.getAsString()).ifPresent(this.actionAspects::add);
    }
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeVarInt(this.actionAspects.size());
    this.actionAspects.forEach(data::writeEnum);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.actionAspects.clear();
    int size = data.readVarInt();
    for (int i = 0; i < size; i++) {
      this.actionAspects.add(data.readEnum(SignalAspect.class));
    }
  }
}
