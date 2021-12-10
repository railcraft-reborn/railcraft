package mods.railcraft.advancements.criterion;

import mods.railcraft.api.item.MinecartFactory;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;

final class CartItemPredicate extends ItemPredicate {

  @Override
  public boolean matches(ItemStack stack) {
    Item item = stack.getItem();
    return item instanceof MinecartFactory || item instanceof MinecartItem;
  }
}
