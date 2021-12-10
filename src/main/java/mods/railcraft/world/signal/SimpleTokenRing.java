package mods.railcraft.world.signal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.TokenRing;
import mods.railcraft.api.signal.TokenSignal;
import mods.railcraft.util.AABBFactory;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.MathTools;
import mods.railcraft.world.level.block.entity.signal.TokenSignalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

/**
 * Created by CovertJaguar on 4/23/2015 for Railcraft. <br>
 * <br>
 * <b> This network is only available on the server! </b>
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class SimpleTokenRing implements TokenRing {

  private static final int MAX_DISTANCE = 256 * 256;
  private final ServerLevel level;
  private final TokenRingManager manager;
  private final UUID id;
  private final Set<BlockPos> peers = new HashSet<>();
  private final Set<UUID> trackedCarts = new HashSet<>();
  private BlockPos centroid = BlockPos.ZERO;
  private boolean linking;

  public SimpleTokenRing(ServerLevel level, TokenRingManager manager, UUID id) {
    this.level = level;
    this.manager = manager;
    this.id = id;
  }

  public SimpleTokenRing(ServerLevel level, TokenRingManager manager, UUID id, BlockPos origin) {
    this(level, manager, id);
    this.addSignal(origin);
  }

  @Override
  public boolean isLinking() {
    return this.linking;
  }

  @Override
  public void startLinking() {
    this.linking = true;
  }

  @Override
  public void stopLinking() {
    this.linking = false;
  }

  @Override
  public boolean addPeer(TokenSignal peer) {
    BlockPos otherPos = peer.asBlockEntity().getBlockPos();

    if (this.peers.stream().anyMatch(pos -> pos.distSqr(otherPos) > MAX_DISTANCE)) {
      return false;
    }
    TokenRing otherRing = peer.getSignalNetwork();
    if (otherRing != this) {
      otherRing.removePeer(peer.asBlockEntity().getBlockPos());
    }
    // TokenRing tokenRing = otherRing.signals.size() > signals.size() ? otherRing : this;
    SimpleTokenRing tokenRing = this;
    peer.setTokenRingId(tokenRing.getId());
    tokenRing.addSignal(peer.asBlockEntity().getBlockPos());
    return true;
  }

  public void tick() {
    if (!this.peers.isEmpty()) {
      BlockPos origin = this.peers.stream().findAny().orElse(BlockPos.ZERO);
      AABBFactory aabbFactory = AABBFactory.start().createBoxForTileAt(origin);
      for (BlockPos pos : this.peers) {
        aabbFactory.expandToCoordinate(pos);
      }
      aabbFactory.grow(16).clampToWorld();
      List<AbstractMinecart> carts = EntitySearcher.findMinecarts()
          .around(aabbFactory.build())
          .in(this.level);
      this.trackedCarts.retainAll(carts.stream().map(Entity::getUUID).collect(Collectors.toSet()));
    }
  }

  public boolean isOrphaned(ServerLevel level) {
    return !this.peers.stream().map(this::getPeer).allMatch(Optional::isPresent);
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

  public void markCart(AbstractMinecart cart) {
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

  @Override
  public SignalAspect getSignalAspect() {
    if (this.isLinking()) {
      return SignalAspect.BLINK_YELLOW;
    } else if (this.peers.size() <= 1) {
      return SignalAspect.BLINK_RED;
    }
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
  public Optional<TokenSignal> getPeer(BlockPos blockPos) {
    if (!this.level.isLoaded(blockPos)) {
      return Optional.empty();
    }
    var blockEntity = this.level.getBlockEntity(blockPos);
    return blockEntity instanceof TokenSignalBlockEntity tokenSignal && !blockEntity.isRemoved()
        ? Optional.of(tokenSignal)
        : Optional.empty();
  }

  @Override
  public boolean removePeer(BlockPos peerPos) {
    if (this.peers.remove(peerPos)) {
      this.signalsChanged();
      return true;
    }
    return false;
  }

  @Override
  public void refresh() {}

  @Override
  public void removed() {}
}
