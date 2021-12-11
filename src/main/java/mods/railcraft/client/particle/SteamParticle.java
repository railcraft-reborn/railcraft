package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class SteamParticle extends CampfireSmokeParticle {

  public SteamParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz) {
    super(level, x, y, z, dx, dy, dz, false);
    this.scale(1.7F);
    // 0.6 min (0.6-1.0), steam isn't as dark as smog
    this.rCol = this.gCol = this.bCol = (0.6f + this.random.nextFloat() * 0.4f);
    // 20 ticks (1sec) 40 tics (max)
    this.lifetime = this.random.nextInt(50) + 30;
  }

  public static class Provider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet spriteSet;

    public Provider(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level,
        double x, double y, double z, double dx, double dy, double dz) {
      SteamParticle steam = new SteamParticle(level, x, y, z, dx, dy, dz);
      steam.pickSprite(this.spriteSet);
      return steam;
    }
  }
}
