package mods.railcraft.client.particle;

import mods.railcraft.particle.ForceSpawnParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public class ForceSpawnParticle extends BaseShrinkingSmokeParticle {

  public static final float SMOKE_GRAVITY = -0.1F;

  public ForceSpawnParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, int color) {
    this(level, x, y, z, dx, dy, dz, color, 1.0F);
  }

  public ForceSpawnParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, int color, float scale) {
    super(level, x, y, z, dx, dy, dz, scale);
    this.gravity = SMOKE_GRAVITY;
    this.rCol = ((color >> 16) & 0xFF) / 255F;
    this.gCol = ((color >> 8) & 0xFF) / 255F;
    this.bCol = ((color) & 0xFF) / 255F;
    this.lifetime = (int) (8.0F / (this.random.nextFloat() * 0.8F + 0.2F));
    this.lifetime = (int) ((float) this.lifetime * scale);
    this.hasPhysics = false;
  }

  public static class Provider implements ParticleProvider<ForceSpawnParticleOptions> {

    private final SpriteSet spriteSet;

    public Provider(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(ForceSpawnParticleOptions options, ClientLevel level,
        double x, double y, double z, double dx, double dy, double dz) {
      var particle = new ForceSpawnParticle(level, x, y, z, dx, dy, dz, options.getColor());
      particle.pickSprite(this.spriteSet);
      return particle;
    }
  }
}
