package mods.railcraft.world.level.block.entity.multiblock;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.MultiblockBlock;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class MultiblockBlockEntity<T extends MultiblockBlockEntity<T, M>, M>
    extends RailcraftBlockEntity implements MenuProvider {

  private static final Logger logger = LogUtils.getLogger();

  private static final int RETRY_INTERVAL_TICKS = 40;

  /**
   * A structure which stays broken is retried less and less often, up to this interval.
   */
  private static final int MAX_RETRY_INTERVAL_TICKS = 1280;

  private final Class<T> clazz;
  private final Collection<MultiblockPattern<M>> patterns;

  // Only present on the server
  @Nullable
  private Membership<T> membership;

  /**
   * Used by the master of a multiblock to store all of its members. <code>null</code> if this block
   * is not the master of a formed multiblock.
   */
  @Nullable
  private Map<BlockPos, T> members;

  /**
   * Used by the master of a multiblock to store the current pattern.
   */
  @Nullable
  private MultiblockPattern<M> currentPattern;

  private boolean evaluationPending;

  /**
   * Whether this block has ever been the master of a formed multiblock. Persisted so that a
   * structure which is temporarily broken keeps trying to re-form itself and does not run the
   * entity check again, entities are only a concern when initially assembling a structure.
   */
  private boolean wasFormed;

  private int retryTimer;

  private int retryInterval = RETRY_INTERVAL_TICKS;

  /**
   * The master this block belonged to when it was saved, used to let the master know this block is
   * back once its chunk is loaded again.
   */
  @Nullable
  private BlockPos savedMasterPos;

  /**
   * Whether this block entity's chunk is being unloaded.
   */
  private boolean unloading;

  /**
   * Whether this block entity was loaded from saved data, as opposed to being newly placed.
   */
  private boolean loaded;

  /**
   * Ticker used while this block needs ticking, for blocks which don't provide a ticker of their
   * own.
   */
  @Nullable
  private DynamicTicker dynamicTicker;

  private int searchRadius = -1;

  // Only present on the client
  @Nullable
  private UnresolvedMembership unresolvedMembership;

  public MultiblockBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState,
      Class<T> clazz, MultiblockPattern<M> pattern) {
    this(type, blockPos, blockState, clazz, Collections.singleton(pattern));
  }

  public MultiblockBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState,
      Class<T> clazz, Collection<MultiblockPattern<M>> patterns) {
    super(type, blockPos, blockState);
    this.clazz = clazz;
    this.patterns = Collections.unmodifiableCollection(patterns);
  }

  public Collection<MultiblockPattern<M>> getPatterns() {
    return this.patterns;
  }

  /**
   * Enqueue structure evaluation for the next tick.
   */
  public void enqueueEvaluation() {
    this.evaluationPending = true;
    this.ensureTicking();
  }

  protected void serverTick() {
    // A structure which was formed before but isn't anymore is retried periodically. Evaluation is
    // otherwise only triggered by neighbouring block updates, which may never come again.
    if (this.wasFormed && !this.isFormed() && ++this.retryTimer >= this.retryInterval) {
      this.retryTimer = 0;
      this.retryInterval = Math.min(this.retryInterval * 2, MAX_RETRY_INTERVAL_TICKS);
      this.evaluationPending = true;
    }

    if (this.evaluationPending) {
      this.evaluate();
    }
  }

  /**
   * Invoked when a block next to this one changed. A formed member lets its master re-evaluate the
   * structure. An unformed block lets every unformed block of the same kind around it evaluate, as
   * any of them may be the master of a structure which is now complete.
   */
  public void neighborChanged() {
    if (!(this.level instanceof ServerLevel serverLevel)) {
      return;
    }

    var membership = this.getMembership();
    if (membership.isPresent()) {
      membership.get().master().enqueueEvaluation();
      return;
    }

    var radius = this.getSearchRadius();
    var pos = this.getBlockPos();
    var minChunkX = (pos.getX() - radius) >> 4;
    var maxChunkX = (pos.getX() + radius) >> 4;
    var minChunkZ = (pos.getZ() - radius) >> 4;
    var maxChunkZ = (pos.getZ() + radius) >> 4;
    for (var chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
      for (var chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
        var chunk = serverLevel.getChunkSource().getChunkNow(chunkX, chunkZ);
        if (chunk == null) {
          continue;
        }
        for (var blockEntity : chunk.getBlockEntities().values()) {
          if (this.clazz.isInstance(blockEntity)) {
            var other = this.clazz.cast(blockEntity);
            var otherPos = other.getBlockPos();
            if (!other.isFormed()
                && Math.abs(otherPos.getX() - pos.getX()) <= radius
                && Math.abs(otherPos.getY() - pos.getY()) <= radius
                && Math.abs(otherPos.getZ() - pos.getZ()) <= radius) {
              other.enqueueEvaluation();
            }
          }
        }
      }
    }
  }

  /**
   * The largest side of any of the patterns, the master of a structure this block is part of is
   * never further away than this.
   */
  private int getSearchRadius() {
    if (this.searchRadius < 0) {
      var radius = 0;
      for (var pattern : this.patterns) {
        radius = Math.max(radius,
            Math.max(pattern.getXSize(), Math.max(pattern.getYSize(), pattern.getZSize())));
      }
      this.searchRadius = radius;
    }
    return this.searchRadius;
  }

  /**
   * Whether this block needs to tick right now.
   */
  protected boolean needsTicking() {
    return this.evaluationPending || this.isMaster() || this.wasFormed && !this.isFormed();
  }

  /**
   * Invoked every tick while {@link #needsTicking()} is <code>true</code>, for blocks which don't
   * provide a ticker of their own.
   */
  protected void dynamicTick() {
    this.serverTick();
  }

  /**
   * Makes sure this block ticks while {@link #needsTicking()} is <code>true</code>.
   */
  protected void ensureTicking() {
    if (!(this.level instanceof ServerLevel serverLevel) || this.isRemoved()
        || this.dynamicTicker != null && !this.dynamicTicker.isRemoved()) {
      return;
    }
    if (this.getBlockState().getTicker(serverLevel, this.getType()) != null) {
      // The block ticks this block entity already
      return;
    }
    this.dynamicTicker = new DynamicTicker();
    serverLevel.addBlockEntityTicker(this.dynamicTicker);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (!(this.level instanceof ServerLevel)) {
      return;
    }
    if (this.evaluationPending || this.wasFormed) {
      this.ensureTicking();
    }
    if (!this.loaded) {
      // A newly placed block may complete a structure. Blocks placed from a structure template
      // don't update their neighbours, so check as if a neighbour changed.
      this.neighborChanged();
    }
    // Let our former master know we're back, it may be waiting to re-form.
    if (this.savedMasterPos != null && this.level.isLoaded(this.savedMasterPos)
        && this.clazz.isInstance(this.level.getBlockEntity(this.savedMasterPos))) {
      MultiblockBlockEntity<?, ?> master =
          this.clazz.cast(this.level.getBlockEntity(this.savedMasterPos));
      if (master.wasFormed && !master.isFormed()) {
        master.enqueueEvaluation();
      }
    }
    this.savedMasterPos = null;
  }

  @Override
  public void onChunkUnloaded() {
    super.onChunkUnloaded();
    this.unloading = true;
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    if (!(this.level instanceof ServerLevel) || this.membership == null) {
      return;
    }
    if (this.membership.master() == this) {
      // Members which stay loaded must not keep a reference to us, they drop their membership on
      // their next evaluation.
      if (this.members != null) {
        for (MultiblockBlockEntity<?, ?> member : this.members.values()) {
          if (member != this && !member.isRemoved() && !member.unloading) {
            member.enqueueEvaluation();
          }
        }
      }
      this.members = null;
      this.currentPattern = null;
    } else {
      MultiblockBlockEntity<?, ?> master = this.membership.master();
      if (!master.isRemoved() && !master.unloading) {
        master.enqueueEvaluation();
      }
    }
  }

  /**
   * Invoked from {@link MultiblockBlock} on master blocks.
   *
   * @param player - the player interacting with the block
   * @param hand
   * @return the result
   */
  public InteractionResult use(ServerPlayer player, InteractionHand hand) {
    player.openMenu(this, this.getBlockPos());
    return InteractionResult.CONSUME;
  }

  /**
   * Evaluate the structure pattern and form a multiblock if possible or disband any existing
   * multiblock if the pattern fails to match.
   */
  public void evaluate() {
    if (this.level.isClientSide()) {
      return;
    }

    this.evaluationPending = false;

    if (this.membership != null && this.membership.master().isRemoved()) {
      // Our master was removed or unloaded
      this.setMembership(null);
    }

    if (this.isFormed() && !this.isMaster()) {
      // The master evaluates the structure once for all of its members
      this.membership.master().enqueueEvaluation();
      return;
    }

    var pattern = this.resolvePattern(!this.isFormed() && !this.wasFormed);
    if (this.isFormed() && (!this.isMaster() || pattern.isPresent())
        || !this.isFormed() && pattern.isEmpty()) {
      return;
    }

    pattern.ifPresentOrElse(pair -> {
      this.currentPattern = pair.getLeft();
      this.members = new HashMap<>();
      var resolvedPattern = pair.getRight();
      for (var entry : resolvedPattern.entrySet()) {
        if (!this.isBlockEntity(entry.getValue())) {
          continue;
        }

        var blockEntity =
            LevelUtil.getBlockEntity(this.level, entry.getKey(), this.clazz).orElse(null);
        if (blockEntity == null) {
          logger.warn("Invalid block @ [{}]", entry.getKey());
          this.disband();
          return;
        } else if (blockEntity.getMembership().isPresent()) {
          this.disband();
          return;
        } else {
          blockEntity.setMembership(new Membership<>(entry.getValue(), this.clazz.cast(this)));
          this.members.put(entry.getKey(), blockEntity);
        }
      }
      if (!this.wasFormed) {
        this.wasFormed = true;
        this.setChanged();
      }
      this.retryTimer = 0;
      this.retryInterval = RETRY_INTERVAL_TICKS;
    }, this::disband);
  }

  /**
   * Clear the {@link Membership} of every member of this multiblock and set {@link #members} to
   * <code>null</code>. Does nothing if {@link #members} is already <code>null</code>.
   */
  private void disband() {
    this.currentPattern = null;
    if (this.members == null) {
      return;
    }
    this.retryTimer = 0;
    this.retryInterval = RETRY_INTERVAL_TICKS;
    for (var entry : this.members.entrySet()) {
      if (!entry.getValue().isRemoved()) {
        entry.getValue().setMembership(null);
      }
    }
    this.members = null;
  }

  /**
   * Determine if the specified pattern element is expected to be a block entity.
   *
   * @param element - the pattern element
   * @return <code>true</code> if it is, <code>false</code> otherwise
   */
  protected abstract boolean isBlockEntity(MultiblockPattern.Element element);

  /**
   * Evaluate the multiblock pattern and resolve the position of each block.
   *
   * @return an empty {@link Optional} if the pattern fails to resolve, otherwise an
   *         {@link Optional} containing a map of block positions to their associated pattern
   *         marker.
   */
  public Optional<Pair<MultiblockPattern<M>, Map<BlockPos, MultiblockPattern.Element>>> resolvePattern() {
    return this.resolvePattern(true);
  }

  /**
   * Evaluate the multiblock pattern and resolve the position of each block.
   *
   * @param checkForEntities - whether the pattern's entity check bounds should be honoured
   * @return an empty {@link Optional} if the pattern fails to resolve, otherwise an
   *         {@link Optional} containing a map of block positions to their associated pattern
   *         marker.
   */
  public Optional<Pair<MultiblockPattern<M>, Map<BlockPos, MultiblockPattern.Element>>> resolvePattern(
      boolean checkForEntities) {
    if (this.level instanceof ServerLevel serverLevel) {
      return this.patterns.stream()
          .flatMap(pattern -> pattern.resolve(this.getBlockPos(), serverLevel, checkForEntities)
              .map(map -> Pair.of(pattern, map))
              .stream())
          .findAny();
    } else {
      throw new IllegalStateException("Resolving multiblock pattern on invalid side.");
    }
  }

  /**
   * Set this block's {@link Membership}.
   *
   * @param membership - the {@link Membership} or <code>null</code> if it has none.
   */
  protected void setMembership(@Nullable Membership<T> membership) {
    this.membership = membership;
    this.membershipChanged(membership);
    this.invalidateCapabilities();
    this.setChanged();
    this.syncToClient();
  }

  /**
   * Called upon membership change, e.g. if this block is now part of a formed multiblock or if a
   * multiblock has been disbanded. <b>This should not be called if this block has been removed.</b>
   */
  protected abstract void membershipChanged(@Nullable Membership<T> membership);

  /**
   * Determine if this block is a member of a formed multiblock.
   *
   * @return <code>true</code> if it is a memember, <code>false</code> otherwise
   */
  public boolean isFormed() {
    return this.membership != null && !this.membership.master().isRemoved();
  }

  /**
   * Determine if this block is the master of a formed multiblock (if it is part of any).
   *
   * @return <code>false</code> if this block is not part of a formed multiblock or if it is not the
   *         master, <code>true</code> otherwise
   */
  public boolean isMaster() {
    return this.level.isClientSide()
        ? this.unresolvedMembership != null
            && this.unresolvedMembership.masterPos().equals(this.worldPosition)
        : this.isFormed() && this.membership.master() == this;
  }

  /**
   * Retrieve this block's {@link Membership}.
   *
   * @return an optional {@link Membership}
   */
  public Optional<Membership<T>> getMembership() {
    // A membership whose master was removed or unloaded is no membership at all
    return this.level.isClientSide() || !this.isFormed()
        ? Optional.empty()
        : Optional.of(this.membership);
  }

  public Optional<T> getMasterBlockEntity() {
    return this.getMembership().map(Membership::master);
  }

  public Optional<MultiblockPattern<M>> getCurrentPattern() {
    return Optional.ofNullable(this.currentPattern);
  }

  public Optional<UnresolvedMembership> getUnresolvedMembership() {
    if (!this.level.isClientSide()) {
      throw new IllegalStateException("getUnresolvedMembership is client-side only.");
    }
    return Optional.ofNullable(this.unresolvedMembership);
  }

  public Optional<Collection<T>> getMembers() {
    return Optional.ofNullable(this.members).map(Map::values);
  }

  public Stream<T> streamMembers() {
    return Stream.ofNullable(this.members)
        .map(Map::values)
        .flatMap(Collection::stream);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    this.loaded = true;
    this.wasFormed = input.getBooleanOr(CompoundTagKeys.WAS_FORMED, false);
    if (input.getBooleanOr(CompoundTagKeys.MASTER, false)) {
      this.wasFormed = true;
      this.enqueueEvaluation();
    }
    this.savedMasterPos = input.read(CompoundTagKeys.MASTER_POS, BlockPos.CODEC).orElse(null);
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putBoolean(CompoundTagKeys.MASTER, this.membership != null && this.membership.master() == this);
    output.putBoolean(CompoundTagKeys.WAS_FORMED, this.wasFormed);
    if (this.isFormed() && !this.isMaster()) {
      output.store(CompoundTagKeys.MASTER_POS, BlockPos.CODEC,
          this.membership.master().getBlockPos());
    }
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeNullable(this.isFormed() ? this.membership : null, (buf, membership) -> {
      var patternElement = membership.patternElement();
      buf.writeBlockPos(patternElement.relativePos());
      buf.writeChar(patternElement.marker());
      buf.writeBlockPos(membership.master().getBlockPos());
    });
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf in) {
    super.readFromBuf(in);
    this.unresolvedMembership = in.readNullable(buf -> new UnresolvedMembership(
        new MultiblockPattern.Element(buf.readBlockPos(), buf.readChar()), buf.readBlockPos()));
  }

  private final class DynamicTicker implements TickingBlockEntity {

    private boolean done;

    @Override
    public void tick() {
      var blockEntity = MultiblockBlockEntity.this;
      if (blockEntity.isRemoved() || !blockEntity.needsTicking()) {
        this.done = true;
        return;
      }
      blockEntity.dynamicTick();
    }

    @Override
    public boolean isRemoved() {
      return this.done || MultiblockBlockEntity.this.isRemoved();
    }

    @Override
    public BlockPos getPos() {
      return MultiblockBlockEntity.this.getBlockPos();
    }

    @Override
    public String getType() {
      return String.valueOf(
          BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(MultiblockBlockEntity.this.getType()));
    }
  }

  /**
   * Contains information about a formed multiblock member such as its pattern marker and the master
   * of the multiblock.
   *
   * @author Sm0keySa1m0n
   *
   * @param <T> - the type of multiblock
   */
  public record Membership<T extends MultiblockBlockEntity<T, ?>> (
      MultiblockPattern.Element patternElement, T master) {}

  /**
   * An unresolved version of {@link Membership} which contains the position of the master instead
   * of its instance.
   *
   * @author Sm0keySa1m0n
   */
  public record UnresolvedMembership(MultiblockPattern.Element patternElement,
      BlockPos masterPos) {}
}
