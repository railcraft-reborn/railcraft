package mods.railcraft.world.level.block;

import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.WaterTankSidingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class WaterTankSiding extends MultiblockBlock {

  private static final MapCodec<WaterTankSiding> CODEC = simpleCodec(WaterTankSiding::new);

  public WaterTankSiding(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends MultiblockBlock> codec() {
    return CODEC;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
    return new WaterTankSidingBlockEntity(pos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide()
        ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.WATER_TANK_SIDING.get(),
            WaterTankSidingBlockEntity::serverTick);
  }
}
