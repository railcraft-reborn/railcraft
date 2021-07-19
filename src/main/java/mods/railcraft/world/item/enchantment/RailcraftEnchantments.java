package mods.railcraft.world.item.enchantment;

import mods.railcraft.Railcraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftEnchantments {

  public static final DeferredRegister<Enchantment> ENCHANTMENTS =
      DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Railcraft.ID);

  public static final RegistryObject<Enchantment> WRECKING =
      ENCHANTMENTS.register("wrecking",
          () -> new RailcraftDamageEnchantment(Enchantment.Rarity.RARE, 1, 11, 20, null, 0.75f));

  public static final RegistryObject<Enchantment> IMPLOSION =
      ENCHANTMENTS.register("implosion",
          () -> new RailcraftDamageEnchantment(Enchantment.Rarity.RARE, 5, 8, 20,
              CreeperEntity.class::isInstance, 2.5f));

  public static final RegistryObject<DestructionEnchantment> DESTRUCTION =
      ENCHANTMENTS.register("destruction",
          () -> new DestructionEnchantment(Enchantment.Rarity.VERY_RARE));

  public static final RegistryObject<Enchantment> SMACK =
      ENCHANTMENTS.register("smack",
          () -> new SmackEnchantment(Enchantment.Rarity.VERY_RARE));
}
