package mods.railcraft.util.container.manipulator;

import java.util.List;
import mods.railcraft.util.container.ContainerTools;
import net.minecraft.world.item.ItemStack;

class ManipulatorUtil {

  static int tryPut(List<SlotAccessor> slots, ItemStack stack, int injected, boolean simulate) {
    if (injected >= stack.getCount())
      return injected;
    for (var slot : slots) {
      int amountToInsert = stack.getCount() - injected;
      ItemStack remainder = slot.insert(ContainerTools.copy(stack, amountToInsert), simulate);
      if (remainder.isEmpty())
        return stack.getCount();
      injected += amountToInsert - remainder.getCount();
      if (injected >= stack.getCount())
        return injected;
    }
    return injected;
  }
}
