package mods.railcraft.world.level.block.track.actuator;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class SwitchTrackLeverBlock extends SwitchTrackActuatorBlock {

  public SwitchTrackLeverBlock(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType use(BlockState blockState, World level, BlockPos blockPos,
      PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    setSwitched(blockState, level, blockPos, !blockState.getValue(SWITCHED));
    return ActionResultType.sidedSuccess(level.isClientSide());
  }
}
