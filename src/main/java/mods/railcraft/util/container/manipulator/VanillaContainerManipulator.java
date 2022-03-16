package mods.railcraft.util.container.manipulator;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import mods.railcraft.util.container.ContainerTools;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/**
 * An implementation of {@link ContainerManipulator} for Minecraft's {@link Container}.
 * 
 * @author Sm0keySa1m0n
 */
@FunctionalInterface
public interface VanillaContainerManipulator extends ContainerManipulator<ModifiableSlotAccessor> {

  Container getContainer();

  @Override
  default int slotCount() {
    return this.getContainer().getContainerSize();
  }

  @Override
  default Stream<ModifiableSlotAccessor> stream() {
    return IntStream.range(0, this.getContainer().getContainerSize())
        .mapToObj(index -> new VanillaSlotAccessor<>(this.getContainer(), index));
  }

  public class VanillaSlotAccessor<T extends Container> implements ModifiableSlotAccessor {

    private final T container;
    private final int index;

    public VanillaSlotAccessor(T container, int index) {
      this.container = container;
      this.index = index;
    }

    public T getContainer() {
      return this.container;
    }

    public int getIndex() {
      return this.index;
    }

    @Override
    public ItemStack getItem() {
      return this.container.getItem(index);
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
    public boolean canRemoveItem() {
      return true;
    }

    @Override
    public ItemStack shrink() {
      return this.container.removeItem(this.index, 1);
    }

    @Override
    public ItemStack shrink(int amount, boolean simulate) {
      if (!simulate) {
        return this.container.removeItem(this.index, amount);
      }
      ItemStack stack = getItem();
      return ContainerTools.copy(stack, Math.min(amount, stack.getCount()));
    }

    @Override
    public ItemStack grow(ItemStack stack, boolean simulate) {
      int available = stack.getCount();
      if (available <= 0) {
        return stack.copy();
      }
      int max = Math.min(stack.getMaxStackSize(), this.container.getMaxStackSize());
      int wanted = 0;

      ItemStack stackInSlot = getItem();
      if (stackInSlot.isEmpty()) {
        wanted = Math.min(available, max);
        if (wanted > 0 && !simulate) {
          setItem(ContainerTools.copy(stack, wanted));
        }
      } else if (ContainerTools.isItemEqual(stack, stackInSlot)) {
        wanted = Math.min(available, max - stackInSlot.getCount());
        if (wanted > 0 && !simulate) {
          var newStack = stackInSlot.copy();
          newStack.grow(wanted);
          this.setItem(newStack);
        }
      }
      return ContainerTools.copy(stack, available - wanted);
    }

    @Override
    public int maxStackSize() {
      return this.container.getMaxStackSize();
    }
  }
}
