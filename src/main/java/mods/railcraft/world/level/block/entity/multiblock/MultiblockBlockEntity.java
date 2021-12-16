package mods.railcraft.world.level.block.entity.multiblock;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import it.unimi.dsi.fastutil.objects.Object2CharMap;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MultiblockBlockEntity<T extends MultiblockBlockEntity<T>>
    extends RailcraftBlockEntity implements MenuProvider {

  private static final Logger logger = LogManager.getLogger();

  private final Class<T> clazz;
  private final MultiblockPattern pattern;

  @Nullable
  private Membership membership;

  private final Map<BlockPos, T> members = new HashMap<>();

  private boolean master;

  private boolean pendingEvaluation;

  public MultiblockBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState,
      Class<T> clazz, MultiblockPattern pattern) {
    super(type, blockPos, blockState);
    this.clazz = clazz;
    this.pattern = pattern;
  }

  public MultiblockPattern getPattern() {
    return this.pattern;
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
    if (this.isFormed() && !this.isMaster()) {
      return;
    }

    pattern.ifPresentOrElse(resolvedPattern -> {
      this.master = true;
      this.members.clear();
      for (var entry : resolvedPattern.object2CharEntrySet()) {
        if (!this.isBlockEntity(entry.getCharValue())) {
          continue;
        }

        var blockEntity =
            LevelUtil.getBlockEntity(this.level, entry.getKey(), this.clazz).orElse(null);
        if (blockEntity == null) {
          logger.warn("Invalid block @ {}", entry.getKey());
          this.disband();
          return;
        } else if (blockEntity.isFormed()) {
          this.disband();
          return;
        } else {
          blockEntity.setIdentity(new Membership(entry.getCharValue(), this.getBlockPos()));
          this.members.put(entry.getKey(), blockEntity);
        }
      }
    }, this::disband);
  }

  private void disband() {
    this.master = false;
    for (var entry : this.members.entrySet()) {
      LevelUtil.getBlockEntity(this.level, entry.getKey(), this.clazz)
          .ifPresent(blockEntity -> blockEntity.setIdentity(null));
    }
    this.members.clear();
  }

  protected abstract boolean isBlockEntity(char marker);

  /**
   * Handles the pattern detection.
   * 
   * @return true if pattern detection is ok, false if not.
   */
  public Optional<Object2CharMap<BlockPos>> resolvePattern() {
    if (this.level instanceof ServerLevel serverLevel) {
      return this.pattern.verifyPattern(this.getBlockPos(), serverLevel);
    } else {
      throw new IllegalStateException("Resolving multiblock pattern on invalid side.");
    }
  }

  /**
   * Sets this tilentity's parent, delinking first.
   * 
   * @param parentPos - The position of the parent in the world.
   */
  public void setIdentity(Membership identity) {
    this.membership = identity;
    this.identityChanged();
    this.setChanged();
  }

  protected abstract void identityChanged();

  /**
   * Is this a parent.
   * 
   * @return TRUE if yes.
   */
  public boolean isMaster() {
    return this.master;
  }

  public boolean isFormed() {
    return this.membership != null;
  }

  public Optional<Membership> getMembership() {
    return Optional.ofNullable(this.membership);
  }

  public Optional<T> getMaster() {
    if (this.membership == null) {
      return Optional.empty();
    }

    if (this.membership.masterPos().equals(this.getBlockPos())) {
      return Optional.of(this.clazz.cast(this));
    }

    MultiblockBlockEntity<T> master =
        LevelUtil.getBlockEntity(this.level, this.membership.masterPos(), this.clazz).orElse(null);
    if (master == null || !master.members.containsKey(this.getBlockPos())) {
      this.setIdentity(null);
      return Optional.empty();
    }

    return Optional.of(this.clazz.cast(master));
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (this.master) {
      this.evaluate();
    }
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.master = tag.getBoolean("master");
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putBoolean("master", this.master);
  }

  public record Membership(char marker, BlockPos masterPos) {
  }
}
