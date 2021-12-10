package mods.railcraft.util.container;

import java.util.Iterator;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class WorldlyContainerIterator extends StandardContainerIterator {

  private final WorldlyContainer container;

  protected WorldlyContainerIterator(WorldlyContainer container) {
    super(container);
    this.container = container;
  }

  @Override
  public Iterator<ModifiableContainerSlot> iterator() {
    return new Iterator<ModifiableContainerSlot>() {
      final int[] slots = container.getSlotsForFace(Direction.DOWN);
      int index;

      @Override
      public boolean hasNext() {
        return slots != null && index < slots.length;
      }

      @Override
      public ModifiableContainerSlot next() {
        return slot(slots[index++]);
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Remove not supported.");
      }

    };
  }

  @Override
  public ModifiableContainerSlot slot(int index) {
    return new Slot(index);
  }

  private class Slot extends StandardContainerIterator.Slot {

    public Slot(int slot) {
      super(slot);
    }

    @Override
    public boolean canPutStackInSlot(ItemStack stack) {
      return container.canPlaceItemThroughFace(slot, stack, Direction.DOWN);
    }

    @Override
    public boolean canTakeStackFromSlot() {
      return container.canTakeItemThroughFace(slot, getStack(), Direction.DOWN);
    }
  }
}
