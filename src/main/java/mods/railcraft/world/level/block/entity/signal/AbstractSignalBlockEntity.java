package mods.railcraft.world.level.block.entity.signal;

import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.RailcraftTickableBlockEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;

public abstract class AbstractSignalBlockEntity extends RailcraftTickableBlockEntity {

  public AbstractSignalBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  public int getLightValue() {
    return this.getPrimarySignalAspect().getBlockLight();
  }

  public abstract SignalAspect getPrimarySignalAspect();

  @Override
  public double getViewDistance() {
    return 512.0D;
  }

  @Override
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }
}
