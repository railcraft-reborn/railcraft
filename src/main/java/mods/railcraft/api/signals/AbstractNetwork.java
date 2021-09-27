/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signals;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import com.google.common.collect.MapMaker;
import mods.railcraft.api.core.CollectionToolsAPI;
import mods.railcraft.api.core.INetworkedObject;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class AbstractNetwork implements IMutableNetwork, INetworkedObject {

  protected static final Random rand = new Random();
  private static final boolean IS_BUKKIT;

  static {
    boolean foundBukkit;
    try {
      foundBukkit = Class.forName("org.spigotmc.SpigotConfig") != null;
    } catch (ClassNotFoundException er) {
      foundBukkit = false;
    }
    IS_BUKKIT = foundBukkit;
  }

  private static final int SAFE_TIME = 32;
  private static final int PAIR_CHECK_INTERVAL = 16;
  public final TileEntity blockEntity;
  public final String locTag;
  public final int maxPeers;
  protected final Deque<BlockPos> peers = CollectionToolsAPI.blockPosDeque(LinkedList::new);
  protected final Set<BlockPos> invalidPeers = CollectionToolsAPI.blockPosSet(HashSet::new);
  private final Collection<BlockPos> safePeers = Collections.unmodifiableCollection(peers);
  private final Set<BlockPos> peersToTest = CollectionToolsAPI.blockPosSet(HashSet::new);
  private final Set<BlockPos> peersToTestNext = CollectionToolsAPI.blockPosSet(HashSet::new);
  private final Map<BlockPos, TileEntity> tileCache =
      CollectionToolsAPI.blockPosMap(new MapMaker().weakValues().makeMap());
  private BlockPos blockPos;
  private boolean linking;
  private int update = rand.nextInt();
  private int ticksExisted;
  private boolean needsInit = true;
  private @Nullable String name;

  protected AbstractNetwork(String locTag, TileEntity blockEntity, int maxPairings) {
    this.blockEntity = blockEntity;
    this.maxPeers = maxPairings;
    this.locTag = locTag;
  }

  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    if (name == null || this.name == null || !Objects.equals(this.name, name)) {
      this.name = name;
      informPairsOfNameChange();
    }
  }

  public void informPairsOfNameChange() {}

  public void onPairNameChange(BlockPos coords, @Nullable String name) {}

  protected boolean isLoaded() {
    return ticksExisted >= SAFE_TIME;
  }

  protected void addPairing(BlockPos other) {
    peers.remove(other);
    peers.add(other);
    while (peers.size() > getMaxPairings()) {
      peers.remove();
    }
    this.peersChanged();
  }

  protected abstract void peersChanged();

  public void clearPairing(BlockPos other) {
    invalidPeers.add(other);
  }

  @Override
  public void endLinking() {
    linking = false;
  }

  public void tickClient() {
    if (needsInit) {
      needsInit = false;
    }
  }

  protected abstract void requestPeers();

  public void tickServer() {
    update++;
    if (!isLoaded())
      ticksExisted++;
    else if (update % PAIR_CHECK_INTERVAL == 0)
      validatePairings();
  }

  protected void validatePairings() {
    if (!peersToTestNext.isEmpty()) {
      peersToTestNext.retainAll(peers);
      for (BlockPos coord : peersToTestNext) {

        World world = blockEntity.getLevel();
        if (!world.isLoaded(coord))
          continue;

        BlockState blockState = world.getBlockState(coord);
        if (!blockState.getBlock().hasTileEntity(blockState)) {
          clearPairing(coord);
          continue;
        }

        TileEntity target = world.getBlockEntity(coord);
        if (target != null && !isValidPair(coord, target))
          clearPairing(coord);
      }
      peersToTestNext.clear();
    }
    cleanPeers();
    for (BlockPos coord : peers) {
      getPairAt(coord);
    }
    peersToTestNext.addAll(peersToTest);
    peersToTest.clear();
  }

  public void cleanPeers() {
    if (invalidPeers.isEmpty())
      return;
    boolean changed = peers.removeAll(invalidPeers);
    invalidPeers.clear();
    if (changed)
      this.peersChanged();
  }

  protected @Nullable TileEntity getPairAt(BlockPos coord) {
    if (!peers.contains(coord))
      return null;

    boolean useCache;
    try {
      useCache = !IS_BUKKIT && SignalTools.isInSameChunk(getBlockPos(), coord);
    } catch (Throwable er) {
      useCache = false;
    }

    if (useCache) {
      TileEntity cacheTarget = tileCache.get(coord);
      if (cacheTarget != null) {
        if (cacheTarget.isRemoved() || !Objects.equals(cacheTarget.getBlockPos(), coord))
          tileCache.remove(coord);
        else if (isValidPair(coord, cacheTarget))
          return cacheTarget;
      }
    }

    if (coord.getY() <= 0) {
      clearPairing(coord);
      return null;
    }

    World world = blockEntity.getLevel();
    if (!world.isLoaded(coord))
      return null;

    BlockState blockState = world.getBlockState(coord);
    if (!blockState.getBlock().hasTileEntity(blockState)) {
      peersToTest.add(coord);
      return null;
    }

    TileEntity target = world.getBlockEntity(coord);
    if (target != null && !isValidPair(coord, target)) {
      peersToTest.add(coord);
      return null;
    }

    if (useCache && target != null) {
      tileCache.put(coord, target);
    }

    return target;
  }

  public boolean isValidPair(BlockPos otherCoord, TileEntity otherTile) {
    return false;
  }

  public BlockPos getBlockPos() {
    if (blockPos == null)
      blockPos = blockEntity.getBlockPos().immutable();
    return blockPos;
  }

  public String getLocalizationTag() {
    return locTag;
  }

  public int getMaxPairings() {
    return maxPeers;
  }

  public int getNumPairs() {
    return peers.size();
  }

  public boolean isPaired() {
    return !peers.isEmpty();
  }

  @Override
  public Collection<BlockPos> getPeers() {
    return safePeers;
  }

  public TileEntity getTile() {
    return blockEntity;
  }

  @Override
  public void startLinking() {
    linking = true;
  }

  public boolean isLinking() {
    return linking;
  }

  public boolean isPeer(BlockPos other) {
    return peers.contains(other);
  }

  protected abstract String getTagName();

  public final void writeToNBT(CompoundNBT data) {
    CompoundNBT tag = new CompoundNBT();
    saveNBT(tag);
    data.put(getTagName(), tag);
  }

  protected void saveNBT(CompoundNBT data) {
    ListNBT list = new ListNBT();
    for (BlockPos c : peers) {
      CompoundNBT tag = new CompoundNBT();
      SignalTools.writeToNBT(tag, "coords", c);
      list.add(tag);
    }
    data.put("pairings", list);
    if (name != null) {
      data.putString("name", name);
    }
  }

  public final void readFromNBT(CompoundNBT data) {
    CompoundNBT tag = data.getCompound(getTagName());
    loadNBT(tag);
  }

  protected void loadNBT(CompoundNBT data) {
    ListNBT list = data.getList("pairings", 10);
    for (byte entry = 0; entry < list.size(); entry++) {
      CompoundNBT tag = list.getCompound(entry);
      BlockPos p = SignalTools.readFromNBT(tag, "coords");
      if (p != null)
        peers.add(p);
    }
    this.name = data.getString("name");
    if (name.isEmpty()) {
      this.name = null;
    }
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    data.writeUtf(name != null ? name : "");
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    this.name = data.readUtf(0x7FFF);
    if (name.isEmpty()) {
      this.name = null;
    }
  }

  @Override
  public void sendUpdateToClient() {
    ((INetworkedObject) getTile()).sendUpdateToClient();
  }

  @Override
  public @Nullable World theWorld() {
    return getTile().getLevel();
  }

  @Override
  public void add(BlockPos pos) {
    peers.add(pos);
  }

  @Override
  public void remove(BlockPos pos) {
    peers.remove(pos);
  }

  @Override
  public void clear() {
    peers.clear();
    if (!blockEntity.getLevel().isClientSide())
      this.peersChanged();
  }
}
