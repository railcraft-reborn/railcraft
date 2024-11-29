package mods.railcraft.client.particle;

import mods.railcraft.particle.ChimneyParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public class ChimneyParticle extends BaseSmokeParticle {

  public ChimneyParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, int color, SpriteSet sprites) {
    this(level, x, y, z, dx, dy, dz, 3f, color, sprites);
  }

  public ChimneyParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, float scale, int color, SpriteSet sprites) {
    super(level, x, y, z, dx, dy, dz, scale);
    this.gravity = SMOKE_GRAVITY;
    this.rCol =
        Mth.clamp((this.random.nextFloat() * 0.1f - 0.05f) + ARGB.red(color) / 255.0F, 0, 1);
    this.gCol =
        Mth.clamp((this.random.nextFloat() * 0.1f - 0.05f) + ARGB.green(color) / 255.0F, 0, 1);
    this.bCol =
        Mth.clamp((this.random.nextFloat() * 0.1f - 0.05f) + ARGB.blue(color) / 255.0F, 0, 1);
    this.lifetime = (int) ((24.0F / (this.random.nextFloat() * 0.5F + 0.2F)) * scale);
    this.pickSprite(sprites);
  }

  public static class Provider implements ParticleProvider<ChimneyParticleOptions> {

    private final SpriteSet spriteSet;

    public Provider(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(ChimneyParticleOptions options, ClientLevel level,
        double x, double y, double z, double dx, double dy, double dz) {
      return new ChimneyParticle(level, x, y, z, dx, dy, dz, options.color(), this.spriteSet);
    }
  }
}
