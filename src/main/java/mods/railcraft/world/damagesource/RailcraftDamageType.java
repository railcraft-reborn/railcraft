package mods.railcraft.world.damagesource;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class RailcraftDamageType {

  public static final ResourceKey<DamageType> BORE = createKey("bore");
  public static final ResourceKey<DamageType> CRUSHER = createKey("crusher");
  public static final ResourceKey<DamageType> ELECTRIC = createKey("electric");
  public static final ResourceKey<DamageType> STEAM = createKey("steam");
  public static final ResourceKey<DamageType> TRACK_ELECTRIC = createKey("track_electric");
  public static final ResourceKey<DamageType> TRAIN = createKey("train");
  public static final ResourceKey<DamageType> CREOSOTE = createKey("creosote");

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
    return ResourceKey.create(Registries.DAMAGE_TYPE, RailcraftConstants.rl(name));
  }
}
