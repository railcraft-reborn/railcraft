package mods.railcraft.world.level.block.signal;

import java.util.function.Supplier;
import mods.railcraft.plugins.WorldPlugin;
import mods.railcraft.util.ISecureObject;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SignalEmitter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FourWayBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * Created by CovertJaguar on 9/8/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class SignalBoxBlock extends FourWayBlock {

  private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 15, 14);

  public static final BooleanProperty CAP = BooleanProperty.create("cap");

  private final Supplier<? extends AbstractSignalBoxBlockEntity> blockEntityFactory;

  public SignalBoxBlock(Supplier<? extends AbstractSignalBoxBlockEntity> blockEntityFactory,
      Properties properties) {
    super(2.0F, 2.0F, 16.0F, 16.0F, 24.0F, properties);
    this.blockEntityFactory = blockEntityFactory;
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(CAP, false)
        .setValue(NORTH, false)
        .setValue(EAST, false)
        .setValue(SOUTH, false)
        .setValue(WEST, false)
        .setValue(WATERLOGGED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(NORTH, EAST, WEST, SOUTH, CAP, WATERLOGGED);
  }

  @Override
  public VoxelShape getBlockSupportShape(BlockState state, IBlockReader reader, BlockPos pos) {
    return VoxelShapes.block();
  }

  public static boolean isConnected(BlockState state, Direction face) {
    BooleanProperty property = PROPERTY_BY_DIRECTION.get(face);
    return property != null && state.getValue(property);
  }

  @Override
  public void neighborChanged(BlockState state, World level, BlockPos pos,
      Block neighborBlock, BlockPos neighborPos, boolean p_220069_6_) {
    WorldPlugin.getTileEntity(level, pos, AbstractSignalBoxBlockEntity.class)
        .ifPresent(entity -> entity.neighborChanged());
  }

  @SuppressWarnings("deprecation")
  @Override
  public int getSignal(BlockState state, IBlockReader reader, BlockPos pos,
      Direction direction) {
    return WorldPlugin.getTileEntity(reader, pos, SignalEmitter.class)
        .map(entity -> entity.getSignal(direction))
        .orElseGet(() -> super.getSignal(state, reader, pos, direction));
  }

  @Override
  public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_,
      ISelectionContext p_220053_4_) {
    return SHAPE;
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    IBlockReader level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    FluidState fluidState = level.getFluidState(pos);
    return this.defaultBlockState()
        .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState updateShape(BlockState state, Direction direction,
      BlockState otherState, IWorld world, BlockPos pos, BlockPos otherPos) {
    if (state.getValue(WATERLOGGED)) {
      world.getLiquidTicks().scheduleTick(pos, Fluids.WATER,
          Fluids.WATER.getTickDelay(world));
    }

    return direction.getAxis().isHorizontal()
        ? state.setValue(PROPERTY_BY_DIRECTION.get(direction),
            WorldPlugin.getTileEntity(world, pos, AbstractSignalBoxBlockEntity.class)
                .map(entity -> entity.attachesTo(otherState, otherPos, direction))
                .orElse(false))
        : direction == Direction.UP
            ? state.setValue(CAP, !otherState.isAir(world, otherPos))
            : state;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader reader) {
    return this.blockEntityFactory.get();
  }

  @SuppressWarnings("deprecation")
  @Override
  public float getDestroyProgress(BlockState state, PlayerEntity player, IBlockReader worldIn,
      BlockPos pos) {
    if (WorldPlugin.getTileEntity(worldIn, pos, ISecureObject.class)
        .map(ISecureObject::isSecure)
        .orElse(false)) {
      return 0.0F;
    }
    return super.getDestroyProgress(state, player, worldIn, pos);
  }
}
