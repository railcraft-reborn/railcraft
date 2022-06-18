package mods.railcraft.util.container;

import java.util.function.Predicate;
import mods.railcraft.util.Predicates;
import mods.railcraft.util.container.manipulator.VanillaContainerManipulator;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Wraps a {@link Container}, treating a specific portion of it as a separate {@link Container}.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class ContainerMapper implements Container, VanillaContainerManipulator {

  private final Container container;
  private boolean checkItems = true;
  private final int start;
  private final int size;
  private int stackSizeLimit = -1;
  private Predicate<ItemStack> filter = StackFilter.ALL;

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
  public final ContainerMapper addFilters(Predicate<ItemStack>... filters) {
    this.filter = Predicates.and(this.filter, filters);
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
    return !this.checkItems || (filter.test(stack) && container.canPlaceItem(start + slot, stack));
  }

  @Override
  public Container getContainer() {
    return this;
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    return this.container.removeItemNoUpdate(slot);
  }

  @Override
  public void setChanged() {
    this.container.setChanged();
  }

  @Override
  public boolean stillValid(Player player) {
    return this.container.stillValid(player);
  }

  @Override
  public void startOpen(Player player) {
    this.container.startOpen(player);
  }

  @Override
  public void stopOpen(Player player) {
    this.container.stopOpen(player);
  }

  @Override
  public void clearContent() {
    this.container.clearContent();
  }

  public boolean checkItems() {
    return this.checkItems;
  }

  @Override
  public boolean isEmpty() {
    return this.container.isEmpty();
  }

  public boolean containsSlot(int absoluteIndex) {
    return absoluteIndex >= start && absoluteIndex < start + size;
  }

  private void validSlot(int slot) {
    if (slot < 0 || slot >= size)
      throw new IllegalArgumentException("Slot index out of bounds.");
  }
}
