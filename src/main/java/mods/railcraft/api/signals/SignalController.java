/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signals;

import javax.annotation.Nullable;
import mods.railcraft.world.signal.NetworkType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class SignalController extends AbstractNetwork {

  protected SignalController(String locTag, TileEntity tile, int maxPairings) {
    super(locTag, tile, maxPairings);
  }

  public @Nullable SignalReceiver getReceiverAt(BlockPos coord) {
    TileEntity recv = getPairAt(coord);
    if (recv != null) {
      return ((IReceiverProvider) recv).getReceiver();
    }
    return null;
  }

  public abstract SignalAspect getAspectFor(BlockPos receiver);

  @Override
  public void informPairsOfNameChange() {
    for (BlockPos coord : getPeers()) {
      SignalReceiver recv = getReceiverAt(coord);
      if (recv != null) {
        recv.onPairNameChange(getBlockPos(), getName());
      }
    }
  }

  @Override
  protected void peersChanged() {
    SignalTools.packetBuilder.sendPeerUpdate(NetworkType.CONTROLLER, this.getBlockPos(),
        this.getPeers(), this.getBlockEntity().getLevel().dimension());
  }

  @Override
  protected void requestPeers() {
    SignalTools.packetBuilder.sendPeerUpdateRequest(NetworkType.CONTROLLER, this.getBlockPos());
  }

  @Override
  protected String getTagName() {
    return "controller";
  }

  @Override
  public boolean isValidPair(BlockPos otherCoord, TileEntity otherTile) {
    if (otherTile instanceof IReceiverProvider) {
      SignalReceiver receiver = ((IReceiverProvider) otherTile).getReceiver();
      return receiver.isPeer(getBlockPos());
    }
    return false;
  }

  @Override
  public boolean add(TileEntity other) {
    if (other instanceof IReceiverProvider) {
      registerReceiver(((IReceiverProvider) other).getReceiver());
      return true;
    }
    return false;
  }

  protected void registerReceiver(SignalReceiver receiver) {
    BlockPos coords = receiver.getBlockPos();
    addPairing(coords);
    receiver.registerController(this);
    receiver.onControllerAspectChange(this, getAspectFor(coords));
  }

  @Override
  public void tickClient() {
    super.tickClient();
    if (SignalTools.effectManager != null && SignalTools.effectManager.isTuningAuraActive()) {
      for (BlockPos coord : getPeers()) {
        SignalReceiver receiver = getReceiverAt(coord);
        if (receiver != null) {
          SignalTools.effectManager.tuningEffect(getBlockEntity(), receiver.getBlockEntity());
        }
      }
    }
  }
}
