package mods.railcraft.world.level.block.signal;

import mods.railcraft.world.level.block.entity.signal.DistantSignalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DistantSignalBlock extends SingleSignalBlock {

  public DistantSignalBlock(Properties properties) {
    super(properties);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new DistantSignalBlockEntity(blockPos, blockState);
  }
}
