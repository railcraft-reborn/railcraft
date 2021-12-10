package mods.railcraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class SlotUntouchable extends SlotRailcraft {

  public SlotUntouchable(Container contents, int id, int x, int y) {
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
