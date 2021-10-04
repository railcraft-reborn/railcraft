package mods.railcraft.world.level.block.signal;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

/**
 * 
 * @author Sm0keySa1m0n
 *
 */
public class SignalBlock extends AbstractSignalBlock {

  public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

  public static final VoxelShape SHAPE = box(3.0D, 6.0D, 3.0D, 13.0D, 16.0D, 13.0D);

  public SignalBlock(Supplier<? extends AbstractSignalBlockEntity> blockEntityFactory,
      Properties properties) {
    super(SHAPE, blockEntityFactory, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(NORTH, false)
        .setValue(EAST, false)
        .setValue(SOUTH, false)
        .setValue(WEST, false)
        .setValue(DOWN, false)
        .setValue(FACING, Direction.NORTH)
        .setValue(WATERLOGGED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(DOWN);
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    IBlockReader level = context.getLevel();
    BlockPos pos = context.getClickedPos();
    Direction facing = context.getHorizontalDirection().getOpposite();
    BlockPos downPos = pos.below();
    BlockState downState = level.getBlockState(downPos);
    return super.getStateForPlacement(context)
        .setValue(DOWN, this.connectsTo(downState,
            downState.isFaceSturdy(level, downPos, Direction.UP), Direction.DOWN, facing));
  }
}
