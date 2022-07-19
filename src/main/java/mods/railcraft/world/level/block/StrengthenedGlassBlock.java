package mods.railcraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class StrengthenedGlassBlock extends AbstractStrengthenedGlassBlock {

  public StrengthenedGlassBlock(Properties properties) {
    super(properties);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    var blockPos = context.getClickedPos();
    var level = context.getLevel();
    return this.defaultBlockState().setValue(TYPE, Type.determine(blockPos, level, this));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos,
      Block neighborBlock, BlockPos neighborPos, boolean moved) {
    super.neighborChanged(blockState, level, blockPos, neighborBlock, neighborPos, moved);
    if (neighborPos.getX() != blockPos.getX() || neighborPos.getZ() != blockPos.getZ()) {
      return;
    }

    var type = Type.determine(blockPos, level, this);
    if (type != blockState.getValue(TYPE)) {
      level.setBlockAndUpdate(blockPos, blockState.setValue(TYPE, type));
    }
  }
}
