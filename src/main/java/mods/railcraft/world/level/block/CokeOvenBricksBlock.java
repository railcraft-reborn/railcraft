package mods.railcraft.world.level.block;

import mods.railcraft.world.level.block.entity.multiblock.CokeOvenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
public class CokeOvenBricksBlock extends MultiblockBlock<CokeOvenBlockEntity> {
  public static final BooleanProperty LIT = BlockStateProperties.LIT;

  public CokeOvenBricksBlock(Properties properties) {
    super(CokeOvenBlockEntity.class, properties);
  }

  @Override
  protected BlockState addDefaultBlockState(BlockState defaultBlockState) {
    defaultBlockState = super.addDefaultBlockState(defaultBlockState);
    defaultBlockState.setValue(LIT, Boolean.FALSE);
    return defaultBlockState;
  }

  @Override
  protected void createBlockStateDefinition(
      StateDefinition.Builder<Block, BlockState> stateContainer) {
    super.createBlockStateDefinition(stateContainer);
    stateContainer.add(LIT);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new CokeOvenBlockEntity(blockPos, blockState);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level,
      BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    // interceptor for buckets
    CokeOvenBlockEntity blockEntity = (CokeOvenBlockEntity) level.getBlockEntity(pos);
    if (!CokeOvenBlockEntity.class.isInstance(blockEntity)) {
      return InteractionResult.CONSUME;
    }

    if (blockEntity.interact(player, hand).consumesAction()) {
      return InteractionResult.CONSUME;
    }

    return super.use(blockState, level, pos, player, hand, rayTraceResult);
  }

}
