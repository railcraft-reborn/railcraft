package mods.railcraft.world.level.block.signal;

import javax.annotation.Nullable;
import mods.railcraft.client.ClientDist;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.BlockSignalRelayBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockSignalRelayBoxBlock extends SignalBoxBlock implements EntityBlock {

  public BlockSignalRelayBoxBlock(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (level.isClientSide()) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.BLOCK_SIGNAL_RELAY_BOX.get())
          .ifPresent(ClientDist::openActionSignalBoxScreen);
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.CONSUME;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new BlockSignalRelayBoxBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return BaseEntityBlock.createTickerHelper(type,
        RailcraftBlockEntityTypes.BLOCK_SIGNAL_RELAY_BOX.get(),
        level.isClientSide()
            ? BlockSignalRelayBoxBlockEntity::clientTick
            : BlockSignalRelayBoxBlockEntity::serverTick);
  }
}
