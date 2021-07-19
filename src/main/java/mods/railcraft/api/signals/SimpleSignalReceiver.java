/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signals;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class SimpleSignalReceiver extends SignalReceiver {

  private SignalAspect aspect = SignalAspect.BLINK_RED;

  public SimpleSignalReceiver(String locTag, TileEntity tile) {
    super(locTag, tile, 1);
  }

  @Override
  public void tickServer() {
    super.tickServer();
    SignalAspect prevAspect = getAspect();
    if (!isPaired()) {
      setAspect(SignalAspect.BLINK_RED);
    }
    if (prevAspect != getAspect()) {
      sendUpdateToClient();
    }
  }

  public SignalAspect getAspect() {
    return aspect;
  }

  public void setAspect(SignalAspect aspect) {
    this.aspect = aspect;
  }

  @Override
  public void onControllerAspectChange(SignalController con, SignalAspect aspect) {
    if (this.aspect != aspect) {
      this.aspect = aspect;
      super.onControllerAspectChange(con, aspect);
    }
  }

  @Override
  protected void saveNBT(CompoundNBT data) {
    super.saveNBT(data);
    data.putInt("aspect", aspect.getId());
  }

  @Override
  protected void loadNBT(CompoundNBT data) {
    super.loadNBT(data);
    aspect = SignalAspect.byId(data.getInt("aspect"));
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);
    data.writeVarInt(aspect.getId());
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);
    aspect = SignalAspect.byId(data.readVarInt());
  }

  @Override
  public String toString() {
    return String.format("Receiver:%s (%s)", aspect, super.toString());
  }
}
