package mods.railcraft.world.level.block.signal;

import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.DualDistantSignalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class DualDistantSignalBlock extends DualSignalBlock {

  public DualDistantSignalBlock(Properties properties) {
    super(properties);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState,
      boolean moved) {
    if (!blockState.is(newState.getBlock())) {
      level.getBlockEntity(blockPos, RailcraftBlockEntityTypes.DUAL_DISTANT_SIGNAL.get())
          .ifPresent(DualDistantSignalBlockEntity::blockRemoved);
    }
    super.onRemove(blockState, level, blockPos, newState, moved);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new DualDistantSignalBlockEntity(blockPos, blockState);
  }
}
