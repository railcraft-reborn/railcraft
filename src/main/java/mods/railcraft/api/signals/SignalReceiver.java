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
public abstract class SignalReceiver extends AbstractNetwork {

  private boolean needsInit = true;

  protected SignalReceiver(String locTag, TileEntity tile, int maxPairings) {
    super(locTag, tile, maxPairings);
  }

  public @Nullable SignalController getControllerAt(BlockPos coord) {
    TileEntity con = getPairAt(coord);
    if (con != null) {
      return ((IControllerProvider) con).getController();
    }
    return null;
  }

  @Override
  public void informPairsOfNameChange() {
    for (BlockPos coord : getPeers()) {
      SignalController ctrl = getControllerAt(coord);
      if (ctrl != null) {
        ctrl.onPairNameChange(getBlockPos(), getName());
      }
    }
  }

  @Override
  protected void peersChanged() {
    SignalTools.packetBuilder.sendPeerUpdate(NetworkType.RECIEVER, this.getBlockPos(),
        this.getPeers(), this.getTile().getLevel().dimension());
  }

  @Override
  protected void requestPeers() {
    SignalTools.packetBuilder.sendPeerUpdateRequest(NetworkType.RECIEVER, this.getBlockPos());
  }

  @Override
  protected String getTagName() {
    return "receiver";
  }

  @Override
  public boolean isValidPair(BlockPos otherCoord, TileEntity otherTile) {
    if (otherTile instanceof IControllerProvider) {
      SignalController controller = ((IControllerProvider) otherTile).getController();
      return controller.isPeer(getBlockPos());
    }
    return false;
  }

  public void onControllerAspectChange(SignalController con, SignalAspect aspect) {
    ((IReceiverProvider) blockEntity).onControllerAspectChange(con, aspect);
  }

  @Override
  public boolean add(TileEntity other) {
    if (blockEntity instanceof IControllerProvider) {
      registerController(((IControllerProvider) other).getController());
      return true;
    }
    return false;
  }

  protected void registerController(SignalController controller) {
    addPairing(controller.getBlockPos());
  }

  @Override
  public void tickServer() {
    super.tickServer();
    if (needsInit) {
      needsInit = false;
      for (BlockPos pair : getPeers()) {
        SignalController controller = getControllerAt(pair);
        if (controller != null) {
          onControllerAspectChange(controller, controller.getAspectFor(getBlockPos()));
        }
      }
    }
  }
}
