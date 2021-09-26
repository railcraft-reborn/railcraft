package mods.railcraft.client.particle;

import mods.railcraft.Railcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Handler and initializer of all particles railcraft has.
 * @author LetterN (https://github.com/LetterN)
 */
public class RailcraftParticles {
  private static final ParticleManager PARTICLE_ENGINE = Minecraft.getInstance().particleEngine;
  private static final DeferredRegister<ParticleType<?>> PARTICLE =
    DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Railcraft.ID);

  public static final void register(IEventBus bus) {
    PARTICLE.register(bus);
    PARTICLE_ENGINE.register(STEAM.get(), ParticleSteam.Factory::new);
  }

  public static final RegistryObject<BasicParticleType> STEAM =
    PARTICLE.register("steam", () -> new BasicParticleType(false));
}
