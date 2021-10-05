package mods.railcraft.world.level.block.post;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.VoxelShapeUtil;
import mods.railcraft.world.level.block.signal.AbstractSignalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LeadItem;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class PostBlock extends Block implements IWaterLoggable {

  public static final VoxelShape TOP_COLUMN_SHAPE =
      box(6.0D, 6.0D, 6.0D, 10.0D, 16.0D, 10.0D);
  public static final VoxelShape MIDDLE_COLUMN_SHAPE =
      box(6.0D, 9.0D, 6.0D, 10.0D, 13.0D, 10.0D);
  public static final VoxelShape FULL_COLUMN_SHAPE =
      box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
  public static final VoxelShape PLATFORM_SHAPE =
      VoxelShapes.or(
          box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D),
          box(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D));

  public static final Map<Direction, VoxelShape> HORIZONTAL_CONNECTION_SHAPES =
      Collections.unmodifiableMap(
          VoxelShapeUtil.createHorizontalShapes(7.0D, 7.0D, 7.0D, 9.0D, 15.0D, 9.0D));

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

  private final Map<Column, VoxelShape[]> shapes =
      Util.make(new EnumMap<>(Column.class), map -> {
        map.put(Column.FULL,
            VoxelShapeUtil.makeShapes(FULL_COLUMN_SHAPE, HORIZONTAL_CONNECTION_SHAPES));
        map.put(Column.TOP,
            VoxelShapeUtil.makeShapes(TOP_COLUMN_SHAPE, HORIZONTAL_CONNECTION_SHAPES));
        map.put(Column.SMALL,
            VoxelShapeUtil.makeShapes(MIDDLE_COLUMN_SHAPE, HORIZONTAL_CONNECTION_SHAPES));
        map.put(Column.PLATFORM,
            VoxelShapeUtil.makeShapes(PLATFORM_SHAPE, HORIZONTAL_CONNECTION_SHAPES));
      });

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
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(COLUMN, NORTH, SOUTH, EAST, WEST, WATERLOGGED);
  }

  @Override
  public VoxelShape getShape(BlockState blockState, IBlockReader level, BlockPos blockPos,
      ISelectionContext context) {
    return this.shapes.get(blockState.getValue(COLUMN))[this.getShapeIndex(blockState)];
  }

  @Override
  public VoxelShape getBlockSupportShape(BlockState blockState, IBlockReader level,
      BlockPos blockPos) {
    // Allow anything to be placed on us.
    return VoxelShapes.block();
  }

  public final int getShapeIndex(BlockState blockState) {
    return this.stateToIndex.computeIntIfAbsent(blockState, this::computeShapeIndex);
  }

  protected int computeShapeIndex(BlockState blockState) {
    int i = 0;
    for (Map.Entry<Direction, EnumProperty<Connection>> entry : propertyByDirection.entrySet()) {
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
  public ActionResultType use(BlockState blockState, World level, BlockPos pos,
      PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    if (level.isClientSide()) {
      ItemStack itemStack = player.getItemInHand(hand);
      return itemStack.getItem() == Items.LEAD ? ActionResultType.SUCCESS : ActionResultType.PASS;
    } else {
      return LeadItem.bindPlayerMobs(player, level, pos);
    }
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    IBlockReader level = context.getLevel();
    BlockPos blockPos = context.getClickedPos();
    FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
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
        .setValue(WATERLOGGED, Boolean.valueOf(fluidState.getType() == Fluids.WATER));
  }


  @Override
  public BlockState updateShape(BlockState blockState, Direction direction,
      BlockState neighborState, IWorld level, BlockPos blockPos, BlockPos neighborPos) {
    if (blockState.getValue(WATERLOGGED)) {
      level.getLiquidTicks().scheduleTick(blockPos, Fluids.WATER,
          Fluids.WATER.getTickDelay(level));
    }

    return direction.getAxis().getPlane() == Direction.Plane.HORIZONTAL
        ? blockState.setValue(propertyByDirection.get(direction),
            this.getConnection(neighborState,
                neighborState.isFaceSturdy(level, neighborPos, direction.getOpposite()),
                direction.getOpposite()))
        : blockState.setValue(COLUMN, this.getColumn(level, blockPos));
  }

  public Connection getConnection(BlockState blockState, boolean sturdy, Direction direction) {
    Block block = blockState.getBlock();

    if (blockState.is(RailcraftTags.Blocks.SIGNAL)) {
      return AbstractSignalBlock.connectsToDirection(blockState, direction.getOpposite())
          ? Connection.DOUBLE
          : Connection.NONE;
    }

    if (block.is(RailcraftTags.Blocks.POST) || !isExceptionForConnection(block) && sturdy) {
      return Connection.DOUBLE;
    }

    return Connection.NONE;
  }

  @SuppressWarnings("deprecation")
  public Column getColumn(IBlockReader level, BlockPos blockPos) {
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

    if (!aboveState.isAir(level, abovePos)) {
      return Column.TOP;
    }

    return Column.SMALL;
  }

  public boolean isPlatform(BlockState state) {
    return false;
  }

  @Override
  public boolean propagatesSkylightDown(BlockState blockState,
      IBlockReader level, BlockPos pos) {
    return !blockState.getValue(WATERLOGGED);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState blockState) {
    return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false)
        : super.getFluidState(blockState);
  }


  @Override
  public boolean isPathfindable(BlockState state, IBlockReader level, BlockPos pos,
      PathType pathType) {
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

  @Override
  public boolean canCreatureSpawn(BlockState state, IBlockReader level, BlockPos pos,
      EntitySpawnPlacementRegistry.PlacementType placementType, EntityType<?> entityType) {
    return false;
  }
}
