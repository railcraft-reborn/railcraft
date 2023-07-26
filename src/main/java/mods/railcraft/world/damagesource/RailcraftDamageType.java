package mods.railcraft.world.damagesource;

import mods.railcraft.Railcraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class RailcraftDamageType {

  static final ResourceKey<DamageType> BORE = createKey("bore");
  static final ResourceKey<DamageType> CRUSHER = createKey("crusher");
  static final ResourceKey<DamageType> ELECTRIC = createKey("electric");
  static final ResourceKey<DamageType> STEAM = createKey("steam");
  static final ResourceKey<DamageType> TRACK_ELECTRIC = createKey("track_electric");
  static final ResourceKey<DamageType> TRAIN = createKey("train");
  static final ResourceKey<DamageType> CREOSOTE = createKey("creosote");

  public static void bootstrap(BootstapContext<DamageType> context) {
    context.register(BORE, new DamageType("bore", 0));
    context.register(CRUSHER, new DamageType("crusher", 0.1f));
    context.register(ELECTRIC, new DamageType("electric", 0));
    context.register(STEAM, new DamageType("steam", 0.1f));
    context.register(TRACK_ELECTRIC, new DamageType("track_electric", 0));
    context.register(TRAIN, new DamageType("train", 0));
    context.register(CREOSOTE, new DamageType("creosote", 0));
  }

  private static ResourceKey<DamageType> createKey(String name) {
    return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Railcraft.ID, name));
  }
}
