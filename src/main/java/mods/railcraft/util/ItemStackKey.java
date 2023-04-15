/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 https://railcraft.info
 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at https://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/

package mods.railcraft.util;

import mods.railcraft.util.container.ContainerTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/**
 * Created by CovertJaguar on 10/28/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public record ItemStackKey(ItemStack stack) {

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ItemStackKey other = (ItemStackKey) obj;
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

  public ItemStack copyStack() {
    return this.stack.copy();
  }

  public static ItemStackKey make(ItemStack stack) {
    return new ItemStackKey(stack);
  }
}
