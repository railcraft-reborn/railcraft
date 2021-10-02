package mods.railcraft.world.item.enchantment;

import mods.railcraft.api.item.Crowbar;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

/**
 * Boost enchantment.
 */
public class SmackEnchantment extends RailcraftToolEnchantment {

  public SmackEnchantment(Rarity rarity) {
    super(rarity, EquipmentSlotType.MAINHAND);
  }

  @Override
  public int getMinCost(int level) {
    return 9 + level * 8;
  }

  @Override
  public int getMaxCost(int level) {
    return getMinCost(level) + 20;
  }

  @Override
  public int getMaxLevel() {
    return 4;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return stack.getItem() instanceof Crowbar;
  }
}
