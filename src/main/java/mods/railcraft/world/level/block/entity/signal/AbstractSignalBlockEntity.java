package mods.railcraft.world.level.block.entity.signal;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public abstract class AbstractSignalBlockEntity extends RailcraftBlockEntity {

  public AbstractSignalBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState) {
    super(type, blockPos, blockState);
  }

  public int getLightValue() {
    return this.getPrimarySignalAspect().getBlockLight();
  }

  public abstract SignalAspect getPrimarySignalAspect();

  @Override
  public void setCustomName(@Nullable Component name) {
    super.setCustomName(name);
  }

  @Override
  public AABB getRenderBoundingBox() {
    return new AABB(this.blockPos().offset(-1, 0, -1), this.blockPos().offset(2, 2, 2));
  }
}
