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
public class SimpleSignalReceiverNetwork extends SignalReceiverNetwork {

  private SignalAspect aspect = SignalAspect.BLINK_RED;

  public SimpleSignalReceiverNetwork(TileEntity blockEntity, Runnable sync) {
    super(blockEntity, 1, sync);
  }

  @Override
  public void tickServer() {
    super.tickServer();
    SignalAspect prevAspect = getAspect();
    if (!hasPeers()) {
      setAspect(SignalAspect.BLINK_RED);
    }
    if (prevAspect != getAspect()) {
      syncToClient();
    }
  }

  public SignalAspect getAspect() {
    return aspect;
  }

  public void setAspect(SignalAspect aspect) {
    this.aspect = aspect;
  }

  @Override
  public void onControllerAspectChange(SignalControllerNetwork con, SignalAspect aspect) {
    if (this.aspect != aspect) {
      this.aspect = aspect;
      super.onControllerAspectChange(con, aspect);
    }
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT tag = super.serializeNBT();
    tag.putString("aspect", this.aspect.getName());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundNBT tag) {
    super.deserializeNBT(tag);
    this.aspect = SignalAspect.getByName(tag.getString("aspect")).get();
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeEnum(this.aspect);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.aspect = data.readEnum(SignalAspect.class);
  }

  @Override
  public String toString() {
    return String.format("Receiver:%s (%s)", aspect, super.toString());
  }
}
