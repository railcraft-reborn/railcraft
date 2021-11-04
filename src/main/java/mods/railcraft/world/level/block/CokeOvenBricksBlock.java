package mods.railcraft.world.level.block;

import mods.railcraft.world.level.block.entity.multiblock.CokeOvenBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class CokeOvenBricksBlock extends MultiblockBlock {
  public static final BooleanProperty LIT = BlockStateProperties.LIT;

  public CokeOvenBricksBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected BlockState addDefaultBlockState(BlockState defaultBlockState) {
    defaultBlockState = super.addDefaultBlockState(defaultBlockState);
    defaultBlockState.setValue(LIT, Boolean.FALSE);
    return defaultBlockState;
  }

  @Override
  protected void createBlockStateDefinition(
        StateContainer.Builder<Block, BlockState> stateContainer) {
    super.createBlockStateDefinition(stateContainer);
    stateContainer.add(LIT);
  }

  @Override
  public TileEntity createTileEntity(BlockState blockState, IBlockReader level) {
    return new CokeOvenBlockEntity();
  }

  @Override
  public ActionResultType use(BlockState blockState, World level,
      BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    if (level.isClientSide()) {
      return ActionResultType.sidedSuccess(level.isClientSide());
    }

    if (!(level.getBlockEntity(pos) instanceof CokeOvenBlockEntity)) {
      return ActionResultType.PASS;
    }

    return super.use(blockState, level, pos, player, hand, rayTraceResult);
  }
}
