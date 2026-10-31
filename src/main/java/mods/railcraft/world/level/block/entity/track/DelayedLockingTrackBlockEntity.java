package mods.railcraft.world.level.block.entity.track;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.outfitted.DelayedLockingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingMode;
import mods.railcraft.world.level.block.track.outfitted.PoweredOutfittedTrackBlock;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class DelayedLockingTrackBlockEntity extends LockingTrackBlockEntity {

  public static final int MIN_DELAY = 0;
  public static final int MAX_DELAY = 60 * SharedConstants.TICKS_PER_SECOND;
  public static final int DEFAULT_DELAY = 5 * SharedConstants.TICKS_PER_SECOND;

  private int delay = DEFAULT_DELAY;
  private int waited;

  public DelayedLockingTrackBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.DELAYED_LOCKING_TRACK.get(), blockPos, blockState);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      DelayedLockingTrackBlockEntity blockEntity) {
    LockingTrackBlockEntity.serverTick(level, blockPos, blockState, blockEntity);
    var holding = blockEntity.locked && blockEntity.waited > 0;
    if (DelayedLockingTrackBlock.isHolding(blockState) != holding) {
      level.setBlock(blockPos, DelayedLockingTrackBlock.setHolding(blockState, holding),
          Block.UPDATE_CLIENTS);
    }
  }

  @Override
  protected LockingMode getLockingMode(BlockState blockState) {
    return LockingMode.TRAIN_HOLDING;
  }

  /**
   * Unlike the regular locking track, carts are only held while the track is powered, and only for
   * the configured delay.
   */
  @Override
  protected void calculateLocked() {
    if (this.currentCart == null) {
      this.waited = 0;
    }
    var powered = PoweredOutfittedTrackBlock.isPowered(this.getBlockState());
    var sameTrainOrCart = this.isSameTrainOrCart();
    if (!powered || this.waited >= this.delay) {
      this.locked = false;
      this.waited = 0;
      if (sameTrainOrCart) {
        this.trainLeaving = true;
      }
      return;
    }

    if (this.trainLeaving && sameTrainOrCart) {
      this.locked = false;
    } else {
      this.locked = true;
      this.trainLeaving = false;
    }

    if (this.currentCart != null && this.locked) {
      this.waited++;
    }
  }

  public int getDelay() {
    return this.delay;
  }

  public void setDelay(int delay) {
    this.delay = Mth.clamp(delay, MIN_DELAY, MAX_DELAY);
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.putInt(CompoundTagKeys.DELAY, this.delay);
    tag.putInt(CompoundTagKeys.WAITED, this.waited);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.setDelay(tag.contains(CompoundTagKeys.DELAY)
        ? tag.getInt(CompoundTagKeys.DELAY)
        : DEFAULT_DELAY);
    this.waited = tag.getInt(CompoundTagKeys.WAITED);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeVarInt(this.delay);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
    super.readFromBuf(data);
    this.delay = data.readVarInt();
  }
}
