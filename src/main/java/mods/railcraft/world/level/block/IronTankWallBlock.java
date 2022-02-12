package mods.railcraft.world.level.block;

import javax.annotation.Nullable;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.multiblock.IronTankBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.TankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class IronTankWallBlock extends BaseTankBlock {

  protected IronTankWallBlock(Properties properties) {
    super(properties);
  }

  @Override
  public TankBlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new IronTankBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.IRON_TANK.get(),
            IronTankBlockEntity::serverTick);
  }
}
