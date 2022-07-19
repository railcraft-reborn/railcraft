package mods.railcraft.world.level.block.signal;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.ToIntFunction;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.util.VoxelShapeUtil;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
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

  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final BooleanProperty NORTH = PipeBlock.NORTH;
  public static final BooleanProperty EAST = PipeBlock.EAST;
  public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
  public static final BooleanProperty WEST = PipeBlock.WEST;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public static final Map<Direction, BooleanProperty> propertyByDirection =
      Util.make(new EnumMap<>(Direction.class), (map) -> {
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.EAST, EAST);
        map.put(Direction.SOUTH, SOUTH);
        map.put(Direction.WEST, WEST);
      });

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
    return this.stateToIndex.computeIfAbsent(blockState,
        (ToIntFunction<BlockState>) this::computeShapeIndex);
  }

  protected int computeShapeIndex(BlockState blockState) {
    int i = 0;
    for (Map.Entry<Direction, BooleanProperty> entry : propertyByDirection.entrySet()) {
      if (blockState.getValue(entry.getValue())) {
        i |= VoxelShapeUtil.indexFor(entry.getKey());
      }
    }
    return i;
  }

  @Override
  public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter,
      BlockPos blockPos) {
    return !blockState.getValue(WATERLOGGED);
  }

  @SuppressWarnings("deprecation")
  @Override
  public int getLightBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
    return LevelUtil.getBlockEntity(blockGetter, blockPos, AbstractSignalBlockEntity.class)
        .map(AbstractSignalBlockEntity::getLightValue)
        .orElseGet(() -> super.getLightBlock(blockState, blockGetter, blockPos));
  }

  @Override
  public BlockState rotate(BlockState blockState, Rotation rotation) {
    return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
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
  public BlockState updateShape(BlockState state, Direction direction,
      BlockState newState, LevelAccessor world, BlockPos pos, BlockPos newPos) {
    if (state.getValue(WATERLOGGED)) {
      world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }

    return direction.getAxis().isHorizontal()
        ? state.setValue(propertyByDirection.get(direction), this.connectsTo(newState,
            newState.isFaceSturdy(world, newPos, direction.getOpposite()), direction,
            state.getValue(FACING)))
        : state;
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
