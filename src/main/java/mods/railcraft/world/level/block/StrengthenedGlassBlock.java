package mods.railcraft.world.level.block;

import org.jspecify.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.state.BlockState;

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

  @Override
  protected BlockState updateShape(BlockState blockState, LevelReader level,
      ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction,
      BlockPos neighborPos, BlockState neighborState, RandomSource random) {
    if (direction.getAxis() == Direction.Axis.Y) {
      blockState = blockState.setValue(TYPE, Type.determine(blockPos, level, this));
    }
    return super.updateShape(blockState, level, scheduledTickAccess, blockPos, direction,
        neighborPos, neighborState, random);
  }
}
