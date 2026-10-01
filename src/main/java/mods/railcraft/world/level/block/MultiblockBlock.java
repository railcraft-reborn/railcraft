package mods.railcraft.world.level.block;

import org.jspecify.annotations.Nullable;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;

public abstract class MultiblockBlock extends BaseEntityBlock {

  public MultiblockBlock(Properties properties) {
    super(properties);
  }

  @Override
  public RenderShape getRenderShape(BlockState blockState) {
    return RenderShape.MODEL;
  }

  @Override
  protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos,
      Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
    super.neighborChanged(blockState, level, blockPos, neighborBlock, orientation, movedByPiston);
    neighborChanged(level, blockPos);
  }

  /**
   * Lets the multiblock block entity at the specified position know that a neighbouring block
   * changed, so it can evaluate its structure.
   */
  public static void neighborChanged(Level level, BlockPos blockPos) {
    if (!level.isClientSide()
        && level.getBlockEntity(blockPos) instanceof MultiblockBlockEntity<?, ?> blockEntity) {
      blockEntity.neighborChanged();
    }
  }

  @Override
  protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState,
      Level level, BlockPos pos, Player player, InteractionHand interactionHand,
      BlockHitResult rayTraceResult) {
    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    return LevelUtil.getBlockEntity(level, pos, MultiblockBlockEntity.class)
        .map(blockEntity -> (MultiblockBlockEntity<?, ?>) blockEntity)
        .flatMap(MultiblockBlockEntity::getMembership)
        .map(MultiblockBlockEntity.Membership::master)
        .map(master -> master.use((ServerPlayer) player, interactionHand))
        .orElse(InteractionResult.TRY_WITH_EMPTY_HAND);
  }
}
