/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signals;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class SimpleBlockSignal extends BlockSignalNetwork {

  private SignalAspect aspect = SignalAspect.BLINK_RED;

  public SimpleBlockSignal(TileEntity tile, Runnable sync) {
    super(tile, 1, sync);
  }

  @Override
  public void updateSignalAspect() {
    aspect = determineAspect(peers.peek());
  }

  @Override
  public SignalAspect getSignalAspect() {
    return aspect;
  }

  @Override
  protected SignalAspect getSignalAspectForPeer(BlockPos otherCoord) {
    return SignalAspect.GREEN;
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
}
