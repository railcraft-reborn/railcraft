package mods.railcraft.world.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;

public class BlockFilterSlot extends ItemFilterSlot {

  public BlockFilterSlot(Container container, int slotIndex, int posX, int posY) {
    super(itemStack -> itemStack.getItem() instanceof BlockItem, container, slotIndex, posX, posY);
    this.setPhantom();
    this.setStackLimit(1);
  }
}
