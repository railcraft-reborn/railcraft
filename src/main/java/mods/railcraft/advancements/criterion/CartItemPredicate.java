package mods.railcraft.advancements.criterion;

import mods.railcraft.api.item.MinecartFactory;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MinecartItem;

final class CartItemPredicate extends ItemPredicate {

  @Override
  public boolean matches(ItemStack stack) {
    Item item = stack.getItem();
    return item instanceof MinecartFactory || item instanceof MinecartItem;
  }
}
