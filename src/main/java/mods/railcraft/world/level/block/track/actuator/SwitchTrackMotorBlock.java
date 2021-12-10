package mods.railcraft.world.level.block.track.actuator;

import mods.railcraft.client.ClientDist;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.SwitchTrackMotorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SwitchTrackMotorBlock extends SwitchTrackActuatorBlock implements EntityBlock {

  public SwitchTrackMotorBlock(Properties properties) {
    super(properties);
  }

  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos,
      Block neighborBlock, BlockPos neighborBlockPos, boolean moved) {
    LevelUtil.getBlockEntity(level, blockPos, SwitchTrackMotorBlockEntity.class)
        .ifPresent(SwitchTrackMotorBlockEntity::neighborChanged);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (level.isClientSide()) {
      LevelUtil.getBlockEntity(level, pos, SwitchTrackMotorBlockEntity.class)
          .ifPresent(ClientDist::openSwitchTrackMotorScreen);
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.CONSUME;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new SwitchTrackMotorBlockEntity(blockPos, blockState);
  }
}
