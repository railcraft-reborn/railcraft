package mods.railcraft.world.level.block.entity.signal;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

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

  @Override
  public void setCustomName(@Nullable Component name) {
    super.setCustomName(name);
  }

  public void neighborChanged() {}

  public abstract SignalAspect getSignalAspect(Direction direction);

  public void blockRemoved() {
    this.updateNeighborSignalBoxes(true);
  }

  @Override
  public void setChanged() {
    super.setChanged();
    if (this.level == null) {
      return;
    }
    this.level.blockEntityChanged(this.getBlockPos());
    this.level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
    this.updateNeighborSignalBoxes(false);
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
}
