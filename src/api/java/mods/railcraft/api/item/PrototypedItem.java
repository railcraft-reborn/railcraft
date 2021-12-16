package mods.railcraft.api.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

/**
 * This interface marks an item that can have another item "added" to its NBT. Filter Items and Tank
 * Carts and Cargo Carts all do this. The benefit is that PrototypeRecipe can then be used to set
 * the prototype item.
 *
 *
 * Created by CovertJaguar on 6/7/2017 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface PrototypedItem {

  default boolean isValidPrototype(ItemStack stack) {
    return true;
  }

  default ItemStack setPrototype(ItemStack filter, ItemStack prototype) {
    filter = filter.copy();
    CompoundTag tag = new CompoundTag();
    prototype.save(tag);
    filter.addTagElement("prototype", tag);
    return filter;
  }

  default ItemStack getPrototype(ItemStack stack) {
    CompoundTag tag = stack.getTagElement("prototype");
    return tag == null ? ItemStack.EMPTY : ItemStack.of(tag);
  }
}
