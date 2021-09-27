package mods.railcraft.util.inventory;

import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class ItemHandlerInventoryIterator {

  private abstract static class Base<I extends IItemHandler, S extends IInvSlot>
      extends InventoryIterator<S> {

    protected final I inv;

    protected Base(I inv) {
      this.inv = inv;
    }

    @Override
    public Iterator<S> iterator() {
      return new Iterator<S>() {
        int slot;

        @Override
        public boolean hasNext() {
          return slot < inv.getSlots();
        }

        @Override
        public S next() {
          return slot(slot++);
        }

        @Override
        public void remove() {
          throw new UnsupportedOperationException("Remove not supported.");
        }

      };
    }

    protected class InvSlot implements IInvSlot {

      protected final int slot;

      public InvSlot(int slot) {
        this.slot = slot;
      }

      @Override
      public int getIndex() {
        return slot;
      }

      @Override
      public boolean canPutStackInSlot(ItemStack stack) {
        return inv.isItemValid(slot, stack);
      }

      @Override
      public boolean canTakeStackFromSlot() {
        return !removeFromSlot(1, true).isEmpty();
      }

      @Override
      public ItemStack decreaseStack() {
        return inv.extractItem(slot, 1, false);
      }

      @Override
      public ItemStack removeFromSlot(int amount, boolean simulate) {
        return inv.extractItem(slot, amount, simulate);
      }

      @Override
      public ItemStack addToSlot(ItemStack stack, boolean simulate) {
        return inv.insertItem(slot, stack, simulate);
      }

      @Override
      public ItemStack getStack() {
        return InvTools.makeSafe(inv.getStackInSlot(slot));
      }

      @Override
      public int maxSlotStackSize() {
        return inv.getSlotLimit(getIndex());
      }
    }
  }

  public static class Standard extends Base<IItemHandler, IInvSlot> {
    public Standard(IItemHandler inv) {
      super(inv);
    }

    @Override
    public IInvSlot slot(int index) {
      return new InvSlot(index);
    }
  }

  public static class Modifiable extends Base<IItemHandlerModifiable, IExtInvSlot> {
    public Modifiable(IItemHandlerModifiable inv) {
      super(inv);
    }

    @Override
    public IExtInvSlot slot(int index) {
      return new ExtInvSlot(index);
    }

    protected class ExtInvSlot extends InvSlot implements IExtInvSlot {
      public ExtInvSlot(int slot) {
        super(slot);
      }

      @Override
      public void setStack(ItemStack stack) {
        inv.setStackInSlot(slot, stack);
      }
    }
  }
}
