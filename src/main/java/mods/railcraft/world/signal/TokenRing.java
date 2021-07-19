package mods.railcraft.world.signal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import mods.railcraft.api.core.CollectionToolsAPI;
import mods.railcraft.api.signals.ITokenRing;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.util.AABBFactory;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.MathTools;
import mods.railcraft.world.level.block.entity.TokenSignalBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CovertJaguar on 4/23/2015 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TokenRing implements ITokenRing {

  private static final int MAX_DISTANCE = 256 * 256;
  private final UUID uuid;
  private final Set<BlockPos> signals = CollectionToolsAPI.blockPosSet(HashSet::new);
  private final Set<UUID> trackedCarts = new HashSet<>();
  private BlockPos centroid = BlockPos.ZERO;
  private final TokenManager.TokenWorldManager manager;
  private boolean linking;

  public TokenRing(TokenManager.TokenWorldManager manager, UUID uuid) {
    this.manager = manager;
    this.uuid = uuid;
  }

  public TokenRing(TokenManager.TokenWorldManager manager, UUID uuid, BlockPos origin) {
    this(manager, uuid);
    addSignal(origin);
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
  public boolean add(TileEntity other) {
    if (other instanceof TokenSignalBlockEntity) {
      BlockPos otherPos = other.getBlockPos();
      if (signals.stream().anyMatch(pos -> pos.distSqr(otherPos) > MAX_DISTANCE)) {
        return false;
      }
      TokenSignalBlockEntity tokenTile = (TokenSignalBlockEntity) other;
      TokenRing otherRing = ((TokenSignalBlockEntity) other).getTokenRing();
      otherRing.removeSignal(other.getBlockPos());
      otherRing.endLinking();
      // TokenRing tokenRing = otherRing.signals.size() > signals.size() ? otherRing : this;
      TokenRing tokenRing = this;
      tokenTile.setTokenRingUUID(tokenRing.uuid);
      tokenRing.addSignal(tokenTile.getBlockPos());
      return true;
    }
    return false;
  }

  public void tick(World world) {
    if (!signals.isEmpty()) {
      BlockPos origin = signals.stream().findAny().orElse(BlockPos.ZERO);
      AABBFactory aabbFactory = AABBFactory.start().createBoxForTileAt(origin);
      for (BlockPos pos : signals) {
        aabbFactory.expandToCoordinate(pos);
      }
      aabbFactory.grow(16).clampToWorld();
      List<AbstractMinecartEntity> carts =
          EntitySearcher.findMinecarts().around(aabbFactory.build()).in(world);
      trackedCarts.retainAll(carts.stream().map(Entity::getUUID).collect(Collectors.toSet()));
    }
  }

  public boolean isOrphaned(World world) {
    return signals.stream().noneMatch(
        blockPos -> !world.isLoaded(blockPos) || isTokenSignal(world, blockPos));
  }

  private boolean isTokenSignal(World world, BlockPos pos) {
    TileEntity blockEntity = world.getBlockEntity(pos);
    return blockEntity instanceof TokenSignalBlockEntity;
  }

  void loadSignals(Collection<BlockPos> signals) {
    this.signals.addAll(signals);
    centroid = MathTools.centroid(signals);
  }

  void loadCarts(Collection<UUID> carts) {
    trackedCarts.addAll(carts);
  }

  public boolean addSignal(BlockPos pos) {
    boolean changed = signals.add(pos);
    if (changed)
      signalsChanged();
    return changed;
  }

  public boolean removeSignal(BlockPos pos) {
    boolean changed = signals.remove(pos);
    if (changed)
      signalsChanged();
    return changed;
  }

  private void signalsChanged() {
    manager.setDirty();
    centroid = MathTools.centroid(signals);
  }

  public void markCart(AbstractMinecartEntity cart) {
    UUID cartID = cart.getUUID();
    if (trackedCarts.remove(cartID)) {
      manager.setDirty();
      return;
    }
    if (trackedCarts.add(cartID))
      manager.setDirty();
  }

  public Set<BlockPos> getSignals() {
    return Collections.unmodifiableSet(signals);
  }

  @Override
  public Collection<BlockPos> getPeers() {
    return this.getSignals();
  }

  public Set<UUID> getTrackedCarts() {
    return Collections.unmodifiableSet(trackedCarts);
  }

  public SignalAspect getAspect() {
    if (linking)
      return SignalAspect.BLINK_YELLOW;
    if (signals.size() <= 1)
      return SignalAspect.BLINK_RED;
    return trackedCarts.isEmpty() ? SignalAspect.GREEN : SignalAspect.RED;
  }

  public UUID getUUID() {
    return uuid;
  }

  public BlockPos centroid() {
    return centroid;
  }
}
