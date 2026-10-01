package mods.railcraft.world.level.block.tank;

import mods.railcraft.world.level.block.entity.tank.IronTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IronTankGaugeBlock extends TankGaugeBlock {

  public IronTankGaugeBlock(Properties properties) {
    super(properties);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new IronTankBlockEntity(blockPos, blockState);
  }
}
