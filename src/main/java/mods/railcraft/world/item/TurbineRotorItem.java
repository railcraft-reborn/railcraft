package mods.railcraft.world.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TurbineRotorItem extends Item {

  public TurbineRotorItem(Properties properties) {
    super(properties);
  }

  @Override
  public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
    return false;
  }
}
