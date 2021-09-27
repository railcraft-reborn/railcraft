package mods.railcraft.world.item;

import mods.railcraft.util.MiscTools;
import mods.railcraft.util.inventory.InvTools;
import net.minecraft.item.ItemStack;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class ItemFirestoneCracked extends ItemFirestoneRefined {

  public static int HEAT = 100;

  public ItemFirestoneCracked(Properties properties) {
    super(properties);
    heat = HEAT;
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
  public ItemStack getContainerItem(ItemStack stack) {
    double damageLevel = (double) stack.getDamageValue() / (double) stack.getMaxDamage();
    if (MiscTools.RANDOM.nextDouble() < damageLevel * 0.0001)
      return RailcraftItems.RAW_FIRESTONE.get().getDefaultInstance();
    ItemStack newStack = InvTools.copyOne(stack);
    return newStack.hurt(1, random, null) ? ItemStack.EMPTY : newStack;
  }
}
