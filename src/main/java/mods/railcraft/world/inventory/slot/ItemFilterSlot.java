package mods.railcraft.world.inventory.slot;

import java.util.function.Predicate;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class ItemFilterSlot extends RailcraftSlot {

  private final Predicate<ItemStack> filter;

  public ItemFilterSlot(Predicate<ItemStack> filter, Container container, int slotIndex,
      int posX, int posY) {
    super(container, slotIndex, posX, posY);
    this.filter = filter;
    this.setStackLimit(64);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return filter.test(stack);
  }
}
