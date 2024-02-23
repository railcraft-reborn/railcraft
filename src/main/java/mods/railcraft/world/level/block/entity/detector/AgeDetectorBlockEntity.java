package mods.railcraft.world.level.block.entity.detector;

import java.util.List;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;

public class AgeDetectorBlockEntity extends DetectorBlockEntity {

  public AgeDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.AGE_DETECTOR.get(), blockPos, blockState);
  }

  @Override
  protected int testCarts(List<AbstractMinecart> minecarts) {
    for (var cart : minecarts) {
      var passengers = cart.getPassengers();
      if (passengers.stream()
          .anyMatch(entity -> entity instanceof AgeableMob ageableMob && ageableMob.isBaby())) {
        return Redstone.SIGNAL_NONE;
      }
      if (!passengers.isEmpty()) {
        return Redstone.SIGNAL_MAX;
      }
    }
    return Redstone.SIGNAL_NONE;
  }
}
