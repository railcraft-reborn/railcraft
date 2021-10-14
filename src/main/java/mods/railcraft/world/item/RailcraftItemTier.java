package mods.railcraft.world.item;

import java.util.function.Supplier;
import mods.railcraft.tags.RailcraftTags;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

public enum RailcraftItemTier implements IItemTier {

  STEEL(2, 500, 7, 2.5F, 9, () -> Ingredient.of(RailcraftTags.Items.STEEL_INGOT));

  private final int level;
  private final int uses;
  private final float speed;
  private final float damage;
  private final int enchantmentValue;
  private final LazyValue<Ingredient> repairIngredient;

  private RailcraftItemTier(int level, int uses, float speed, float damage, int enchantmentValue,
      Supplier<Ingredient> repairIngredient) {
    this.level = level;
    this.uses = uses;
    this.speed = speed;
    this.damage = damage;
    this.enchantmentValue = enchantmentValue;
    this.repairIngredient = new LazyValue<>(repairIngredient);
  }

  @Override
  public int getUses() {
    return this.uses;
  }

  @Override
  public float getSpeed() {
    return this.speed;
  }

  @Override
  public float getAttackDamageBonus() {
    return this.damage;
  }

  @Override
  public int getLevel() {
    return this.level;
  }

  @Override
  public int getEnchantmentValue() {
    return this.enchantmentValue;
  }

  @Override
  public Ingredient getRepairIngredient() {
    return this.repairIngredient.get();
  }
}
