package mods.railcraft.world.level.block.entity;

import java.util.BitSet;
import mods.railcraft.api.signals.SignalAspect;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public abstract class ActionManagerBoxBlockEntity extends SecuredSignalBoxBlockEntity implements IAspectResponder {

  private BitSet powerOnAspects = new BitSet(SignalAspect.values().length);

  public ActionManagerBoxBlockEntity(TileEntityType<?> type) {
    super(type);
    setActionState(SignalAspect.GREEN, true);
  }

  @Override
  public boolean doesActionOnAspect(SignalAspect aspect) {
    return powerOnAspects.get(aspect.ordinal());
  }

  protected final void setActionState(SignalAspect aspect, boolean trigger) {
    powerOnAspects.set(aspect.ordinal(), trigger);
  }

  @Override
  public void doActionOnAspect(SignalAspect aspect, boolean trigger) {
    setActionState(aspect, trigger);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putByteArray("powerOnAspects", powerOnAspects.toByteArray());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    if (data.contains("powerOnAspects", Constants.NBT.TAG_BYTE_ARRAY)) {
      powerOnAspects = BitSet.valueOf(data.getByteArray("powerOnAspects"));
    }
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);
    writeActionInfo(data);
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);
    powerOnAspects = BitSet.valueOf(data.readByteArray(data.readVarInt()));
  }

  private void writeActionInfo(PacketBuffer data) {
    byte[] bytes = powerOnAspects.toByteArray();
    data.writeVarInt(bytes.length);
    data.writeByteArray(bytes);
  }
}
