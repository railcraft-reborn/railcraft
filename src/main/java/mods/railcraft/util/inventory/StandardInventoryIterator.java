package mods.railcraft.util.inventory;

import java.util.Iterator;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class StandardInventoryIterator extends InventoryIterator<IExtInvSlot> {

  private final IInventory inv;
  private final int invSize;

  protected StandardInventoryIterator(IInventory inv) {
    this.inv = inv;
    this.invSize = inv.getContainerSize();
  }

  @Override
  public Iterator<IExtInvSlot> iterator() {
    return new Iterator<IExtInvSlot>() {
      int slot;

      @Override
      public boolean hasNext() {
        return slot < invSize;
      }

      @Override
      public IExtInvSlot next() {
        return slot(slot++);
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

  class InvSlot implements IExtInvSlot {

    protected final int slot;

    public InvSlot(int slot) {
      this.slot = slot;
    }

    @Override
    public ItemStack getStack() {
      return inv.getItem(slot);
    }

    @Override
    public void setStack(ItemStack stack) {
      inv.setItem(slot, stack);
    }

    @Override
    public boolean canPutStackInSlot(ItemStack stack) {
      return inv.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeStackFromSlot() {
      return true;
    }

    @Override
    public ItemStack decreaseStack() {
      return inv.removeItem(slot, 1);
    }

    @Override
    public ItemStack removeFromSlot(int amount, boolean simulate) {
      if (!simulate) {
        return inv.removeItem(slot, amount);
      }
      ItemStack stack = getStack();
      return InvTools.copy(stack, Math.min(amount, stack.getCount()));
    }

    @Override
    public ItemStack addToSlot(ItemStack stack, boolean simulate) {
      int available = stack.getCount();
      if (available <= 0)
        return stack.copy();
      int max = Math.min(stack.getMaxStackSize(), inv.getMaxStackSize());
      int wanted = 0;

      ItemStack stackInSlot = getStack();
      if (stackInSlot.isEmpty()) {
        wanted = Math.min(available, max);
        if (wanted > 0 && !simulate) {
          setStack(InvTools.copy(stack, wanted));
        }
      } else if (InvTools.isItemEqual(stack, stackInSlot)) {
        wanted = Math.min(available, max - stackInSlot.getCount());
        if (wanted > 0 && !simulate) {
          setStack(InvTools.incSize(stackInSlot, wanted));
        }
      }
      return InvTools.copy(stack, available - wanted);
    }

    @Override
    public int getIndex() {
      return slot;
    }

    @Override
    public int maxSlotStackSize() {
      return inv.getMaxStackSize();
    }

    @Override
    public String toString() {
      return "SlotNum = " + slot + " Stack = " + InvTools.toString(getStack());
    }

  }
}
