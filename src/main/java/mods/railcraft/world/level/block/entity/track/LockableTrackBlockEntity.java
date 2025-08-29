package mods.railcraft.world.level.block.entity.track;

import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.core.Lockable;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.store(CompoundTagKeys.LOCK, LockableSwitchTrackActuatorBlockEntity.Lock.CODEC, this.lock);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    this.lock = input.read( CompoundTagKeys.LOCK, LockableSwitchTrackActuatorBlockEntity.Lock.CODEC)
        .orElse(LockableSwitchTrackActuatorBlockEntity.Lock.UNLOCKED);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.lock);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
    super.readFromBuf(data);
    this.lock = data.readEnum(LockableSwitchTrackActuatorBlockEntity.Lock.class);
  }
}
