/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.track.TrackScanUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class SimpleBlockSignalNetwork extends BlockEntitySignalNetwork<BlockSignalEntity>
    implements BlockSignal {

  private static final Logger logger = LogUtils.getLogger();

  public static final int SIGNAL_VALIDATION_INTERVAL = 4 * 60 * 20;
  private final Set<BlockPos> signalsToRevalidate = new HashSet<>();

  private final Map<BlockPos, TrackScanUtil.Result> trackScans = new HashMap<>();
  private final TrackLocator trackLocator;

  private int aspectUpdateTimer = 0;
  private int signalValidationTimer = 0;

  private SignalAspect signalAspect = SignalAspect.BLINK_RED;
  @Nullable
  private Consumer<SignalAspect> signalAspectListener;

  public SimpleBlockSignalNetwork(int maxPeers, Runnable syncListener,
      @Nullable Consumer<SignalAspect> signalAspectListener,
      BlockEntity blockEntity) {
    super(BlockSignalEntity.class, maxPeers, syncListener, blockEntity);
    this.trackLocator = new TrackLocator(blockEntity::getLevel, blockEntity.getBlockPos());
    this.signalAspectListener = signalAspectListener;
  }

  @Override
  public SignalAspect aspect() {
    return this.signalAspect;
  }

  @Override
  public TrackLocator trackLocator() {
    return this.trackLocator;
  }

  @Override
  public boolean addPeer(BlockSignalEntity peer) {
    var blockSignal = peer.signalNetwork();
    if (blockSignal == this) {
      return false;
    }

    var trackStatus = this.trackLocator.trackStatus();
    var otherTrackStatus = blockSignal.trackLocator().trackStatus();
    if (trackStatus.invalid() || otherTrackStatus.invalid()) {
      return false;
    }

    var trackPos = this.trackLocator.trackPos();
    var peerTrackPos = blockSignal.trackLocator().trackPos();

    assert trackPos != null;
    assert peerTrackPos != null;

    var result = TrackScanUtil.scanStraightTrackSection(this.getLevel(), trackPos, peerTrackPos);
    if (!result.status().valid()) {
      return false;
    }

    if (!super.addPeer(peer)) {
      return false;
    }

    this.trackScans.put(peerTrackPos, result);

    return true;
  }

  @Override
  public boolean removePeer(BlockPos peerPos) {
    if (super.removePeer(peerPos)) {
      var blockSignalProvider = this.getBlockEntity(peerPos);
      if (blockSignalProvider != null) {
        blockSignalProvider.signalNetwork().removePeer(this.blockPos());
      }
      return true;
    }
    return false;
  }

  @Override
  public void refresh() {
    super.refresh();
    if (this.signalAspectListener != null) {
      this.signalAspectListener.accept(this.signalAspect);
    }
  }

  @Override
  protected boolean refreshPeer(BlockSignalEntity peer) {
    return peer.signalNetwork().isPeer(this.blockPos());
  }

  @Override
  public SignalAspect aspectExcluding(BlockPos exludingPos) {
    return this.stream()
        .filter(peer -> !peer.asBlockEntity().getBlockPos().equals(exludingPos))
        .map(this::calculateSignalAspect)
        .reduce(SignalAspect.GREEN, SignalAspect::mostRestrictive);
  }

  private SignalAspect calculateSignalAspect(BlockSignalEntity peer) {
    var trackPos = this.trackLocator.trackPos();
    if (trackPos == null) {
      return SignalAspect.RED;
    }

    var otherTrack = peer.signalNetwork().trackLocator().trackPos();
    if (otherTrack == null) {
      return SignalAspect.YELLOW;
    }

    var scan = this.getOrCreateTrackScan(otherTrack);
    if (scan == null) {
      return SignalAspect.RED;
    }

    int y1 = scan.minY();
    int y2 = scan.maxY() + 1;

    int x1 = Math.min(trackPos.getX(), otherTrack.getX());
    int z1 = Math.min(trackPos.getZ(), otherTrack.getZ());
    int x2 = Math.max(trackPos.getX(), otherTrack.getX()) + 1;
    int z2 = Math.max(trackPos.getZ(), otherTrack.getZ()) + 1;

    var zAxis = Math.abs(trackPos.getX() - otherTrack.getX()) < Math
        .abs(trackPos.getZ() - otherTrack.getZ());
    int xOffset = otherTrack.getX() > trackPos.getX() ? -3 : 3;
    int zOffset = otherTrack.getZ() > trackPos.getZ() ? -3 : 3;

    var carts = CartUtil.getMinecartsIn(this.getLevel(),
        new BlockPos(x1, y1, z1), new BlockPos(x2, y2, z2));

    var newAspect = SignalAspect.GREEN;
    for (var cart : carts) {
      int cartX = Mth.floor(cart.getX());
      int cartZ = Mth.floor(cart.getZ());
      var motionX = cart.getDeltaMovement().x();
      var motionZ = cart.getDeltaMovement().z();
      if (Math.abs(motionX) < 0.08 && Math.abs(motionZ) < 0.08) {
        return SignalAspect.RED;
      }

      if (zAxis) {
        if (cartZ > trackPos.getZ() + zOffset && motionZ < 0) {
          return SignalAspect.RED;
        }

        if (cartZ < trackPos.getZ() + zOffset && motionZ > 0) {
          return SignalAspect.RED;
        }

        newAspect = SignalAspect.YELLOW;
      }

      if (cartX > trackPos.getX() + xOffset && motionX < 0) {
        return SignalAspect.RED;
      }

      if (cartX < trackPos.getX() + xOffset && motionX > 0) {
        return SignalAspect.RED;
      }

      newAspect = SignalAspect.YELLOW;
    }

    return SignalAspect.mostRestrictive(newAspect,
        peer.signalNetwork().aspectExcluding(this.blockPos()));
  }

  @Nullable
  private TrackScanUtil.Result getOrCreateTrackScan(BlockPos otherTrack) {
    TrackScanUtil.Result result = this.trackScans.get(otherTrack);
    if (result == null) {
      BlockPos trackPos = this.trackLocator.trackPos();
      if (trackPos != null) {
        result = TrackScanUtil.scanStraightTrackSection(getLevel(), trackPos, otherTrack);
        this.trackScans.put(otherTrack, result);
      }
    }
    return result;
  }

  private TrackValidationResult validateSignal(BlockSignal blockSignal) {
    TrackLocator.Status trackStatus = this.trackLocator.trackStatus();
    if (trackStatus == TrackLocator.Status.INVALID)
      return new TrackValidationResult(false, "INVALID_MY_TRACK_NULL");
    TrackLocator.Status otherTrackStatus = blockSignal.trackLocator().trackStatus();
    if (otherTrackStatus == TrackLocator.Status.INVALID)
      return new TrackValidationResult(false, "INVALID_OTHER_TRACK_INVALID");
    BlockPos otherTrackPos = blockSignal.trackLocator().trackPos();
    if (otherTrackPos == null)
      return new TrackValidationResult(true, "UNVERIFIABLE_OTHER_TRACK_NULL");
    BlockPos trackPos = this.trackLocator.trackPos();
    if (trackPos == null)
      return new TrackValidationResult(true, "INVALID_MY_TRACK_NULL");
    TrackScanUtil.Result scanResult =
        TrackScanUtil.scanStraightTrackSection(this.getLevel(), trackPos, otherTrackPos);
    this.trackScans.put(otherTrackPos, scanResult);

    if (scanResult.status().valid()) {
      return new TrackValidationResult(true, "VALID");
    }

    if (scanResult.status().unknown()) {
      return new TrackValidationResult(true, "UNVERIFIABLE_UNLOADED_CHUNK");
    }

    return new TrackValidationResult(false,
        "INVALID_SCAN_FAIL: " + scanResult.status().toString());
  }

  public void serverTick() {
    if (this.aspectUpdateTimer++ >= SignalUtil.aspectUpdateInterval) {
      this.aspectUpdateTimer = 0;
      SignalAspect lastAspect = this.signalAspect;
      if (this.hasPeers()) {
        this.signalAspect = SignalAspect.GREEN;
        for (var peerPos : this.peers) {
          this.peerAt(peerPos).ifPresent(peer -> this.signalAspect =
              SignalAspect.mostRestrictive(this.signalAspect,
                  this.calculateSignalAspect(peer)));
        }
      } else if (this.isLinking()) {
        this.signalAspect = SignalAspect.BLINK_YELLOW;
      } else {
        this.signalAspect = SignalAspect.BLINK_RED;
      }
      if (lastAspect != this.signalAspect && this.signalAspectListener != null) {
        this.signalAspectListener.accept(this.signalAspect);
      }
    }
    if (this.signalValidationTimer++ >= SIGNAL_VALIDATION_INTERVAL) {
      this.signalValidationTimer = 0;
      switch (this.trackLocator.trackStatus()) {
        case INVALID -> {
          this.peers.clear();
          logger.debug("Block signal dropped because no track was found near signal @ [{}]",
              this.blockPos());
        }
        case VALID -> {
          this.signalsToRevalidate.retainAll(this.peers());
          for (var peerPos : this.signalsToRevalidate) {
            this.peerAt(peerPos).ifPresentOrElse(peer -> {
              var result = this.validateSignal(peer.signalNetwork());
              if (!result.valid) {
                this.removePeer(peerPos);
                logger.debug(
                    "Block signal dropped because track between signals was invalid. source:[{}] target:[{}] reason:{}",
                    this.blockPos(), peerPos, result.message);
              }

            }, this.peers::remove);
          }
          this.signalsToRevalidate.clear();

          var iterator = this.peers.iterator();
          while (iterator.hasNext()) {
            var peerPos = iterator.next();
            this.peerAt(peerPos).ifPresentOrElse(peer -> {
              var result = this.validateSignal(peer.signalNetwork());
              if (!result.valid) {
                this.signalsToRevalidate.add(peerPos);
              }
            }, iterator::remove);
          }
        }
        default -> {
        }
      }
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

  private record TrackValidationResult(boolean valid, String message) {}
}
