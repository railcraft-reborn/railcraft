package mods.railcraft.client.particle;

import mods.railcraft.Railcraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Handler and initializer of all particles railcraft has.
 * @author LetterN (https://github.com/LetterN)
 */
public class RailcraftParticles {
  public static final DeferredRegister<ParticleType<?>> PARTICLE =
    DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Railcraft.ID);

  public static final RegistryObject<BasicParticleType> STEAM =
    PARTICLE.register("steam", () -> new BasicParticleType(false));
}
