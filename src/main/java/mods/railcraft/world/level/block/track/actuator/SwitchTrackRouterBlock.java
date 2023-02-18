package mods.railcraft.world.level.block.track.actuator;

import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class SwitchTrackRouterBlock extends SwitchTrackActuatorBlock implements EntityBlock {

  public SwitchTrackRouterBlock(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }
    level.getBlockEntity(pos, RailcraftBlockEntityTypes.SWITCH_TRACK_ROUTER.get())
        .ifPresent(blockEntity -> NetworkHooks.openScreen((ServerPlayer) player, blockEntity, pos));
    return InteractionResult.CONSUME;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new SwitchTrackRouterBlockEntity(blockPos, blockState);
  }

  @Override
  public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState,
      boolean isMoving) {
    if (!state.is(newState.getBlock())
        && level.getBlockEntity(pos) instanceof SwitchTrackRouterBlockEntity trackRouting) {
      Containers.dropContents(level, pos, trackRouting);
      level.updateNeighbourForOutputSignal(pos, this);
    }
    super.onRemove(state, level, pos, newState, isMoving);
  }

  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos,
      Block neighborBlock, BlockPos neighborBlockPos, boolean moved) {
    level.getBlockEntity(blockPos, RailcraftBlockEntityTypes.SWITCH_TRACK_ROUTER.get())
        .ifPresent(SwitchTrackRouterBlockEntity::neighborChanged);
  }
}
