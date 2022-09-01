package mods.railcraft.world.item;

import mods.railcraft.util.container.ContainerTools;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class CrackedFirestoneItem extends RefinedFirestoneItem {

  public static final int HEAT = 100;

  public CrackedFirestoneItem(Properties properties) {
    super(properties);
    this.heat = HEAT;
  }

  public static ItemStack getItemCharged() {
    return RailcraftItems.CRACKED_FIRESTONE.get().getDefaultInstance();
  }

  public static ItemStack getItemEmpty() {
    ItemStack itemStack = RailcraftItems.CRACKED_FIRESTONE.get().getDefaultInstance();
    itemStack.setDamageValue(CHARGES - 1);
    return itemStack;
  }

  @Override
  public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
    double damageLevel = (double) itemStack.getDamageValue() / (double) itemStack.getMaxDamage();
    if (RandomSource.create().nextDouble() < damageLevel * 0.0001) {
      return RailcraftItems.RAW_FIRESTONE.get().getDefaultInstance();
    }
    ItemStack newStack = ContainerTools.copyOne(itemStack);
    return newStack.hurt(1, RandomSource.create(), null) ? ItemStack.EMPTY : newStack;
  }
}
