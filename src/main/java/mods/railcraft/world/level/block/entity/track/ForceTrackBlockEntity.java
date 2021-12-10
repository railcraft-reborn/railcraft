package mods.railcraft.world.level.block.entity.track;

import javax.annotation.Nullable;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.entity.ForceTrackEmitterBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created by CovertJaguar on 8/15/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public final class ForceTrackBlockEntity extends RailcraftBlockEntity {

  @Nullable
  private ForceTrackEmitterBlockEntity emitter;

  public ForceTrackBlockEntity(BlockPos blockPos, BlockState blockState) {
    this(RailcraftBlockEntityTypes.FORCE_TRACK.get(), blockPos, blockState);
  }

  public ForceTrackBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    super(type, blockPos, blockState);
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    if (this.emitter != null) {
      this.emitter.clearTracks(this.getBlockPos());
    }
  }

  public void neighborChanged() {
    if (this.emitter != null) {
      this.emitter.notifyTrackChange();
    }
  }

  public void setEmitter(@Nullable ForceTrackEmitterBlockEntity emitter) {
    this.emitter = emitter;
    if (emitter != null) {
      this.setOwner(emitter.getOwner().orElse(null));
      this.level.setBlockAndUpdate(this.getBlockPos(),
          this.getBlockState().setValue(ForceTrackBlock.COLOR,
              emitter.getBlockState().getValue(ForceTrackEmitterBlock.COLOR)));
    }
  }
}
