package mods.railcraft.world.level.block.entity.multiblock;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.MultiblockBlock;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MultiblockBlockEntity<T extends MultiblockBlockEntity<T, M>, M>
    extends RailcraftBlockEntity implements MenuProvider {

  private static final Logger logger = LogUtils.getLogger();

  private static final int RETRY_INTERVAL_TICKS = 40;

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

  /**
   * Whether this block is being removed because its chunk is unloading, as opposed to being broken.
   */
  private boolean unloading;

  /**
   * The master this member belonged to when it was saved. Used once loaded to let the master know
   * this block is back, it may be waiting for it to re-form or to pick it up again.
   */
  @Nullable
  private BlockPos savedMasterPos;

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
  }

  protected void serverTick() {
    // A structure which was formed before but isn't anymore is retried periodically. Evaluation is
    // otherwise only triggered by neighbouring block updates, which may never come again.
    if (this.wasFormed && !this.isFormed() && ++this.retryTimer >= RETRY_INTERVAL_TICKS) {
      this.retryTimer = 0;
      this.evaluationPending = true;
    }

    if (this.evaluationPending) {
      this.evaluate();
    }
  }

  /**
   * Invoked from {@link MultiblockBlock} on master blocks.
   *
   * @param player - the player interacting with the block
   * @param hand
   * @return the result
   */
  public ItemInteractionResult use(ServerPlayer player, InteractionHand hand) {
    player.openMenu(this, this.getBlockPos());
    return ItemInteractionResult.CONSUME;
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
      // Our master was broken or unloaded
      this.setMembership(null);
    }

    if (this.isFormed() && !this.isMaster()) {
      // Defer to the master's next tick so that it evaluates the structure only once, no matter how
      // many members were notified of the same change.
      this.membership.master.enqueueEvaluation();
      return;
    }

    if (this.isFormed() && !this.currentPattern.isLoaded(this.worldPosition, this.level)) {
      // Part of the structure is unloaded, so it can't be checked right now. Disbanding would make
      // us drop our contents, so keep it as is. Members let us know when they're loaded again.
      return;
    }

    var pattern = this.resolvePattern(!this.isFormed() && !this.wasFormed);
    if (this.isFormed() && pattern.isPresent()) {
      this.reattachMembers(pattern.get().getRight());
      return;
    }
    if (!this.isFormed() && pattern.isEmpty()) {
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
    }, this::disband);
  }

  /**
   * Replace the members which were removed since the structure formed, i.e. unloaded and loaded
   * again, with the block entities now at their position.
   */
  private void reattachMembers(Map<BlockPos, MultiblockPattern.Element> resolvedPattern) {
    for (var entry : this.members.entrySet()) {
      if (!entry.getValue().isRemoved()) {
        continue;
      }
      var element = resolvedPattern.get(entry.getKey());
      var blockEntity =
          LevelUtil.getBlockEntity(this.level, entry.getKey(), this.clazz).orElse(null);
      if (element == null || blockEntity == null || blockEntity.getMembership().isPresent()) {
        logger.warn("Invalid block @ [{}]", entry.getKey());
        this.disband();
        return;
      }
      blockEntity.setMembership(new Membership<>(element, this.clazz.cast(this)));
      entry.setValue(blockEntity);
    }
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
    // A master which was broken or unloaded is no master at all
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
    return this.level.isClientSide() || !this.isFormed()
        ? Optional.empty()
        : Optional.ofNullable(this.membership);
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

  /**
   * The members of the multiblock this block is the master of, leaving out unloaded ones.
   */
  public Optional<Collection<T>> getMembers() {
    return Optional.ofNullable(this.members).map(members -> this.streamMembers().toList());
  }

  /**
   * The members of the multiblock this block is the master of, leaving out unloaded ones.
   */
  public Stream<T> streamMembers() {
    return Stream.ofNullable(this.members)
        .map(Map::values)
        .flatMap(Collection::stream)
        .filter(member -> !member.isRemoved());
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (this.savedMasterPos != null && this.level instanceof ServerLevel
        && this.level.isLoaded(this.savedMasterPos)
        && this.clazz.isInstance(this.level.getBlockEntity(this.savedMasterPos))) {
      this.clazz.cast(this.level.getBlockEntity(this.savedMasterPos)).enqueueEvaluation();
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
    if (this.unloading || !(this.level instanceof ServerLevel) || this.membership == null) {
      return;
    }
    // This block was broken or replaced, let the rest of the structure find out
    var master = this.membership.master();
    if (master != this) {
      if (!master.isRemoved()) {
        master.enqueueEvaluation();
      }
    } else if (this.members != null) {
      for (var member : this.members.values()) {
        if (member != this && !member.isRemoved()) {
          member.enqueueEvaluation();
        }
      }
    }
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.wasFormed = tag.getBoolean(CompoundTagKeys.WAS_FORMED);
    if (tag.getBoolean(CompoundTagKeys.MASTER)) {
      this.wasFormed = true;
      this.enqueueEvaluation();
    }
    this.savedMasterPos = NbtUtils.readBlockPos(tag, CompoundTagKeys.MASTER_POS).orElse(null);
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.putBoolean(CompoundTagKeys.MASTER, this.isMaster());
    tag.putBoolean(CompoundTagKeys.WAS_FORMED, this.wasFormed);
    if (this.isFormed() && !this.isMaster()) {
      tag.put(CompoundTagKeys.MASTER_POS,
          NbtUtils.writeBlockPos(this.membership.master().getBlockPos()));
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
