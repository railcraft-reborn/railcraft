package mods.railcraft.data;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.particle.RailcraftParticleTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class RailcraftParticleProvider extends ParticleDescriptionProvider {

  public RailcraftParticleProvider(PackOutput output) {
    super(output);
  }

  @Override
  protected void addDescriptions() {
    this.spriteSet(RailcraftParticleTypes.STEAM.get(), RailcraftConstants.rl("steam"));
    this.spriteSet(RailcraftParticleTypes.PUMPKIN.get(), RailcraftConstants.rl("pumpkin"));
    this.spriteSet(RailcraftParticleTypes.SPARK.get(), RailcraftConstants.rl("spark"));
    this.spriteSet(RailcraftParticleTypes.FIRE_SPARK.get(),
        ResourceLocation.withDefaultNamespace("lava"));

    this.spriteSet(RailcraftParticleTypes.CHIMNEY.get(),
        ResourceLocation.withDefaultNamespace("generic"), 8, true);
    this.spriteSet(RailcraftParticleTypes.CHUNK_LOADER.get(),
        ResourceLocation.withDefaultNamespace("generic"), 8, true);
    this.spriteSet(RailcraftParticleTypes.FORCE_SPAWN.get(),
        ResourceLocation.withDefaultNamespace("generic"), 8, true);
    this.spriteSet(RailcraftParticleTypes.TUNING_AURA.get(),
        ResourceLocation.withDefaultNamespace("generic"), 8, true);
  }
}
