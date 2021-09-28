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
public class SimpleSignalControllerNetwork extends SignalControllerNetwork {

  private SignalAspect aspect = SignalAspect.BLINK_RED;
  private boolean loaded;

  public SimpleSignalControllerNetwork(TileEntity blockEntity, Runnable sync) {
    super(blockEntity, 1, sync);
  }

  public SignalAspect getAspect() {
    return this.aspect;
  }

  public void setAspect(SignalAspect aspect) {
    if (this.aspect != aspect) {
      this.aspect = aspect;
      syncReceivers();
    }
  }

  @Override
  public SignalAspect getAspectFor(BlockPos receiver) {
    return aspect;
  }

  @Override
  public void tickServer() {
    super.tickServer();
    if (!this.loaded) {
      this.loaded = true;
      this.syncReceivers();
    }
  }

  private void syncReceivers() {
    for (BlockPos peerPos : this.getPeers()) {
      SignalReceiver receiver = this.getPeerAt(peerPos);
      if (receiver != null) {
        receiver.getSignalReceiverNetwork().syncToClient();
      }
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
    return String.format("Controller:%s (%s)", aspect, super.toString());
  }
}
