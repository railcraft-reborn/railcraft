package mods.railcraft.world.item.enchantment;

import mods.railcraft.api.item.Crowbar;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Boost enchantment.
 */
public class SmackEnchantment extends Enchantment {

  public SmackEnchantment(Rarity rarity, EquipmentSlot... slots) {
    super(rarity, RailcraftEnchantmentCategories.RAILWAY_TOOL, slots);
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
