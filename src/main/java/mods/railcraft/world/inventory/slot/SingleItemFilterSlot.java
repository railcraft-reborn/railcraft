package mods.railcraft.world.inventory.slot;

import java.util.function.Predicate;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class SingleItemFilterSlot extends ItemFilterSlot {

  public SingleItemFilterSlot(Predicate<ItemStack> filter, Container container, int slotIndex,
      int posX, int posY) {
    super(filter, container, slotIndex, posX, posY);
    this.setStackLimit(1);
    this.setPhantom();
  }
}
