package mods.railcraft.world.level.block.signal;

import java.util.function.Supplier;
import mods.railcraft.api.core.Secure;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBoxBlockEntity;
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
 * @author CovertJaguar <https://www.railcraft.info>
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
    LevelUtil.getBlockEntity(level, pos, AbstractSignalBoxBlockEntity.class)
        .ifPresent(AbstractSignalBoxBlockEntity::neighborChanged);
  }

  @Override
  public boolean isSignalSource(BlockState blockState) {
    return true;
  }

  @Override
  public int getSignal(BlockState state, IBlockReader level, BlockPos pos,
      Direction direction) {
    return LevelUtil.getBlockEntity(level, pos, AbstractSignalBoxBlockEntity.class)
        .map(blockEntity -> blockEntity.getRedstoneSignal(direction))
        .orElse(0);
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
  public BlockState updateShape(BlockState blockState, Direction direction,
      BlockState otherState, IWorld world, BlockPos pos, BlockPos otherPos) {
    if (blockState.getValue(WATERLOGGED)) {
      world.getLiquidTicks().scheduleTick(pos, Fluids.WATER,
          Fluids.WATER.getTickDelay(world));
    }
    return direction.getAxis().isHorizontal()
        ? blockState.setValue(PROPERTY_BY_DIRECTION.get(direction),
            attachesTo(blockState, otherState))
        : direction == Direction.UP
            ? blockState.setValue(CAP, !otherState.isAir(world, otherPos))
            : blockState;
  }

  public boolean attachesTo(BlockState blockState, BlockState otherBlockState) {
    if (!isAspectEmitter(blockState) && !isAspectReceiver(blockState)) {
      return false;
    }
    if (isAspectReceiver(blockState) && isAspectEmitter(otherBlockState)
        || isAspectEmitter(blockState) && isAspectReceiver(otherBlockState)) {
      return true;
    }
    return false;
  }

  public static boolean isAspectEmitter(BlockState blockState) {
    return blockState.is(RailcraftTags.Blocks.ASPECT_EMITTER);
  }

  public static boolean isAspectReceiver(BlockState blockState) {
    return blockState.is(RailcraftTags.Blocks.ASPECT_RECEIVER);
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
    return LevelUtil.getBlockEntity(worldIn, pos, Secure.class)
        .filter(Secure::isSecure)
        .map(__ -> 0.0F)
        .orElseGet(() -> super.getDestroyProgress(state, player, worldIn, pos));
  }
}
