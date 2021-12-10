package mods.railcraft.world.item.enchantment;

import mods.railcraft.api.item.Crowbar;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class DestructionEnchantment extends RailcraftToolEnchantment {

  public DestructionEnchantment(Rarity rarity) {
    super(rarity, EquipmentSlot.MAINHAND);
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
