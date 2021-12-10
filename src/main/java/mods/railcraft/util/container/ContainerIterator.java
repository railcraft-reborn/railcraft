package mods.railcraft.util.container;

import java.util.Objects;
import java.util.stream.Stream;
import com.google.common.collect.Streams;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class ContainerIterator<T extends ContainerSlot> implements Iterable<T> {

  public static ContainerIterator<ModifiableContainerSlot> get(Container container) {
    return container instanceof WorldlyContainer wordly
        ? new WorldlyContainerIterator(wordly)
        : new StandardContainerIterator(container);
  }

  public static ContainerIterator<ContainerSlot> get(IItemHandler itemHandler) {
    return new ItemHandlerIterator.Standard(itemHandler);
  }

  /**
   * Only use this on inventories we control.
   */
  public static ContainerIterator<ModifiableContainerSlot> get(IItemHandlerModifiable itemHandler) {
    return new ItemHandlerIterator.Modifiable(itemHandler);
  }

  public static ContainerIterator<? extends ContainerSlot> get(ContainerAdaptor container) {
    Objects.requireNonNull(container.getBackingObject());
    if (container.getBackingObject() instanceof WorldlyContainer)
      return new WorldlyContainerIterator((WorldlyContainer) container.getBackingObject());
    if (container.getBackingObject() instanceof Container)
      return new StandardContainerIterator((Container) container.getBackingObject());
    if (container.getBackingObject() instanceof IItemHandler)
      return new ItemHandlerIterator.Standard((IItemHandler) container.getBackingObject());
    throw new IllegalArgumentException("Invalid container: " + container.getClass().getName());
  }

  public abstract T slot(int index);

  public Stream<T> stream() {
    return Streams.stream(this);
  }

  public Stream<ItemStack> streamStacks() {
    return stream().filter(ContainerSlot::hasStack).map(ContainerSlot::getStack);
  }
}
