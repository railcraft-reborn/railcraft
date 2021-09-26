package mods.railcraft.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

public class ParticleSteam extends SmokeParticle {

  public ParticleSteam(ClientWorld world, double x, double y, double z, double dx, double dy,
      double dz, float scale, IAnimatedSprite sprite) {
    super(world, x, y, z, dx, dy, dz, scale, sprite);
    this.rCol = this.gCol = this.bCol = (float) (Math.random() * 0.4) + 0.4f;
    this.lifetime = (int) ((8.0D / (Math.random() * 0.8D + 0.2D)) * scale);
  }

  @Override
  public IParticleRenderType getRenderType() {
    return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  public static class Factory implements IParticleFactory<BasicParticleType> {
    private final IAnimatedSprite spriteSet;

    public Factory(IAnimatedSprite spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      return new ParticleSteam(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 1.0f, this.spriteSet);
    }
  }
}
