package mods.railcraft.world.level.block.signal;

import mods.railcraft.world.level.block.entity.signal.DualDistantSignalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DualDistantSignalBlock extends DualSignalBlock {

  public DualDistantSignalBlock(Properties properties) {
    super(properties);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new DualDistantSignalBlockEntity(blockPos, blockState);
  }
}
