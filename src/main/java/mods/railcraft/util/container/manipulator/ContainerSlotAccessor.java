package mods.railcraft.util.container.manipulator;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class ContainerSlotAccessor<T extends Container> implements ModifiableSlotAccessor {

  protected final T container;
  protected final int index;

  private ContainerSlotAccessor(T container, int index) {
    this.container = container;
    this.index = index;
  }

  @Override
  public ItemStack item() {
    return this.container.getItem(this.index);
  }

  @Override
  public void setItem(ItemStack stack) {
    this.container.setItem(this.index, stack);
  }

  @Override
  public boolean isValid(ItemStack stack) {
    return this.container.canPlaceItem(this.index, stack);
  }

  @Override
  public ItemStack extract(int amount, boolean simulate) {
    if (simulate) {
      var remaining = this.item();
      var removed = remaining.split(amount);
      this.container.setItem(this.index, remaining);
      return removed;
    }

    return this.container.removeItem(this.index, amount);
  }

  @Override
  public ItemStack insert(ItemStack stack, boolean simulate) {
    if (stack.isEmpty()) {
      return ItemStack.EMPTY;
    }

    if (!this.container.canPlaceItem(this.index, stack)) {
      return stack;
    }

    var currentItem = this.item();
    if (!currentItem.isEmpty()) {
      if (currentItem.getCount() >= Math.min(currentItem.getMaxStackSize(), this.maxStackSize())) {
        return stack;
      }

      if (!ItemHandlerHelper.canItemStacksStack(stack, currentItem)) {
        return stack;
      }
    }

    var insertable =
        Math.min(stack.getMaxStackSize(), this.maxStackSize()) - currentItem.getCount();

    // copy the stack to not modify the original one
    stack = stack.copy();
    if (simulate) {
      stack.shrink(insertable);
    } else {
      var copy = stack.split(insertable);
      if (currentItem.getCount() > 0) {
        copy.grow(currentItem.getCount());
      }
      this.container.setItem(this.index, copy);
      this.container.setChanged();
    }

    return stack.isEmpty() ? ItemStack.EMPTY : stack;
  }

  @Override
  public int maxStackSize() {
    return this.container.getMaxStackSize();
  }

  public static Stream<ModifiableSlotAccessor> createSlots(Container container) {
    return IntStream.range(0, container.getContainerSize())
        .mapToObj(i -> new ContainerSlotAccessor<>(container, i));
  }

  public static Stream<ModifiableSlotAccessor> createSlots(WorldlyContainer container,
      Direction face) {
    return Arrays.stream(container.getSlotsForFace(face))
        .mapToObj(i -> new Worldly(container, i, face));
  }

  public static class Worldly extends ContainerSlotAccessor<WorldlyContainer> {

    private final Direction face;

    private Worldly(WorldlyContainer container, int index, Direction face) {
      super(container, index);
      this.face = face;
    }

    @Override
    public boolean isValid(ItemStack itemStack) {
      return this.container.canPlaceItemThroughFace(this.index, itemStack, this.face);
    }

    @Override
    public ItemStack extract(int amount, boolean simulate) {
      return this.container.canTakeItemThroughFace(this.index, this.item(), this.face)
          ? super.extract()
          : ItemStack.EMPTY;
    }
  }
}
