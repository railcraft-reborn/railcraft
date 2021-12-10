package mods.railcraft.world.level.block.entity;

import mods.railcraft.util.container.ContainerManipulator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.state.BlockState;

public class ItemLoaderBlockEntity extends ItemManipulatorBlockEntity {

  public ItemLoaderBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.ITEM_LOADER.get(), blockPos, blockState);
  }

  @Override
  public ContainerManipulator getSource() {
    return this.chests;
  }

  @Override
  public ContainerManipulator getDestination() {
    return this.cart;
  }

  @Override
  public Slot getBufferSlot(int id, int x, int y) {
    return new Slot(this, id, x, y);
  }
}
