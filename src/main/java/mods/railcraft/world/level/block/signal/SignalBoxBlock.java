package mods.railcraft.world.level.block.signal;

import org.jspecify.annotations.Nullable;
import mods.railcraft.api.core.Lockable;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class SignalBoxBlock extends CrossCollisionBlock {

  public static final BooleanProperty CAP = BooleanProperty.create("cap");
  private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 15, 14);

  public SignalBoxBlock(Properties properties) {
    super(2.0F, 2.0F, 16.0F, 16.0F, 24.0F, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(CAP, false)
        .setValue(NORTH, false)
        .setValue(EAST, false)
        .setValue(SOUTH, false)
        .setValue(WEST, false)
        .setValue(WATERLOGGED, false));
  }

  public static boolean isConnected(BlockState state, Direction face) {
    BooleanProperty property = PROPERTY_BY_DIRECTION.get(face);
    return property != null && state.getValue(property);
  }

  public static boolean isAspectEmitter(BlockState blockState) {
    return blockState.is(RailcraftTags.Blocks.ASPECT_EMITTER);
  }

  public static boolean isAspectReceiver(BlockState blockState) {
    return blockState.is(RailcraftTags.Blocks.ASPECT_RECEIVER);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(NORTH, EAST, WEST, SOUTH, CAP, WATERLOGGED);
  }

  @Override
  protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
    return Shapes.block();
  }

  @Override
  protected void neighborChanged(BlockState state, Level level, BlockPos pos,
      Block neighborBlock, @Nullable Orientation orientation, boolean isMoving) {
    LevelUtil.getBlockEntity(level, pos, AbstractSignalBoxBlockEntity.class)
        .ifPresent(AbstractSignalBoxBlockEntity::neighborChanged);
  }

  @Override
  protected boolean isSignalSource(BlockState blockState) {
    return true;
  }

  @Override
  protected int getSignal(BlockState state, BlockGetter level, BlockPos pos,
      Direction direction) {
    return LevelUtil.getBlockEntity(level, pos, AbstractSignalBoxBlockEntity.class)
        .map(blockEntity -> blockEntity.getRedstoneSignal(direction))
        .orElse(0);
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
      CollisionContext context) {
    return SHAPE;
  }

  @Override
  protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
      CollisionContext context) {
    return SHAPE;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockGetter level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    FluidState fluidState = level.getFluidState(pos);
    return this.defaultBlockState()
        .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
  }

  @Override
  protected BlockState updateShape(BlockState blockState, LevelReader levelReader,
      ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos neighborPos,
      BlockState neighborState, RandomSource randomSource) {
    if (blockState.getValue(WATERLOGGED)) {
      scheduledTickAccess.scheduleTick(blockPos, Fluids.WATER,
          Fluids.WATER.getTickDelay(levelReader));
    }
    return direction.getAxis().isHorizontal()
        ? blockState.setValue(PROPERTY_BY_DIRECTION.get(direction),
        attachesTo(blockState, neighborState))
        : direction == Direction.UP
            ? blockState.setValue(CAP, !neighborState.isAir())
            : blockState;
  }

  public boolean attachesTo(BlockState blockState, BlockState otherBlockState) {
    if (!isAspectEmitter(blockState) && !isAspectReceiver(blockState)) {
      return false;
    }
    return isAspectReceiver(blockState) && isAspectEmitter(otherBlockState)
        || isAspectEmitter(blockState) && isAspectReceiver(otherBlockState);
  }

  @Override
  protected float getDestroyProgress(BlockState state, Player player, BlockGetter blockGetter,
      BlockPos pos) {
    return LevelUtil.getBlockEntity(blockGetter, pos, Lockable.class)
        .filter(Lockable::isLocked)
        .map(__ -> 0.0F)
        .orElseGet(() -> super.getDestroyProgress(state, player, blockGetter, pos));
  }
}
