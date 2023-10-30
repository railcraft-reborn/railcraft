package mods.railcraft.util.container;

import org.jetbrains.annotations.NotNull;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class CombinedInvWrapper implements IItemHandlerModifiable {

  private final InvWrapper inputInv;
  private final InvWrapper outputInv;

  public CombinedInvWrapper(Container inputInv, Container outputInv) {
    this.inputInv = new InvWrapper(inputInv);
    this.outputInv = new InvWrapper(outputInv);
  }

  @Override
  public void setStackInSlot(int slot, @NotNull ItemStack stack) {
    if (slot < this.inputInv.getSlots()) {
      this.inputInv.setStackInSlot(slot, stack);
    }
  }

  @Override
  public int getSlots() {
    return this.inputInv.getSlots() + this.outputInv.getSlots();
  }

  @Override
  @NotNull
  public ItemStack getStackInSlot(int slot) {
    if (slot < this.inputInv.getSlots()) {
      return this.inputInv.getStackInSlot(slot);
    } else {
      return this.outputInv.getStackInSlot(slot - this.inputInv.getSlots());
    }
  }

  @Override
  @NotNull
  public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
    if (slot >= this.inputInv.getSlots()) {
      return stack;
    }
    return this.inputInv.insertItem(slot, stack, simulate);
  }

  @Override
  @NotNull
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (slot < this.inputInv.getSlots()) {
      return ItemStack.EMPTY;
    }
    return this.outputInv.extractItem(slot - this.inputInv.getSlots(), amount, simulate);
  }

  @Override
  public int getSlotLimit(int slot) {
    if (slot < this.inputInv.getSlots()) {
      return this.inputInv.getSlotLimit(slot);
    } else {
      return this.outputInv.getSlotLimit(slot - this.inputInv.getSlots());
    }
  }

  @Override
  public boolean isItemValid(int slot, @NotNull ItemStack stack) {
    if (slot < this.inputInv.getSlots()) {
      return this.inputInv.isItemValid(slot, stack);
    } else {
      return false;
    }
  }
}
