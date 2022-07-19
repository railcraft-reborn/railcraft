package mods.railcraft.world.level.block.signal;

import mods.railcraft.client.ClientDist;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.SignalReceiverBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SignalReceiverBoxBlock extends SignalBoxBlock implements EntityBlock {

  public SignalReceiverBoxBlock(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (level.isClientSide()) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.SIGNAL_RECEIVER_BOX.get())
          .ifPresent(ClientDist::openActionSignalBoxScreen);
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.CONSUME;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new SignalReceiverBoxBlockEntity(blockPos, blockState);
  }
}
