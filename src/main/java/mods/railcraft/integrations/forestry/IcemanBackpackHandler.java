package mods.railcraft.integrations.forestry;

import forestry.api.core.backpacks.BackpackEvent;
import forestry.api.core.backpacks.BackpackResupplyEvent;
import forestry.api.core.backpacks.BackpackStowEvent;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;

public final class IcemanBackpackHandler {

  private static final int SNOWBALLS_PER_BLOCK = 4;
  private static final int MAX_SNOWBALLS = 16;
  private static final int MIN_SNOWBALLS = 8;

  private IcemanBackpackHandler() {}

  @SubscribeEvent
  public static void handleStow(BackpackStowEvent event) {
    handle(event);
  }

  @SubscribeEvent
  public static void handleResupply(BackpackResupplyEvent event) {
    handle(event);
  }

  private static void handle(BackpackEvent event) {
    if (event.backpackDefinition == RailcraftBackpacks.ICEMAN) {
      manageSnow(event.backpackInventory);
    }
  }

  static void manageSnow(Container inventory) {
    int snowballs = count(inventory, Items.SNOWBALL);
    boolean changed = false;
    while (snowballs > MAX_SNOWBALLS
        && canFit(inventory, new ItemStack(Items.SNOW_BLOCK))
        && remove(inventory, Items.SNOWBALL, SNOWBALLS_PER_BLOCK)) {
      add(inventory, new ItemStack(Items.SNOW_BLOCK));
      snowballs -= SNOWBALLS_PER_BLOCK;
      changed = true;
    }
    while (snowballs < MIN_SNOWBALLS
        && canFit(inventory, new ItemStack(Items.SNOWBALL, SNOWBALLS_PER_BLOCK))
        && remove(inventory, Items.SNOW_BLOCK, 1)) {
      add(inventory, new ItemStack(Items.SNOWBALL, SNOWBALLS_PER_BLOCK));
      snowballs += SNOWBALLS_PER_BLOCK;
      changed = true;
    }
    if (changed) {
      inventory.setChanged();
    }
  }

  private static int count(Container inventory, Item item) {
    int count = 0;
    for (int i = 0; i < inventory.getContainerSize(); i++) {
      var stack = inventory.getItem(i);
      if (stack.is(item)) {
        count += stack.getCount();
      }
    }
    return count;
  }

  private static boolean remove(Container inventory, Item item, int amount) {
    if (count(inventory, item) < amount) {
      return false;
    }
    for (int i = 0; i < inventory.getContainerSize() && amount > 0; i++) {
      var stack = inventory.getItem(i);
      if (stack.is(item)) {
        int taken = Math.min(amount, stack.getCount());
        stack.shrink(taken);
        if (stack.isEmpty()) {
          inventory.setItem(i, ItemStack.EMPTY);
        }
        amount -= taken;
      }
    }
    return true;
  }

  private static int maxStackSize(Container inventory, ItemStack stack) {
    return Math.min(inventory.getMaxStackSize(), stack.getMaxStackSize());
  }

  private static boolean canFit(Container inventory, ItemStack stack) {
    int remaining = stack.getCount();
    for (int i = 0; i < inventory.getContainerSize(); i++) {
      if (!inventory.canPlaceItem(i, stack)) {
        continue;
      }
      var slot = inventory.getItem(i);
      if (slot.isEmpty()) {
        remaining -= maxStackSize(inventory, stack);
      } else if (ItemStack.isSameItemSameComponents(slot, stack)) {
        remaining -= maxStackSize(inventory, stack) - slot.getCount();
      }
      if (remaining <= 0) {
        return true;
      }
    }
    return false;
  }

  private static void add(Container inventory, ItemStack stack) {
    // Top up partial stacks first, then use empty slots
    for (int i = 0; i < inventory.getContainerSize() && !stack.isEmpty(); i++) {
      var slot = inventory.getItem(i);
      if (!slot.isEmpty() && ItemStack.isSameItemSameComponents(slot, stack)
          && inventory.canPlaceItem(i, stack)) {
        int moved = Math.min(stack.getCount(), maxStackSize(inventory, stack) - slot.getCount());
        if (moved > 0) {
          slot.grow(moved);
          stack.shrink(moved);
        }
      }
    }
    for (int i = 0; i < inventory.getContainerSize() && !stack.isEmpty(); i++) {
      if (inventory.getItem(i).isEmpty() && inventory.canPlaceItem(i, stack)) {
        int moved = Math.min(stack.getCount(), maxStackSize(inventory, stack));
        inventory.setItem(i, stack.split(moved));
      }
    }
  }
}
