package mods.railcraft.world.item.enchantment;

import mods.railcraft.api.item.Crowbar;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class RailcraftToolEnchantment extends Enchantment {

  public RailcraftToolEnchantment(Rarity rarity, EquipmentSlot... slots) {
    super(rarity, EnchantmentCategory.DIGGER, slots);
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    return stack.getItem() instanceof Crowbar;
    // || stack.getItem() instanceof ItemSpikeMaul;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return canEnchant(stack);
  }
}
