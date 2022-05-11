package mods.railcraft.world.level.block.tank;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

public abstract class TankValveBlock extends BaseTankBlock {

  public static final Property<Direction.Axis> AXIS = BlockStateProperties.AXIS;

  protected TankValveBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(AXIS);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(AXIS, determineAxis(context.getLevel(),
        context.getClickedPos(), context.getClickedFace().getAxis()));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos,
      Block neighborBlock, BlockPos neighborPos, boolean moved) {
    super.neighborChanged(blockState, level, blockPos, neighborBlock, neighborPos, moved);
    var currentAxis = blockState.getValue(AXIS);
    var axis = determineAxis(level, blockPos, currentAxis);
    if (axis != currentAxis) {
      level.setBlockAndUpdate(blockPos, blockState.setValue(AXIS, axis));
    }
  }

  private static Direction.Axis determineAxis(Level level, BlockPos blockPos,
      Direction.Axis fallback) {
    for (var direction : Direction.values()) {
      if (level.getBlockState(blockPos.relative(direction)).isAir()) {
        return direction.getAxis();
      }
    }
    return fallback;
  }
}
