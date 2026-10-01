package mods.railcraft.world.level.block.tank;

import com.mojang.serialization.MapCodec;
import mods.railcraft.world.level.block.entity.tank.SteelTankBlockEntity;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SteelTankWallBlock extends BaseTankBlock {

  private static final MapCodec<SteelTankWallBlock> CODEC = simpleCodec(SteelTankWallBlock::new);

  public SteelTankWallBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends BaseTankBlock> codec() {
    return CODEC;
  }

  @Override
  public TankBlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new SteelTankBlockEntity(blockPos, blockState);
  }
}
