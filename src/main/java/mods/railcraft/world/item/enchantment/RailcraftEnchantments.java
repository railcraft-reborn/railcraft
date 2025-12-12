package mods.railcraft.world.item.enchantment;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.tags.RailcraftTags;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

public class RailcraftEnchantments {

  public static final ResourceKey<Enchantment> WRECKING = createKey("wrecking");
  public static final ResourceKey<Enchantment> IMPLOSION = createKey("implosion");
  public static final ResourceKey<Enchantment> DESTRUCTION = createKey("destruction");
  public static final ResourceKey<Enchantment> SMACK = createKey("smack");

  private static ResourceKey<Enchantment> createKey(String name) {
    return ResourceKey.create(Registries.ENCHANTMENT, RailcraftConstants.id(name));
  }

  private static void register(BootstrapContext<Enchantment> context,
      ResourceKey<Enchantment> enchantment, Enchantment.Builder builder) {
    context.register(enchantment, builder.build(enchantment.identifier()));
  }

  private static Enchantment.Builder customDamageEnchantment(HolderGetter<Item> items,
      int baseEnchantability, int levelEnchantability, int thresholdEnchantability) {
    return Enchantment.enchantment(
        Enchantment.definition(items.getOrThrow(RailcraftTags.Items.ENCHANTMENTS), 1, 5,
            Enchantment.dynamicCost(baseEnchantability, levelEnchantability),
          Enchantment.dynamicCost(baseEnchantability + thresholdEnchantability, levelEnchantability),
          4, EquipmentSlotGroup.MAINHAND));
  }

  public static void bootstrap(BootstrapContext<Enchantment> context) {
    var items = context.lookup(Registries.ITEM);
    var entityType = context.lookup(Registries.ENTITY_TYPE);

    register(context, WRECKING, customDamageEnchantment(items, 1, 11, 20));
    register(context, IMPLOSION, customDamageEnchantment(items, 5, 8, 20)
        .withEffect(EnchantmentEffectComponents.DAMAGE,
            new AddValue(LevelBasedValue.perLevel(2.5f)),
            LootItemEntityPropertyCondition.hasProperties(
                LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity()
                    .entityType(EntityTypePredicate.of(entityType, EntityType.CREEPER)))));
    register(context, DESTRUCTION, Enchantment.enchantment(
        Enchantment.definition(items.getOrThrow(RailcraftTags.Items.CROWBAR), 1, 3,
            Enchantment.dynamicCost(5, 10), Enchantment.dynamicCost(15, 10),
            8, EquipmentSlotGroup.MAINHAND)));
    register(context, SMACK, Enchantment.enchantment(
        Enchantment.definition(items.getOrThrow(RailcraftTags.Items.CROWBAR), 1, 4,
            Enchantment.dynamicCost(9, 8), Enchantment.dynamicCost(29, 8),
            8, EquipmentSlotGroup.MAINHAND)));
  }
}
