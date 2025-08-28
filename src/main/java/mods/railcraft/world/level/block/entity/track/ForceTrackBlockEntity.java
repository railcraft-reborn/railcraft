package mods.railcraft.world.level.block.entity.track;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.entity.ForceTrackEmitterBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public final class ForceTrackBlockEntity extends RailcraftBlockEntity {

  @Nullable
  private ForceTrackEmitterBlockEntity emitter;
  @Nullable
  private BlockPos emitterPos;

  public ForceTrackBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.FORCE_TRACK.get(), blockPos, blockState);
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
    if (this.emitterPos == null) {
      this.emitter = null;
      return;
    }
    this.emitter = this.level
        .getBlockEntity(this.emitterPos, RailcraftBlockEntityTypes.FORCE_TRACK_EMITTER.get())
        .orElse(null);
  }

  @Override
  public void preRemoveSideEffects(BlockPos pos, BlockState state) {
    super.preRemoveSideEffects(pos, state);
    if (this.emitter != null) {
      this.emitter.clearTracks(this.getBlockPos());
    }

    if (this.level instanceof ServerLevel serverLevel) {
      var block = (ForceTrackBlock) state.getBlock();
      if (block.getTrackType().isElectric()) {
        block.deregisterNode(serverLevel, pos);
      }
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.storeNullable(CompoundTagKeys.EMITTER_POS, BlockPos.CODEC, this.emitterPos);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    this.emitterPos = tag.read(CompoundTagKeys.EMITTER_POS, BlockPos.CODEC).orElse(null);
  }
}
