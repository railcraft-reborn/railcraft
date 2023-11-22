package mods.railcraft.world.effect;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftMobEffects {

  private static final DeferredRegister<MobEffect> deferredRegister =
      DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, RailcraftConstants.ID);

  public static final DeferredHolder<MobEffect, CreosoteEffect> CREOSOTE =
      deferredRegister.register("creosote",
          () -> new CreosoteEffect(MobEffectCategory.BENEFICIAL, 0xCCA300));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
