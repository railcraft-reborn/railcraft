package mods.railcraft.world.item.alchemy;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.effect.RailcraftMobEffects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftPotions {

  private static final DeferredRegister<Potion> deferredRegister =
      DeferredRegister.create(BuiltInRegistries.POTION, RailcraftConstants.ID);

  public static final DeferredHolder<Potion, Potion> CREOSOTE =
      deferredRegister.register("creosote",
          () -> new Potion(new MobEffectInstance(RailcraftMobEffects.CREOSOTE.get(), 3600)));

  public static final DeferredHolder<Potion, Potion> LONG_CREOSOTE =
      deferredRegister.register("long_creosote",
          () -> new Potion("creosote",
              new MobEffectInstance(RailcraftMobEffects.CREOSOTE.get(), 9600)));

  public static final DeferredHolder<Potion, Potion> STRONG_CREOSOTE =
      deferredRegister.register("strong_creosote",
          () -> new Potion("creosote",
              new MobEffectInstance(RailcraftMobEffects.CREOSOTE.get(), 1800, 1)));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
