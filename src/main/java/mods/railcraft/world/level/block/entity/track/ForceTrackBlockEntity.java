package mods.railcraft.world.level.block.entity.track;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.entity.ForceTrackEmitterBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;

public final class ForceTrackBlockEntity extends RailcraftBlockEntity {

  @Nullable
  private ForceTrackEmitterBlockEntity emitter;
  @Nullable
  private BlockPos emitterPos;

  public ForceTrackBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.FORCE_TRACK.get(), blockPos, blockState);
  }

  public void blockRemoved() {
    this.emitter.clearTracks(this.getBlockPos());
  }

  public void neighborChanged() {
    this.emitter.notifyTrackChange();
  }

  public void setEmitter(ForceTrackEmitterBlockEntity emitter) {
    this.emitter = emitter;
    this.emitterPos = emitter.getBlockPos();
    this.setOwner(emitter.getOwner().orElse(null));
    this.level.setBlockAndUpdate(this.getBlockPos(),
        this.getBlockState().setValue(ForceTrackBlock.COLOR,
            emitter.getBlockState().getValue(ForceTrackEmitterBlock.COLOR)));
  }

  @Override
  public void onLoad() {
    super.onLoad();
    this.emitter = this.emitterPos == null
        ? null
        : this.level
            .getBlockEntity(this.emitterPos, RailcraftBlockEntityTypes.FORCE_TRACK_EMITTER.get())
            .orElse(null);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("emitterPos", NbtUtils.writeBlockPos(this.emitterPos));
  }

  @Override
  public void load(CompoundTag tag) {
    this.emitterPos = NbtUtils.readBlockPos(tag.getCompound("emitterPos"));
  }
}
