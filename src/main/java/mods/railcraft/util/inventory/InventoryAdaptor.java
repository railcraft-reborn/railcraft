package mods.railcraft.util.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import mods.railcraft.util.inventory.wrappers.SidedInventoryDecorator;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.items.IItemHandler;

/**
 * Represents a single Inventory object.
 *
 * It operates as an adaptor class to provide a single wrapper interface for a variety if inventory
 * APIs.
 *
 * Supported inventory API objects include: {@link IInventory}, {@link ISidedInventory}, and
 * {@link IItemHandler}
 *
 * Most manipulation functions are implemented through an {@link InventoryIterator} where the actual
 * polymorphism happens.
 *
 * This particular framework is designed to operation on slot based APIs. If an API is not slot
 * based, it will probably be difficult to adapt this framework to it.
 *
 * Created by CovertJaguar on 3/15/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class InventoryAdaptor implements IInventoryManipulator {

  private final InventoryIterator<?> inventory;

  private InventoryAdaptor(InventoryIterator<?> inventory) {
    this.inventory = inventory;
  }

  public InventoryIterator<?> getBackingObject() {
    return inventory;
  }

  public static InventoryAdaptor of(final IInventory inventory) {
    Objects.requireNonNull(inventory);
    return new InventoryAdaptor(InventoryIterator.get(inventory)) {

      @Override
      public int slotCount() {
        return inventory.getContainerSize();
      }
    };
  }

  public static InventoryAdaptor of(final IItemHandler inventory) {
    Objects.requireNonNull(inventory);
    return new InventoryAdaptor(InventoryIterator.get(inventory)) {

      @Override
      public int slotCount() {
        return inventory.getSlots();
      }
    };
  }

  static InventoryAdaptor of(ISidedInventory sidedInventory, Direction direction) {
    return of(new SidedInventoryDecorator(sidedInventory, direction));
  }

  @Override
  public boolean canFit(ItemStack stack) {
    return addStack(stack, true).isEmpty();
  }

  /**
   * Attempt to add the stack to the inventory returning the remainder.
   *
   * If the entire stack was accepted, it returns an empty stack.
   *
   * @return The remainder
   */
  @Override
  public ItemStack addStack(ItemStack stack, boolean simulate) {
    if (stack.isEmpty())
      return ItemStack.EMPTY;
    stack = stack.copy();
    List<IInvSlot> filledSlots = new ArrayList<>();
    List<IInvSlot> emptySlots = new ArrayList<>();
    for (IInvSlot slot : this.inventory) {
      if (slot.canPutStackInSlot(stack)) {
        if (slot.getStack().isEmpty())
          emptySlots.add(slot);
        else
          filledSlots.add(slot);
      }
    }

    int injected = 0;
    injected = InvTools.tryPut(filledSlots, stack, injected, simulate);
    injected = InvTools.tryPut(emptySlots, stack, injected, simulate);
    InvTools.decSize(stack, injected);
    if (stack.isEmpty())
      return ItemStack.EMPTY;
    return stack;
  }

  /**
   * Removes up to maxAmount items in one slot matching the filter.
   */
  @Override
  public ItemStack removeStack(int maxAmount, Predicate<ItemStack> filter, boolean simulate) {
    for (IInvSlot slot : this.inventory) {
      ItemStack stack = slot.getStack();
      if (!stack.isEmpty() && slot.canTakeStackFromSlot() && filter.test(stack)) {
        return slot.removeFromSlot(maxAmount, simulate);
      }
    }
    return ItemStack.EMPTY;
  }

  @Override
  public List<ItemStack> extractItems(int maxAmount, Predicate<ItemStack> filter,
      boolean simulate) {
    int amountNeeded = maxAmount;
    List<ItemStack> outputList = new ArrayList<>();
    for (IInvSlot slot : this.inventory) {
      if (amountNeeded <= 0)
        break;
      ItemStack stack = slot.getStack();
      if (!stack.isEmpty() && slot.canTakeStackFromSlot() && filter.test(stack)) {
        ItemStack output = slot.removeFromSlot(amountNeeded, simulate);
        if (!output.isEmpty()) {
          amountNeeded -= output.getCount();
          outputList.add(output);
        }
      }
    }
    return outputList;
  }

  @Override
  public ItemStack moveOneItemTo(IInventoryManipulator dest, Predicate<ItemStack> filter) {
    for (IInvSlot slot : this.inventory) {
      if (slot.hasStack() && slot.canTakeStackFromSlot() && slot.matches(filter)) {
        ItemStack stack = slot.getStack();
        stack = InvTools.copyOne(stack);
        stack = dest.addStack(stack);
        if (stack.isEmpty())
          return slot.decreaseStack();
      }
    }
    return ItemStack.EMPTY;
  }

  @Override
  public Stream<? extends IInvSlot> streamSlots() {
    return this.inventory.stream();
  }

  @Override
  public Stream<ItemStack> streamStacks() {
    return this.inventory.streamStacks();
  }
}
