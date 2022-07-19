package mods.railcraft.world.level.block.manipulator;

import javax.annotation.Nullable;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.manipulator.ItemUnloaderBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ItemUnloaderBlock extends ManipulatorBlock<ItemUnloaderBlockEntity> {

  public ItemUnloaderBlock(Properties properties) {
    super(ItemUnloaderBlockEntity.class, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(POWERED, false));
  }

  @Override
  public Direction getFacing(BlockState blockState) {
    return Direction.UP;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new ItemUnloaderBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
      BlockEntityType<T> type) {
    return level.isClientSide() ? null
        : createTickerHelper(type, RailcraftBlockEntityTypes.ITEM_UNLOADER.get(),
            ManipulatorBlockEntity::serverTick);
  }
}
