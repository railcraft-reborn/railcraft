package mods.railcraft.world.level.block.tank;

import com.mojang.serialization.MapCodec;
import mods.railcraft.world.level.block.entity.tank.IronTankBlockEntity;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class IronTankWallBlock extends BaseTankBlock {

  private static final MapCodec<IronTankWallBlock> CODEC = simpleCodec(IronTankWallBlock::new);

  public IronTankWallBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends BaseTankBlock> codec() {
    return CODEC;
  }

  @Override
  public TankBlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new IronTankBlockEntity(blockPos, blockState);
  }
}
