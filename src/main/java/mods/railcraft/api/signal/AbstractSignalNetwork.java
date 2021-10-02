/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javax.annotation.Nullable;
import mods.railcraft.api.core.BlockEntityLike;
import mods.railcraft.api.core.Syncable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * 
 * @author Sm0keySa1m0n
 *
 * @param <T>
 */
public abstract class AbstractSignalNetwork<T extends BlockEntityLike>
    implements SignalNetwork<T>, INBTSerializable<CompoundNBT>, Syncable {

  protected static final Random random = new Random();

  private final Class<T> peerType;
  private final int maxPeers;
  private final Runnable syncListener;

  protected final Deque<BlockPos> peers = new ArrayDeque<>();
  private final Collection<BlockPos> unmodifiablePeers =
      Collections.unmodifiableCollection(this.peers);

  private boolean linking;

  public AbstractSignalNetwork(Class<T> peerType, Runnable syncListener) {
    this(peerType, -1, syncListener);
  }

  public AbstractSignalNetwork(Class<T> peerType, int maxPeers, Runnable syncListener) {
    this.peerType = peerType;
    this.maxPeers = maxPeers;
    this.syncListener = syncListener;
  }

  public abstract World getLevel();

  @Override
  public Optional<T> getPeer(BlockPos blockPos) {
    return this.peers.contains(blockPos)
        ? Optional.ofNullable(this.getBlockEntity(blockPos))
        : Optional.empty();
  }

  @Nullable
  protected T getBlockEntity(BlockPos blockPos) {
    TileEntity blockEntity = this.getLevel().getBlockEntity(blockPos);
    return this.peerType.isInstance(blockEntity) && !blockEntity.isRemoved()
        ? this.peerType.cast(blockEntity)
        : null;
  }

  @Override
  public Collection<BlockPos> getPeers() {
    return this.unmodifiablePeers;
  }

  @Override
  public boolean addPeer(T peer) {
    if (this.peers.contains(peer.asBlockEntity().getBlockPos())) {
      return false;
    }
    BlockPos peerPos = peer.asBlockEntity().getBlockPos();
    this.peers.add(peerPos);
    if (this.maxPeers > -1 && this.peers.size() > this.maxPeers) {
      this.removePeer(this.peers.peek());
    }
    this.syncToClient();
    return true;
  }

  @Override
  public void removed() {
    List<BlockPos> peers = new ArrayList<>(this.peers);
    for (BlockPos peerPos : peers) {
      this.removePeer(peerPos);
    }
  }

  @Override
  public void refresh() {
    this.peers.removeIf(peerPos -> !this.getPeer(peerPos).filter(this::refreshPeer).isPresent());
  }

  protected boolean refreshPeer(T peer) {
    return true;
  }

  @Override
  public boolean removePeer(BlockPos peerPos) {
    if (this.peers.remove(peerPos)) {
      this.syncToClient();
      return true;
    }
    return false;
  }

  @Override
  public boolean isLinking() {
    return this.linking;
  }

  @Override
  public void startLinking() {
    this.linking = true;
    this.syncToClient();
  }

  @Override
  public void stopLinking() {
    this.linking = false;
    this.syncToClient();
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT tag = new CompoundNBT();
    ListNBT peersTag = new ListNBT();
    for (BlockPos peer : this.peers) {
      peersTag.add(NBTUtil.writeBlockPos(peer));
    }
    tag.put("peers", peersTag);
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundNBT data) {
    ListNBT peersTag = data.getList("peers", Constants.NBT.TAG_COMPOUND);
    for (INBT peerTag : peersTag) {
      this.peers.add(NBTUtil.readBlockPos((CompoundNBT) peerTag));
    }
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    data.writeBoolean(this.linking);
    data.writeVarInt(this.peers.size());
    for (BlockPos peerPos : this.peers) {
      data.writeBlockPos(peerPos);
    }
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    this.linking = data.readBoolean();
    int peersSize = data.readVarInt();
    this.peers.clear();
    for (int i = 0; i < peersSize; i++) {
      this.peers.add(data.readBlockPos());
    }
  }

  @Override
  public void syncToClient() {
    this.syncListener.run();
  }
}
