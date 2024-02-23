package mods.railcraft.world.level.block.entity.detector;

import java.util.List;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;

public class EmptyDetectorBlockEntity extends DetectorBlockEntity {

  public EmptyDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.EMPTY_DETECTOR.get(), blockPos, blockState);
  }

  @Override
  protected int testCarts(List<AbstractMinecart> minecarts) {
    for (var cart : minecarts) {
      if (cart.canBeRidden() && cart.getPassengers().isEmpty()) {
        return Redstone.SIGNAL_MAX;
      }
    }
    return Redstone.SIGNAL_NONE;
  }
}
