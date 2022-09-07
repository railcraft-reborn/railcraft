package mods.railcraft.world.item.enchantment;

import mods.railcraft.api.item.Crowbar;
import mods.railcraft.world.item.SpikeMaulItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class RailcraftToolEnchantment extends Enchantment {

  public RailcraftToolEnchantment(Rarity rarity, EquipmentSlot... slots) {
    super(rarity, EnchantmentCategory.DIGGER, slots);
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    var item = stack.getItem();
    return item instanceof Crowbar || item instanceof SpikeMaulItem;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return canEnchant(stack);
  }
}
