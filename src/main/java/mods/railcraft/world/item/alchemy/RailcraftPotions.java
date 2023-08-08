package mods.railcraft.world.item.alchemy;

import mods.railcraft.Railcraft;
import mods.railcraft.world.effect.RailcraftMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftPotions {

  private static final DeferredRegister<Potion> deferredRegister =
      DeferredRegister.create(ForgeRegistries.POTIONS, Railcraft.ID);

  public static final RegistryObject<Potion> CREOSOTE =
      deferredRegister.register("creosote",
          () -> new Potion(new MobEffectInstance(RailcraftMobEffects.CREOSOTE.get(), 3600)));

  public static final RegistryObject<Potion> LONG_CREOSOTE =
      deferredRegister.register("long_creosote",
          () -> new Potion("creosote", new MobEffectInstance(RailcraftMobEffects.CREOSOTE.get(), 9600)));

  public static final RegistryObject<Potion> STRONG_CREOSOTE =
      deferredRegister.register("strong_creosote",
          () -> new Potion("creosote", new MobEffectInstance(RailcraftMobEffects.CREOSOTE.get(), 1800, 1)));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
