package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class PumpkinParticle extends SteamParticle {

  private PumpkinParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, SpriteSet sprites) {
    super(level, x, y, z, dx, dy, dz, sprites);
    this.gravity = -0.01F;
    this.lifetime = (int) (16.0D / (this.random.nextGaussian() * 0.8D + 0.2D));
  }

  @Override
  public float getQuadSize(float partialTicks) {
    return this.quadSize * Mth.sin(
        Mth.clamp((this.age + partialTicks) / this.lifetime, 0.0F, 1.0F) * (float) Math.PI);
  }

  public static class Provider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet spriteSet;

    public Provider(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level,
        double x, double y, double z, double dx, double dy, double dz) {
      return new PumpkinParticle(level, x, y, z, dx, dy, dz, spriteSet);
    }
  }
}
