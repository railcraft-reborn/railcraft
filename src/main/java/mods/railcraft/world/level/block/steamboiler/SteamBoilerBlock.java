package mods.railcraft.world.level.block.steamboiler;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.world.level.block.MultiblockBlock;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SteamBoilerBlock extends MultiblockBlock {

  public SteamBoilerBlock(Properties properties) {
    super(properties);
  }

  @Override
  public abstract SteamBoilerBlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);

  protected abstract BlockEntityType<? extends SteamBoilerBlockEntity> getBlockEntityType();

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide()
        ? null
        : createTickerHelper(type, this.getBlockEntityType(), SteamBoilerBlockEntity::serverTick);
  }
}
