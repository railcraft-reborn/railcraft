package mods.railcraft.world.level.block.entity.signal;

import javax.annotation.Nullable;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.world.level.block.entity.RailcraftTickableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractSignalBoxBlockEntity extends RailcraftTickableBlockEntity {

  public AbstractSignalBoxBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  public abstract SignalAspect getBoxSignalAspect(@Nullable Direction side);

  public void neighboringSignalBoxChanged(AbstractSignalBoxBlockEntity neighbor, Direction side) {}

  public final void updateNeighborBoxes() {
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      TileEntity blockEntity = this.adjacentCache.getTileOnSide(direction);
      if (blockEntity instanceof AbstractSignalBoxBlockEntity) {
        AbstractSignalBoxBlockEntity box = (AbstractSignalBoxBlockEntity) blockEntity;
        box.neighboringSignalBoxChanged(this, direction);
      }
    }
  }

  public boolean attachesTo(BlockState state, BlockPos pos, Direction direction) {
    if (!this.canReceiveAspect() && !this.canTransferAspect()) {
      return false;
    }
    TileEntity blockEntity = this.level.getBlockEntity(pos);
    if (blockEntity instanceof AbstractSignalBoxBlockEntity) {
      AbstractSignalBoxBlockEntity signalBox = (AbstractSignalBoxBlockEntity) blockEntity;
      if (this.canReceiveAspect() && signalBox.canTransferAspect()
          || this.canTransferAspect() && signalBox.canReceiveAspect()) {
        return true;
      }
    }
    return false;
  }

  protected boolean canReceiveAspect() {
    return false;
  }

  protected boolean canTransferAspect() {
    return false;
  }

  /**
   * This function is essentially the same as
   * {@link mods.railcraft.common.blocks.interfaces.ITileRedstoneEmitter#getPowerOutput(Direction)},
   * but that function interfaces with the world and is filtered to not emit redstone in the
   * direction of other boxes. This is done to reduce the amount of redstone pollution in the world.
   *
   * This function is unfiltered and used by other boxes to determine what its neighbors are doing.
   *
   * Some semantics changes may be in order.
   *
   * @param side the side being tested
   * @return true if "emitting" redstone
   */
  public boolean isEmittingRedstone(Direction side) {
    return false;
  }

  public boolean canConnectRedstone(@Nullable Direction dir) {
    return true;
  }

  @Override
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }
}
