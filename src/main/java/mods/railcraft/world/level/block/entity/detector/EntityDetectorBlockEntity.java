package mods.railcraft.world.level.block.entity.detector;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;

public abstract class EntityDetectorBlockEntity<T extends Entity> extends DetectorBlockEntity {

  private final Class<T> entityClass;

  protected EntityDetectorBlockEntity(BlockEntityType<?> type,
      BlockPos blockPos, BlockState blockState, Class<T> entityClass) {
    super(type, blockPos, blockState);
    this.entityClass = entityClass;
  }

  @Override
  protected int testCarts(List<AbstractMinecart> minecarts) {
    for (var cart : minecarts) {
      if (cart.getPassengers().stream().anyMatch(entityClass::isInstance)) {
        return Redstone.SIGNAL_MAX;
      }
    }
    return Redstone.SIGNAL_NONE;
  }
}
