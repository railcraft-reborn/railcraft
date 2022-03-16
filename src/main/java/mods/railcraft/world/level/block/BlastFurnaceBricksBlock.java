package mods.railcraft.world.level.block;

import javax.annotation.Nullable;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.multiblock.BlastFurnaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlastFurnaceBricksBlock extends FurnaceMultiblockBlock {

  public BlastFurnaceBricksBlock(Properties properties) {
    super(properties);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState blockState, Level level, BlockPos blockPos,
      BlockState newBlockState, boolean moved) {
    if (!blockState.is(newBlockState.getBlock())) {
      level.getBlockEntity(blockPos, RailcraftBlockEntityTypes.BLAST_FURNACE.get())
          .ifPresent(cokeOven -> Containers.dropContents(level, blockPos,
              cokeOven.getBlastFurnaceModule()));
      super.onRemove(blockState, level, blockPos, newBlockState, moved);
    }
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new BlastFurnaceBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.BLAST_FURNACE.get(),
            BlastFurnaceBlockEntity::serverTick);
  }
}
