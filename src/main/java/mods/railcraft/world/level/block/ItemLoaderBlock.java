package mods.railcraft.world.level.block;

import mods.railcraft.world.level.block.entity.ItemLoaderBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

public class ItemLoaderBlock extends ManipulatorBlock<ItemLoaderBlockEntity> {

  protected ItemLoaderBlock(Properties properties) {
    super(ItemLoaderBlockEntity.class, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(POWERED, false));
  }

  @Override
  public Direction getFacing(BlockState blockState) {
    return Direction.DOWN;
  }

  @Override
  public ItemLoaderBlockEntity createTileEntity(BlockState blockState, IBlockReader level) {
    return new ItemLoaderBlockEntity();
  }
}
