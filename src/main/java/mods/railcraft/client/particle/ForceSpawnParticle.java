package mods.railcraft.client.particle;

import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.particle.ForceSpawnParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public class ForceSpawnParticle extends BaseShrinkingSmokeParticle {

  private ForceSpawnParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, int color, SpriteSet sprites) {
    this(level, x, y, z, dx, dy, dz, color, 1, sprites);
  }

  public ForceSpawnParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz, int color, float scale, SpriteSet sprites) {
    super(level, x, y, z, dx, dy, dz, scale);
    this.gravity = -0.1F;
    this.rCol = RenderUtil.getRed(color);
    this.gCol = RenderUtil.getGreen(color);
    this.bCol = RenderUtil.getBlue(color);
    this.lifetime = (int) (8.0F / (this.random.nextFloat() * 0.8F + 0.2F));
    this.lifetime = (int) ((float) this.lifetime * scale);
    this.hasPhysics = false;
    this.pickSprite(sprites);
  }

  public static class Provider implements ParticleProvider<ForceSpawnParticleOptions> {

    private final SpriteSet spriteSet;

    public Provider(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(ForceSpawnParticleOptions options, ClientLevel level,
        double x, double y, double z, double dx, double dy, double dz) {
      return new ForceSpawnParticle(level, x, y, z, dx, dy, dz, options.color(), this.spriteSet);
    }
  }
}
