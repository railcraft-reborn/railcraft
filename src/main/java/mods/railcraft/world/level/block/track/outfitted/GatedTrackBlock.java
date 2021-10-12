package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class GatedTrackBlock extends ReversiblePoweredOutfittedTrackBlock {

  public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
  public static final BooleanProperty IN_WALL = BlockStateProperties.IN_WALL;
  public static final BooleanProperty ONE_WAY = BooleanProperty.create("one_way");

  private static final double MOTION_MIN = 0.2D;

  protected static final VoxelShape Z_SHAPE = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
  protected static final VoxelShape X_SHAPE = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);
  protected static final VoxelShape Z_SHAPE_LOW = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 13.0D, 10.0D);
  protected static final VoxelShape X_SHAPE_LOW = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 13.0D, 16.0D);
  protected static final VoxelShape Z_COLLISION_SHAPE =
      box(0.0D, 0.0D, 6.0D, 16.0D, 24.0D, 10.0D);
  protected static final VoxelShape X_COLLISION_SHAPE =
      box(6.0D, 0.0D, 0.0D, 10.0D, 24.0D, 16.0D);
  protected static final VoxelShape Z_OCCLUSION_SHAPE =
      VoxelShapes.or(
          box(0.0D, 5.0D, 7.0D, 2.0D, 16.0D, 9.0D),
          box(14.0D, 5.0D, 7.0D, 16.0D, 16.0D, 9.0D));
  protected static final VoxelShape X_OCCLUSION_SHAPE =
      VoxelShapes.or(
          box(7.0D, 5.0D, 0.0D, 9.0D, 16.0D, 2.0D),
          box(7.0D, 5.0D, 14.0D, 9.0D, 16.0D, 16.0D));
  protected static final VoxelShape Z_OCCLUSION_SHAPE_LOW =
      VoxelShapes.or(
          box(0.0D, 2.0D, 7.0D, 2.0D, 13.0D, 9.0D),
          box(14.0D, 2.0D, 7.0D, 16.0D, 13.0D, 9.0D));
  protected static final VoxelShape X_OCCLUSION_SHAPE_LOW =
      VoxelShapes.or(
          box(7.0D, 2.0D, 0.0D, 9.0D, 13.0D, 2.0D),
          box(7.0D, 2.0D, 14.0D, 9.0D, 13.0D, 16.0D));

  public GatedTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH)
        .setValue(POWERED, false)
        .setValue(REVERSED, false)
        .setValue(OPEN, false)
        .setValue(IN_WALL, false)
        .setValue(ONE_WAY, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(OPEN, IN_WALL, ONE_WAY);
  }

  @Override
  public VoxelShape getShape(BlockState blockState, IBlockReader level, BlockPos pos,
      ISelectionContext context) {
    if (blockState.getValue(IN_WALL)) {
      return getRailShapeRaw(blockState) == RailShape.EAST_WEST ? X_SHAPE_LOW : Z_SHAPE_LOW;
    } else {
      return getRailShapeRaw(blockState) == RailShape.EAST_WEST ? X_SHAPE : Z_SHAPE;
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState updateShape(BlockState blockState, Direction neighborDirection,
      BlockState neighborState, IWorld level, BlockPos pos, BlockPos neighborPos) {
    if (ReversibleOutfittedTrackBlock.getFacing(blockState).getClockWise()
        .getAxis() != neighborDirection.getAxis()) {
      return super.updateShape(blockState, neighborDirection, neighborState, level, pos,
          neighborPos);
    } else {
      return blockState.setValue(IN_WALL, this.isWall(neighborState)
          || this.isWall(level.getBlockState(pos.relative(neighborDirection.getOpposite()))));
    }
  }

  @Override
  public VoxelShape getCollisionShape(BlockState blockState, IBlockReader level,
      BlockPos pos, ISelectionContext context) {
    if (blockState.getValue(OPEN)) {
      return VoxelShapes.empty();
    } else {
      return getRailShapeRaw(blockState) == RailShape.NORTH_SOUTH
          ? Z_COLLISION_SHAPE
          : X_COLLISION_SHAPE;
    }
  }

  @Override
  public VoxelShape getOcclusionShape(BlockState blockState, IBlockReader level,
      BlockPos pos) {
    if (blockState.getValue(IN_WALL)) {
      return getRailShapeRaw(blockState) == RailShape.EAST_WEST
          ? X_OCCLUSION_SHAPE_LOW
          : Z_OCCLUSION_SHAPE_LOW;
    } else {
      return getRailShapeRaw(blockState) == RailShape.EAST_WEST
          ? X_OCCLUSION_SHAPE
          : Z_OCCLUSION_SHAPE;
    }
  }

  @Override
  public boolean isPathfindable(BlockState blockState, IBlockReader level,
      BlockPos pos, PathType type) {
    switch (type) {
      case LAND:
        return blockState.getValue(OPEN);
      case WATER:
        return false;
      case AIR:
        return blockState.getValue(OPEN);
      default:
        return false;
    }
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    BlockState blockState = super.getStateForPlacement(context);
    World level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    RailShape railShape = getRailShapeRaw(blockState);
    boolean inWall = railShape == RailShape.NORTH_SOUTH
        && (this.isWall(level.getBlockState(pos.west()))
            || this.isWall(level.getBlockState(pos.east())))
        || railShape == RailShape.EAST_WEST && (this.isWall(level.getBlockState(pos.north()))
            || this.isWall(level.getBlockState(pos.south())));
    return blockState
        .setValue(OPEN, isPowered(blockState))
        .setValue(IN_WALL, inWall);
  }

  private boolean isWall(BlockState blockState) {
    return blockState.getBlock().is(BlockTags.WALLS);
  }

  @Override
  public int getPowerPropagation(BlockState blockState, World level, BlockPos pos) {
    return 0;
  }

  @Override
  public void onMinecartPass(BlockState blockState, World level, BlockPos pos,
      AbstractMinecartEntity cart) {
    if (isOneWay(blockState) && isOpen(blockState)) {
      RailShape shape = getRailShapeRaw(blockState);
      Vector3d deltaMovement = cart.getDeltaMovement();
      if (shape == RailShape.NORTH_SOUTH) {
        double motion = Math.max(Math.abs(deltaMovement.z()), MOTION_MIN);
        cart.setDeltaMovement(deltaMovement.x(), deltaMovement.y(),
            motion * (isReversed(blockState) ? 1.0D : -1.0D));
      } else {
        double motion = Math.max(Math.abs(deltaMovement.x()), MOTION_MIN);
        cart.setDeltaMovement(motion * (isReversed(blockState) ? -1.0D : 1.0D), deltaMovement.y(),
            deltaMovement.z());
      }
    }
  }

  @Override
  public ActionResultType use(BlockState blockState, World level, BlockPos pos,
      PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    ActionResultType result = super.use(blockState, level, pos, player, hand, rayTraceResult);
    if (result.consumesAction()) {
      return result;
    }

    if (!level.isClientSide()) {
      boolean open = !isOpen(blockState);
      level.setBlockAndUpdate(pos, blockState.setValue(OPEN, open));
      level.levelEvent(null, open ? Constants.WorldEvents.FENCE_GATE_OPEN_SOUND
          : Constants.WorldEvents.FENCE_GATE_CLOSE_SOUND, pos, 0);
    }

    return ActionResultType.sidedSuccess(level.isClientSide());
  }

  @Override
  public boolean crowbarWhack(BlockState blockState, World level, BlockPos pos, PlayerEntity player,
      Hand hand, ItemStack itemStack) {
    if (level.isClientSide()) {
      return true;
    }
    final int newState = ((isOneWay(blockState) ? 2 : 0) | (isReversed(blockState) ? 1 : 0))
        + (player.isShiftKeyDown() ? 3 : 1);
    level.setBlockAndUpdate(pos, blockState
        .setValue(ONE_WAY, (newState & 2) == 2)
        .setValue(REVERSED, (newState & 1) == 1));
    return true;
  }

  @Override
  public void neighborChanged(BlockState blockState, World level, BlockPos pos,
      Block neighborBlock, BlockPos neighborPos, boolean moved) {
    super.neighborChanged(blockState, level, pos, neighborBlock, neighborPos, moved);
    if (!level.isClientSide()) {
      boolean powered = isPowered(level.getBlockState(pos));
      if (powered != isOpen(blockState)) {
        level.setBlock(pos, blockState.setValue(OPEN, powered),
            Constants.BlockFlags.BLOCK_UPDATE);
        level.levelEvent(null, powered ? Constants.WorldEvents.FENCE_GATE_OPEN_SOUND
            : Constants.WorldEvents.FENCE_GATE_CLOSE_SOUND, pos, 0);
      }
    }
  }

  public static boolean isOpen(BlockState blockState) {
    return blockState.getValue(OPEN);
  }

  public static boolean isOneWay(BlockState blockState) {
    return blockState.getValue(ONE_WAY);
  }
}
