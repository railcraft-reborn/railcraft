package mods.railcraft.world.level.block.track.actuator;

import mods.railcraft.client.ClientDist;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
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

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SwitchTrackMotorBlock extends SwitchTrackActuatorBlock implements EntityBlock {

  public SwitchTrackMotorBlock(Properties properties) {
    super(properties);
  }

  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos,
      Block neighborBlock, BlockPos neighborBlockPos, boolean moved) {
    level.getBlockEntity(blockPos, RailcraftBlockEntityTypes.SWITCH_TRACK_MOTOR.get())
        .ifPresent(SwitchTrackMotorBlockEntity::neighborChanged);
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos pos,
      Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    if (level.isClientSide()) {
      level.getBlockEntity(pos, RailcraftBlockEntityTypes.SWITCH_TRACK_MOTOR.get())
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
