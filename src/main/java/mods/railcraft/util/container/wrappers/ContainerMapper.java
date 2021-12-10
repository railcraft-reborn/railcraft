package mods.railcraft.util.container.wrappers;

import java.util.Iterator;
import java.util.function.Predicate;
import com.google.common.collect.Iterators;
import mods.railcraft.util.Predicates;
import mods.railcraft.util.container.ContainerAdaptor;
import mods.railcraft.util.container.filters.StackFilters;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/**
 * Wrapper class used to specify part of an existing inventory to be treated as a complete
 * inventory. Used primarily to map a side of an ISidedInventory, but it is also helpful for complex
 * inventories such as the Tunnel Bore.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class ContainerMapper extends AbstractContainerMapper {

  private final Container container;
  private final int start;
  private final int size;
  private int stackSizeLimit = -1;
  private Predicate<ItemStack> filter = StackFilters.ALL;

  public static ContainerMapper make(Container container) {
    return new ContainerMapper(container, 0, container.getContainerSize());
  }

  public static ContainerMapper make(Container container, int start, int size) {
    return new ContainerMapper(container, start, size);
  }

  /**
   * Creates a new {@link ContainerMapper}
   *
   * @param container - the backing container
   * @param start - the starting index
   * @param size - the size of the new container, take care not to exceed the end of the backing
   *        container
   */
  public ContainerMapper(Container container, int start, int size) {
    super(container);
    this.container = container;
    this.start = start;
    this.size = size;
  }

  /**
   * If called the container will ignore isItemValidForSlot checks.
   */
  public ContainerMapper ignoreItemChecks() {
    checkItems = false;
    return this;
  }

  @SafeVarargs
  public final ContainerMapper withFilters(Predicate<ItemStack>... filters) {
    this.filter = Predicates.and(filter, filters);
    return this;
  }

  public Predicate<ItemStack> filter() {
    return filter;
  }

  public ContainerMapper withStackSizeLimit(int limit) {
    stackSizeLimit = limit;
    return this;
  }

  @Override
  public Iterator<ContainerAdaptor> adaptors() {
    return Iterators.singletonIterator(ContainerAdaptor.of(this));
  }

  @Override
  public int getContainerSize() {
    return size;
  }

  @Override
  public ItemStack getItem(int slot) {
    validSlot(slot);
    return container.getItem(start + slot);
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    validSlot(slot);
    return container.removeItem(start + slot, amount);
  }

  @Override
  public void setItem(int slot, ItemStack itemstack) {
    validSlot(slot);
    container.setItem(start + slot, itemstack);
  }

  @Override
  public int getMaxStackSize() {
    return stackSizeLimit > 0 ? stackSizeLimit : container.getMaxStackSize();
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    validSlot(slot);
    return !checkItems() || (filter.test(stack) && container.canPlaceItem(start + slot, stack));
  }

  public boolean containsSlot(int absoluteIndex) {
    return absoluteIndex >= start && absoluteIndex < start + size;
  }

  private void validSlot(int slot) {
    if (slot < 0 || slot >= size)
      throw new IllegalArgumentException("Slot index out of bounds.");
  }
}
