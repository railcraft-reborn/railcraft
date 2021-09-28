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
import mods.railcraft.api.core.Syncable;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class AbstractNetwork<T extends BlockEntityLike>
    implements ClientNetwork<T>, Syncable, INBTSerializable<CompoundNBT> {

  protected static final Random random = new Random();
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

  private final Class<T> peerType;
  private final TileEntity blockEntity;
  private final int maxPeers;
  private final Runnable sync;

  protected final Deque<BlockPos> peers = new LinkedList<>();
  private boolean peersChanged;

  private final Collection<BlockPos> safePeers = Collections.unmodifiableCollection(peers);
  private final Set<BlockPos> peersToTest = new HashSet<>();
  private final Set<BlockPos> peersToTestNext = new HashSet<>();

  private final Map<BlockPos, T> peerCache = new MapMaker().weakValues().makeMap();

  private boolean linking;

  private int update = random.nextInt();

  private int ticksExisted;
  private boolean needsInit = true;

  @Nullable
  private ITextComponent name;

  public AbstractNetwork(Class<T> peerType, TileEntity blockEntity, int maxPairings,
      Runnable sync) {
    this.peerType = peerType;
    this.blockEntity = blockEntity;
    this.maxPeers = maxPairings;
    this.sync = sync;
  }

  public TileEntity getBlockEntity() {
    return this.blockEntity;
  }

  public World getLevel() {
    return this.blockEntity.getLevel();
  }

  public BlockPos getBlockPos() {
    return this.blockEntity.getBlockPos();
  }

  public int getMaxPeers() {
    return this.maxPeers;
  }

  @Nullable
  public ITextComponent getName() {
    return this.name;
  }

  public void setName(@Nullable ITextComponent name) {
    if (name == null || this.name == null || !Objects.equals(this.name, name)) {
      this.name = name;
      this.nameChanged();
    }
  }

  protected void nameChanged() {}

  protected boolean isLoaded() {
    return ticksExisted >= SAFE_TIME;
  }

  protected void addPairing(BlockPos other) {
    this.removePeer(other);
    peers.add(other);
    while (peers.size() > getMaxPeers()) {
      peers.remove();
    }
    this.peersChanged();
  }

  protected abstract void peersChanged();

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
    if (!this.peersToTestNext.isEmpty()) {
      this.peersToTestNext.retainAll(this.peers);
      for (BlockPos coord : this.peersToTestNext) {

        if (!this.getLevel().isLoaded(coord))
          continue;

        BlockState blockState = this.getLevel().getBlockState(coord);
        if (!blockState.getBlock().hasTileEntity(blockState)) {
          this.removePeer(coord);
          continue;
        }

        TileEntity target = this.getLevel().getBlockEntity(coord);
        if (!this.peerType.isInstance(target)
            || !this.isValidPeer(coord, this.peerType.cast(target))) {
          this.removePeer(coord);
        }
      }
      this.peersToTestNext.clear();
    }

    if (this.peersChanged) {
      this.peersChanged = false;
      this.peersChanged();
    }

    for (BlockPos coord : this.peers) {
      this.getPeerAt(coord);
    }

    this.peersToTestNext.addAll(this.peersToTest);
    this.peersToTest.clear();
  }

  @Nullable
  protected T getPeerAt(BlockPos blockPos) {
    if (!this.peers.contains(blockPos))
      return null;

    boolean useCache;
    try {
      useCache = !IS_BUKKIT && SignalTools.isInSameChunk(getBlockPos(), blockPos);
    } catch (Throwable er) {
      useCache = false;
    }

    if (useCache) {
      T cacheTarget = this.peerCache.get(blockPos);
      if (cacheTarget != null) {
        if (!Objects.equals(cacheTarget.asBlockEntity().getBlockPos(), blockPos)
            || cacheTarget.asBlockEntity().isRemoved())
          this.peerCache.remove(blockPos);
        else if (this.isValidPeer(blockPos, cacheTarget))
          return cacheTarget;
      }
    }

    if (blockPos.getY() <= 0) {
      this.removePeer(blockPos);
      return null;
    }

    if (!this.getLevel().isLoaded(blockPos))
      return null;

    BlockState blockState = this.getLevel().getBlockState(blockPos);
    if (!blockState.getBlock().hasTileEntity(blockState)) {
      peersToTest.add(blockPos);
      return null;
    }

    TileEntity blockEntity = this.getLevel().getBlockEntity(blockPos);
    T peer = this.peerType.cast(blockEntity);
    if (!this.peerType.isInstance(blockEntity)
        || !this.isValidPeer(blockPos, peer)) {
      this.peersToTest.add(blockPos);
      return null;
    }

    if (useCache && blockEntity != null) {
      this.peerCache.put(blockPos, peer);
    }

    return peer;
  }

  public boolean isValidPeer(BlockPos peerPos, T peer) {
    return false;
  }

  @Override
  public void removePeer(BlockPos pos) {
    this.peers.remove(pos);
  }

  @Override
  public Collection<BlockPos> getPeers() {
    return this.safePeers;
  }

  @Override
  public void startLinking() {
    this.linking = true;
  }

  public boolean isLinking() {
    return this.linking;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT tag = new CompoundNBT();
    ListNBT peersTag = new ListNBT();
    for (BlockPos peer : this.peers) {
      peersTag.add(NBTUtil.writeBlockPos(peer));
    }
    tag.put("peers", peersTag);
    if (this.name != null) {
      tag.putString("name", ITextComponent.Serializer.toJson(this.name));
    }
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundNBT data) {
    ListNBT peersTag = data.getList("peers", 10);
    for (INBT peerTag : peersTag) {
      this.peers.add(NBTUtil.readBlockPos((CompoundNBT) peerTag));
    }
    this.name = data.contains("name", Constants.NBT.TAG_STRING)
        ? ITextComponent.Serializer.fromJson(data.getString("name"))
        : null;
  }

  @Override
  public void syncToClient() {
    this.sync.run();
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    if (this.name == null) {
      data.writeBoolean(true);
    } else {
      data.writeBoolean(false);
      data.writeComponent(this.name);
    }
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    this.name = data.readBoolean() ? null : data.readComponent();
  }

  @Override
  public void setClientPeers(Collection<BlockPos> peers) {
    this.peers.clear();
    this.peers.addAll(peers);
  }
}
