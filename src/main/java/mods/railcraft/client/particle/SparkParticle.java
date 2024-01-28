package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class SparkParticle extends DimmableParticle {

  private SparkParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, SpriteSet sprites) {
    super(level, x, y, z, dx, dy, dz);
    this.gravity = 1;
    this.setLifetime(random.nextInt(10) + 10); // 10-20, 0.5sec to 1 sec
    this.setSize(0.15F, 0.15F); // AABB bounding cube so we can bounce
    this.pickSprite(sprites);
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  public static class Provider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet sprites;

    public Provider(SpriteSet sprites) {
      this.sprites = sprites;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level,
        double x, double y, double z, double dx, double dy, double dz) {
      return new SparkParticle(level, x, y, z, dx, dy + 1.0D, dz, this.sprites);
    }
  }
}
