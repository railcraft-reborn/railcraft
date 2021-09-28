/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.message.MessageFormatMessage;
import mods.railcraft.api.carts.CartToolsAPI;
import mods.railcraft.api.signals.TrackLocator.Status;
import mods.railcraft.api.tracks.TrackScanner;
import mods.railcraft.world.signal.NetworkType;
import net.minecraft.block.Block;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class BlockSignalNetwork extends AbstractNetwork<BlockSignal> {

  public static final int VALIDATION_CHECK_INTERVAL = 16384;
  private static final Level DEBUG_LEVEL = Level.INFO;
  // private static final Map<UUID, Deque<WorldCoordinate>> savedData = new HashMap<UUID,
  // Deque<WorldCoordinate>>();
  private final Map<BlockPos, BlockPos> trackCache = new HashMap<>();
  private final Map<BlockPos, TrackScanner.ScanResult> trackScans = new HashMap<>();
  private final Set<BlockPos> waitingForRetest = new HashSet<>();
  private final TrackLocator trackLocator;
  private int update = random.nextInt();
  // private UUID uuid = UUID.randomUUID();
  private boolean changedAspect;

  protected BlockSignalNetwork(TileEntity blockEntity, int numPairs, Runnable sync) {
    super(BlockSignal.class, blockEntity, numPairs, sync);
    this.trackLocator = new TrackLocator(getLevel(), getBlockPos());
  }

  public abstract SignalAspect getSignalAspect();

  public TrackLocator getTrackLocator() {
    return trackLocator;
  }

  public void log(Level level, String msg, Object... args) {
    LogManager.getLogger("railcraft").log(level, new MessageFormatMessage(msg, args));
  }

  private void printDebug(String msg, Object... args) {
    if (SignalTools.printSignalDebug)
      log(DEBUG_LEVEL, msg, args);
  }

  private void printDebugPair(String msg, @Nullable TileEntity ot) {
    if (SignalTools.printSignalDebug)
      if (ot == null)
        log(DEBUG_LEVEL, msg + " source:[{0}] target:[null]", getBlockPos());
      else
        log(DEBUG_LEVEL, msg + " source:[{0}] target:[{1}] target class:{2}",
            getBlockPos(),
            ot.getBlockPos(), ot.getClass());

  }

  private void printDebugPair(String msg, @Nullable BlockPos coord) {
    if (SignalTools.printSignalDebug)
      if (coord == null)
        log(DEBUG_LEVEL, msg + " source:[{0}] target:[null]", getBlockPos());
      else
        log(DEBUG_LEVEL, msg + " source:[{0}] target:[{1}]", getBlockPos(), coord);
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT tag = super.serializeNBT();
    // MiscTools.writeUUID(data, "uuid", uuid);
    ListNBT trackCacheTag = new ListNBT();
    for (Map.Entry<BlockPos, BlockPos> cache : this.trackCache.entrySet()) {
      CompoundNBT entryTag = new CompoundNBT();
      if (cache.getKey() != null && cache.getValue() != null) {
        entryTag.put("key", NBTUtil.writeBlockPos(cache.getKey()));
        entryTag.put("key", NBTUtil.writeBlockPos(cache.getValue()));
        trackCacheTag.add(entryTag);
      }
    }
    tag.put("trackCache", trackCacheTag);
    // if (RailcraftConfig.printSignalDebug()) {
    // Deque<WorldCoordinate> test = new LinkedList<WorldCoordinate>();
    // ListNBT list = data.getList("pairings", 10);
    // for (byte entry = 0; entry < list.size(); entry++) {
    // CompoundNBT tag = list.getCompound(entry);
    // int[] c = tag.getIntArray("coords");
    // test.add(new WorldCoordinate(c[0], c[1], c[2], c[3]));
    // }
    // boolean isConsistent = test.containsAll(getPairs());
    // printDebug("Signal Block saved NBT. [{0}, {1}, {2}] [verified: {3}] [changedAspect: {4}]
    // [data: {5}]", tile.xCoord, tile.yCoord, tile.zCoord, isConsistent, changedAspect, test);
    printDebug("Signal Block saved NBT. [{0}] [changedAspect: {1}] [data: {1}]",
        getBlockPos(),
        changedAspect, getPeers());
    // savedData.put(uuid, new LinkedList<WorldCoordinate>(pairings));
    // }
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundNBT data) {
    super.deserializeNBT(data);
    // uuid = MiscTools.readUUID(data, "uuid");
    ListNBT trackCacheTag = data.getList("trackCache", Constants.NBT.TAG_COMPOUND);
    for (INBT entryTag : trackCacheTag) {
      CompoundNBT entryCompoundTag = (CompoundNBT) entryTag;
      BlockPos key = NBTUtil.readBlockPos(entryCompoundTag.getCompound("key"));
      BlockPos value = NBTUtil.readBlockPos(entryCompoundTag.getCompound("value"));
      this.trackCache.put(key, value);
    }

    // if (RailcraftConfig.printSignalDebug()) {
    // String isConsistent = "unknown";
    // Deque<WorldCoordinate> lastSave = savedData.get(uuid);
    // if (lastSave != null) {
    // if (pairings.containsAll(lastSave))
    // isConsistent = "true";
    // else
    // isConsistent = "false";
    // }

    printDebug("Signal Block loaded NBT. [{0}] [data: {1}]", getBlockPos(), getPeers());
    // }
  }

  @Override
  public void removePeer(BlockPos other) {
    printDebugPair("Signal Block pair cleared. ", other);
    if (SignalTools.printSignalDebug) {
      // logTrace(DEBUG_LEVEL, 10, "Signal Block code Path");

      Block block = getLevel().getBlockState(other).getBlock();
      log(DEBUG_LEVEL, "Signal Block target block [{0}, {1}, {2}] = {3}, {4}", other,
          block.getClass(), block.getRegistryName().toString());
      TileEntity t = getLevel().getBlockEntity(other);
      if (t != null)
        log(DEBUG_LEVEL, "Signal Block target tile [{0}] = {1}", t.getBlockPos(), t.getClass());
      else
        log(DEBUG_LEVEL, "Signal Block target tile [{0}] = null", other);
    }
    super.removePeer(other);
  }

  private void clearSignalBlockPairing(@Nullable BlockPos other, String reason, Object... args) {
    this.printDebug(reason, args);
    if (other == null) {
      this.peers.clear();
      if (!this.getLevel().isClientSide())
        this.syncToClient();
    } else {
      this.removePeer(other);
    }
  }

  @Override
  protected void addPairing(BlockPos other) {
    this.peers.remove(other);
    this.peers.add(other);
    while (this.peers.size() > this.getMaxPeers()) {
      BlockPos pair = this.peers.remove();
      printDebugPair("Signal Block dropped because too many pairs.", pair);
    }
    this.peersChanged();
  }

  @Override
  protected void peersChanged() {
    SignalTools.packetBuilder.sendPeerUpdate(NetworkType.BLOCK_SIGNAL, this.getBlockPos(),
        this.getPeers(), this.getBlockEntity().getLevel().dimension());
  }

  @Override
  protected void requestPeers() {
    SignalTools.packetBuilder.sendPeerUpdateRequest(NetworkType.BLOCK_SIGNAL, this.getBlockPos());
  }

  @Override
  public boolean isValidPeer(BlockPos peerPos, BlockSignal peer) {
    BlockSignalNetwork signalBlock = peer.getSignalNetwork();
    return signalBlock.isPeer(this.getBlockPos());
  }

  @Override
  public boolean addPeer(BlockSignal peer) {
    BlockSignal otherTile = (BlockSignal) peer;
    BlockSignalNetwork otherSignal = otherTile.getSignalNetwork();
    if (otherSignal == this) {
      printDebugPair("Signal Block creation was aborted, cannot pair with self.",
          otherSignal.getBlockEntity());
      return false;
    }
    printDebugPair("Signal Block creation being attempted.", otherSignal.getBlockEntity());
    Status myTrackStatus = trackLocator.getTrackStatus();
    Status otherTrackStatus = otherSignal.getTrackLocator().getTrackStatus();
    if (myTrackStatus == Status.INVALID || otherTrackStatus == Status.INVALID) {
      printDebugPair("Signal Block creation failed, could not find Track.",
          otherSignal.getBlockEntity());
      return false;
    }
    BlockPos myTrack = trackLocator.getTrackPos();
    BlockPos otherTrack = otherSignal.getTrackLocator().getTrackPos();
    assert myTrack != null;
    assert otherTrack != null;
    TrackScanner.ScanResult scan =
        TrackScanner.scanStraightTrackSection(getLevel(), myTrack, otherTrack);
    if (!scan.areConnected) {
      printDebugPair("Signal Block creation failed, could not find Path.",
          otherSignal.getBlockEntity());
      return false;
    }
    addPairing(otherSignal.getBlockPos());
    otherSignal.addPairing(getBlockPos());
    endLinking();
    otherSignal.endLinking();
    trackScans.put(otherTrack, scan);
    printDebugPair("Signal Block created successfully.", otherSignal.getBlockEntity());
    return true;
  }

  protected abstract void updateSignalAspect();

  protected abstract SignalAspect getSignalAspectForPeer(BlockPos peerPos);

  public SignalAspect determineAspect(BlockPos peerPos) {
    if (isWaitingForRetest() || isLinking())
      return SignalAspect.BLINK_YELLOW;
    if (!hasPeers())
      return SignalAspect.BLINK_RED;
    SignalAspect peerAspect = SignalAspect.GREEN;
    BlockSignal peer = this.getPeerAt(peerPos);
    if (peer != null)
      peerAspect = peer.getSignalNetwork().getSignalAspectForPeer(this.getBlockPos());
    SignalAspect myAspect = this.determineMyAspect(peerPos);
    return SignalAspect.mostRestrictive(myAspect, peerAspect);
  }

  private SignalAspect determineMyAspect(BlockPos peerPos) {
    BlockPos myTrack = trackLocator.getTrackPos();
    if (myTrack == null)
      return SignalAspect.RED;
    BlockPos otherTrack = getOtherTrackLocation(peerPos);
    if (otherTrack == null)
      return SignalAspect.YELLOW;

    TrackScanner.ScanResult scan = getOrCreateTrackScan(otherTrack);
    if (scan == null)
      return SignalAspect.RED;

    int y1 = scan.minY;
    int y2 = scan.maxY + 1;

    int x1 = Math.min(myTrack.getX(), otherTrack.getX());
    int z1 = Math.min(myTrack.getZ(), otherTrack.getZ());
    int x2 = Math.max(myTrack.getX(), otherTrack.getX()) + 1;
    int z2 = Math.max(myTrack.getZ(), otherTrack.getZ()) + 1;

    boolean zAxis =
        Math.abs(myTrack.getX() - otherTrack.getX()) < Math.abs(myTrack.getZ() - otherTrack.getZ());
    int xOffset = otherTrack.getX() > myTrack.getX() ? -3 : 3;
    int zOffset = otherTrack.getZ() > myTrack.getZ() ? -3 : 3;

    List<AbstractMinecartEntity> carts = CartToolsAPI.getMinecartsIn(getLevel(),
        new BlockPos(x1, y1, z1), new BlockPos(x2, y2, z2));
    // System.out.printf("%d, %d, %d, %d, %d, %d\n", i1, j1, k1, i2, j2, k2);
    // System.out.println("carts = " + carts.size());
    SignalAspect newAspect = SignalAspect.GREEN;
    for (AbstractMinecartEntity cart : carts) {
      int cartX = MathHelper.floor(cart.getX());
      int cartZ = MathHelper.floor(cart.getZ());
      double motionX = cart.getDeltaMovement().x();
      double motionZ = cart.getDeltaMovement().z();
      if (Math.abs(motionX) < 0.08 && Math.abs(motionZ) < 0.08)
        return SignalAspect.RED;
      else if (zAxis)
        if (cartZ > myTrack.getZ() + zOffset && motionZ < 0)
          return SignalAspect.RED;
        else if (cartZ < myTrack.getZ() + zOffset && motionZ > 0)
          return SignalAspect.RED;
        else
          newAspect = SignalAspect.YELLOW;
      else if (cartX > myTrack.getX() + xOffset && motionX < 0)
        return SignalAspect.RED;
      else if (cartX < myTrack.getX() + xOffset && motionX > 0)
        return SignalAspect.RED;
      else
        newAspect = SignalAspect.YELLOW;
    }
    return newAspect;
  }

  @Nullable
  private TrackScanner.ScanResult getOrCreateTrackScan(BlockPos otherTrack) {
    TrackScanner.ScanResult result = this.trackScans.get(otherTrack);
    if (result == null) {
      BlockPos trackPos = this.trackLocator.getTrackPos();
      if (trackPos != null) {
        result = TrackScanner.scanStraightTrackSection(getLevel(), trackPos, otherTrack);
        this.trackScans.put(otherTrack, result);
      }
    }
    return result;
  }

  @Nullable
  private BlockPos getOtherTrackLocation(BlockPos peerPos) {
    BlockSignal other = this.getPeerAt(peerPos);
    if (other != null) {
      BlockPos trackPos = other.getTrackLocator().getTrackPos();
      if (trackPos != null)
        this.trackCache.put(peerPos, trackPos);
      return trackPos;
    }
    return this.trackCache.get(peerPos);
  }

  private TrackValidationStatus isSignalBlockValid(BlockPos other) {
    // if (other == null)
    // return new TrackValidationStatus(true, "UNVERIFIABLE_COORD_NULL");
    BlockSignal otherSignalBlock = this.getPeerAt(other);
    if (otherSignalBlock == null)
      return new TrackValidationStatus(true, "UNVERIFIABLE_OTHER_SIGNAL_NULL");
    Status trackStatus = trackLocator.getTrackStatus();
    if (trackStatus == Status.INVALID)
      return new TrackValidationStatus(false, "INVALID_MY_TRACK_NULL");
    Status otherTrackStatus = otherSignalBlock.getTrackLocator().getTrackStatus();
    if (otherTrackStatus == Status.INVALID)
      return new TrackValidationStatus(false, "INVALID_OTHER_TRACK_INVALID");
    BlockPos otherTrack = trackCache.get(other);
    if (otherTrackStatus == Status.UNKNOWN) {
      if (otherTrack == null)
        return new TrackValidationStatus(true, "UNVERIFIABLE_OTHER_TRACK_UNKNOWN");
    } else {
      otherTrack = otherSignalBlock.getTrackLocator().getTrackPos();
      if (otherTrack != null)
        trackCache.put(other, otherTrack);
    }
    if (otherTrack == null)
      return new TrackValidationStatus(true, "UNVERIFIABLE_OTHER_TRACK_NULL");
    BlockPos myTrack = trackLocator.getTrackPos();
    if (myTrack == null)
      return new TrackValidationStatus(true, "INVALID_MY_TRACK_NULL");
    TrackScanner.ScanResult scan =
        TrackScanner.scanStraightTrackSection(getLevel(), myTrack, otherTrack);
    trackScans.put(otherTrack, scan);
    if (scan.verdict == TrackScanner.ScanResult.Verdict.VALID)
      return new TrackValidationStatus(true, "VALID");
    if (scan.verdict == TrackScanner.ScanResult.Verdict.UNKNOWN)
      return new TrackValidationStatus(true, "UNVERIFIABLE_UNLOADED_CHUNK");
    return new TrackValidationStatus(false, "INVALID_SCAN_FAIL: " + scan.verdict.name());
  }

  @Override
  public void tickServer() {
    super.tickServer();
    update++;
    try {
      if (!isLoaded())
        return;
    } catch (Throwable ex) {
      // Game.logErrorAPI("Railcraft", ex, AbstractPair.class);
    }
    if (update % SignalTools.signalUpdateInterval == 0) {
      SignalAspect prev = getSignalAspect();
      if (prev != SignalAspect.BLINK_RED)
        changedAspect = true;
      updateSignalAspect();
      if (getSignalAspect() == SignalAspect.BLINK_RED && prev != SignalAspect.BLINK_RED)
        printDebug("Signal Block changed aspect to BLINK_RED: source:[{0}] pairs: {1}",
            getBlockPos(), getPeers());
    }
    if (update % VALIDATION_CHECK_INTERVAL == 0) {
      Status trackStatus = trackLocator.getTrackStatus();
      switch (trackStatus) {
        case INVALID:
          clearSignalBlockPairing(null,
              "Signal Block dropped because no track was found near Signal. [{0}]",
              getBlockPos());
          break;
        case VALID:
          for (BlockPos otherCoord : waitingForRetest) {
            TrackValidationStatus status = isSignalBlockValid(otherCoord);
            if (!status.valid)
              clearSignalBlockPairing(otherCoord,
                  "Signal Block dropped because track between Signals was invalid. source:[{0}] target:[{1}, {2}, {3}] reason:{4}",
                  getBlockPos(), otherCoord, status.message);
          }
          waitingForRetest.clear();
          for (BlockPos otherCoord : this.getPeers()) {
            if (!isSignalBlockValid(otherCoord).valid)
              waitingForRetest.add(otherCoord);
          }
          break;
        default:
          break;
      }
    }
  }

  public boolean isWaitingForRetest() {
    return !this.waitingForRetest.isEmpty();
  }

  private static class TrackValidationStatus {

    private final boolean valid;
    private final String message;

    private TrackValidationStatus(boolean valid, String message) {
      this.valid = valid;
      this.message = message;
    }
  }
}
