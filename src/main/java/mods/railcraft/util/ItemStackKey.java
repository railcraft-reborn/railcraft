package mods.railcraft.util;

import java.util.Objects;
import net.minecraft.world.item.ItemStack;

public record ItemStackKey(ItemStack itemStack) {

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof ItemStackKey other) {
      return ItemStack.isSameItem(this.itemStack, other.itemStack);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.itemStack.getItem(),
        this.itemStack.getDamageValue(),
        this.itemStack.getTag());
  }

  public ItemStack copyStack() {
    return this.itemStack.copy();
  }

  public static ItemStackKey make(ItemStack stack) {
    return new ItemStackKey(stack);
  }
}
