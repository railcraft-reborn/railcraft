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
public class DualSignalReceiver extends SignalReceiverNetwork {

  private SignalAspect topAspect = SignalAspect.BLINK_RED;
  private SignalAspect bottomAspect = SignalAspect.BLINK_RED;

  public DualSignalReceiver(TileEntity tile, Runnable sync) {
    super(tile, 2, sync);
  }

  @Override
  public void onControllerAspectChange(SignalControllerNetwork controller, SignalAspect aspect) {
    BlockPos blockPos = this.peers.peekFirst();
    if (blockPos == null) {
      return;
    }
    if (blockPos.equals(controller.getBlockPos())) {
      if (this.topAspect != aspect) {
        this.topAspect = aspect;
        this.syncToClient();
      }
    } else {
      if (this.bottomAspect != aspect) {
        this.bottomAspect = aspect;
        this.syncToClient();
      }
    }
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT tag = super.serializeNBT();
    tag.putString("topAspect", this.topAspect.getName());
    tag.putString("bottomAspect", this.bottomAspect.getName());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundNBT data) {
    super.deserializeNBT(data);
    this.topAspect = SignalAspect.getByName(data.getString("topAspect")).get();
    this.bottomAspect = SignalAspect.getByName(data.getString("bottomAspect")).get();
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeEnum(this.topAspect);
    data.writeEnum(this.bottomAspect);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.topAspect = data.readEnum(SignalAspect.class);
    this.bottomAspect = data.readEnum(SignalAspect.class);
  }

  public SignalAspect getTopAspect() {
    return this.topAspect;
  }

  public boolean setTopAspect(SignalAspect topAspect) {
    if (this.topAspect != topAspect) {
      this.topAspect = topAspect;
      return true;
    }
    return false;
  }

  public SignalAspect getBottomAspect() {
    return this.bottomAspect;
  }

  public boolean setBottomAspect(SignalAspect bottomAspect) {
    if (this.bottomAspect != bottomAspect) {
      this.bottomAspect = bottomAspect;
      return true;
    }
    return false;
  }
}
