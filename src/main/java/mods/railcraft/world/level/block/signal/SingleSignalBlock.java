package mods.railcraft.world.level.block.signal;

import java.util.EnumMap;
import java.util.Map;
import mods.railcraft.util.VoxelShapeUtil;
import mods.railcraft.world.level.block.post.PostBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * 
 * @author Sm0keySa1m0n
 *
 */
public abstract class SingleSignalBlock extends SignalBlock {

  public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

  public static final VoxelShape SHAPE = box(3.0D, 6.0D, 3.0D, 13.0D, 16.0D, 13.0D);
  public static final VoxelShape POST_SHAPE =
      box(8.0D - 2.0D, 0.0D, 8.0D - 2.0D, 8.0D + 2.0D, 6.0D, 8.0D + 2.0D);

  public SingleSignalBlock(Properties properties) {
    super(SHAPE, createConnectionShapes(), properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(NORTH, false)
        .setValue(EAST, false)
        .setValue(SOUTH, false)
        .setValue(WEST, false)
        .setValue(DOWN, false)
        .setValue(FACING, Direction.NORTH)
        .setValue(WATERLOGGED, false));
  }

  private static Map<Direction, VoxelShape> createConnectionShapes() {
    Map<Direction, VoxelShape> connectionShapes = new EnumMap<>(Direction.class);
    connectionShapes.putAll(PostBlock.HORIZONTAL_CONNECTION_SHAPES);
    connectionShapes.put(Direction.DOWN, POST_SHAPE);
    return connectionShapes;
  }

  @Override
  protected int computeShapeIndex(BlockState blockState) {
    int i = super.computeShapeIndex(blockState);
    if (blockState.getValue(DOWN)) {
      i |= VoxelShapeUtil.indexFor(Direction.DOWN);
    }
    return i;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(DOWN);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockGetter level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    Direction facing = context.getHorizontalDirection().getOpposite();
    BlockPos downPos = pos.below();
    BlockState downState = level.getBlockState(downPos);
    return super.getStateForPlacement(context)
        .setValue(DOWN, this.connectsTo(downState,
            downState.isFaceSturdy(level, downPos, Direction.UP), Direction.DOWN, facing));
  }

  @Override
  public BlockState updateShape(BlockState state, Direction direction,
      BlockState newState, LevelAccessor world, BlockPos pos, BlockPos newPos) {
    state = super.updateShape(state, direction, newState, world, pos, newPos);
    return direction == Direction.DOWN
        ? state.setValue(DOWN, this.connectsTo(newState,
            newState.isFaceSturdy(world, newPos, direction.getOpposite()), direction,
            state.getValue(FACING)))
        : state;
  }
}
