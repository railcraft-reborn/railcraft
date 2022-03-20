package mods.railcraft.util.container.manipulator;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import mods.railcraft.util.container.ContainerTools;
import net.minecraft.world.item.ItemStack;

class ManipulatorUtil {

  static int tryPut(List<SlotAccessor> slots, ItemStack stack, int injected, boolean simulate) {
    if (injected >= stack.getCount())
      return injected;
    for (var slot : slots) {
      int amountToInsert = stack.getCount() - injected;
      ItemStack remainder = slot.grow(ContainerTools.copy(stack, amountToInsert), simulate);
      if (remainder.isEmpty())
        return stack.getCount();
      injected += amountToInsert - remainder.getCount();
      if (injected >= stack.getCount())
        return injected;
    }
    return injected;
  }

  static boolean tryRemove(CompositeContainerManipulator<?> comp, int amount, Predicate<ItemStack> filter,
      boolean simulate) {
    var amountNeeded = new AtomicInteger(amount);
    comp.streamContainers()
        .takeWhile(inv -> {
          var stacks = inv.extractItems(amountNeeded.getPlain(), filter, simulate);
          return amountNeeded.addAndGet(-stacks.stream().mapToInt(ItemStack::getCount).sum()) > 0;
        })
        .close();
    return amountNeeded.getPlain() == 0;
  }
}
