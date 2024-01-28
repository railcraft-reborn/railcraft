package mods.railcraft.world.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class BlockFilterSlot extends RailcraftSlot {

  public BlockFilterSlot(Container container, int slotIndex, int posX, int posY) {
    super(container, slotIndex, posX, posY);
    setPhantom();
    setStackLimit(1);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return stack.getItem() instanceof BlockItem;
  }
}
