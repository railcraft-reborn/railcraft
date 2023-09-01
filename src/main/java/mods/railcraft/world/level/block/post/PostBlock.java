package mods.railcraft.world.level.block.post;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.ToIntFunction;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.VoxelShapeUtil;
import mods.railcraft.world.level.block.signal.SignalBlock;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PostBlock extends Block implements SimpleWaterloggedBlock {

  public static final VoxelShape TOP_COLUMN_SHAPE =
      box(6.0D, 6.0D, 6.0D, 10.0D, 16.0D, 10.0D);
  public static final VoxelShape MIDDLE_COLUMN_SHAPE =
      box(6.0D, 9.0D, 6.0D, 10.0D, 13.0D, 10.0D);
  public static final VoxelShape FULL_COLUMN_SHAPE =
      box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
  public static final VoxelShape PLATFORM_SHAPE =
      Shapes.or(
          box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D),
          box(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D));

  public static final Map<Direction, VoxelShape> HORIZONTAL_CONNECTION_SHAPES =
      Map.copyOf(VoxelShapeUtil.createHorizontalShapes(7.0D, 7.0D, 7.0D, 9.0D, 15.0D, 9.0D));

  public static final EnumProperty<Column> COLUMN =
      EnumProperty.create("column", Column.class);
  public static final EnumProperty<Connection> NORTH =
      EnumProperty.create("north", Connection.class);
  public static final EnumProperty<Connection> SOUTH =
      EnumProperty.create("south", Connection.class);
  public static final EnumProperty<Connection> EAST =
      EnumProperty.create("east", Connection.class);
  public static final EnumProperty<Connection> WEST =
      EnumProperty.create("west", Connection.class);
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public static final Map<Direction, EnumProperty<Connection>> propertyByDirection =
      Util.make(new EnumMap<>(Direction.class), (map) -> {
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.EAST, EAST);
        map.put(Direction.SOUTH, SOUTH);
        map.put(Direction.WEST, WEST);
      });

  private final Map<Column, VoxelShape[]> shapes = Map.of(
      Column.FULL, VoxelShapeUtil.makeShapes(FULL_COLUMN_SHAPE, HORIZONTAL_CONNECTION_SHAPES),
      Column.TOP, VoxelShapeUtil.makeShapes(TOP_COLUMN_SHAPE, HORIZONTAL_CONNECTION_SHAPES),
      Column.SMALL, VoxelShapeUtil.makeShapes(MIDDLE_COLUMN_SHAPE, HORIZONTAL_CONNECTION_SHAPES),
      Column.PLATFORM, VoxelShapeUtil.makeShapes(PLATFORM_SHAPE, HORIZONTAL_CONNECTION_SHAPES));

  private final Object2IntMap<BlockState> stateToIndex = new Object2IntOpenHashMap<>();

  public PostBlock(Properties properties) {
    super(properties);
    properties.strength(3, 15);


    for (BlockState blockstate : this.stateDefinition.getPossibleStates()) {
      this.getShapeIndex(blockstate);
    }

    this.registerDefaultState(this.stateDefinition.any()
        .setValue(COLUMN, Column.FULL)
        .setValue(NORTH, Connection.NONE)
        .setValue(SOUTH, Connection.NONE)
        .setValue(EAST, Connection.NONE)
        .setValue(WEST, Connection.NONE)
        .setValue(WATERLOGGED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(COLUMN, NORTH, SOUTH, EAST, WEST, WATERLOGGED);
  }

  @Override
  public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos,
      CollisionContext context) {
    return this.shapes.get(blockState.getValue(COLUMN))[this.getShapeIndex(blockState)];
  }

  @Override
  public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter level,
      BlockPos blockPos) {
    // Allow anything to be placed on us.
    return Shapes.block();
  }

  public final int getShapeIndex(BlockState blockState) {
    return this.stateToIndex.computeIfAbsent(blockState,
        (ToIntFunction<BlockState>) this::computeShapeIndex);
  }

  protected int computeShapeIndex(BlockState blockState) {
    int i = 0;
    for (var entry : propertyByDirection.entrySet()) {
      if (blockState.getValue(entry.getValue()) != Connection.NONE) {
        i |= VoxelShapeUtil.indexFor(entry.getKey());
      }
    }

    switch (blockState.getValue(COLUMN)) {
      case FULL:
        i |= VoxelShapeUtil.indexFor(Direction.DOWN);
      case TOP:
        i |= VoxelShapeUtil.indexFor(Direction.UP);
        break;
      default:
        break;
    }

    return i;
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (level.isClientSide()) {
      ItemStack itemStack = player.getItemInHand(hand);
      return itemStack.getItem() == Items.LEAD ? InteractionResult.SUCCESS : InteractionResult.PASS;
    } else {
      return LeadItem.bindPlayerMobs(player, level, pos);
    }
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    Level level = context.getLevel();
    BlockPos blockPos = context.getClickedPos();
    FluidState fluidState = level.getFluidState(context.getClickedPos());
    BlockPos northPos = blockPos.north();
    BlockPos eastPos = blockPos.east();
    BlockPos southPos = blockPos.south();
    BlockPos westPos = blockPos.west();
    BlockState northState = level.getBlockState(northPos);
    BlockState eastState = level.getBlockState(eastPos);
    BlockState southState = level.getBlockState(southPos);
    BlockState westState = level.getBlockState(westPos);
    return super.getStateForPlacement(context)
        .setValue(COLUMN, this.getColumn(level, blockPos))
        .setValue(NORTH, this.getConnection(northState,
            northState.isFaceSturdy(level, northPos, Direction.SOUTH), Direction.SOUTH))
        .setValue(EAST, this.getConnection(eastState,
            eastState.isFaceSturdy(level, eastPos, Direction.WEST), Direction.WEST))
        .setValue(SOUTH, this.getConnection(southState,
            southState.isFaceSturdy(level, southPos, Direction.NORTH), Direction.NORTH))
        .setValue(WEST, this.getConnection(westState,
            westState.isFaceSturdy(level, westPos, Direction.EAST), Direction.EAST))
        .setValue(WATERLOGGED, fluidState.getType().isSame(Fluids.WATER));
  }


  @Override
  public BlockState updateShape(BlockState blockState, Direction direction,
      BlockState neighborState, LevelAccessor level, BlockPos blockPos, BlockPos neighborPos) {
    if (blockState.getValue(WATERLOGGED)) {
      level.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
    }

    return direction.getAxis().getPlane() == Direction.Plane.HORIZONTAL
        ? blockState.setValue(propertyByDirection.get(direction),
            this.getConnection(neighborState,
                neighborState.isFaceSturdy(level, neighborPos, direction.getOpposite()),
                direction.getOpposite()))
        : blockState.setValue(COLUMN, this.getColumn(level, blockPos));
  }

  public Connection getConnection(BlockState blockState, boolean sturdy, Direction direction) {
    if (blockState.is(RailcraftTags.Blocks.SIGNAL)) {
      return SignalBlock.connectsToDirection(blockState, direction.getOpposite())
          ? Connection.DOUBLE
          : Connection.NONE;
    }

    if (blockState.is(RailcraftTags.Blocks.POST)
        || !isExceptionForConnection(blockState) && sturdy) {
      return Connection.DOUBLE;
    }

    return Connection.NONE;
  }

  public Column getColumn(BlockGetter level, BlockPos blockPos) {
    BlockPos abovePos = blockPos.above();
    BlockState aboveState = level.getBlockState(abovePos);
    BlockPos belowPos = blockPos.below();
    BlockState belowState = level.getBlockState(belowPos);

    if (aboveState.is(BlockTags.RAILS)) {
      return Column.PLATFORM;
    }

    if (belowState.is(RailcraftTags.Blocks.POST)
        || belowState.is(RailcraftTags.Blocks.SIGNAL)
        || belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
      return Column.FULL;
    }

    if (!aboveState.isAir()) {
      return Column.TOP;
    }

    return Column.SMALL;
  }

  @Override
  public boolean propagatesSkylightDown(BlockState blockState,
      BlockGetter level, BlockPos pos) {
    return !blockState.getValue(WATERLOGGED);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState blockState) {
    return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false)
        : super.getFluidState(blockState);
  }


  @Override
  public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos,
      PathComputationType pathType) {
    return false;
  }

  @Override
  public BlockState rotate(BlockState blockState, Rotation rotation) {
    switch (rotation) {
      case CLOCKWISE_180:
        return blockState.setValue(NORTH, blockState.getValue(SOUTH))
            .setValue(EAST, blockState.getValue(WEST)).setValue(SOUTH, blockState.getValue(NORTH))
            .setValue(WEST, blockState.getValue(EAST));
      case COUNTERCLOCKWISE_90:
        return blockState.setValue(NORTH, blockState.getValue(EAST))
            .setValue(EAST, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(WEST))
            .setValue(WEST, blockState.getValue(NORTH));
      case CLOCKWISE_90:
        return blockState.setValue(NORTH, blockState.getValue(WEST))
            .setValue(EAST, blockState.getValue(NORTH)).setValue(SOUTH, blockState.getValue(EAST))
            .setValue(WEST, blockState.getValue(SOUTH));
      default:
        return blockState;
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState blockState, Mirror mirror) {
    switch (mirror) {
      case LEFT_RIGHT:
        return blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(SOUTH,
            blockState.getValue(NORTH));
      case FRONT_BACK:
        return blockState.setValue(EAST, blockState.getValue(WEST)).setValue(WEST,
            blockState.getValue(EAST));
      default:
        return super.mirror(blockState, mirror);
    }
  }
}
