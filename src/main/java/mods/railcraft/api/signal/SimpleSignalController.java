/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class SimpleSignalController extends BlockEntitySignalNetwork<SignalReceiverProvider>
    implements SignalController {

  private final boolean blinkRedWithoutPeers;
  @Nullable
  private final Consumer<SignalAspect> signalAspectListener;
  private SignalAspect signalAspect = SignalAspect.GREEN;

  public SimpleSignalController(int maxPeers, Runnable syncListener, TileEntity blockEntity,
      boolean blinkRedWithoutPeers) {
    this(maxPeers, syncListener, blockEntity, blinkRedWithoutPeers, null);
  }

  public SimpleSignalController(int maxPeers, Runnable syncListener, TileEntity blockEntity,
      boolean blinkRedWithoutPeers,
      @Nullable Consumer<SignalAspect> signalAspectListener) {
    super(SignalReceiverProvider.class, maxPeers, syncListener, blockEntity);
    this.blinkRedWithoutPeers = blinkRedWithoutPeers;
    this.signalAspectListener = signalAspectListener;
  }

  @Override
  public boolean addPeer(SignalReceiverProvider peer) {
    boolean hadPeers = this.hasPeers();
    if (super.addPeer(peer)) {
      peer.getSignalReceiver().linked(this);
      if (!hadPeers && this.blinkRedWithoutPeers && this.signalAspectListener != null) {
        this.signalAspectListener.accept(this.getSignalAspect());
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean removePeer(BlockPos peerPos) {
    if (super.removePeer(peerPos)) {
      SignalReceiverProvider signalReceiverProvider = this.getBlockEntity(peerPos);
      if (signalReceiverProvider != null) {
        signalReceiverProvider.getSignalReceiver().unlinked(this);
      }
      if (!this.hasPeers() && this.blinkRedWithoutPeers && this.signalAspectListener != null) {
        this.signalAspectListener.accept(SignalAspect.BLINK_RED);
      }
      return true;
    }
    return false;
  }

  @Override
  public void startLinking() {
    super.startLinking();
    this.broadcastSignalAspect(SignalAspect.BLINK_YELLOW);
  }

  @Override
  public void stopLinking() {
    super.stopLinking();
    this.broadcastSignalAspect(this.getSignalAspect());
  }

  @Override
  public boolean refreshPeer(SignalReceiverProvider peer) {
    peer.getSignalReceiver().receiveSignalAspect(this, this.getSignalAspect());
    return true;
  }

  @Override
  public SignalAspect getSignalAspect() {
    if (this.isLinking()) {
      return SignalAspect.BLINK_YELLOW;
    } else if (this.hasPeers() || !this.blinkRedWithoutPeers) {
      return this.signalAspect;
    } else {
      return SignalAspect.BLINK_RED;
    }
  }

  public void setSignalAspect(SignalAspect signalAspect) {
    if (this.signalAspect != signalAspect) {
      this.signalAspect = signalAspect;
      this.broadcastSignalAspect(signalAspect);
      this.syncToClient();
    }
  }

  private void broadcastSignalAspect(SignalAspect signalAspect) {
    this.stream().forEach(peer -> peer.getSignalReceiver().receiveSignalAspect(this, signalAspect));
    if (this.signalAspectListener != null) {
      this.signalAspectListener.accept(signalAspect);
    }
  }

  public void spawnTuningAuraParticles() {
    if (SignalTools.effectManager != null && SignalTools.effectManager.isTuningAuraActive()) {
      this.stream()
          .forEach(peer -> SignalTools.effectManager.spawnTuningAuraParticles(this.getBlockEntity(),
              peer.asBlockEntity()));
    }
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeEnum(this.signalAspect);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.signalAspect = data.readEnum(SignalAspect.class);
  }
}
