package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.RailcraftTickableBlockEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;

public abstract class AbstractSignalBoxBlockEntity extends RailcraftTickableBlockEntity {

  public AbstractSignalBoxBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  public void neighborChanged() {}

  public abstract SignalAspect getSignalAspect(Direction direction);

  public void neighboringSignalBoxChanged(AbstractSignalBoxBlockEntity signalBox,
      Direction direction) {}

  public final void updateNeighborBoxes() {
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      TileEntity blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(direction));
      if (blockEntity instanceof AbstractSignalBoxBlockEntity) {
        AbstractSignalBoxBlockEntity box = (AbstractSignalBoxBlockEntity) blockEntity;
        box.neighboringSignalBoxChanged(this, direction);
      }
    }
  }

  public int getRedstoneSignal(Direction direction) {
    return 0;
  }

  @Override
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }
}
