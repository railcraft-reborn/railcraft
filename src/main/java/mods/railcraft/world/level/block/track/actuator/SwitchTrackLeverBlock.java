package mods.railcraft.world.level.block.track.actuator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SwitchTrackLeverBlock extends SwitchTrackActuatorBlock {

  public SwitchTrackLeverBlock(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    setSwitched(blockState, level, blockPos, !blockState.getValue(SWITCHED));
    return InteractionResult.sidedSuccess(level.isClientSide());
  }
}
