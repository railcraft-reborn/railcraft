package mods.railcraft.world.damagesource;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;

public class RailcraftDamageSources {

  private static Registry<DamageType> damageTypes(RegistryAccess registryAccess) {
    return registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE);
  }

  public static RailcraftDamageSource bore(RegistryAccess registryAccess) {
    return new RailcraftDamageSource(damageTypes(registryAccess)
        .getOrThrow(RailcraftDamageType.BORE));
  }

  public static RailcraftDamageSource crusher(RegistryAccess registryAccess) {
    return new RailcraftDamageSource(damageTypes(registryAccess)
        .getOrThrow(RailcraftDamageType.CRUSHER), 8);
  }

  public static RailcraftDamageSource electric(RegistryAccess registryAccess) {
    return new RailcraftDamageSource(damageTypes(registryAccess)
        .getOrThrow(RailcraftDamageType.ELECTRIC));
  }

  public static RailcraftDamageSource steam(RegistryAccess registryAccess) {
    return new RailcraftDamageSource(damageTypes(registryAccess)
        .getOrThrow(RailcraftDamageType.STEAM));
  }

  public static RailcraftDamageSource trackElectric(RegistryAccess registryAccess) {
    return new RailcraftDamageSource(damageTypes(registryAccess)
        .getOrThrow(RailcraftDamageType.TRACK_ELECTRIC));
  }

  public static RailcraftDamageSource train(RegistryAccess registryAccess) {
    return new RailcraftDamageSource(damageTypes(registryAccess)
        .getOrThrow(RailcraftDamageType.TRAIN));
  }

  public static RailcraftDamageSource creosote(RegistryAccess registryAccess) {
    return new RailcraftDamageSource(damageTypes(registryAccess)
        .getOrThrow(RailcraftDamageType.CREOSOTE));
  }
}
