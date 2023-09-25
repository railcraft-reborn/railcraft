package mods.railcraft.world.effect;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftMobEffects {

  private static final DeferredRegister<MobEffect> deferredRegister =
      DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, RailcraftConstants.ID);

  public static final RegistryObject<CreosoteEffect> CREOSOTE =
      deferredRegister.register("creosote",
          () -> new CreosoteEffect(MobEffectCategory.BENEFICIAL, 0xCCA300));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
