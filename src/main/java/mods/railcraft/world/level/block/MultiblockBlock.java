package mods.railcraft.world.level.block;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockListener;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.BlockHitResult;

public abstract class MultiblockBlock extends BaseEntityBlock {

  public MultiblockBlock(Properties properties) {
    super(properties);
  }

  @Override
  public RenderShape getRenderShape(BlockState blockState) {
    return RenderShape.MODEL;
  }

  @Nullable
  @Override
  public <T extends BlockEntity> GameEventListener getListener(ServerLevel level, T blockEntity) {
    return blockEntity instanceof MultiblockBlockEntity<?, ?> multiblock
        ? new MultiblockListener(multiblock)
        : null;
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level,
      BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {

    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    return LevelUtil.getBlockEntity(level, pos, MultiblockBlockEntity.class)
        .map(blockEntity -> (MultiblockBlockEntity<?, ?>) blockEntity)
        .flatMap(MultiblockBlockEntity::getMembership)
        .map(MultiblockBlockEntity.Membership::master)
        .map(master -> master.use((ServerPlayer) player, hand))
        .orElse(InteractionResult.PASS);
  }
}
