package mods.railcraft.world.level.block;

import mods.railcraft.util.PowerUtil;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public abstract class ManipulatorBlock extends Block {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  protected ManipulatorBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(POWERED);
  }

  public abstract Direction getFacing(BlockState blockState);

  @Override
  public int getSignal(BlockState blockState, IBlockReader level, BlockPos blockPos,
      Direction direction) {
    boolean emit = false;
    if (isPowered(blockState)) {
      BlockState neighborBlockState =
          level.getBlockState(blockPos.relative(direction.getOpposite()));
      emit = AbstractRailBlock.isRail(neighborBlockState)
          || neighborBlockState.is(Blocks.REDSTONE_WIRE)
          || neighborBlockState.is(Blocks.REPEATER);
    }
    return emit ? PowerUtil.FULL_POWER : PowerUtil.NO_POWER;
  }

  public static boolean isPowered(BlockState blockState) {
    return blockState.getValue(POWERED);
  }
}
