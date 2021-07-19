package mods.railcraft.world.level.block;

import java.util.Optional;
import mods.railcraft.api.tracks.ISwitchMotor;
import mods.railcraft.api.tracks.ITrackKitSwitch;
import mods.railcraft.client.renderer.blockentity.AbstractSwitchMotor;
import mods.railcraft.plugins.PowerPlugin;
import mods.railcraft.plugins.WorldPlugin;
import mods.railcraft.util.AABBFactory;
import mods.railcraft.util.TrackTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class SwitchTrackActuatorBlock extends HorizontalBlock implements IWaterLoggable {

  private static final float BOUNDS = -0.2F;
  private static final VoxelShape SHAPE =
      VoxelShapes.create(AABBFactory.start()
          .box()
          .expandHorizontally(BOUNDS)
          .raiseCeilingPixel(-3)
          .build());

  public static final EnumProperty<ISwitchMotor.ArrowDirection> RED_FLAG =
      EnumProperty.create("red_flag", ISwitchMotor.ArrowDirection.class);
  public static final EnumProperty<ISwitchMotor.ArrowDirection> WHITE_FLAG =
      EnumProperty.create("white_flag", ISwitchMotor.ArrowDirection.class);
  public static final BooleanProperty THROWN = BooleanProperty.create("thrown");
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public SwitchTrackActuatorBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(FACING, Direction.NORTH)
        .setValue(RED_FLAG, ISwitchMotor.ArrowDirection.NORTH_SOUTH)
        .setValue(WHITE_FLAG, ISwitchMotor.ArrowDirection.EAST_WEST)
        .setValue(THROWN, false));
    // setSoundType(SoundType.METAL);
    // setResistance(50);
    // hardness 8

  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader level, BlockPos pos) {
    return !state.getValue(WATERLOGGED);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    IBlockReader level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    FluidState fluidState = level.getFluidState(pos);
    return this.defaultBlockState()
        .setValue(FACING, Direction.Plane.HORIZONTAL.stream()
            .filter(side -> TrackTools.isTrackInstanceAt(level, pos.relative(side),
                ITrackKitSwitch.class))
            .findFirst()
            .orElse(context.getHorizontalDirection().getOpposite()))
        .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
  }

  @Override
  public BlockState updateShape(BlockState state, Direction direction,
      BlockState newState, IWorld world, BlockPos pos, BlockPos newPos) {
    if (state.getValue(WATERLOGGED)) {
      world.getLiquidTicks().scheduleTick(pos, Fluids.WATER,
          Fluids.WATER.getTickDelay(world));
    }
    return TrackTools.isTrackInstanceAt(world, newPos, ITrackKitSwitch.class)
        ? state.setValue(FACING, direction)
        : state;
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(FACING, RED_FLAG, WHITE_FLAG, THROWN);
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos,
      ISelectionContext context) {
    return SHAPE;
  }

  @Override
  public boolean hasAnalogOutputSignal(BlockState state) {
    return true;
  }

  @Override
  public int getAnalogOutputSignal(BlockState state, World level, BlockPos pos) {
    Optional<AbstractSwitchMotor> tile = WorldPlugin.getTileEntity(level, pos, AbstractSwitchMotor.class);
    boolean thrown = tile.map(t -> t.shouldSwitch(null)).orElse(false);
    return thrown ? PowerPlugin.FULL_POWER : PowerPlugin.NO_POWER;
  }

  @Override
  public boolean canSurvive(BlockState state, IWorldReader level, BlockPos pos) {
    return canSupportRigidBlock(level, pos.below());
  }
}
