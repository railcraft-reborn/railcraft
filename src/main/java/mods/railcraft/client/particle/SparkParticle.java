package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SparkParticle extends DimmableParticle {

  private SparkParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, TextureAtlasSprite sprite) {
    super(level, x, y, z, dx, dy, dz, sprite);
    this.gravity = 1;
    this.setLifetime(random.nextInt(10) + 10); // 10-20, 0.5sec to 1 sec
    this.setSize(0.15F, 0.15F); // AABB bounding cube so we can bounce
  }

  @Override
  protected Layer getLayer() {
    return Layer.TRANSLUCENT;
  }

  public static class Provider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet sprites;

    public Provider(SpriteSet sprites) {
      this.sprites = sprites;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level,
        double x, double y, double z, double dx, double dy, double dz, RandomSource randomSource) {
      return new SparkParticle(level, x, y, z, dx, dy + 1.0D, dz, this.sprites.get(randomSource));
    }
  }
}
