/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signals;

import mods.railcraft.world.signal.NetworkType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class SignalReceiverNetwork extends AbstractNetwork<SignalController> {

  private boolean loaded;

  protected SignalReceiverNetwork(TileEntity blockEntity, int maxPairings, Runnable sync) {
    super(SignalController.class, blockEntity, maxPairings, sync);
  }

  @Override
  protected void peersChanged() {
    SignalTools.packetBuilder.syncNetworkPeers(NetworkType.SIGNAL_RECIEVER, this.getBlockPos(),
        this.getPeers(), this.getBlockEntity().getLevel().dimension());
  }

  @Override
  protected void requestPeers() {
    SignalTools.packetBuilder.requestNetworkPeersSync(NetworkType.SIGNAL_RECIEVER,
        this.getBlockPos());
  }

  @Override
  public boolean isValidPeer(BlockPos peerPos, SignalController peer) {
    return peer.getSignalControllerNetwork().isPeer(this.getBlockPos());
  }

  public void onControllerAspectChange(SignalControllerNetwork controller, SignalAspect aspect) {
    ((SignalReceiver) this.getBlockEntity()).onControllerAspectChange(controller, aspect);
  }

  @Override
  public boolean addPeer(SignalController peer) {
    this.registerController(peer.getSignalControllerNetwork());
    return true;
  }

  protected void registerController(SignalControllerNetwork controller) {
    this.addPairing(controller.getBlockPos());
  }

  @Override
  public void tickServer() {
    super.tickServer();
    if (!this.loaded) {
      this.loaded = true;
      for (BlockPos peerPos : this.getPeers()) {
        SignalController controller = this.getPeerAt(peerPos);
        if (controller != null) {
          this.syncToClient();
        }
      }
    }
  }
}
