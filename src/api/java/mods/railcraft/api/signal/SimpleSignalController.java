/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal;

import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.signal.entity.SignalReceiverEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SimpleSignalController extends BlockEntitySignalNetwork<SignalReceiverEntity>
    implements SignalController {

  private final boolean blinkRedWithoutPeers;
  @Nullable
  private final Consumer<SignalAspect> signalAspectListener;
  private SignalAspect signalAspect = SignalAspect.GREEN;

  public SimpleSignalController(int maxPeers, Runnable syncListener, BlockEntity blockEntity,
      boolean blinkRedWithoutPeers) {
    this(maxPeers, syncListener, blockEntity, blinkRedWithoutPeers, null);
  }

  public SimpleSignalController(int maxPeers, Runnable syncListener, BlockEntity blockEntity,
      boolean blinkRedWithoutPeers,
      @Nullable Consumer<SignalAspect> signalAspectListener) {
    super(SignalReceiverEntity.class, maxPeers, syncListener, blockEntity);
    this.blinkRedWithoutPeers = blinkRedWithoutPeers;
    this.signalAspectListener = signalAspectListener;
  }

  @Override
  public boolean addPeer(SignalReceiverEntity peer) {
    boolean hadPeers = this.hasPeers();
    if (super.addPeer(peer)) {
      peer.getSignalReceiver().linked(this);
      if (!hadPeers && this.blinkRedWithoutPeers && this.signalAspectListener != null) {
        this.signalAspectListener.accept(this.aspect());
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean removePeer(BlockPos peerPos) {
    if (super.removePeer(peerPos)) {
      var signalReceiverProvider = this.getBlockEntity(peerPos);
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
    this.broadcastSignalAspect(this.aspect());
  }

  @Override
  public boolean refreshPeer(SignalReceiverEntity peer) {
    peer.getSignalReceiver().receiveSignalAspect(this, this.aspect());
    return true;
  }

  @Override
  public SignalAspect aspect() {
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
    var handler = SignalUtil.tuningAuraHandler();
    if (handler.isTuningAuraActive()) {
      this.stream().forEach(peer -> handler.spawnTuningAura(
          this.getBlockEntity(), peer.asBlockEntity()));
    }
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.signalAspect);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.signalAspect = data.readEnum(SignalAspect.class);
  }
}
