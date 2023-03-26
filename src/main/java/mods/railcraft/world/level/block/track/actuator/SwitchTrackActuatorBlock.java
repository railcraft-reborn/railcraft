package mods.railcraft.world.level.block.track.actuator;

import mods.railcraft.api.track.ArrowDirection;
import mods.railcraft.util.PowerUtil;
import mods.railcraft.world.level.block.track.outfitted.SwitchTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SwitchTrackActuatorBlock extends HorizontalDirectionalBlock
    implements SimpleWaterloggedBlock {

  public static final EnumProperty<ArrowDirection> RED_ARROW_DIRECTION =
      EnumProperty.create("red_flag", ArrowDirection.class);
  public static final EnumProperty<ArrowDirection> WHITE_ARROW_DIRECTION =
      EnumProperty.create("white_flag", ArrowDirection.class);
  public static final BooleanProperty SWITCHED = BooleanProperty.create("switched");
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  private static final VoxelShape BASE_SHAPE = box(4.0D, 0.0D, 4.0D, 12.0D, 5.0D, 12.0D);
  private static final VoxelShape POST_SHAPE = box(7.0D, 5.0D, 7.0D, 9.0D, 8.0D, 9.0D);
  private static final VoxelShape NORTH_SOUTH_WINGS_SHAPE =
      box(6.0D, 0.0D, 0.0D, 10.0D, 3.0D, 16.0D);
  private static final VoxelShape EAST_WEST_WINGS_SHAPE =
      box(0.0D, 0.0D, 6.0D, 16.0D, 3.0D, 10.0D);

  private static final VoxelShape NORTH_SOUTH_SHAPE =
      Shapes.or(BASE_SHAPE, NORTH_SOUTH_WINGS_SHAPE, POST_SHAPE);
  private static final VoxelShape EAST_WEST_SHAPE =
      Shapes.or(BASE_SHAPE, EAST_WEST_WINGS_SHAPE, POST_SHAPE);


  public SwitchTrackActuatorBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(FACING, Direction.NORTH)
        .setValue(RED_ARROW_DIRECTION, ArrowDirection.NORTH_SOUTH)
        .setValue(WHITE_ARROW_DIRECTION, ArrowDirection.EAST_WEST)
        .setValue(SWITCHED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, RED_ARROW_DIRECTION, WHITE_ARROW_DIRECTION, SWITCHED, WATERLOGGED);
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
    return !state.getValue(WATERLOGGED);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    var level = context.getLevel();
    var pos = context.getClickedPos();
    var fluidState = level.getFluidState(pos);
    return this.defaultBlockState()
        .setValue(FACING, Direction.Plane.HORIZONTAL.stream()
            .filter(side -> level.getBlockState(pos.relative(side))
                .getBlock() instanceof SwitchTrackBlock)
            .findFirst()
            .orElse(context.getHorizontalDirection().getOpposite()))
        .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
  }

  @Override
  public BlockState updateShape(BlockState state, Direction direction,
      BlockState newState, LevelAccessor level, BlockPos pos, BlockPos newPos) {
    if (state.getValue(WATERLOGGED)) {
      level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
    }
    return level.getBlockState(newPos).getBlock() instanceof SwitchTrackBlock
        ? state.setValue(FACING, direction)
        : state;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
      CollisionContext context) {
    return state.getValue(FACING).getAxis() == Direction.Axis.Z
        ? NORTH_SOUTH_SHAPE
        : EAST_WEST_SHAPE;
  }

  @Override
  public boolean hasAnalogOutputSignal(BlockState state) {
    return true;
  }

  @Override
  public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
    return isSwitched(state) ? PowerUtil.FULL_POWER : PowerUtil.NO_POWER;
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    return canSupportRigidBlock(level, pos.below());
  }

  public static boolean isSwitched(BlockState blockState) {
    return blockState.getValue(SWITCHED);
  }

  public static void setSwitched(BlockState blockState, Level level, BlockPos blockPos,
      boolean switched) {
    if (blockState.getValue(SWITCHED) == switched) {
      return;
    }
    level.setBlockAndUpdate(blockPos, blockState.setValue(SWITCHED, switched));
    level.playSound(null, blockPos, switched
            ? SoundEvents.PISTON_CONTRACT
            : SoundEvents.PISTON_EXTEND,
        SoundSource.BLOCKS, 0.25F, level.getRandom().nextFloat() * 0.25F + 0.7F);
    Direction.Plane.HORIZONTAL.forEach(direction -> {
      BlockPos neighborPos = blockPos.relative(direction);
      if (level.getBlockState(neighborPos).getBlock() instanceof ComparatorBlock) {
        level.updateNeighborsAt(neighborPos, blockState.getBlock());
      }
    });
  }

  public static ArrowDirection getRedArrowDirection(BlockState blockState) {
    return blockState.getValue(RED_ARROW_DIRECTION);
  }

  public static ArrowDirection getWhiteArrowDirection(BlockState blockState) {
    return blockState.getValue(WHITE_ARROW_DIRECTION);
  }

  public static void updateArrowDirections(BlockState blockState, Level level, BlockPos blockPos,
      ArrowDirection redArrowDirection, ArrowDirection whiteArrowDirection) {
    BlockState newState = blockState;
    boolean changed = false;
    if (getRedArrowDirection(blockState) != redArrowDirection) {
      newState = newState.setValue(
          SwitchTrackActuatorBlock.RED_ARROW_DIRECTION, redArrowDirection);
      changed = true;
    }
    if (getWhiteArrowDirection(blockState) != whiteArrowDirection) {
      newState = newState.setValue(
          SwitchTrackActuatorBlock.WHITE_ARROW_DIRECTION, whiteArrowDirection);
      changed = true;
    }
    if (changed) {
      level.setBlockAndUpdate(blockPos, newState);
    }
  }
}
