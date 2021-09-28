package mods.railcraft.world.signal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.api.signals.TokenRingNetwork;
import mods.railcraft.util.AABBFactory;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.MathTools;
import mods.railcraft.world.level.block.entity.signal.TokenSignalBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Created by CovertJaguar on 4/23/2015 for Railcraft. <br>
 * <br>
 * <b> This network is only available on the server! </b>
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class SimpleTokenRingNetwork implements TokenRingNetwork {

  private static final int MAX_DISTANCE = 256 * 256;
  private final UUID id;
  private final Set<BlockPos> peers = new HashSet<>();
  private final Set<UUID> trackedCarts = new HashSet<>();
  private BlockPos centroid = BlockPos.ZERO;
  private final TokenManager manager;
  private boolean linking;

  public SimpleTokenRingNetwork(TokenManager manager, UUID id) {
    this.manager = manager;
    this.id = id;
  }

  public SimpleTokenRingNetwork(TokenManager manager, UUID id, BlockPos origin) {
    this(manager, id);
    this.addSignal(origin);
  }

  @Override
  public void startLinking() {
    this.linking = true;
  }

  @Override
  public void endLinking() {
    this.linking = false;
  }

  @Override
  public boolean addPeer(TokenSignalBlockEntity peer) {
    if (peer instanceof TokenSignalBlockEntity) {
      BlockPos otherPos = peer.getBlockPos();
      if (this.peers.stream().anyMatch(pos -> pos.distSqr(otherPos) > MAX_DISTANCE)) {
        return false;
      }
      TokenSignalBlockEntity tokenTile = (TokenSignalBlockEntity) peer;
      SimpleTokenRingNetwork otherRing = ((TokenSignalBlockEntity) peer).getSignalNetwork();
      otherRing.removePeer(peer.getBlockPos());
      otherRing.endLinking();
      // TokenRing tokenRing = otherRing.signals.size() > signals.size() ? otherRing : this;
      SimpleTokenRingNetwork tokenRing = this;
      tokenTile.setTokenRingId(tokenRing.getId());
      tokenRing.addSignal(tokenTile.getBlockPos());
      return true;
    }
    return false;
  }

  public void tick(ServerWorld level) {
    if (!this.peers.isEmpty()) {
      BlockPos origin = this.peers.stream().findAny().orElse(BlockPos.ZERO);
      AABBFactory aabbFactory = AABBFactory.start().createBoxForTileAt(origin);
      for (BlockPos pos : this.peers) {
        aabbFactory.expandToCoordinate(pos);
      }
      aabbFactory.grow(16).clampToWorld();
      List<AbstractMinecartEntity> carts = EntitySearcher.findMinecarts()
          .around(aabbFactory.build())
          .in(level);
      this.trackedCarts.retainAll(carts.stream().map(Entity::getUUID).collect(Collectors.toSet()));
    }
  }

  public boolean isOrphaned(ServerWorld level) {
    return this.peers.stream().noneMatch(
        blockPos -> !level.isLoaded(blockPos) || this.isTokenSignal(level, blockPos));
  }

  private boolean isTokenSignal(World world, BlockPos pos) {
    TileEntity blockEntity = world.getBlockEntity(pos);
    return blockEntity instanceof TokenSignalBlockEntity;
  }

  void loadSignals(Collection<BlockPos> signals) {
    this.peers.addAll(signals);
    this.centroid = MathTools.centroid(signals);
  }

  void loadCarts(Collection<UUID> carts) {
    this.trackedCarts.addAll(carts);
  }

  public boolean addSignal(BlockPos pos) {
    boolean changed = this.peers.add(pos);
    if (changed)
      this.signalsChanged();
    return changed;
  }

  private void signalsChanged() {
    this.manager.setDirty();
    this.centroid = MathTools.centroid(this.peers);
  }

  public void markCart(AbstractMinecartEntity cart) {
    UUID cartID = cart.getUUID();
    if (this.trackedCarts.remove(cartID)) {
      this.manager.setDirty();
      return;
    }
    if (this.trackedCarts.add(cartID))
      this.manager.setDirty();
  }

  @Override
  public Collection<BlockPos> getPeers() {
    return Collections.unmodifiableSet(this.peers);
  }

  @Override
  public Set<UUID> getTrackedCarts() {
    return Collections.unmodifiableSet(this.trackedCarts);
  }

  public SignalAspect getAspect() {
    if (this.linking)
      return SignalAspect.BLINK_YELLOW;
    if (this.peers.size() <= 1)
      return SignalAspect.BLINK_RED;
    return this.trackedCarts.isEmpty() ? SignalAspect.GREEN : SignalAspect.RED;
  }

  @Override
  public UUID getId() {
    return this.id;
  }

  @Override
  public BlockPos getCentroid() {
    return this.centroid;
  }

  @Override
  public void removePeer(BlockPos peerPos) {
    if (this.peers.remove(peerPos)) {
      this.signalsChanged();
    }
  }
}
