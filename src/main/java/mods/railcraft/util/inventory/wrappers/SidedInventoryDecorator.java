package mods.railcraft.util.inventory.wrappers;

import java.util.Iterator;
import com.google.common.collect.Iterators;
import mods.railcraft.util.inventory.InventoryAdaptor;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

/**
 * Wrapper class used to bake the side variable into the object itself instead of passing it around
 * to all the inventory tools.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class SidedInventoryDecorator extends InvWrapperBase implements ISidedInventory {

  private final ISidedInventory inv;
  private final Direction side;

  public SidedInventoryDecorator(ISidedInventory inv, Direction side) {
    this(inv, side, true);
  }

  public SidedInventoryDecorator(ISidedInventory inv, Direction side, boolean checkItems) {
    super(inv, checkItems);
    this.inv = inv;
    this.side = side;
  }

  @Override
  public Iterator<InventoryAdaptor> adaptors() {
    return Iterators.singletonIterator(InventoryAdaptor.of(this));
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return inv.getSlotsForFace(side);
  }

  @Override
  public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction s) {
    return !checkItems() || inv.canPlaceItemThroughFace(slot, stack, side);
  }

  @Override
  public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction s) {
    return !checkItems() || inv.canTakeItemThroughFace(slot, stack, side);
  }
}
