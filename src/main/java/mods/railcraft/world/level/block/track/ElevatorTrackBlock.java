package mods.railcraft.world.level.block.track;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.util.BoxBuilder;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.world.entity.vehicle.MinecartExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Implementation of the iron ladder blocks. Iron ladders act much like normal (wooden) ladders.
 * Climbing down iron ladders is a bit faster than climbing down normal ladders. Additionally,
 * minecarts can run down vertically on iron ladders, as if they were vertical rail tracks.
 *
 * @author DizzyDragon
 */
public class ElevatorTrackBlock extends Block {

  public static final byte ELEVATOR_TIMER = 20;

  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
  protected static final VoxelShape EAST_SHAPE = box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
  protected static final VoxelShape WEST_SHAPE = box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
  protected static final VoxelShape SOUTH_SHAPE = box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
  protected static final VoxelShape NORTH_SHAPE = box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);


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
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, POWERED);
  }

  private boolean canAttachTo(BlockGetter world, BlockPos pos, Direction direction) {
    BlockState blockstate = world.getBlockState(pos);
    return blockstate.isFaceSturdy(world, pos, direction);
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    Direction direction = state.getValue(FACING);
    return this.canAttachTo(world, pos.relative(direction.getOpposite()), direction);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState updateShape(BlockState state, Direction direction,
      BlockState newState, LevelAccessor world, BlockPos pos, BlockPos newPos) {
    if (direction.getOpposite() == state.getValue(FACING)
        && !state.canSurvive(world, pos)) {
      return Blocks.AIR.defaultBlockState();
    } else {
      return super.updateShape(state, direction, newState, world, pos, newPos);
    }
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    if (!context.replacingClickedOnBlock()) {
      BlockState blockstate = context.getLevel().getBlockState(
          context.getClickedPos().relative(context.getClickedFace().getOpposite()));
      if (blockstate.is(this) && blockstate.getValue(FACING) == context.getClickedFace()) {
        return null;
      }
    }

    BlockState defaultBlockState = this.defaultBlockState();
    LevelReader level = context.getLevel();
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

  public static boolean getPowered(BlockGetter world, BlockPos pos) {
    return getPowered(world.getBlockState(pos));
  }

  public static boolean getPowered(BlockState state) {
    return state.getValue(POWERED);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world,
      BlockPos pos, CollisionContext context) {
    switch (state.getValue(FACING)) {
      case NORTH:
        return NORTH_SHAPE;
      case SOUTH:
        return SOUTH_SHAPE;
      case WEST:
        return WEST_SHAPE;
      case EAST:
      default:
        return EAST_SHAPE;
    }
  }

  @Override
  public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, LivingEntity placer,
      ItemStack stack) {
    boolean powered = getPowered(blockState);
    if (powered != this.determinePowered(level, pos, blockState))
      level.setBlockAndUpdate(pos, blockState.setValue(POWERED, !powered));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block neighborBlock,
      BlockPos neighborPos, boolean something) {
    super.neighborChanged(blockState, level, pos, neighborBlock, neighborPos, something);
    boolean powered = getPowered(blockState);
    if (powered != this.determinePowered(level, pos, blockState))
      level.setBlockAndUpdate(pos, blockState.setValue(POWERED, !powered));
  }

  @Override
  public void entityInside(BlockState blockState, Level level, BlockPos pos, Entity entityIn) {
    entityIn.fallDistance = 0;
    if (level.isClientSide() || !(entityIn instanceof AbstractMinecart))
      return;
    minecartInteraction(level, (AbstractMinecart) entityIn, pos);
  }

  protected boolean determinePowered(Level level, BlockPos pos, BlockState state) {
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
  protected void minecartInteraction(Level level, AbstractMinecart cart, BlockPos pos) {
    MinecartExtension.getOrThrow(cart).setElevatorRemainingTicks(ELEVATOR_TIMER);
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

  private boolean moveUp(Level level, BlockState state, AbstractMinecart cart, BlockPos pos) {
    BlockPos posUp = pos.above();
    boolean hasPath = level.getBlockState(posUp).is(this) && getPowered(level, posUp);
    if (hasPath) {
      if (isPathEmpty(state, cart, posUp, true)) {
        Vec3 motion = cart.getDeltaMovement();
        cart.setDeltaMovement(motion.x(), RIDE_VELOCITY, motion.z());
      } else {
        holdPosition(state, cart, pos);
      }
      return true;
    }
    return false;
  }

  private boolean moveDown(Level level, BlockState state, AbstractMinecart cart,
      BlockPos pos) {
    BlockPos posDown = pos.below();
    boolean hasPath = level.getBlockState(posDown).is(this) && !getPowered(level, posDown);
    if (hasPath) {
      if (isPathEmpty(state, cart, posDown, false)) {
        Vec3 motion = cart.getDeltaMovement();
        cart.setDeltaMovement(motion.x(), -RIDE_VELOCITY, motion.z());
      } else {
        holdPosition(state, cart, pos);
      }
      return true;
    }
    return false;
  }

  private void holdPosition(BlockState state, AbstractMinecart cart, BlockPos pos) {
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
      AbstractMinecart cart) {
    if (BaseRailBlock.isRail(cart.level, pos.below())
        || BaseRailBlock.isRail(cart.level, pos.below(2)))
      cart.setCanUseRail(false);
    else
      cart.setCanUseRail(true);
    Vec3 motion = cart.getDeltaMovement();
    cart.setDeltaMovement((pos.getX() + 0.5) - cart.getX(), motion.y(),
        (pos.getZ() + 0.5) - cart.getZ());

    alignMinecart(state, cart);
  }

  /**
   * Aligns the minecart to the ladder
   *
   * @param cart the minecart for which rotation will be adjusted
   */
  protected void alignMinecart(BlockState state, AbstractMinecart cart) {
    cart.setYRot(this.getCartRotation(state, cart));
  }

  private float getCartRotation(BlockState state, AbstractMinecart cart) {
    return state.getValue(FACING).getStepY();
  }

  private boolean isPathEmpty(BlockState state, AbstractMinecart cart, BlockPos pos,
      boolean up) {
    if (cart.level.getBlockState(pos).getMaterial().isSolid())
      return false;
    Direction.Axis axis = state.getValue(FACING).getAxis();
    BoxBuilder factory = BoxBuilder.create().at(pos).expandAxis(axis, 1.0);
    if (up) {
      factory.raiseCeiling(0.5);
      factory.raiseFloor(0.2);
    } else {
      factory.raiseCeiling(-0.2);
      factory.raiseFloor(-0.5);
    }

    return EntitySearcher.findMinecarts()
        .in(factory.build())
        .except(cart)
        .list(cart.level)
        .isEmpty();
  }

  /**
   * Pushes a Minecart onto a Railcraft block opposite the elevator if possible.
   */
  private boolean pushMinecartOntoRail(Level level, BlockPos pos, BlockState state,
      AbstractMinecart cart, boolean up) {
    cart.setCanUseRail(true);
    Direction.Axis axis = state.getValue(FACING).getAxis();
    for (BlockPos target : new BlockPos[] {pos, up ? pos.above() : pos.below()}) {
      for (Direction.AxisDirection direction : Direction.AxisDirection.values()) {
        if (BaseRailBlock.isRail(level,
            target.relative(Direction.get(direction, axis)))) {
          holdPosition(state, cart, target);
          double vel = direction.getStep() * RIDE_VELOCITY;
          Vec3 motion = cart.getDeltaMovement();
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
