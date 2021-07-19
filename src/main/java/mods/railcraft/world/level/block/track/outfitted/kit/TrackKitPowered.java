package mods.railcraft.world.level.block.track.outfitted.kit;

import mods.railcraft.api.tracks.ITrackKitPowered;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

/**
 * Created by CovertJaguar on 3/31/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class TrackKitPowered extends TrackKitRailcraft implements ITrackKitPowered {

  protected boolean powered;

  @Override
  public int getRenderState() {
    return powered ? 1 : 0;
  }

  @Override
  public boolean isPowered() {
    return powered;
  }

  @Override
  public void setPowered(boolean powered) {
    if (this.powered != powered) {
      this.powered = powered;
      sendUpdateToClient();
    }
  }

  @Override
  public void writeToNBT(CompoundNBT data) {
    super.writeToNBT(data);
    data.putBoolean("powered", powered);
  }

  @Override
  public void readFromNBT(CompoundNBT data) {
    super.readFromNBT(data);
    powered = data.getBoolean("powered");
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);

    data.writeBoolean(powered);
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);

    boolean p = data.readBoolean();

    if (p != powered) {
      powered = p;
      markBlockNeedsUpdate();
    }
  }
}
