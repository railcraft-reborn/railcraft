package mods.railcraft.world.level.block.detector;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.detector.DetectorBlockEntity;
import mods.railcraft.world.level.block.entity.detector.RoutingDetectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;

public class RoutingDetectorBlock extends DetectorBlock {

  public RoutingDetectorBlock(Properties properties) {
    super(properties);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new RoutingDetectorBlockEntity(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.ROUTING_DETECTOR.get(),
            DetectorBlockEntity::serverTick);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos pos,
      Player player, BlockHitResult rayTraceResult) {
    if (player instanceof ServerPlayer serverPlayer) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.ROUTING_DETECTOR.get())
          .ifPresent(blockEntity -> serverPlayer.openMenu(blockEntity, pos));
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos,
      Block neighborBlock, @Nullable Orientation orientation, boolean moved) {
    level.getBlockEntity(blockPos, RailcraftBlockEntityTypes.ROUTING_DETECTOR.get())
        .ifPresent(RoutingDetectorBlockEntity::neighborChanged);
  }
}
