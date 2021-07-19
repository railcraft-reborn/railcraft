package mods.railcraft.world.item;

import java.util.function.Supplier;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public enum RailcraftArmorMaterial implements IArmorMaterial {

  OVERALLS("overalls", 5, new int[] {1, 2, 3, 1}, 15,
      SoundEvents.ARMOR_EQUIP_LEATHER, 0F, 0F, () -> Ingredient.of(Blocks.BLUE_WOOL));

  private static final int[] HEALTH_PER_SLOT = new int[] {13, 15, 16, 11};
  private final String name;
  private final int durabilityMultiplier;
  private final int[] slotProtections;
  private final int enchantmentValue;
  private final SoundEvent sound;
  private final float toughness;
  private final float knockbackResistance;
  private final LazyValue<Ingredient> repairIngredient;

  private RailcraftArmorMaterial(String name, int durabilityMultiplier, int[] slotProtections,
      int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance,
      Supplier<Ingredient> repairIngredient) {
    this.name = name;
    this.durabilityMultiplier = durabilityMultiplier;
    this.slotProtections = slotProtections;
    this.enchantmentValue = enchantmentValue;
    this.sound = sound;
    this.toughness = toughness;
    this.knockbackResistance = knockbackResistance;
    this.repairIngredient = new LazyValue<>(repairIngredient);
  }

  @Override
  public int getDurabilityForSlot(EquipmentSlotType slot) {
    return HEALTH_PER_SLOT[slot.getIndex()] * this.durabilityMultiplier;
  }

  @Override
  public int getDefenseForSlot(EquipmentSlotType slot) {
    return this.slotProtections[slot.getIndex()];
  }

  @Override
  public int getEnchantmentValue() {
    return this.enchantmentValue;
  }

  @Override
  public SoundEvent getEquipSound() {
    return this.sound;
  }

  @Override
  public Ingredient getRepairIngredient() {
    return this.repairIngredient.get();
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public float getToughness() {
    return this.toughness;
  }

  @Override
  public float getKnockbackResistance() {
    return this.knockbackResistance;
  }
}
