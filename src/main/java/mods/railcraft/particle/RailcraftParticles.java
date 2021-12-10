package mods.railcraft.particle;

import mods.railcraft.Railcraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Railcraft particle types.
 *
 * @author LetterN (https://github.com/LetterN)
 */
public class RailcraftParticles {

  public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
      DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Railcraft.ID);

  public static final RegistryObject<SimpleParticleType> STEAM =
    PARTICLE_TYPES.register("steam", () -> new SimpleParticleType(false));

  public static final RegistryObject<SimpleParticleType> SPARK =
    PARTICLE_TYPES.register("spark", () -> new SimpleParticleType(false));

  public static final RegistryObject<SimpleParticleType> PUMPKIN =
    PARTICLE_TYPES.register("pumpkin", () -> new SimpleParticleType(false));
}
