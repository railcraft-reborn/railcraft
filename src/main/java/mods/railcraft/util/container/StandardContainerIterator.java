package mods.railcraft.util.container;

import java.util.Iterator;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class StandardContainerIterator extends ContainerIterator<ModifiableContainerSlot> {

  private final Container inv;
  private final int invSize;

  protected StandardContainerIterator(Container inv) {
    this.inv = inv;
    this.invSize = inv.getContainerSize();
  }

  @Override
  public Iterator<ModifiableContainerSlot> iterator() {
    return new Iterator<ModifiableContainerSlot>() {
      int slot;

      @Override
      public boolean hasNext() {
        return slot < invSize;
      }

      @Override
      public ModifiableContainerSlot next() {
        return slot(slot++);
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Remove not supported.");
      }

    };
  }

  @Override
  public ModifiableContainerSlot slot(int index) {
    return new Slot(index);
  }

  class Slot implements ModifiableContainerSlot {

    protected final int slot;

    public Slot(int slot) {
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
      return ContainerTools.copy(stack, Math.min(amount, stack.getCount()));
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
          setStack(ContainerTools.copy(stack, wanted));
        }
      } else if (ContainerTools.isItemEqual(stack, stackInSlot)) {
        wanted = Math.min(available, max - stackInSlot.getCount());
        if (wanted > 0 && !simulate) {
          setStack(ContainerTools.incSize(stackInSlot, wanted));
        }
      }
      return ContainerTools.copy(stack, available - wanted);
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
      return "SlotNum = " + slot + " Stack = " + ContainerTools.toString(getStack());
    }

  }
}
