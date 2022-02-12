package mods.railcraft.world.level.block.entity.multiblock;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import it.unimi.dsi.fastutil.objects.Object2CharMap;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MultiblockBlockEntity<T extends MultiblockBlockEntity<T>>
    extends RailcraftBlockEntity implements MenuProvider {

  private static final Logger logger = LogManager.getLogger();

  private final Class<T> clazz;
  private final Collection<MultiblockPattern> patterns;

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
  private MultiblockPattern currentPattern;

  private boolean pendingEvaluation;

  public MultiblockBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState,
      Class<T> clazz, MultiblockPattern pattern) {
    this(type, blockPos, blockState, clazz, Collections.singleton(pattern));
  }

  public MultiblockBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState,
      Class<T> clazz, Collection<MultiblockPattern> patterns) {
    super(type, blockPos, blockState);
    this.clazz = clazz;
    this.patterns = Collections.unmodifiableCollection(patterns);
  }

  public Collection<MultiblockPattern> getPatterns() {
    return this.patterns;
  }

  /**
   * Enqueue structure evaluation for the next tick.
   */
  public void enqueueEvaluation() {
    this.pendingEvaluation = true;
  }

  protected void serverTick() {
    if (this.pendingEvaluation) {
      this.pendingEvaluation = false;
      this.evaluate();
    }
  }

  /**
   * Evaluate the structure pattern and form a multiblock if possible or disband any existing
   * multiblock if the pattern fails to match.
   */
  public void evaluate() {
    if (this.level.isClientSide()) {
      return;
    }

    var pattern = this.resolvePattern();
    if (this.isFormed() && (!this.isMaster() || pattern.isPresent())
        || !this.isFormed() && pattern.isEmpty()) {
      return;
    }

    pattern.ifPresentOrElse(pair -> {
      this.currentPattern = pair.getLeft();
      this.members = new HashMap<>();
      var resolvedPattern = pair.getRight();
      for (var entry : resolvedPattern.object2CharEntrySet()) {
        if (!this.isBlockEntity(entry.getCharValue())) {
          continue;
        }

        var blockEntity =
            LevelUtil.getBlockEntity(this.level, entry.getKey(), this.clazz).orElse(null);
        if (blockEntity == null) {
          logger.warn("Invalid block @ [{}]", entry.getKey());
          this.disband();
          return;
        } else {
          blockEntity.setMembership(new Membership<>(entry.getCharValue(), this.clazz.cast(this)));
          this.members.put(entry.getKey(), blockEntity);
        }
      }
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
    for (var entry : this.members.entrySet()) {
      if (!entry.getValue().isRemoved()) {
        entry.getValue().setMembership(null);
      }
    }
    this.members = null;
  }

  /**
   * Determine if the specified pattern marker is expected to be a block entity.
   * 
   * @param marker - the pattern marker
   * @return <code>true</code> if it is, <code>false</code> otherwise
   */
  protected abstract boolean isBlockEntity(char marker);

  /**
   * Evaluate the multiblock pattern and resolve the position of each block.
   * 
   * @return an empty {@link Optional} if the pattern fails to resolve, otherwise an
   *         {@link Optional} containing a map of block positions to their associated pattern
   *         marker.
   */
  public Optional<Pair<MultiblockPattern, Object2CharMap<BlockPos>>> resolvePattern() {
    if (this.level instanceof ServerLevel serverLevel) {
      return this.patterns.stream()
          .flatMap(pattern -> pattern.resolve(this.getBlockPos(), serverLevel)
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
    return this.membership != null;
  }

  /**
   * Determine if this block is the master of a formed multiblock (if it is part of any).
   * 
   * @return <code>false</code> if this block is not part of a formed multiblock or if it is not the
   *         master, <code>true</code> otherwise
   */
  public boolean isMaster() {
    return this.membership != null && this.membership.master() == this;
  }

  /**
   * Retrieve this block's {@link Membership}.
   * 
   * @return an optional {@link Membership}
   */
  public Optional<Membership<T>> getMembership() {
    return Optional.ofNullable(this.membership);
  }

  public Optional<MultiblockPattern> getCurrentPattern() {
    return Optional.ofNullable(this.currentPattern);
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
  public void setRemoved() {
    super.setRemoved();
    this.disband();
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    if (tag.getBoolean("master")) {
      this.enqueueEvaluation();
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putBoolean("master", this.membership != null && this.membership.master() == this);
  }

  @Override
  public void writeToBuf(FriendlyByteBuf out) {
    super.writeToBuf(out);
    if (this.membership == null) {
      out.writeBoolean(true);
    } else {
      out.writeBoolean(false);
      out.writeChar(this.membership.marker());
      out.writeBlockPos(this.membership.master().getBlockPos());
    }
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    super.readFromBuf(in);
    if (in.readBoolean()) {
      this.membership = null;
    } else {
      var marker = in.readChar();
      var masterPos = in.readBlockPos();
      var master = LevelUtil.getBlockEntity(this.level, masterPos, this.clazz).orElse(null);
      if (master == null) {
        logger.error("Master block not found @ [{}]", masterPos.toShortString());
        this.membership = null;
      } else {
        this.membership = new Membership<>(marker, master);
      }
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
  public record Membership<T extends MultiblockBlockEntity<T>> (char marker, T master) {}
}
