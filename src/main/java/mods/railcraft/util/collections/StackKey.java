/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 https://railcraft.info
 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at https://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/

package mods.railcraft.util.collections;

import net.minecraft.world.item.ItemStack;
import mods.railcraft.util.container.ContainerTools;
import net.minecraft.nbt.CompoundTag;

/**
 * Created by CovertJaguar on 10/28/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class StackKey {

  private final ItemStack stack;

  public StackKey(ItemStack stack) {
    this.stack = ContainerTools.copyOne(stack);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final StackKey other = (StackKey) obj;
    return ContainerTools.isItemEqual(stack, other.stack);
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 23 * hash + stack.getItem().hashCode();
    hash = 23 * hash + stack.getDamageValue();
    CompoundTag nbt = stack.getTag();
    if (nbt != null)
      hash = 23 * hash + nbt.hashCode();
    return hash;
  }

  public ItemStack get() {
    return stack.copy();
  }

  public static StackKey make(ItemStack stack) {
    return new StackKey(stack);
  }
}
