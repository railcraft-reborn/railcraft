/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import mods.railcraft.api.carts.CartUtil;
import mods.railcraft.api.track.TrackScanner;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class SimpleBlockSignalNetwork extends BlockEntitySignalNetwork<BlockSignal>
    implements BlockSignalNetwork {

  private static final Logger logger = LogUtils.getLogger();

  public static final int SIGNAL_VALIDATION_INTERVAL = 4 * 60 * 20;
  private final Set<BlockPos> signalsToRevalidate = new HashSet<>();

  private final Map<BlockPos, TrackScanner.Result> trackScans = new HashMap<>();
  private final TrackLocator trackLocator;

  private int aspectUpdateTimer = 0;
  private int signalValidationTimer = 0;

  private SignalAspect signalAspect = SignalAspect.BLINK_RED;
  @Nullable
  private Consumer<SignalAspect> signalAspectListener;

  public SimpleBlockSignalNetwork(int maxPeers, Runnable syncListener,
      @Nullable Consumer<SignalAspect> signalAspectListener,
      BlockEntity blockEntity) {
    super(BlockSignal.class, maxPeers, syncListener, blockEntity);
    this.trackLocator = new TrackLocator(blockEntity);
    this.signalAspectListener = signalAspectListener;
  }

  @Override
  public SignalAspect getSignalAspect() {
    return this.signalAspect;
  }

  @Override
  public TrackLocator getTrackLocator() {
    return this.trackLocator;
  }

  @Override
  public boolean addPeer(BlockSignal peer) {
    BlockSignalNetwork blockSignal = peer.getSignalNetwork();
    if (blockSignal == this) {
      return false;
    }

    TrackLocator.Status trackStatus = this.trackLocator.getTrackStatus();
    TrackLocator.Status otherTrackStatus = blockSignal.getTrackLocator().getTrackStatus();
    if (trackStatus == TrackLocator.Status.INVALID
        || otherTrackStatus == TrackLocator.Status.INVALID) {
      return false;
    }

    BlockPos trackPos = this.trackLocator.getTrackPos();
    BlockPos peerTrackPos = blockSignal.getTrackLocator().getTrackPos();

    assert trackPos != null;
    assert peerTrackPos != null;

    TrackScanner.Result result =
        TrackScanner.scanStraightTrackSection(this.getLevel(), trackPos, peerTrackPos);
    if (result.getStatus() != TrackScanner.Status.VALID) {
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
      BlockSignal blockSignalProvider = this.getBlockEntity(peerPos);
      if (blockSignalProvider != null) {
        blockSignalProvider.getSignalNetwork().removePeer(this.getBlockPos());
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
  protected boolean refreshPeer(BlockSignal peer) {
    return peer.getSignalNetwork().isPeer(this.getBlockPos());
  }

  @Override
  public SignalAspect getSignalAspectExcluding(BlockPos exludingPos) {
    return this.stream()
        .filter(peer -> !peer.asBlockEntity().getBlockPos().equals(exludingPos))
        .map(this::calculateSignalAspect)
        .reduce(SignalAspect.GREEN, SignalAspect::mostRestrictive);
  }

  private SignalAspect calculateSignalAspect(BlockSignal peer) {
    BlockPos trackPos = this.trackLocator.getTrackPos();
    if (trackPos == null)
      return SignalAspect.RED;
    BlockPos otherTrack = peer.getSignalNetwork().getTrackLocator().getTrackPos();
    if (otherTrack == null)
      return SignalAspect.YELLOW;

    TrackScanner.Result scan = this.getOrCreateTrackScan(otherTrack);
    if (scan == null)
      return SignalAspect.RED;

    int y1 = scan.getMinY();
    int y2 = scan.getMaxY() + 1;

    int x1 = Math.min(trackPos.getX(), otherTrack.getX());
    int z1 = Math.min(trackPos.getZ(), otherTrack.getZ());
    int x2 = Math.max(trackPos.getX(), otherTrack.getX()) + 1;
    int z2 = Math.max(trackPos.getZ(), otherTrack.getZ()) + 1;

    boolean zAxis =
        Math.abs(trackPos.getX() - otherTrack.getX()) < Math
            .abs(trackPos.getZ() - otherTrack.getZ());
    int xOffset = otherTrack.getX() > trackPos.getX() ? -3 : 3;
    int zOffset = otherTrack.getZ() > trackPos.getZ() ? -3 : 3;

    List<AbstractMinecart> carts = CartUtil.getMinecartsIn(getLevel(),
        new BlockPos(x1, y1, z1), new BlockPos(x2, y2, z2));
    // System.out.printf("%d, %d, %d, %d, %d, %d\n", i1, j1, k1, i2, j2, k2);
    // System.out.println("carts = " + carts.size());
    SignalAspect newAspect = SignalAspect.GREEN;
    for (AbstractMinecart cart : carts) {
      int cartX = Mth.floor(cart.getX());
      int cartZ = Mth.floor(cart.getZ());
      double motionX = cart.getDeltaMovement().x();
      double motionZ = cart.getDeltaMovement().z();
      if (Math.abs(motionX) < 0.08 && Math.abs(motionZ) < 0.08)
        return SignalAspect.RED;
      else if (zAxis)
        if (cartZ > trackPos.getZ() + zOffset && motionZ < 0)
          return SignalAspect.RED;
        else if (cartZ < trackPos.getZ() + zOffset && motionZ > 0)
          return SignalAspect.RED;
        else
          newAspect = SignalAspect.YELLOW;
      else if (cartX > trackPos.getX() + xOffset && motionX < 0)
        return SignalAspect.RED;
      else if (cartX < trackPos.getX() + xOffset && motionX > 0)
        return SignalAspect.RED;
      else
        newAspect = SignalAspect.YELLOW;
    }

    return SignalAspect.mostRestrictive(newAspect,
        peer.getSignalNetwork().getSignalAspectExcluding(this.getBlockPos()));
  }

  @Nullable
  private TrackScanner.Result getOrCreateTrackScan(BlockPos otherTrack) {
    TrackScanner.Result result = this.trackScans.get(otherTrack);
    if (result == null) {
      BlockPos trackPos = this.trackLocator.getTrackPos();
      if (trackPos != null) {
        result = TrackScanner.scanStraightTrackSection(getLevel(), trackPos, otherTrack);
        this.trackScans.put(otherTrack, result);
      }
    }
    return result;
  }

  private TrackValidationResult validateSignal(BlockSignalNetwork blockSignal) {
    TrackLocator.Status trackStatus = this.trackLocator.getTrackStatus();
    if (trackStatus == TrackLocator.Status.INVALID)
      return new TrackValidationResult(false, "INVALID_MY_TRACK_NULL");
    TrackLocator.Status otherTrackStatus = blockSignal.getTrackLocator().getTrackStatus();
    if (otherTrackStatus == TrackLocator.Status.INVALID)
      return new TrackValidationResult(false, "INVALID_OTHER_TRACK_INVALID");
    BlockPos otherTrackPos = blockSignal.getTrackLocator().getTrackPos();
    if (otherTrackPos == null)
      return new TrackValidationResult(true, "UNVERIFIABLE_OTHER_TRACK_NULL");
    BlockPos trackPos = this.trackLocator.getTrackPos();
    if (trackPos == null)
      return new TrackValidationResult(true, "INVALID_MY_TRACK_NULL");
    TrackScanner.Result scanResult =
        TrackScanner.scanStraightTrackSection(this.getLevel(), trackPos, otherTrackPos);
    this.trackScans.put(otherTrackPos, scanResult);
    if (scanResult.getStatus() == TrackScanner.Status.VALID)
      return new TrackValidationResult(true, "VALID");
    if (scanResult.getStatus() == TrackScanner.Status.UNKNOWN)
      return new TrackValidationResult(true, "UNVERIFIABLE_UNLOADED_CHUNK");
    return new TrackValidationResult(false,
        "INVALID_SCAN_FAIL: " + scanResult.getStatus().toString());
  }

  public void serverTick() {
    if (this.aspectUpdateTimer++ >= SignalTools.aspectUpdateInterval) {
      this.aspectUpdateTimer = 0;
      SignalAspect lastAspect = this.signalAspect;
      if (this.hasPeers()) {
        this.signalAspect = SignalAspect.GREEN;
        for (BlockPos peerPos : this.peers) {
          this.getPeer(peerPos).ifPresent(peer -> this.signalAspect =
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
      switch (this.trackLocator.getTrackStatus()) {
        case INVALID:
          this.peers.clear();
          logger.debug("Block signal dropped because no track was found near signal @ [{}]",
              this.getBlockPos());
          break;
        case VALID:
          this.signalsToRevalidate.retainAll(this.getPeers());
          for (BlockPos peerPos : this.signalsToRevalidate) {
            BlockSignal peer = this.getPeer(peerPos).orElse(null);
            if (peer == null) {
              // Silently remove
              this.peers.remove();
            }
            TrackValidationResult result = this.validateSignal(peer.getSignalNetwork());
            if (!result.valid) {
              this.removePeer(peerPos);
              logger.debug(
                  "Block signal dropped because track between signals was invalid. source:[{}] target:[{}] reason:{}",
                  this.getBlockPos(), peerPos, result.message);
            }
          }
          this.signalsToRevalidate.clear();

          Iterator<BlockPos> iterator = this.peers.iterator();
          while (iterator.hasNext()) {
            BlockPos peerPos = iterator.next();
            BlockSignal peer = this.getPeer(peerPos).orElse(null);
            if (peer == null) {
              // Silently remove
              iterator.remove();
              continue;
            }

            TrackValidationResult result = this.validateSignal(peer.getSignalNetwork());
            if (!result.valid) {
              this.signalsToRevalidate.add(peerPos);
            }
          }
          break;
        default:
          break;
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

  private static class TrackValidationResult {

    private final boolean valid;
    private final String message;

    private TrackValidationResult(boolean valid, String message) {
      this.valid = valid;
      this.message = message;
    }
  }
}
