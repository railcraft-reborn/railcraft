package mods.railcraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotOutput extends Slot {

  public SlotOutput(Container iinventory, int slotIndex, int posX, int posY) {
    super(iinventory, slotIndex, posX, posY);
  }

  @Override
  public boolean mayPlace(ItemStack itemstack) {
    return false;
  }
}
