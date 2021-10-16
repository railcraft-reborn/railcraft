package mods.railcraft.world.level.block;

import mods.railcraft.world.level.block.entity.ItemUnloaderBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

public class ItemUnloaderBlock extends ManipulatorBlock<ItemUnloaderBlockEntity> {

  protected ItemUnloaderBlock(Properties properties) {
    super(ItemUnloaderBlockEntity.class, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(POWERED, false));
  }

  @Override
  public Direction getFacing(BlockState blockState) {
    return Direction.UP;
  }

  @Override
  public ItemUnloaderBlockEntity createTileEntity(BlockState blockState, IBlockReader level) {
    return new ItemUnloaderBlockEntity();
  }
}
