package mods.railcraft.world.level.block.tank;

import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.tank.SteelTankBlockEntity;
import mods.railcraft.world.level.block.entity.tank.TankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.STEEL_TANK.get(),
            SteelTankBlockEntity::serverTick);
  }
}
