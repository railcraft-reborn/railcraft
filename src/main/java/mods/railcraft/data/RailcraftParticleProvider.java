package mods.railcraft.data;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.particle.RailcraftParticleTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider;

public class RailcraftParticleProvider extends ParticleDescriptionProvider {

  public RailcraftParticleProvider(PackOutput output) {
    super(output);
  }

  @Override
  protected void addDescriptions() {
    this.spriteSet(RailcraftParticleTypes.STEAM.get(), RailcraftConstants.id("steam"));
    this.spriteSet(RailcraftParticleTypes.PUMPKIN.get(), RailcraftConstants.id("pumpkin"));
    this.spriteSet(RailcraftParticleTypes.SPARK.get(), RailcraftConstants.id("spark"));
    this.spriteSet(RailcraftParticleTypes.FIRE_SPARK.get(),
        Identifier.withDefaultNamespace("lava"));

    this.spriteSet(RailcraftParticleTypes.CHIMNEY.get(),
        Identifier.withDefaultNamespace("generic"), 8, true);
    this.spriteSet(RailcraftParticleTypes.CHUNK_LOADER.get(),
        Identifier.withDefaultNamespace("generic"), 8, true);
    this.spriteSet(RailcraftParticleTypes.FORCE_SPAWN.get(),
        Identifier.withDefaultNamespace("generic"), 8, true);
    this.spriteSet(RailcraftParticleTypes.TUNING_AURA.get(),
        Identifier.withDefaultNamespace("generic"), 8, true);
  }
}
