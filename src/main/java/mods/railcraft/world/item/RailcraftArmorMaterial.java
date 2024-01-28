package mods.railcraft.world.item;

import java.util.function.Supplier;
import com.google.common.base.Suppliers;
import mods.railcraft.tags.RailcraftTags;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

public enum RailcraftArmorMaterial implements ArmorMaterial {

  OVERALLS("railcraft:overalls", 5, new int[] {1, 2, 3, 1}, 15, SoundEvents.ARMOR_EQUIP_LEATHER,
      0, 0, () -> Ingredient.of(Blocks.BLUE_WOOL)),
  GOGGLES("railcraft:goggles", 20, new int[] {1, 2, 3, 1}, 15, SoundEvents.ARMOR_EQUIP_LEATHER,
      0, 0, () -> Ingredient.of(RailcraftTags.Items.STEEL_INGOT)),
  STEEL("railcraft:steel", 25, new int[] {2, 5, 6, 2}, 8, SoundEvents.ARMOR_EQUIP_IRON, 0.8F, 0,
      () -> Ingredient.of(RailcraftTags.Items.STEEL_INGOT));

  private static final int[] HEALTH_PER_SLOT = new int[] {13, 15, 16, 11};
  private final String name;
  private final int durabilityMultiplier;
  private final int[] slotProtections;
  private final int enchantmentValue;
  private final SoundEvent sound;
  private final float toughness;
  private final float knockbackResistance;
  private final Supplier<Ingredient> repairIngredient;

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
    this.repairIngredient = Suppliers.memoize(repairIngredient::get);
  }

  @Override
  public int getDurabilityForType(ArmorItem.Type type) {
    return HEALTH_PER_SLOT[type.getSlot().getIndex()] * this.durabilityMultiplier;
  }
  @Override
  public int getDefenseForType(ArmorItem.Type type) {
    return this.slotProtections[type.getSlot().getIndex()];
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
