package mods.railcraft.world.inventory;

import java.util.function.Predicate;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class SlotStackFilter extends SlotRailcraft {

  private final Predicate<ItemStack> filter;

  public SlotStackFilter(Predicate<ItemStack> filter, IInventory iinventory, int slotIndex,
      int posX, int posY) {
    super(iinventory, slotIndex, posX, posY);
    this.filter = filter;
    setStackLimit(64);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return filter.test(stack);
  }
}
