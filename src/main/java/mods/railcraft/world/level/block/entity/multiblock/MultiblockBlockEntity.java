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
  private Membership<T> membership;

  /**
   * Used by the master block to store all the members of the multiblock. <code>null</code> if this
   * block is not the master block.
   */
  @Nullable
  private Map<BlockPos, T> members;

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
    if (this.isFormed() && (!this.isMaster() || pattern.isPresent())
        || !this.isFormed() && pattern.isEmpty()) {
      return;
    }

    pattern.ifPresentOrElse(resolvedPattern -> {
      this.members = new HashMap<>();
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
        } else {
          blockEntity.setMembership(new Membership<>(entry.getCharValue(), this.clazz.cast(this)));
          this.members.put(entry.getKey(), blockEntity);
        }
      }
    }, this::disband);
  }

  private void disband() {
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

  protected void setMembership(Membership<T> membership) {
    this.membership = membership;
    this.membershipChanged();
    this.setChanged();
  }

  protected abstract void membershipChanged();

  public boolean isFormed() {
    return this.membership != null;
  }

  public boolean isMaster() {
    return this.membership != null && this.membership.master() == this;
  }

  public Optional<Membership<T>> getMembership() {
    return Optional.ofNullable(this.membership);
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

  public record Membership<T extends MultiblockBlockEntity<T>> (char marker, T master) {}
}
