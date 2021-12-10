package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public abstract class AbstractSignalBoxBlockEntity extends RailcraftBlockEntity {

  public AbstractSignalBoxBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState) {
    super(type, blockPos, blockState);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    this.updateNeighborSignalBoxes(false);
  }

  public void neighborChanged() {}

  public abstract SignalAspect getSignalAspect(Direction direction);

  @Override
  public void setChanged() {
    super.setChanged();
    this.updateNeighborSignalBoxes(false);
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    this.updateNeighborSignalBoxes(true);
  }

  protected void neighborSignalBoxChanged(AbstractSignalBoxBlockEntity neighborSignalBox,
      Direction neighborDirection, boolean removed) {}

  public final void updateNeighborSignalBoxes(boolean removed) {
    for (var direction : Direction.Plane.HORIZONTAL) {
      var blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(direction));
      if (blockEntity instanceof AbstractSignalBoxBlockEntity box) {
        box.neighborSignalBoxChanged(this, direction, removed);
      }
    }
  }

  public int getRedstoneSignal(Direction direction) {
    return 0;
  }

  @Override
  public AABB getRenderBoundingBox() {
    return BlockEntity.INFINITE_EXTENT_AABB;
  }
}
