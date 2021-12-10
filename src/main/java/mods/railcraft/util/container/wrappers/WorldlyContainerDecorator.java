package mods.railcraft.util.container.wrappers;

import java.util.Iterator;
import com.google.common.collect.Iterators;
import mods.railcraft.util.container.ContainerAdaptor;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;

/**
 * Wrapper class used to bake the side variable into the object itself instead of passing it around
 * to all the inventory tools.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class WorldlyContainerDecorator extends AbstractContainerMapper implements WorldlyContainer {

  private final WorldlyContainer container;
  private final Direction side;

  public WorldlyContainerDecorator(WorldlyContainer container, Direction side) {
    this(container, side, true);
  }

  public WorldlyContainerDecorator(WorldlyContainer container, Direction side, boolean checkItems) {
    super(container, checkItems);
    this.container = container;
    this.side = side;
  }

  @Override
  public Iterator<ContainerAdaptor> adaptors() {
    return Iterators.singletonIterator(ContainerAdaptor.of(this));
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return container.getSlotsForFace(side);
  }

  @Override
  public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction s) {
    return !checkItems() || container.canPlaceItemThroughFace(slot, stack, side);
  }

  @Override
  public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction s) {
    return !checkItems() || container.canTakeItemThroughFace(slot, stack, side);
  }
}
