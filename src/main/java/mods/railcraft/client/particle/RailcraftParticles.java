package mods.railcraft.client.particle;

import mods.railcraft.Railcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handler and initializer of all particles railcraft has.
 * @author LetterN (https://github.com/LetterN)
 */
@Mod.EventBusSubscriber(modid = Railcraft.ID, bus = Bus.MOD, value = Dist.CLIENT)
public class RailcraftParticles {
  public static final DeferredRegister<ParticleType<?>> PARTICLE =
    DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Railcraft.ID);

  public static final RegistryObject<BasicParticleType> STEAM =
    PARTICLE.register("steam", () -> new BasicParticleType(false));

  /**
   * You may be asking "why is this not in the ClientDist"
   * Well, for some unorthadoxial reason, this same-named function does NOT invoke properly in the client dist,
   * throwing this error: <code>Redundant texture list for particle railcraft:steam</code>
   *
   * If you managed to fix this, make sure to put it on the ClientDist
   *  @see mods.railcraft.client.ClientDist#particleRegistration() ClientDist
   */
  @SubscribeEvent
	public static void particleRegistration(ParticleFactoryRegisterEvent evt) {
    final ParticleManager particleEngine = Minecraft.getInstance().particleEngine;
		particleEngine.register(STEAM.get(), ParticleSteam.SteamParticleFactory::new);
	}
}
