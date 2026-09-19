package mods.railcraft.client.particle;

import mods.railcraft.particle.ChimneyParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class ChimneyParticle extends BaseSmokeParticle {

  public ChimneyParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, int color, TextureAtlasSprite sprite) {
    this(level, x, y, z, dx, dy, dz, 3f, color, sprite);
  }

  public ChimneyParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, float scale, int color, TextureAtlasSprite sprite) {
    super(level, x, y, z, dx, dy, dz, scale, sprite);
    this.gravity = SMOKE_GRAVITY;
    this.rCol =
        Mth.clamp((this.random.nextFloat() * 0.1f - 0.05f) + ARGB.red(color) / 255.0F, 0, 1);
    this.gCol =
        Mth.clamp((this.random.nextFloat() * 0.1f - 0.05f) + ARGB.green(color) / 255.0F, 0, 1);
    this.bCol =
        Mth.clamp((this.random.nextFloat() * 0.1f - 0.05f) + ARGB.blue(color) / 255.0F, 0, 1);
    this.lifetime = (int) ((24.0F / (this.random.nextFloat() * 0.5F + 0.2F)) * scale);
  }

  public static class Provider implements ParticleProvider<ChimneyParticleOptions> {

    private final SpriteSet spriteSet;

    public Provider(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(ChimneyParticleOptions options, ClientLevel level,
        double x, double y, double z, double dx, double dy, double dz, RandomSource randomSource) {
      return new ChimneyParticle(level, x, y, z, dx, dy, dz, options.color(),
          this.spriteSet.get(randomSource));
    }
  }
}
