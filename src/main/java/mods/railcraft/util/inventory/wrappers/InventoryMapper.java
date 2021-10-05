package mods.railcraft.util.inventory.wrappers;

import java.util.function.Predicate;
import mods.railcraft.util.Predicates;
import mods.railcraft.util.inventory.filters.StackFilters;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Wrapper class used to specify part of an existing inventory to be treated as a complete
 * inventory. Used primarily to map a side of an ISidedInventory, but it is also helpful for complex
 * inventories such as the Tunnel Bore.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class InventoryMapper extends InvWrapperBase {

  private final IInventory inv;
  private final int start;
  private final int size;
  private int stackSizeLimit = -1;
  private Predicate<ItemStack> filter = StackFilters.ALL;

  public static InventoryMapper make(IInventory inv) {
    return new InventoryMapper(inv, 0, inv.getContainerSize());
  }

  public static InventoryMapper make(IInventory inv, int start, int size) {
    return new InventoryMapper(inv, start, size);
  }

  /**
   * Creates a new InventoryMapper
   *
   * @param inv The backing inventory
   * @param start The starting index
   * @param size The size of the new inventory, take care not to exceed the end of the backing
   *        inventory
   */
  public InventoryMapper(IInventory inv, int start, int size) {
    super(inv);
    this.inv = inv;
    this.start = start;
    this.size = size;
  }

  /**
   * If called the inventory will ignore isItemValidForSlot checks.
   */
  public InventoryMapper ignoreItemChecks() {
    checkItems = false;
    return this;
  }

  @SafeVarargs
  public final InventoryMapper withFilters(Predicate<ItemStack>... filters) {
    this.filter = Predicates.and(filter, filters);
    return this;
  }

  public Predicate<ItemStack> filter() {
    return filter;
  }

  public InventoryMapper withStackSizeLimit(int limit) {
    stackSizeLimit = limit;
    return this;
  }

  @Override
  public int getContainerSize() {
    return size;
  }

  @Override
  public ItemStack getItem(int slot) {
    validSlot(slot);
    return inv.getItem(start + slot);
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    validSlot(slot);
    return inv.removeItem(start + slot, amount);
  }

  @Override
  public void setItem(int slot, ItemStack itemstack) {
    validSlot(slot);
    inv.setItem(start + slot, itemstack);
  }

  @Override
  public int getMaxStackSize() {
    return stackSizeLimit > 0 ? stackSizeLimit : inv.getMaxStackSize();
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    validSlot(slot);
    return !checkItems() || (filter.test(stack) && inv.canPlaceItem(start + slot, stack));
  }

  public boolean containsSlot(int absoluteIndex) {
    return absoluteIndex >= start && absoluteIndex < start + size;
  }

  private void validSlot(int slot) {
    if (slot < 0 || slot >= size)
      throw new IllegalArgumentException("Slot index out of bounds.");
  }
}
