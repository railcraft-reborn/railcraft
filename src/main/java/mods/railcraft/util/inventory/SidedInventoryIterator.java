package mods.railcraft.util.inventory;

import java.util.Iterator;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class SidedInventoryIterator extends StandardInventoryIterator {

  private final ISidedInventory inv;

  protected SidedInventoryIterator(ISidedInventory inv) {
    super(inv);
    this.inv = inv;
  }

  @Override
  public Iterator<IExtInvSlot> iterator() {
    return new Iterator<IExtInvSlot>() {
      final int[] slots = inv.getSlotsForFace(Direction.DOWN);
      int index;

      @Override
      public boolean hasNext() {
        return slots != null && index < slots.length;
      }

      @Override
      public IExtInvSlot next() {
        return slot(slots[index++]);
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Remove not supported.");
      }

    };
  }

  @Override
  public IExtInvSlot slot(int index) {
    return new InvSlot(index);
  }

  private class InvSlot extends StandardInventoryIterator.InvSlot {

    public InvSlot(int slot) {
      super(slot);
    }

    @Override
    public boolean canPutStackInSlot(ItemStack stack) {
      return inv.canPlaceItemThroughFace(slot, stack, Direction.DOWN);
    }

    @Override
    public boolean canTakeStackFromSlot() {
      return inv.canTakeItemThroughFace(slot, getStack(), Direction.DOWN);
    }
  }
}
