/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signals;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class SimpleSignalController extends SignalController {

  private SignalAspect aspect = SignalAspect.BLINK_RED;
  private boolean needsInit = true;

  public SimpleSignalController(String locTag, TileEntity tile) {
    super(locTag, tile, 1);
  }

  public SignalAspect getAspect() {
    return aspect;
  }

  public void setAspect(SignalAspect aspect) {
    if (this.aspect != aspect) {
      this.aspect = aspect;
      updateReceiver();
    }
  }

  @Override
  public SignalAspect getAspectFor(BlockPos receiver) {
    return aspect;
  }

  @Override
  public void tickServer() {
    super.tickServer();
    if (needsInit) {
      needsInit = false;
      updateReceiver();
    }
  }

  private void updateReceiver() {
    for (BlockPos recv : getPeers()) {
      SignalReceiver receiver = getReceiverAt(recv);
      if (receiver != null) {
        receiver.onControllerAspectChange(this, aspect);
      }
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
    return String.format("Controller:%s (%s)", aspect, super.toString());
  }
}
