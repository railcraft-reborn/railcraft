package mods.railcraft.world.level.block.track.actuator;

import mods.railcraft.client.ClientDist;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.SwitchTrackMotorBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class SwitchTrackMotorBlock extends SwitchTrackActuatorBlock {

  public SwitchTrackMotorBlock(Properties properties) {
    super(properties);
  }

  @Override
  public boolean hasTileEntity(BlockState blockState) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState blockState, IBlockReader level) {
    return new SwitchTrackMotorBlockEntity();
  }

  @Override
  public void neighborChanged(BlockState blockState, World level, BlockPos blockPos,
      Block neighborBlock, BlockPos neighborBlockPos, boolean moved) {
    LevelUtil.getBlockEntity(level, blockPos, SwitchTrackMotorBlockEntity.class)
        .ifPresent(SwitchTrackMotorBlockEntity::neighborChanged);
  }

  @Override
  public ActionResultType use(BlockState blockState, World level, BlockPos pos,
      PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    if (level.isClientSide()) {
      LevelUtil.getBlockEntity(level, pos, SwitchTrackMotorBlockEntity.class)
          .ifPresent(ClientDist::openSwitchTrackMotorScreen);
      return ActionResultType.SUCCESS;
    }
    return ActionResultType.CONSUME;
  }
}
