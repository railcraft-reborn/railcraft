package mods.railcraft.particle;

import mods.railcraft.Railcraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Railcraft particle types.
 *
 * @author LetterN (https://github.com/LetterN)
 */
public class RailcraftParticles {

  public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
      DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Railcraft.ID);

  public static final RegistryObject<BasicParticleType> STEAM =
    PARTICLE_TYPES.register("steam", () -> new BasicParticleType(false));

  public static final RegistryObject<BasicParticleType> SPARK =
    PARTICLE_TYPES.register("spark", () -> new BasicParticleType(false));

  public static final RegistryObject<BasicParticleType> PUMPKIN =
    PARTICLE_TYPES.register("pumpkin", () -> new BasicParticleType(false));
}
