package mods.railcraft.world.item.enchantment;

import mods.railcraft.Railcraft;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftEnchantments {

  public static final DeferredRegister<Enchantment> ENCHANTMENTS =
      DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Railcraft.ID);

  public static final RegistryObject<Enchantment> WRECKING =
      ENCHANTMENTS.register("wrecking",
          () -> new RailcraftDamageEnchantment(Enchantment.Rarity.RARE, 1, 11, 20, null, 0.75f));

  public static final RegistryObject<Enchantment> IMPLOSION =
      ENCHANTMENTS.register("implosion",
          () -> new RailcraftDamageEnchantment(Enchantment.Rarity.RARE, 5, 8, 20,
              Creeper.class::isInstance, 2.5f));

  public static final RegistryObject<DestructionEnchantment> DESTRUCTION =
      ENCHANTMENTS.register("destruction",
          () -> new DestructionEnchantment(Enchantment.Rarity.VERY_RARE));

  public static final RegistryObject<Enchantment> SMACK =
      ENCHANTMENTS.register("smack",
          () -> new SmackEnchantment(Enchantment.Rarity.VERY_RARE));
}
