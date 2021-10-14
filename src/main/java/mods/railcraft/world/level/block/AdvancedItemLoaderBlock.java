package mods.railcraft.world.level.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public class AdvancedItemLoaderBlock extends ItemLoaderBlock {

  public static final DirectionProperty FACING = BlockStateProperties.FACING;

  protected AdvancedItemLoaderBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(POWERED, false)
        .setValue(FACING, Direction.DOWN));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(FACING);
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    return this.defaultBlockState()
        .setValue(FACING, context.getNearestLookingDirection().getOpposite());
  }

  @Override
  public Direction getFacing(BlockState blockState) {
    return blockState.getValue(FACING);
  }
}
