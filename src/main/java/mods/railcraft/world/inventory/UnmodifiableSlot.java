package mods.railcraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class UnmodifiableSlot extends RailcraftSlot {

  public UnmodifiableSlot(Container container, int id, int x, int y) {
    super(container, id, x, y);
    this.setPhantom();
    this.setCanAdjustPhantom(false);
    this.blockShift();
  }

  @Override
  public boolean mayPlace(ItemStack item) {
    return false;
  }
}
