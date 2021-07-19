package mods.railcraft.world.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotUntouchable extends SlotRailcraft {

  public SlotUntouchable(IInventory contents, int id, int x, int y) {
    super(contents, id, x, y);
    setPhantom();
    setCanAdjustPhantom(false);
    blockShift();
  }

  @Override
  public boolean mayPlace(ItemStack itemstack) {
    return false;
  }
}
