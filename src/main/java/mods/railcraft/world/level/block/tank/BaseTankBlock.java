package mods.railcraft.world.level.block.tank;

import javax.annotation.Nullable;
import mods.railcraft.world.level.block.MultiblockBlock;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public abstract class BaseTankBlock extends MultiblockBlock {

  public BaseTankBlock(Properties properties) {
    super(properties);
  }

  @Override
  @Nullable
  public abstract TankBlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);
}
