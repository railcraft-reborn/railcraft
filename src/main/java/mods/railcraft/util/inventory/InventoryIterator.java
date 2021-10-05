package mods.railcraft.util.inventory;

import java.util.Objects;
import java.util.stream.Stream;
import com.google.common.collect.Streams;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class InventoryIterator<T extends IInvSlot> implements Iterable<T> {

  public static InventoryIterator<IExtInvSlot> get(IInventory inv) {
    if (inv instanceof ISidedInventory)
      return new SidedInventoryIterator((ISidedInventory) inv);
    return new StandardInventoryIterator(inv);
  }

  public static InventoryIterator<IInvSlot> get(IItemHandler inv) {
    return new ItemHandlerInventoryIterator.Standard(inv);
  }

  /**
   * Only use this on inventories we control.
   */
  public static InventoryIterator<IExtInvSlot> get(IItemHandlerModifiable inv) {
    return new ItemHandlerInventoryIterator.Modifiable(inv);
  }

  public static InventoryIterator<? extends IInvSlot> get(InventoryAdaptor inv) {
    Objects.requireNonNull(inv.getBackingObject());
    if (inv.getBackingObject() instanceof ISidedInventory)
      return new SidedInventoryIterator((ISidedInventory) inv.getBackingObject());
    if (inv.getBackingObject() instanceof IInventory)
      return new StandardInventoryIterator((IInventory) inv.getBackingObject());
    if (inv.getBackingObject() instanceof IItemHandler)
      return new ItemHandlerInventoryIterator.Standard((IItemHandler) inv.getBackingObject());
    throw new IllegalArgumentException("Invalid Inventory Object");
  }

  public abstract T slot(int index);

  public Stream<T> stream() {
    return Streams.stream(this);
  }

  public Stream<ItemStack> streamStacks() {
    return stream().filter(IInvSlot::hasStack).map(IInvSlot::getStack);
  }
}
