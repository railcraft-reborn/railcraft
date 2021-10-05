package mods.railcraft.world.level.block.signal;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.util.VoxelShapeUtil;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SixWayBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

/**
 * 
 * @author Sm0keySa1m0n
 *
 */
public abstract class AbstractSignalBlock extends Block implements IWaterLoggable {

  public static final Map<Direction, VoxelShape> HORIZONTAL_CONNECTION_SHAPES =
      Maps.immutableEnumMap(VoxelShapeUtil.createHorizontalShapes(
          8.0D - 2.0D, 7.0D, 8.0D - 2.0D, 8.0D + 2.0D, 16.0D, 8.0D + 2.0D));

  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final BooleanProperty NORTH = SixWayBlock.NORTH;
  public static final BooleanProperty EAST = SixWayBlock.EAST;
  public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
  public static final BooleanProperty WEST = SixWayBlock.WEST;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public static final Map<Direction, BooleanProperty> propertyByDirection =
      Util.make(new EnumMap<>(Direction.class), (map) -> {
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.EAST, EAST);
        map.put(Direction.SOUTH, SOUTH);
        map.put(Direction.WEST, WEST);
      });

  private final Supplier<? extends AbstractSignalBlockEntity> blockEntityFactory;
  private final VoxelShape[] shapes;
  protected final Object2IntMap<BlockState> stateToIndex = new Object2IntOpenHashMap<>();

  protected AbstractSignalBlock(VoxelShape shape, Map<Direction, VoxelShape> connectionShapes,
      Supplier<? extends AbstractSignalBlockEntity> blockEntityFactory,
      Properties properties) {
    super(properties);
    this.blockEntityFactory = blockEntityFactory;
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
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(NORTH, EAST, WEST, SOUTH, FACING, WATERLOGGED);
  }

  @Override
  public VoxelShape getShape(BlockState blockState, IBlockReader level, BlockPos blockPos,
      ISelectionContext context) {
    return this.shapes[this.getShapeIndex(blockState)];
  }

  public final int getShapeIndex(BlockState blockState) {
    return this.stateToIndex.computeIntIfAbsent(blockState, this::computeShapeIndex);
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
  public boolean propagatesSkylightDown(BlockState blockState, IBlockReader level,
      BlockPos blockPos) {
    return !blockState.getValue(WATERLOGGED);
  }

  @Override
  public int getLightValue(BlockState state, IBlockReader reader, BlockPos pos) {
    return LevelUtil.getBlockEntity(reader, pos, AbstractSignalBlockEntity.class)
        .map(AbstractSignalBlockEntity::getLightValue)
        .orElseGet(() -> super.getLightValue(state, reader, pos));
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return state.rotate(mirror.getRotation(state.getValue(FACING)));
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    IBlockReader level = context.getLevel();
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
      BlockState newState, IWorld world, BlockPos pos, BlockPos newPos) {
    if (state.getValue(WATERLOGGED)) {
      world.getLiquidTicks().scheduleTick(pos, Fluids.WATER,
          Fluids.WATER.getTickDelay(world));
    }

    return direction.getAxis().isHorizontal()
        ? state.setValue(propertyByDirection.get(direction), this.connectsTo(newState,
            newState.isFaceSturdy(world, newPos, direction.getOpposite()), direction,
            state.getValue(FACING)))
        : state;
  }

  public boolean connectsTo(BlockState state, boolean faceStudry, Direction direction,
      Direction facing) {
    Block block = state.getBlock();

    if (facing == direction) {
      return false;
    }

    if (block instanceof AbstractSignalBlock) {
      return connectsToDirection(state, direction);
    }

    if (block.is(RailcraftTags.Blocks.POST)) {
      return true;
    }

    if (isExceptionForConnection(block) || !faceStudry) {
      return false;
    }

    if (state.is(BlockTags.FENCES) || state.is(BlockTags.WALLS)) {
      return direction.getAxis().getPlane() != Direction.Plane.HORIZONTAL;
    }

    return true;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader reader) {
    return this.blockEntityFactory.get();
  }

  public static boolean connectsToDirection(BlockState blockState, Direction direction) {
    return direction.getAxis().isVertical()
        || direction.getOpposite() != blockState.getValue(FACING);
  }
}
