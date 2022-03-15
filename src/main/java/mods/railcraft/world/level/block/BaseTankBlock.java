package mods.railcraft.world.level.block;

import javax.annotation.Nullable;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.TankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class BaseTankBlock extends MultiblockBlock {

  public BaseTankBlock(Properties properties) {
    super(properties);
  }

  @Override
  @Nullable
  public abstract TankBlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);

  @Override
  public InteractionResult use(BlockState blockState, Level level,
      BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {

    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    return LevelUtil.getBlockEntity(level, pos, TankBlockEntity.class)
        .flatMap(MultiblockBlockEntity::getMembership)
        .map(MultiblockBlockEntity.Membership::master)
        .map(master -> {
          master.use((ServerPlayer) player, hand);
          return InteractionResult.CONSUME;
        })
        .orElse(InteractionResult.PASS);
  }
}
