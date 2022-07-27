package mods.railcraft.world.item;

import java.util.concurrent.ThreadLocalRandom;
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
  public ItemStack getCraftingRemainingItem(ItemStack stack) {
    double damageLevel = (double) stack.getDamageValue() / (double) stack.getMaxDamage();
    if (ThreadLocalRandom.current().nextDouble() < damageLevel * 0.0001) {
      return RailcraftItems.RAW_FIRESTONE.get().getDefaultInstance();
    }
    ItemStack newStack = ContainerTools.copyOne(stack);
    return newStack.hurt(1, RandomSource.create(), null) ? ItemStack.EMPTY : newStack;
  }
}
