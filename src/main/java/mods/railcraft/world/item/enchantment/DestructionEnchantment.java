package mods.railcraft.world.item.enchantment;

import mods.railcraft.api.item.Crowbar;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class DestructionEnchantment extends Enchantment {

  public DestructionEnchantment(Rarity rarity, EquipmentSlot... slots) {
    super(rarity, RailcraftEnchantmentCategories.RAILWAY_TOOL, slots);
  }

  @Override
  public int getMinCost(int level) {
    return 5 + (level - 1) * 10;
  }

  @Override
  public int getMaxCost(int level) {
    return this.getMinCost(level) + 10;
  }

  @Override
  public int getMaxLevel() {
    return 3;
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    return stack.getItem() instanceof Crowbar;
  }
}
