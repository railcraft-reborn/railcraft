package mods.railcraft.world.level.block.entity.track;

import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.core.Lockable;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class LockableTrackBlockEntity extends RailcraftBlockEntity implements Lockable {

  private LockableSwitchTrackActuatorBlockEntity.Lock lock;

  public LockableTrackBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState) {
    super(type, blockPos, blockState);
    this.lock = LockableSwitchTrackActuatorBlockEntity.Lock.UNLOCKED;
  }

  public LockableSwitchTrackActuatorBlockEntity.Lock getLock() {
    return this.lock;
  }

  public void setLock(@Nullable GameProfile gameProfile) {
    this.lock = gameProfile == null
        ? LockableSwitchTrackActuatorBlockEntity.Lock.UNLOCKED
        : LockableSwitchTrackActuatorBlockEntity.Lock.LOCKED;
    this.setOwner(gameProfile);
  }

  @Override
  public boolean isLocked() {
    return this.lock == LockableSwitchTrackActuatorBlockEntity.Lock.LOCKED;
  }

  public boolean canAccess(GameProfile gameProfile) {
    return !this.isLocked() || this.isOwnerOrOperator(gameProfile);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString(CompoundTagKeys.LOCK, this.lock.getSerializedName());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.lock =
        LockableSwitchTrackActuatorBlockEntity.Lock.fromName(tag.getString(CompoundTagKeys.LOCK));
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.lock);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.lock = data.readEnum(LockableSwitchTrackActuatorBlockEntity.Lock.class);
  }
}
