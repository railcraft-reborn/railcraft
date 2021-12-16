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
  private Identity identity;

  private final Map<BlockPos, T> slaves = new HashMap<>();

  private boolean master;

  private boolean pendingValidation;

  public MultiblockBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState,
      Class<T> clazz, MultiblockPattern pattern) {
    super(type, blockPos, blockState);
    this.clazz = clazz;
    this.pattern = pattern;
  }

  public MultiblockPattern getPattern() {
    return this.pattern;
  }

  public void setPendingValidation() {
    this.pendingValidation = true;
  }

  protected void serverTick() {
    if (this.pendingValidation) {
      this.pendingValidation = false;
      this.tryToForm();
    }
  }

  /**
   * Try to make this tile (entity) a parent.
   * 
   * @return true if ok, false if not.
   */
  public void tryToForm() {
    if (this.level.isClientSide()) {
      return;
    }

    var pattern = this.resolvePattern();
    if (this.isFormed() && !this.isMaster()) {
      return;
    }

    pattern.ifPresentOrElse(resolvedPattern -> {
      this.master = true;
      this.slaves.clear();
      for (var entry : resolvedPattern.object2CharEntrySet()) {

        if (!this.isPatternEntity(entry.getCharValue())) {
          continue;
        }

        LevelUtil.getBlockEntity(this.level, entry.getKey(), this.clazz)
            .ifPresentOrElse(blockEntity -> {
              blockEntity.setIdentity(new Identity(entry.getCharValue(), this.getBlockPos()));
              this.slaves.put(entry.getKey(), blockEntity);
            }, () -> logger.warn("Invalid block @ {}", entry.getKey()));
      }
    }, () -> {
      this.master = false;
      for (var entry : this.slaves.entrySet()) {
        LevelUtil.getBlockEntity(this.level, entry.getKey(), this.clazz)
            .ifPresent(blockEntity -> blockEntity.setIdentity(null));
      }
      this.slaves.clear();
    });
  }

  protected abstract boolean isPatternEntity(char marker);

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
  public void setIdentity(Identity identity) {
    this.identity = identity;
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
    return this.identity != null;
  }

  public Optional<Identity> getIdentity() {
    return Optional.ofNullable(this.identity);
  }

  public Optional<T> getMaster() {
    if (this.identity == null) {
      return Optional.empty();
    }

    if (this.identity.masterPos().equals(this.getBlockPos())) {
      return Optional.of(this.clazz.cast(this));
    }

    MultiblockBlockEntity<T> master =
        LevelUtil.getBlockEntity(this.level, this.identity.masterPos(), this.clazz).orElse(null);
    if (master == null || !master.slaves.containsKey(this.getBlockPos())) {
      this.setIdentity(null);
      return Optional.empty();
    }

    return Optional.of(this.clazz.cast(master));
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (this.master) {
      this.tryToForm();
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

  public record Identity(char marker, BlockPos masterPos) {
  }
}
