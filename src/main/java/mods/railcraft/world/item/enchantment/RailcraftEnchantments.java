package mods.railcraft.world.item.enchantment;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

public class RailcraftEnchantments {

  private static final DeferredRegister<Enchantment> deferredRegister =
      DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, RailcraftConstants.ID);

  public static final RegistryObject<Enchantment> WRECKING =
      deferredRegister.register("wrecking",
          () -> new RailcraftDamageEnchantment(Enchantment.Rarity.RARE, 1, 11, 20, null, 0.75f));

  public static final RegistryObject<Enchantment> IMPLOSION =
      deferredRegister.register("implosion",
          () -> new RailcraftDamageEnchantment(Enchantment.Rarity.RARE, 5, 8, 20,
              Creeper.class::isInstance, 2.5f));

  public static final RegistryObject<DestructionEnchantment> DESTRUCTION =
      deferredRegister.register("destruction",
          () -> new DestructionEnchantment(Enchantment.Rarity.VERY_RARE));

  public static final RegistryObject<Enchantment> SMACK =
      deferredRegister.register("smack",
          () -> new SmackEnchantment(Enchantment.Rarity.VERY_RARE));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
