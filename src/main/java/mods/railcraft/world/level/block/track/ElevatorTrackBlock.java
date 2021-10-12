package mods.railcraft.world.level.block.track;

import javax.annotation.Nullable;
import mods.railcraft.util.AABBFactory;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.world.entity.cart.CartConstants;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

/**
 * Implementation of the iron ladder blocks. Iron ladders act much like normal (wooden) ladders.
 * Climbing down iron ladders is a bit faster than climbing down normal ladders. Additionally,
 * minecarts can run down vertically on iron ladders, as if they were vertical rail tracks.
 *
 * @author DizzyDragon
 */
public class ElevatorTrackBlock extends Block {

  public static final byte ELEVATOR_TIMER = 20;

  public static final DirectionProperty FACING = HorizontalBlock.FACING;
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
  protected static final VoxelShape EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
  protected static final VoxelShape WEST_AABB = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
  protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
  protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);


  // /**
  // * The upward velocity of an entity climbing the ladder.
  // */
  // public static double CLIMB_UP_VELOCITY = 0.2;
  // /**
  // * The downward velocity of an entity climbing the ladder
  // */
  // public static double CLIMB_DOWN_VELOCITY = -0.3;
  /**
   * The inverse of the downward motion an entity gets within a single update of the game engine due
   * to gravity.
   */
  public static final double FALL_DOWN_CORRECTION = 0.039999999105930328D;
  /**
   * Velocity at which a minecart travels up on the rail
   */
  public static final double RIDE_VELOCITY = 0.4;

  public ElevatorTrackBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(FACING, Direction.NORTH)
        .setValue(POWERED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(FACING, POWERED);
  }

  private boolean canAttachTo(IBlockReader world, BlockPos pos, Direction direction) {
    BlockState blockstate = world.getBlockState(pos);
    return blockstate.isFaceSturdy(world, pos, direction);
  }

  @Override
  public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
    Direction direction = state.getValue(FACING);
    return this.canAttachTo(world, pos.relative(direction.getOpposite()), direction);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState updateShape(BlockState state, Direction direction,
      BlockState newState, IWorld world, BlockPos pos, BlockPos newPos) {
    if (direction.getOpposite() == state.getValue(FACING)
        && !state.canSurvive(world, pos)) {
      return Blocks.AIR.defaultBlockState();
    } else {
      return super.updateShape(state, direction, newState, world, pos, newPos);
    }
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    if (!context.replacingClickedOnBlock()) {
      BlockState blockstate = context.getLevel().getBlockState(
          context.getClickedPos().relative(context.getClickedFace().getOpposite()));
      if (blockstate.is(this) && blockstate.getValue(FACING) == context.getClickedFace()) {
        return null;
      }
    }

    BlockState defaultBlockState = this.defaultBlockState();
    IWorldReader level = context.getLevel();
    BlockPos pos = context.getClickedPos();

    for (Direction direction : context.getNearestLookingDirections()) {
      if (direction.getAxis().isHorizontal()) {
        defaultBlockState = defaultBlockState.setValue(FACING, direction.getOpposite());
        if (defaultBlockState.canSurvive(level, pos)) {
          return defaultBlockState;
        }
      }
    }

    return null;
  }

  @Override
  public BlockState rotate(BlockState blockState, Rotation rotation) {
    return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState blockState, Mirror mirror) {
    return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
  }

  public static boolean getPowered(IBlockReader world, BlockPos pos) {
    return getPowered(world.getBlockState(pos));
  }

  public static boolean getPowered(BlockState state) {
    return state.getValue(POWERED);
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world,
      BlockPos pos, ISelectionContext context) {
    switch (state.getValue(FACING)) {
      case NORTH:
        return NORTH_AABB;
      case SOUTH:
        return SOUTH_AABB;
      case WEST:
        return WEST_AABB;
      case EAST:
      default:
        return EAST_AABB;
    }
  }

  @Override
  public void setPlacedBy(World level, BlockPos pos, BlockState blockState, LivingEntity placer,
      ItemStack stack) {
    boolean powered = getPowered(blockState);
    if (powered != this.determinePowered(level, pos, blockState))
      level.setBlockAndUpdate(pos, blockState.setValue(POWERED, !powered));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState blockState, World level, BlockPos pos, Block neighborBlock,
      BlockPos neighborPos, boolean something) {
    super.neighborChanged(blockState, level, pos, neighborBlock, neighborPos, something);
    boolean powered = getPowered(blockState);
    if (powered != this.determinePowered(level, pos, blockState))
      level.setBlockAndUpdate(pos, blockState.setValue(POWERED, !powered));
  }

  @Override
  public void entityInside(BlockState blockState, World level, BlockPos pos, Entity entityIn) {
    entityIn.fallDistance = 0;
    if (level.isClientSide() || !(entityIn instanceof AbstractMinecartEntity))
      return;
    minecartInteraction(level, (AbstractMinecartEntity) entityIn, pos);
  }

  protected boolean determinePowered(World level, BlockPos pos, BlockState state) {
    BlockPos posUp = pos.above();
    BlockState stateUp = level.getBlockState(posUp);
    return level.hasNeighborSignal(pos)
        || stateUp.getBlock() == this && this.determinePowered(level, posUp, stateUp);
  }

  /**
   * Updates the state of a single minecart that is within the block's area of effect according to
   * the state of the block.
   *
   * @param level the world in which the block resides
   * @param cart the minecart for which the state will be updated. It is assumed that the minecart
   *        is within the area of effect of the block
   */
  protected void minecartInteraction(World level, AbstractMinecartEntity cart, BlockPos pos) {
    cart.getPersistentData().putByte(CartConstants.TAG_ELEVATOR, ELEVATOR_TIMER);
    cart.setNoGravity(true);
    BlockState state = level.getBlockState(pos);
    keepMinecartConnected(pos, state, cart);
    boolean hasPath;
    boolean up = getPowered(state);
    if (up) {
      hasPath = moveUp(level, state, cart, pos);
    } else {
      hasPath = moveDown(level, state, cart, pos);
    }
    if (!hasPath) {
      pushMinecartOntoRail(level, pos, state, cart, up);
    }
  }

  private boolean moveUp(World level, BlockState state, AbstractMinecartEntity cart, BlockPos pos) {
    BlockPos posUp = pos.above();
    boolean hasPath = level.getBlockState(posUp).is(this) && getPowered(level, posUp);
    if (hasPath) {
      if (isPathEmpty(state, cart, posUp, true)) {
        Vector3d motion = cart.getDeltaMovement();
        cart.setDeltaMovement(motion.x(), RIDE_VELOCITY, motion.z());
      } else {
        holdPosition(state, cart, pos);
      }
      return true;
    }
    return false;
  }

  private boolean moveDown(World level, BlockState state, AbstractMinecartEntity cart,
      BlockPos pos) {
    BlockPos posDown = pos.below();
    boolean hasPath = level.getBlockState(posDown).is(this) && !getPowered(level, posDown);
    if (hasPath) {
      if (isPathEmpty(state, cart, posDown, false)) {
        Vector3d motion = cart.getDeltaMovement();
        cart.setDeltaMovement(motion.x(), -RIDE_VELOCITY, motion.z());
      } else {
        holdPosition(state, cart, pos);
      }
      return true;
    }
    return false;
  }

  private void holdPosition(BlockState state, AbstractMinecartEntity cart, BlockPos pos) {
    cart.moveTo(cart.getX(), pos.getY() - cart.getBbHeight() / 2.0 + 0.5, cart.getZ(),
        getCartRotation(state, cart), 0F);
    cart.setDeltaMovement(cart.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
  }

  /**
   * Adjusts the motion and rotation yaw of a minecart so that it stays in position and aligned to
   * the iron ladder.
   *
   * @param cart the minecart for which motion and rotation will be adjusted
   */
  protected void keepMinecartConnected(BlockPos pos, BlockState state,
      AbstractMinecartEntity cart) {
    if (AbstractRailBlock.isRail(cart.level, pos.below())
        || AbstractRailBlock.isRail(cart.level, pos.below(2)))
      cart.setCanUseRail(false);
    else
      cart.setCanUseRail(true);
    Vector3d motion = cart.getDeltaMovement();
    cart.setDeltaMovement((pos.getX() + 0.5) - cart.getX(), motion.y(),
        (pos.getZ() + 0.5) - cart.getZ());

    alignMinecart(state, cart);
  }

  /**
   * Aligns the minecart to the ladder
   *
   * @param cart the minecart for which rotation will be adjusted
   */
  protected void alignMinecart(BlockState state, AbstractMinecartEntity cart) {
    cart.yRot = getCartRotation(state, cart);
  }

  private float getCartRotation(BlockState state, AbstractMinecartEntity cart) {
    return state.getValue(FACING).getStepY();
  }

  private boolean isPathEmpty(BlockState state, AbstractMinecartEntity cart, BlockPos pos,
      boolean up) {
    if (cart.level.getBlockState(pos).getMaterial().isSolid())
      return false;
    Direction.Axis axis = state.getValue(FACING).getAxis();
    AABBFactory factory = AABBFactory.start().createBoxForTileAt(pos).expandAxis(axis, 1.0);
    if (up) {
      factory.raiseCeiling(0.5);
      factory.raiseFloor(0.2);
    } else {
      factory.raiseCeiling(-0.2);
      factory.raiseFloor(-0.5);
    }

    return EntitySearcher.findMinecarts()
        .around(factory.build())
        .except(cart)
        .in(cart.level)
        .isEmpty();
  }

  /**
   * Pushes a Minecart onto a Railcraft block opposite the elevator if possible.
   */
  private boolean pushMinecartOntoRail(World world, BlockPos pos, BlockState state,
      AbstractMinecartEntity cart, boolean up) {
    cart.setCanUseRail(true);
    Direction.Axis axis = state.getValue(FACING).getAxis();
    for (BlockPos target : new BlockPos[] {pos, up ? pos.above() : pos.below()}) {
      for (Direction.AxisDirection direction : Direction.AxisDirection.values()) {
        if (AbstractRailBlock.isRail(world,
            target.relative(Direction.get(direction, axis)))) {
          holdPosition(state, cart, target);
          double vel = direction.getStep() * RIDE_VELOCITY;
          Vector3d motion = cart.getDeltaMovement();
          if (axis == Direction.Axis.Z)
            cart.setDeltaMovement(motion.x(), motion.y(), vel);
          else
            cart.setDeltaMovement(vel, motion.y(), motion.z());
          return true;
        }
      }
    }
    return false;
  }
}
