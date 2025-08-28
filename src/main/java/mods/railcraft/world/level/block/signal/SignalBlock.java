package mods.railcraft.world.level.block.signal;

import java.util.Map;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.VoxelShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 *
 * @author Sm0keySa1m0n
 *
 */
public abstract class SignalBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

  public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final BooleanProperty NORTH = PipeBlock.NORTH;
  public static final BooleanProperty EAST = PipeBlock.EAST;
  public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
  public static final BooleanProperty WEST = PipeBlock.WEST;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public static final Map<Direction, BooleanProperty> propertyByDirection = Map.of(
      Direction.NORTH, NORTH,
      Direction.EAST, EAST,
      Direction.SOUTH, SOUTH,
      Direction.WEST, WEST);

  private final VoxelShape[] shapes;
  protected final Object2IntMap<BlockState> stateToIndex = new Object2IntOpenHashMap<>();

  protected SignalBlock(VoxelShape shape, Map<Direction, VoxelShape> connectionShapes,
      Properties properties) {
    super(properties);
    this.shapes = VoxelShapeUtil.makeShapes(shape, connectionShapes);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(NORTH, false)
        .setValue(EAST, false)
        .setValue(SOUTH, false)
        .setValue(WEST, false)
        .setValue(FACING, Direction.NORTH)
        .setValue(WATERLOGGED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(NORTH, EAST, WEST, SOUTH, FACING, WATERLOGGED);
  }

  @Override
  public RenderShape getRenderShape(BlockState blockState) {
    return RenderShape.MODEL;
  }

  @Override
  public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos,
      CollisionContext context) {
    return this.shapes[this.getShapeIndex(blockState)];
  }

  public final int getShapeIndex(BlockState blockState) {
    return this.stateToIndex.computeIfAbsent(blockState, this::computeShapeIndex);
  }

  protected int computeShapeIndex(BlockState blockState) {
    int i = 0;
    for (var entry : propertyByDirection.entrySet()) {
      if (blockState.getValue(entry.getValue())) {
        i |= VoxelShapeUtil.indexFor(entry.getKey());
      }
    }
    return i;
  }

  @Override
  public boolean propagatesSkylightDown(BlockState blockState) {
    return !blockState.getValue(WATERLOGGED);
  }

  @Override
  protected int getLightBlock(BlockState state) {
    return 0;
    /*return LevelUtil.getBlockEntity(state, state, AbstractSignalBlockEntity.class)
        .map(AbstractSignalBlockEntity::getLightValue)
        .orElseGet(() -> super.getLightBlock(state));*/
  }

  @Override
  public BlockState rotate(BlockState blockState, Rotation rotation) {
    return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  protected BlockState mirror(BlockState state, Mirror mirror) {
    return state.rotate(mirror.getRotation(state.getValue(FACING)));
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockGetter level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    FluidState fluidState = level.getFluidState(pos);
    Direction facing = context.getHorizontalDirection().getOpposite();
    BlockPos northPos = pos.north();
    BlockPos southPos = pos.south();
    BlockPos westPos = pos.west();
    BlockPos eastPos = pos.east();
    BlockState northState = level.getBlockState(northPos);
    BlockState southState = level.getBlockState(southPos);
    BlockState westState = level.getBlockState(westPos);
    BlockState eastState = level.getBlockState(eastPos);
    return this.defaultBlockState()
        .setValue(NORTH, this.connectsTo(northState,
            northState.isFaceSturdy(level, northPos, Direction.SOUTH), Direction.NORTH, facing))
        .setValue(SOUTH, this.connectsTo(southState,
            southState.isFaceSturdy(level, southPos, Direction.NORTH), Direction.SOUTH, facing))
        .setValue(WEST, this.connectsTo(westState,
            westState.isFaceSturdy(level, westPos, Direction.EAST), Direction.WEST, facing))
        .setValue(EAST, this.connectsTo(eastState,
            eastState.isFaceSturdy(level, eastPos, Direction.WEST), Direction.EAST, facing))
        .setValue(FACING, context.getHorizontalDirection().getOpposite())
        .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
  }

  @Override
  protected BlockState updateShape(BlockState blockState, LevelReader levelReader,
      ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos neighborPos,
      BlockState neighborState, RandomSource randomSource) {
    if (blockState.getValue(WATERLOGGED)) {
      scheduledTickAccess.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelReader));
    }

    return direction.getAxis().isHorizontal()
        ? blockState.setValue(propertyByDirection.get(direction), this.connectsTo(neighborState,
        neighborState.isFaceSturdy(levelReader, neighborPos, direction.getOpposite()), direction,
            blockState.getValue(FACING)))
        : blockState;
  }

  public boolean connectsTo(BlockState blockState, boolean faceStudry, Direction direction,
      Direction facing) {
    if (facing == direction) {
      return false;
    }

    if (blockState.is(RailcraftTags.Blocks.SIGNAL)) {
      return connectsToDirection(blockState, direction);
    }

    if (blockState.is(RailcraftTags.Blocks.POST)) {
      return true;
    }

    if (isExceptionForConnection(blockState) || !faceStudry) {
      return false;
    }

    if (blockState.is(BlockTags.FENCES) || blockState.is(BlockTags.WALLS)) {
      return direction.getAxis().getPlane() != Direction.Plane.HORIZONTAL;
    }

    return true;
  }

  public static boolean connectsToDirection(BlockState blockState, Direction direction) {
    return direction.getAxis().isVertical()
        || direction.getOpposite() != blockState.getValue(FACING);
  }
}
