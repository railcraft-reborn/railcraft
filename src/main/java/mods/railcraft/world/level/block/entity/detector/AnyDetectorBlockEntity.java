package mods.railcraft.world.level.block.entity.detector;

import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AnyDetectorBlockEntity extends DetectorBlockEntity {

  public AnyDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.ANY_DETECTOR.get(), blockPos, blockState);
  }
}
