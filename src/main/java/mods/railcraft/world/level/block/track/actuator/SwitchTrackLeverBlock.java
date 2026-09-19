package mods.railcraft.world.level.block.track.actuator;

import org.jspecify.annotations.Nullable;
import mods.railcraft.world.level.block.entity.SwitchTrackLeverBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SwitchTrackLeverBlock extends SwitchTrackActuatorBlock implements EntityBlock {

  public SwitchTrackLeverBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos,
      Player player, BlockHitResult rayTraceResult) {
    setSwitched(blockState, level, blockPos, !blockState.getValue(SWITCHED));
    return InteractionResult.SUCCESS;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new SwitchTrackLeverBlockEntity(pos, state);
  }
}
