package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class PumpkinParticle extends SteamParticle {

  private PumpkinParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, TextureAtlasSprite sprite) {
    super(level, x, y, z, dx, dy, dz, sprite);
    this.gravity = -0.01F;
    // The campfire smoke base makes particles ~1 block wide, too big for a pumpkin icon
    this.quadSize = 0.2F + this.random.nextFloat() * 0.1F;
    // Keep the pumpkin colors, without the grey tint of the steam
    this.rCol = this.gCol = this.bCol = 1.0F;
    this.lifetime = 40 + this.random.nextInt(40);
  }

  @Override
  public float getQuadSize(float partialTicks) {
    return this.quadSize *
        Mth.sin(Mth.clamp((this.age + partialTicks) / this.lifetime, 0, 1) * Mth.PI);
  }

  public static class Provider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet spriteSet;

    public Provider(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level,
        double x, double y, double z, double dx, double dy, double dz, RandomSource randomSource) {
      return new PumpkinParticle(level, x, y, z, dx, dy, dz, spriteSet.get(randomSource));
    }
  }
}
