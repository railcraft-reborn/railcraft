package mods.railcraft.world.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotLinked extends Slot {

  private final Slot masterSlot;
  private boolean allowNull;

  public SlotLinked(Container container, int slot, int x, int y, Slot masterSlot) {
    super(container, slot, x, y);
    this.masterSlot = masterSlot;
    this.allowNull = false;
  }

  public SlotLinked setAllowNull() {
    allowNull = true;
    return this;
  }

  @Override
  public int getMaxStackSize() {
    return 64;
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    var master = masterSlot.getItem();
    if (master.isEmpty()) {
      return allowNull;
    }
    return master.is(stack.getItem());
  }
}
