package mods.railcraft.world.level.block.signal;

import java.util.function.Supplier;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SixWayBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

/**
 * Created by CovertJaguar on 9/8/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class AbstractSignalBlock extends SixWayBlock implements IWaterLoggable {

  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public static final float BLOCK_BOUNDS = 0.15F;

  private final Supplier<? extends AbstractSignalBlockEntity> blockEntityFactory;

  protected AbstractSignalBlock(Supplier<? extends AbstractSignalBlockEntity> blockEntityFactory,
      Properties properties) {
    super(BLOCK_BOUNDS, properties);
    this.blockEntityFactory = blockEntityFactory;
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(NORTH, false)
        .setValue(EAST, false)
        .setValue(SOUTH, false)
        .setValue(WEST, false)
        .setValue(UP, false)
        .setValue(DOWN, false)
        .setValue(FACING, Direction.NORTH)
        .setValue(WATERLOGGED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(NORTH, EAST, WEST, SOUTH, UP, DOWN, FACING, WATERLOGGED);
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader,
      BlockPos pos) {
    return !state.getValue(WATERLOGGED);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false)
        : super.getFluidState(state);
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
    BlockPos upPos = pos.above();
    BlockPos downPos = pos.below();
    BlockState northState = level.getBlockState(northPos);
    BlockState southState = level.getBlockState(southPos);
    BlockState westState = level.getBlockState(westPos);
    BlockState eastState = level.getBlockState(eastPos);
    BlockState upState = level.getBlockState(upPos);
    BlockState downState = level.getBlockState(downPos);
    return this.defaultBlockState()
        .setValue(NORTH, this.attachsTo(northState,
            northState.isFaceSturdy(level, northPos, Direction.SOUTH), Direction.NORTH, facing))
        .setValue(SOUTH, this.attachsTo(southState,
            southState.isFaceSturdy(level, southPos, Direction.NORTH), Direction.SOUTH, facing))
        .setValue(WEST, this.attachsTo(westState,
            westState.isFaceSturdy(level, westPos, Direction.EAST), Direction.WEST, facing))
        .setValue(EAST, this.attachsTo(eastState,
            eastState.isFaceSturdy(level, eastPos, Direction.WEST), Direction.EAST, facing))
        .setValue(UP, this.attachsTo(upState,
            upState.isFaceSturdy(level, upPos, Direction.DOWN), Direction.UP, facing))
        .setValue(DOWN, this.attachsTo(downState,
            downState.isFaceSturdy(level, downPos, Direction.UP), Direction.DOWN, facing))
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
    return state.setValue(PROPERTY_BY_DIRECTION.get(direction), this.attachsTo(newState,
        newState.isFaceSturdy(world, newPos, direction.getOpposite()), direction,
        state.getValue(FACING)));
  }

  public final boolean attachsTo(BlockState state, boolean faceStudry, Direction direction,
      Direction facing) {
    Block block = state.getBlock();
    if (isExceptionForConnection(block)
        || (!faceStudry && !(block instanceof AbstractSignalBlock))
        || facing == direction) {
      return false;
    }

    if (block instanceof AbstractSignalBlock) {
      return direction.getAxis().isVertical()
          || direction.getOpposite() != state.getValue(FACING);
    }

    if (state.is(BlockTags.FENCES) || state.is(BlockTags.WALLS)) {
      return direction.getAxis().getPlane() != Direction.Plane.HORIZONTAL;
    }

    return true;
  }

  @Override
  public boolean isPathfindable(BlockState state, IBlockReader world, BlockPos pos, PathType type) {
    return false;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader reader) {
    return this.blockEntityFactory.get();
  }
}
