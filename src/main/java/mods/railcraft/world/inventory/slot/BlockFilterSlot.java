package mods.railcraft.world.inventory.slot;

import java.util.function.Predicate;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class BlockFilterSlot extends ItemFilterSlot {

  public BlockFilterSlot(Predicate<ItemStack> filter, Container container,
      int slotIndex, int posX, int posY) {
    super(filter, container, slotIndex, posX, posY);
    this.setPhantom();
    this.setStackLimit(1);
  }

  public BlockFilterSlot(Container container, int slotIndex, int posX, int posY) {
    this(itemStack -> itemStack.getItem() instanceof BlockItem, container, slotIndex, posX, posY);
  }
}
