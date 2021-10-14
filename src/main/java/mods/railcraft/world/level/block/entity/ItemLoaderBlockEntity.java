package mods.railcraft.world.level.block.entity;

import mods.railcraft.util.inventory.IInventoryManipulator;
import net.minecraft.inventory.container.Slot;

public class ItemLoaderBlockEntity extends ItemManipulatorBlockEntity {

  public ItemLoaderBlockEntity() {
    super(RailcraftBlockEntityTypes.ITEM_LOADER.get());
  }

  @Override
  public IInventoryManipulator getSource() {
    return this.chests;
  }

  @Override
  public IInventoryManipulator getDestination() {
    return this.cart;
  }

  @Override
  public Slot getBufferSlot(int id, int x, int y) {
    return new Slot(this, id, x, y);
  }
}
