package mods.railcraft.world.level.block.signal;

import javax.annotation.Nullable;
import mods.railcraft.client.ClientDist;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.signal.AnalogSignalControllerBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AnalogSignalControllerBoxBlock extends SignalBoxBlock implements EntityBlock {

  public AnalogSignalControllerBoxBlock(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (level.isClientSide()) {
      LevelUtil.getBlockEntity(level, pos, AnalogSignalControllerBoxBlockEntity.class)
          .ifPresent(ClientDist::openAnalogSignalControllerBoxScreen);
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.CONSUME;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new AnalogSignalControllerBoxBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide()
        ? createTickerHelper(type, RailcraftBlockEntityTypes.ANALOG_SIGNAL_CONTROLLER_BOX.get(),
            AnalogSignalControllerBoxBlockEntity::clientTick)
        : null;
  }
}
