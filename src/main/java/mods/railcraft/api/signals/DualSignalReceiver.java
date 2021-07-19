/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signals;

import java.util.EnumMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class DualSignalReceiver extends SignalReceiver {

  private EnumMap<DualLamp, SignalAspect> aspects = new EnumMap<>(DualLamp.class);

  public DualSignalReceiver(String locTag, TileEntity tile) {
    super(locTag, tile, 2);
  }

  {
    for (DualLamp lamp : DualLamp.values()) {
      aspects.put(lamp, SignalAspect.BLINK_RED);
    }
  }

  @Override
  public void onControllerAspectChange(SignalController con, SignalAspect aspect) {
    BlockPos coord = peers.peekFirst();
    if (coord == null) {
      return;
    }
    if (coord.equals(con.getBlockPos())) {
      if (setAspect(DualLamp.TOP, aspect)) {
        super.onControllerAspectChange(con, aspect);
      }
    } else {
      if (setAspect(DualLamp.BOTTOM, aspect)) {
        super.onControllerAspectChange(con, aspect);
      }
    }
  }

  @Override
  protected void saveNBT(CompoundNBT data) {
    super.saveNBT(data);
    data.putByte("topAspect", (byte) aspects.get(DualLamp.TOP).ordinal());
    data.putByte("bottomAspect", (byte) aspects.get(DualLamp.BOTTOM).ordinal());
  }

  @Override
  protected void loadNBT(CompoundNBT data) {
    super.loadNBT(data);
    setAspect(DualLamp.TOP, SignalAspect.values()[data.getByte("topAspect")]);
    setAspect(DualLamp.BOTTOM, SignalAspect.values()[data.getByte("bottomAspect")]);
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);
    data.writeByte(aspects.get(DualLamp.TOP).ordinal());
    data.writeByte(aspects.get(DualLamp.BOTTOM).ordinal());
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);
    setAspect(DualLamp.TOP, SignalAspect.values()[data.readByte()]);
    setAspect(DualLamp.BOTTOM, SignalAspect.values()[data.readByte()]);
  }

  public SignalAspect getAspect(DualLamp lamp) {
    return aspects.get(lamp);
  }

  public boolean setAspect(DualLamp lamp, SignalAspect aspect) {
    return aspects.put(lamp, aspect) != aspect;
  }
}
