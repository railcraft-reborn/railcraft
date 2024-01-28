package mods.railcraft.world.item.enchantment;

import java.lang.ref.WeakReference;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.util.Predicates;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;

public class RailcraftDamageEnchantment extends Enchantment {

  private final int baseEnchantability, levelEnchantability, thresholdEnchantability;
  private final Predicate<? super Entity> check;
  private final float damageBonusPerLevel;
  private WeakReference<Entity> target;

  public RailcraftDamageEnchantment(Rarity rarity, int baseEnchantability,
      int levelEnchantability, int thresholdEnchantability,
      @Nullable Predicate<? super Entity> check, float damageBonusPerLevel,
      EquipmentSlot... slots) {
    super(rarity, RailcraftEnchantmentCategories.RAILWAY_TOOL, slots);
    this.baseEnchantability = baseEnchantability;
    this.levelEnchantability = levelEnchantability;
    this.thresholdEnchantability = thresholdEnchantability;
    this.check = check == null ? Predicates.alwaysTrue() : check;
    this.damageBonusPerLevel = damageBonusPerLevel;
  }

  @Override
  public int getMinCost(int level) {
    return this.baseEnchantability + (level - 1) * this.levelEnchantability;
  }

  @Override
  public int getMaxCost(int level) {
    return this.getMinCost(level) + this.thresholdEnchantability;
  }

  @Override
  public int getMaxLevel() {
    return 5;
  }

  @Override
  public float getDamageBonus(int level, MobType mobType, ItemStack enchantedItem) {
    float modifier = 0;
    if (target != null && check.test(target.get())) {
      modifier = level * damageBonusPerLevel;
    }
    target = null;
    return modifier;
  }

  @Override
  public boolean checkCompatibility(Enchantment enchantment) {
    return !(enchantment instanceof RailcraftDamageEnchantment)
        && !(enchantment instanceof DamageEnchantment);
  }
}
