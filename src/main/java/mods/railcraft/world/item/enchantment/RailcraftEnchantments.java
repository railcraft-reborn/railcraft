package mods.railcraft.world.item.enchantment;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftEnchantments {

  private static final DeferredRegister<Enchantment> deferredRegister =
      DeferredRegister.create(BuiltInRegistries.ENCHANTMENT, RailcraftConstants.ID);

  public static final DeferredHolder<Enchantment, RailcraftDamageEnchantment> WRECKING =
      deferredRegister.register("wrecking",
          () -> new RailcraftDamageEnchantment(Enchantment.Rarity.RARE, 1, 11, 20, null, 0.75f,
              EquipmentSlot.MAINHAND));

  public static final DeferredHolder<Enchantment, RailcraftDamageEnchantment> IMPLOSION =
      deferredRegister.register("implosion",
          () -> new RailcraftDamageEnchantment(Enchantment.Rarity.RARE, 5, 8, 20,
              Creeper.class::isInstance, 2.5f,
              EquipmentSlot.MAINHAND));

  public static final DeferredHolder<Enchantment, DestructionEnchantment> DESTRUCTION =
      deferredRegister.register("destruction",
          () -> new DestructionEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));

  public static final DeferredHolder<Enchantment, SmackEnchantment> SMACK =
      deferredRegister.register("smack",
          () -> new SmackEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
