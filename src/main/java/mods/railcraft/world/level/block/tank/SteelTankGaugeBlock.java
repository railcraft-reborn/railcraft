package mods.railcraft.world.level.block.tank;

import mods.railcraft.world.level.block.entity.tank.SteelTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SteelTankGaugeBlock extends TankGaugeBlock {

  public SteelTankGaugeBlock(Properties properties) {
    super(properties);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new SteelTankBlockEntity(blockPos, blockState);
  }
}
