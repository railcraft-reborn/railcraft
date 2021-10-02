package mods.railcraft.world.item.enchantment;

import mods.railcraft.api.item.Crowbar;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class RailcraftToolEnchantment extends Enchantment {

  public RailcraftToolEnchantment(Rarity rarity, EquipmentSlotType... slots) {
    super(rarity, EnchantmentType.DIGGER, slots);
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
