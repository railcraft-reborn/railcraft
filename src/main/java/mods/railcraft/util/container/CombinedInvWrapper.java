package mods.railcraft.util.container;

import org.jetbrains.annotations.NotNull;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

public class CombinedInvWrapper implements IItemHandlerModifiable {

  private final Container inputInv;
  private final Container outputInv;

  public CombinedInvWrapper(Container inputInv, Container outputInv) {
    this.inputInv = inputInv;
    this.outputInv = outputInv;
  }

  @Override
  public void setStackInSlot(int slot, @NotNull ItemStack stack) {
    if (slot < this.inputInv.getContainerSize()) {
      this.inputInv.setItem(slot, stack);
    }
  }

  @Override
  public int getSlots() {
    return this.inputInv.getContainerSize() + this.outputInv.getContainerSize();
  }

  @Override
  @NotNull
  public ItemStack getStackInSlot(int slot) {
    if (slot < this.inputInv.getContainerSize()) {
      return this.inputInv.getItem(slot);
    } else {
      return this.outputInv.getItem(slot - this.inputInv.getContainerSize());
    }
  }

  @Override
  @NotNull
  public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
    if (slot >= this.inputInv.getContainerSize()) {
      return stack;
    }

    if (stack.isEmpty()) {
      return ItemStack.EMPTY;
    }

    ItemStack stackInSlot = this.inputInv.getItem(slot);

    int m;
    if (!stackInSlot.isEmpty()) {
      if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), getSlotLimit(slot))) {
        return stack;
      }

      if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot)) {
        return stack;
      }

      if (!this.inputInv.canPlaceItem(slot, stack)) {
        return stack;
      }

      m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - stackInSlot.getCount();

      if (stack.getCount() <= m) {
        if (!simulate) {
          ItemStack copy = stack.copy();
          copy.grow(stackInSlot.getCount());
          this.inputInv.setItem(slot, copy);
          this.inputInv.setChanged();
        }

        return ItemStack.EMPTY;
      } else {
        // copy the stack to not modify the original one
        stack = stack.copy();
        if (!simulate) {
          ItemStack copy = stack.split(m);
          copy.grow(stackInSlot.getCount());
          this.inputInv.setItem(slot, copy);
          this.inputInv.setChanged();
          return stack;
        } else {
          stack.shrink(m);
          return stack;
        }
      }
    } else {
      if (!this.inputInv.canPlaceItem(slot, stack)) {
        return stack;
      }

      m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
      if (m < stack.getCount()) {
        // copy the stack to not modify the original one
        stack = stack.copy();
        if (!simulate) {
          this.inputInv.setItem(slot, stack.split(m));
          this.inputInv.setChanged();
          return stack;
        } else {
          stack.shrink(m);
          return stack;
        }
      } else {
        if (!simulate) {
          this.inputInv.setItem(slot, stack);
          this.inputInv.setChanged();
        }
        return ItemStack.EMPTY;
      }
    }
  }

  @Override
  @NotNull
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (slot < this.inputInv.getContainerSize()) {
      return ItemStack.EMPTY;
    }

    slot = slot - this.inputInv.getContainerSize();

    if (amount == 0) {
      return ItemStack.EMPTY;
    }

    ItemStack stackInSlot = this.outputInv.getItem(slot);

    if (stackInSlot.isEmpty()) {
      return ItemStack.EMPTY;
    }

    if (simulate) {
      if (stackInSlot.getCount() < amount) {
        return stackInSlot.copy();
      } else {
        ItemStack copy = stackInSlot.copy();
        copy.setCount(amount);
        return copy;
      }
    } else {
      int m = Math.min(stackInSlot.getCount(), amount);

      ItemStack decrStackSize = this.outputInv.removeItem(slot, m);
      this.outputInv.setChanged();
      return decrStackSize;
    }
  }

  @Override
  public int getSlotLimit(int slot) {
    if (slot < this.inputInv.getContainerSize()) {
      return this.inputInv.getMaxStackSize();
    } else {
      return this.outputInv.getMaxStackSize();
    }
  }

  @Override
  public boolean isItemValid(int slot, @NotNull ItemStack stack) {
    if (slot < this.inputInv.getContainerSize()) {
      return this.inputInv.canPlaceItem(slot, stack);
    } else {
      return false;
    }
  }
}
