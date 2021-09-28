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
public abstract class SignalControllerNetwork extends AbstractNetwork<SignalReceiver> {

  protected SignalControllerNetwork(TileEntity tile, int maxPairings, Runnable sync) {
    super(SignalReceiver.class, tile, maxPairings, sync);
  }

  public abstract SignalAspect getAspectFor(BlockPos receiver);

  @Override
  protected void peersChanged() {
    SignalTools.packetBuilder.syncNetworkPeers(NetworkType.SIGNAL_CONTROLLER, this.getBlockPos(),
        this.getPeers(), this.getBlockEntity().getLevel().dimension());
  }

  @Override
  protected void requestPeers() {
    SignalTools.packetBuilder.requestNetworkPeersSync(NetworkType.SIGNAL_CONTROLLER,
        this.getBlockPos());
  }

  @Override
  public boolean isValidPeer(BlockPos peerPos, SignalReceiver peer) {
    return peer.getSignalReceiverNetwork().isPeer(this.getBlockPos());
  }

  @Override
  public boolean addPeer(SignalReceiver peer) {
    this.registerReceiver(peer.getSignalReceiverNetwork());
    return true;
  }

  protected void registerReceiver(SignalReceiverNetwork receiver) {
    BlockPos coords = receiver.getBlockPos();
    addPairing(coords);
    receiver.registerController(this);
    receiver.syncToClient();
  }

  @Override
  public void tickClient() {
    super.tickClient();
    if (SignalTools.effectManager != null && SignalTools.effectManager.isTuningAuraActive()) {
      for (BlockPos coord : getPeers()) {
        SignalReceiver receiver = this.getPeerAt(coord);
        if (receiver != null) {
          SignalTools.effectManager.tuningEffect(getBlockEntity(), receiver.asBlockEntity());
        }
      }
    }
  }
}
