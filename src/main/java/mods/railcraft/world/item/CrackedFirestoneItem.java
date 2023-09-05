package mods.railcraft.world.item;

import net.minecraft.world.item.ItemStack;

public class CrackedFirestoneItem extends RefinedFirestoneItem {

  public static final int HEAT = 100;

  public CrackedFirestoneItem(Properties properties) {
    super(properties);
    this.heat = HEAT;
  }

  public static ItemStack getItemEmpty() {
    ItemStack itemStack = RailcraftItems.CRACKED_FIRESTONE.get().getDefaultInstance();
    itemStack.setDamageValue(CHARGES - 1);
    return itemStack;
  }

  @Override
  public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
    double damageLevel = (double) itemStack.getDamageValue() / (double) itemStack.getMaxDamage();
    if (random.nextDouble() < damageLevel * 0.0001) {
      return RailcraftItems.RAW_FIRESTONE.get().getDefaultInstance();
    }
    ItemStack newStack = itemStack.copyWithCount(1);
    return newStack.hurt(1, random, null) ? ItemStack.EMPTY : newStack;
  }
}
