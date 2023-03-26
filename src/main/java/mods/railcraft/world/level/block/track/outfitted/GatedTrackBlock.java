package mods.railcraft.world.level.block.track.outfitted;

import java.util.List;
import java.util.function.Supplier;
import mods.railcraft.Translations;
import mods.railcraft.api.track.TrackType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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
      Shapes.or(
          box(0.0D, 5.0D, 7.0D, 2.0D, 16.0D, 9.0D),
          box(14.0D, 5.0D, 7.0D, 16.0D, 16.0D, 9.0D));
  protected static final VoxelShape X_OCCLUSION_SHAPE =
      Shapes.or(
          box(7.0D, 5.0D, 0.0D, 9.0D, 16.0D, 2.0D),
          box(7.0D, 5.0D, 14.0D, 9.0D, 16.0D, 16.0D));
  protected static final VoxelShape Z_OCCLUSION_SHAPE_LOW =
      Shapes.or(
          box(0.0D, 2.0D, 7.0D, 2.0D, 13.0D, 9.0D),
          box(14.0D, 2.0D, 7.0D, 16.0D, 13.0D, 9.0D));
  protected static final VoxelShape X_OCCLUSION_SHAPE_LOW =
      Shapes.or(
          box(7.0D, 2.0D, 0.0D, 9.0D, 13.0D, 2.0D),
          box(7.0D, 2.0D, 14.0D, 9.0D, 13.0D, 16.0D));

  public GatedTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(OPEN, false)
        .setValue(IN_WALL, false)
        .setValue(ONE_WAY, false);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(OPEN, IN_WALL, ONE_WAY);
  }

  @Override
  public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos,
      CollisionContext context) {
    if (blockState.getValue(IN_WALL)) {
      return getRailShapeRaw(blockState) == RailShape.EAST_WEST ? X_SHAPE_LOW : Z_SHAPE_LOW;
    } else {
      return getRailShapeRaw(blockState) == RailShape.EAST_WEST ? X_SHAPE : Z_SHAPE;
    }
  }

  @Override
  public BlockState updateShape(BlockState blockState, Direction neighborDirection,
      BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
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
  public VoxelShape getCollisionShape(BlockState blockState, BlockGetter level,
      BlockPos pos, CollisionContext context) {
    if (blockState.getValue(OPEN)) {
      return Shapes.empty();
    } else {
      return getRailShapeRaw(blockState) == RailShape.NORTH_SOUTH
          ? Z_COLLISION_SHAPE
          : X_COLLISION_SHAPE;
    }
  }

  @Override
  public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter level, BlockPos pos) {
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
  public boolean isPathfindable(BlockState blockState, BlockGetter level,
      BlockPos pos, PathComputationType type) {
    return switch (type) {
      case LAND, AIR -> blockState.getValue(OPEN);
      case WATER -> false;
    };
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockState blockState = super.getStateForPlacement(context);
    Level level = context.getLevel();
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
    return blockState.is(BlockTags.WALLS);
  }

  @Override
  public int getPowerPropagation(BlockState blockState, Level level, BlockPos pos) {
    return 0;
  }

  @Override
  public void onMinecartPass(BlockState blockState, Level level, BlockPos pos,
      AbstractMinecart cart) {
    if (isOneWay(blockState) && isOpen(blockState)) {
      RailShape shape = getRailShapeRaw(blockState);
      Vec3 deltaMovement = cart.getDeltaMovement();
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
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    InteractionResult result = super.use(blockState, level, pos, player, hand, rayTraceResult);
    if (result.consumesAction()) {
      return result;
    }

    if (!level.isClientSide()) {
      boolean open = !isOpen(blockState);
      level.setBlockAndUpdate(pos, blockState.setValue(OPEN, open));
      level.playSound(null, pos, open
              ? SoundEvents.FENCE_GATE_OPEN
              : SoundEvents.FENCE_GATE_CLOSE, SoundSource.BLOCKS, 1.0F,
          level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    return InteractionResult.sidedSuccess(level.isClientSide());
  }

  @Override
  public boolean crowbarWhack(BlockState blockState, Level level, BlockPos pos, Player player,
      InteractionHand hand, ItemStack itemStack) {
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
  public void neighborChanged(BlockState blockState, Level level, BlockPos pos,
      Block neighborBlock, BlockPos neighborPos, boolean moved) {
    super.neighborChanged(blockState, level, pos, neighborBlock, neighborPos, moved);
    if (!level.isClientSide()) {
      boolean powered = isPowered(blockState);
      if (powered != isOpen(blockState)) {
        level.setBlock(pos, blockState.setValue(OPEN, powered), Block.UPDATE_CLIENTS);
        level.playSound(null, pos, powered
                ? SoundEvents.FENCE_GATE_OPEN
                : SoundEvents.FENCE_GATE_CLOSE, SoundSource.BLOCKS, 1.0F,
            level.getRandom().nextFloat() * 0.1F + 0.9F);
      }
    }
  }

  public static boolean isOpen(BlockState blockState) {
    return blockState.getValue(OPEN);
  }

  public static boolean isOneWay(BlockState blockState) {
    return blockState.getValue(ONE_WAY);
  }

  @Override
  public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> lines,
      TooltipFlag flag) {
    lines.add(Component.translatable(Translations.Tips.GATED_TRACK)
        .withStyle(ChatFormatting.GRAY));
    lines.add(Component.translatable(Translations.Tips.HIT_CROWBAR_TO_CHANGE_MODE)
        .withStyle(ChatFormatting.BLUE));
    lines.add(Component.translatable(Translations.Tips.APPLY_REDSTONE_TO_OPEN)
        .withStyle(ChatFormatting.RED));
  }
}
