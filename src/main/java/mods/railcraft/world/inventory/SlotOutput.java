package mods.railcraft.world.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotOutput extends Slot {

  public SlotOutput(IInventory iinventory, int slotIndex, int posX, int posY) {
    super(iinventory, slotIndex, posX, posY);
  }

  @Override
  public boolean mayPlace(ItemStack itemstack) {
    return false;
  }
}
